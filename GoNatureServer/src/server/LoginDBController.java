package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.CreditCard;
import logic.Employee;
import logic.Subscriber;

public class LoginDBController {
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;

	public LoginDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Message parseData(Message clientMsg) {
		System.out.println("LoginDBController");
		PreparedStatement pstm;
		List<String> info;
		msgFromClient = clientMsg;	
		switch (msgFromClient.getOperationType()) {
		case VisitorLogin:
			try {
				info = (ArrayList<String>) msgFromClient.getObj();
				pstm = sqlConnection.connection
						.prepareStatement("SELECT * from members where visitorID=? or memberNumber=?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(0));

				ResultSet rs = pstm.executeQuery();

				if (rs.next()) {
					if (!isConnected(rs.getInt(2), "subscriber")) {
						visitorConnect(rs.getInt(2), "subscriber");
						Subscriber subscriber = null;
						CreditCard credit = null;
						if (rs.getString(8) != null) {
							System.out.println(rs.getString(8) + rs.getString(9) + rs.getInt(11) + rs.getInt(12)
									+ rs.getString(10));
							credit = new CreditCard(rs.getString(8), rs.getString(9), rs.getInt(11), rs.getInt(12),
									rs.getString(10));
						}

						subscriber = new Subscriber(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(13), credit);
						if (credit != null)
							System.out.println(subscriber.getCreditCard().getCardNumber());
						return new Message(OperationType.SubscriberLogin, ClientControllerType.VisitorController,
								(Object) subscriber);
					} else
						return new Message(OperationType.VisitorAlreadyLoggedIn, ClientControllerType.VisitorController,
								(Object) "You already logged in");

				} else {
					if (!isConnected(Integer.parseInt(info.get(0)), "visitor")) { // if the visitors is already
																					// connected to system
						visitorConnect(Integer.parseInt(info.get(0)), "visitor"); // insert the visitor to the connected
																					// visitors table
						pstm = sqlConnection.connection.prepareStatement("SELECT * from orders where visitorID=?");
						pstm.setString(1, info.get(0));
						rs = pstm.executeQuery();
						if (rs.next()) {
							return new Message(OperationType.VisitorWithOrderLogin,
									ClientControllerType.VisitorController, (Object) Integer.parseInt(info.get(0)));
						} else
							return new Message(OperationType.VisitorLogin, ClientControllerType.VisitorController,
									(Object) Integer.parseInt(info.get(0)));
					} else {// if the visitor is logged in already so send back to client this information
						
						return new Message(OperationType.VisitorAlreadyLoggedIn, ClientControllerType.VisitorController,
								(Object) "You already logged in");
					}

				}
			} catch (SQLException e) {
				System.out.println("CATCH");
				e.printStackTrace();
			}
			break;
		case EmployeeLogin:
			info = (ArrayList<String>) msgFromClient.getObj();
			Employee employee = null;
			try {
				pstm = sqlConnection.connection
						.prepareStatement("SELECT * from employees where userName=? and password=?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				ResultSet rs = pstm.executeQuery();
				// for case that employee didn't exist in system
				if (rs.next()) {
					if (!isConnected(rs.getInt(1), "employee")) {
						visitorConnect(rs.getInt(1), "employee");
						employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
						return new Message(OperationType.EmployeeLogin, ClientControllerType.EmployeeController,
								(Object) employee);
					}
					else {
						return new Message(OperationType.EmployeeAlreadyLoggedIn, ClientControllerType.EmployeeController,
								(Object)"You are already logged in");
					}
				}

				else {
					return new Message(OperationType.ErrorEmployeeLogin, ClientControllerType.EmployeeController,
							(Object) (new String("NotFound")));
				}
			} catch (SQLException e) {
				System.out.println("CATCH");
				e.printStackTrace();
			}
			break;
		case UserDisconnected:
			visitorDisconnected((int) msgFromClient.getObj());
			System.out.println("AAAAAAAAAAAA");
			return new Message(OperationType.UserDisconnectedSuccess, ClientControllerType.VisitorController, (Object)"Disconnected successfully");
		default:
			break;
		}
		return clientMsg;
	}

	private boolean isConnected(int id, String type) {
		PreparedStatement pstm;
		boolean isConnected = false;
		String query = "SELECT * FROM visitorsConnected WHERE id=? and type=?";

		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1, id);
			pstm.setString(2, type);
			ResultSet rs = pstm.executeQuery();
			if (rs.next())
				isConnected = true;
			else
				return isConnected;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isConnected;
	}

	private void visitorConnect(int id, String type) {
		PreparedStatement pstm;
		String query = "INSERT INTO visitorsConnected (id,type) VALUES (?,?)";
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1, id);
			pstm.setString(2, type);
			pstm.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void visitorDisconnected(int id) {
		PreparedStatement pstm;
		String query = "DELETE FROM visitorsConnected WHERE id=?";
		int val;
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1,id);
			val = pstm.executeUpdate();
			System.out.println(val);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
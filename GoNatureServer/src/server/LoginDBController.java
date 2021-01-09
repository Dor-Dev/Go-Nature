package server;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.CreditCard;
import logic.Employee;
import logic.Subscriber;

/**
 * Class controls the login feature. 
 * Access login to system if the user is not connected.
 * @author dorswisa
 *
 */
public class LoginDBController {
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;
	/*
	 * get the instance of the SQL singleton.
	 */
	public LoginDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Parse data for login feature.
	 * @param clientMsg get the message from the client.
	 * @return Message message to client with the relevant data according to the operation type.
	 * @throw {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public Message parseData(Message clientMsg) {
		PreparedStatement pstm;
		List<String> info;
		msgFromClient = clientMsg;	
		switch (msgFromClient.getOperationType()) {
		//login a visitor/subscriber, first check if he is not connected , then allow him to login.
		case VisitorLogin:
			try {
				info = (ArrayList<String>) msgFromClient.getObj(); // get the data of the visitor from text fields.
				pstm = sqlConnection.connection
						.prepareStatement("SELECT * from members where visitorID=? or memberNumber=?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(0));
				ResultSet rs = pstm.executeQuery();
				if (rs.next()) { //if he found the member/visitor is the members table.
					if (!isConnected(rs.getInt(2), "subscriber")) { //if the visitor is not connected already
						visitorConnect(rs.getInt(2), "subscriber"); //insert the visitor id and type to the connected table.
						Subscriber subscriber = null;
						CreditCard credit = null;
						if (rs.getString(8) != null) { //if the visitor have credit card 
							credit = new CreditCard(rs.getString(8), rs.getString(9), rs.getInt(11), rs.getInt(12),
									rs.getString(10));
						}

						subscriber = new Subscriber(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(13), credit);

						return new Message(OperationType.SubscriberLogin, ClientControllerType.VisitorController,
								(Object) subscriber); //Send message to client with the subscriber relevant data
					} else
						return new Message(OperationType.VisitorAlreadyLoggedIn, ClientControllerType.VisitorController,
								(Object) "You already logged in");

				} else {
					if(info.get(0).length()==10)
						return new Message(OperationType.MemberNumberNotExist,ClientControllerType.VisitorController,(Object)"Member not exist !"); //if there are no such member in the system .
					if (!isConnected(Integer.parseInt(info.get(0)), "visitor")) { // if the visitors is already
																					// connected to system
						visitorConnect(Integer.parseInt(info.get(0)), "visitor"); // insert the visitor to the connected
																					// visitors table
						pstm = sqlConnection.connection.prepareStatement("SELECT * from orders where visitorID=?");
						pstm.setString(1, info.get(0));
						rs = pstm.executeQuery();
						if (rs.next()) { //case we have random visitor with past orders.
							return new Message(OperationType.VisitorWithOrderLogin,
									ClientControllerType.VisitorController, (Object) Integer.parseInt(info.get(0)));
						} else
							return new Message(OperationType.VisitorLogin, ClientControllerType.VisitorController,
									(Object) Integer.parseInt(info.get(0))); //If the user is visitor with out any order.
					} else {// if the visitor is logged in already so send back to client this information
						
						return new Message(OperationType.VisitorAlreadyLoggedIn, ClientControllerType.VisitorController,
								(Object) "You already logged in");
					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			//case Employee login 
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
					if (!isConnected(rs.getInt(1), "employee")) { //check if the employee is already logged in.
						visitorConnect(rs.getInt(1), "employee"); //insert the employee to the connected visitors table.
						employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
						return new Message(OperationType.EmployeeLogin, ClientControllerType.EmployeeController,
								(Object) employee); //send the employee data to the client.
					}
					else {
						return new Message(OperationType.EmployeeAlreadyLoggedIn, ClientControllerType.EmployeeController,
								(Object)"You are already logged in"); //send message to client that he is already logged in.
					}
				}

				else {
					return new Message(OperationType.ErrorEmployeeLogin, ClientControllerType.EmployeeController,
							(Object) (new String("NotFound"))); //if there are no such employee in the data base send error message to client.
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			//case for user disconnected , remove the visitor from connected table , and return relevant message to client.
		case UserDisconnected:
			visitorDisconnected((int) msgFromClient.getObj());
			return new Message(OperationType.UserDisconnectedSuccess, ClientControllerType.VisitorController, (Object)"Disconnected successfully");
		default:
			break;
		}
		return clientMsg;
	}
	/**
	 * Check if the visitor is already connected.
	 * @param id
	 * @param type
	 * @return true if the visitors already connected otherwise false.
	 * @throws SQLException
	 */
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
			e.printStackTrace();
		}
		return isConnected;
	}
	/**
	 * Insert to visitors connected the visitor. 
	 * @param id the visitor id.
	 * @param type the visitor type.
	 * @throws SQLException
	 */
	private void visitorConnect(int id, String type) {
		PreparedStatement pstm;
		String query = "INSERT INTO visitorsConnected (id,type) VALUES (?,?)";
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1, id);
			pstm.setString(2, type);
			pstm.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Erase the visitor from the visitors connected table.
	 * @param id the visitor id.
	 * @throws SQLException
	 */
	private void visitorDisconnected(int id) {
		PreparedStatement pstm;
		String query = "DELETE FROM visitorsConnected WHERE id=?";
		int val;
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1,id);
			val = pstm.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
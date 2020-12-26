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
		info =  (ArrayList<String>) msgFromClient.getObj();
		switch(msgFromClient.getOperationType()) {
		case VisitorLogin:
			try {
				
				pstm = sqlConnection.connection.prepareStatement("SELECT * from members where visitorID=? or memberNumber=?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(0));
				
				ResultSet rs = pstm.executeQuery();
				
				if(rs.next()) { 
					Subscriber subscriber = null;
					CreditCard credit = null;
					if(rs.getString(8)!=null)
					{
						System.out.println(rs.getString(8)+rs.getString(9)+rs.getInt(11)+rs.getInt(12)+rs.getString(10));
						credit = new CreditCard(rs.getString(8),rs.getString(9),rs.getInt(11),rs.getInt(12),rs.getString(10));
					}
					
					subscriber=new Subscriber(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getString(13),credit);
					if(credit!=null)
					System.out.println(subscriber.getCreditCard().getCardNumber());
					return new Message(OperationType.SubscriberLogin, ClientControllerType.VisitorController,(Object)subscriber);
					
					
				}
				else {
					pstm = sqlConnection.connection.prepareStatement("SELECT * from orders where visitorID=?");
					pstm.setString(1, info.get(0));
					rs = pstm.executeQuery();
					if(rs.next()) { 
					return new Message(OperationType.VisitorWithOrderLogin, ClientControllerType.VisitorController,(Object)Integer.parseInt(info.get(0)));
					}
				
					else return new Message(OperationType.VisitorLogin, ClientControllerType.VisitorController,(Object)Integer.parseInt(info.get(0)));
				
			}
			}catch (SQLException e) {
				System.out.println("CATCH");
				e.printStackTrace();
			}
			break;
		case EmployeeLogin:
			Employee employee=null;
			try {
				pstm = sqlConnection.connection.prepareStatement("SELECT * from employees where userName=? and password=?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				System.out.println("AFTER QUERY");
				ResultSet rs = pstm.executeQuery();
				//for case that employee didn't exist in system
				if(rs.next()) { 
					employee = new Employee(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
					System.out.println(employee);
					return new Message(OperationType.EmployeeLogin, ClientControllerType.EmployeeController,(Object)employee);
				}
				
				else {
					System.out.println("NotFound");
					return new Message(OperationType.ErrorEmployeeLogin, ClientControllerType.EmployeeController,(Object)(new String("NotFound") ));
				}
			} catch (SQLException e) {
				System.out.println("CATCH");
				e.printStackTrace();
			}
			break;
			
		default:
			break;
		}
		return clientMsg;
	
	}
	
	

	
}
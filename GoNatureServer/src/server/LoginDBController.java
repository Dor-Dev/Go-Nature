package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.Message;
import enums.ClientControllerType;
import enums.DBControllerType;
import enums.OperationType;
import logic.Employee;
import logic.Visitor;

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
		PreparedStatement pstm;
		List<String> info;
		msgFromClient = clientMsg;
		info =  (ArrayList<String>) msgFromClient.getObj();
		switch(msgFromClient.getOperationType()) {
		case VisitorLogin:
			Visitor visitor = null;
			try {
				System.out.println("TRY IN LOGIN DB CONTROLLER" + info.get(0));
				pstm = sqlConnection.connection.prepareStatement("SELECT * from visitors where id=?");
				pstm.setString(1, info.get(0));
				System.out.println("AFTER QUERY");
				ResultSet rs = pstm.executeQuery();
				//for case that traveler didn't exist in system
				if(rs.next()) { 
					visitor = new Visitor(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getBoolean(4),rs.getString(5));
				}
				else {
					visitor = new Visitor(Integer.parseInt(info.get(0)));
				}
				
				return new Message(OperationType.VisitorLogin, ClientControllerType.VisitorController,(Object)visitor);
				
				
			} catch (SQLException e) {
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
					//System.out.println("employee: "+ rs.getString(1) +" "+ rs.getString(2)+" "+rs.getString(3)+rs.getString(4)+rs.getString(5)+rs.getString(6)+rs.getString(7)+rs.getString(8));
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

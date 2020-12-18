package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DBControllerType;
import common.Message;
import common.OperationType;

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
		case TravelerLogin:
			try {
				System.out.println("TRY IN LOGIN DB CONTROLLER" + info.get(0));
				pstm = sqlConnection.connection.prepareStatement("SELECT * from visitors where id=?");
				pstm.setString(1, info.get(0));
				System.out.println("AFTER QUERY");
				ResultSet rs = pstm.executeQuery();
				//for case that traveler didn't exist in system
				if(rs.next()) { 
					System.out.println(rs.getString(1));
				}
				else {
					System.out.println("NotFound");
					return null;
				}
				
				
			} catch (SQLException e) {
				System.out.println("CATCH");
				e.printStackTrace();
			}
			break;
		case EmployeeLogin:
			try {
				pstm = sqlConnection.connection.prepareStatement("SELECT * from employees where userName=? and password=?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				System.out.println("AFTER QUERY");
				ResultSet rs = pstm.executeQuery();
				//for case that employee didn't exist in system
				if(rs.next()) { 
					System.out.println("employee: "+ rs.getString(1) +" "+ rs.getString(2));
					//add return of employee//
				}
				else {
					System.out.println("NotFound");
					return new Message(OperationType.EmployeeLogin, DBControllerType.ErrorloginDBController,"NotFound" );
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

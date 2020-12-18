package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.Message;

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
	public static Message parseData(Message clientMsg) {
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
		default:
			break;
		}
		return clientMsg;
	
	}
	
	

	
}

package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Receipt;

public class ReceiptDBController {
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;
	public ReceiptDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Message parseData(Message clientMsg) {
		PreparedStatement pstm;
		msgFromClient = clientMsg;
		switch(msgFromClient.getOperationType()) {
		case GenerateReceipt:
			
		    LocalDate thisDay = LocalDate.now();
		    Date thisDayToDB = Date.valueOf(thisDay);
		    
		    LocalTime thisTime = LocalTime.now();
		    Time thisTimeToDB = Time.valueOf(thisTime);

			 List<String> info = (ArrayList<String>)msgFromClient.getObj();
			String query= "insert into receipts (date,visitEntry, visitExit,numberOfVisitors,type,parkName,orderNumber,visitorID)" + "values (?,?,?,?,?,?,?,?)";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				
				pstm.setDate(1, thisDayToDB);
				pstm.setTime(2, thisTimeToDB);
				pstm.setTime(2, thisTimeToDB);
				pstm.setInt(4, Integer.parseInt(info.get(1)));
				pstm.setString(5, info.get(3));
				pstm.setString(6, info.get(0));
				pstm.setInt(7, 0);
				pstm.setString(8, info.get(2));
				
				 pstm.execute();
				//for case that traveler didn't exist in system
				
				 pstm = sqlConnection.connection.prepareStatement("SELECT * from receipts where parkName=? and date=? and visitEntry=?");
				 pstm.setString(1, info.get(0));
				 pstm.setDate(2, thisDayToDB);
				 pstm.setTime(3, thisTimeToDB);
					
					ResultSet rs = pstm.executeQuery();
					if(rs.next()) { 
						System.out.println("receipt num: " +rs.getInt(1));
						Receipt receipt = new Receipt(rs.getInt(1), rs.getDate(2), rs.getTime(3), rs.getTime(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
						return new Message(OperationType.ReceiptInfo, ClientControllerType.ReceiptController,(Object) receipt);
					}
					return new Message(OperationType.ReceiptInfo, ClientControllerType.ReceiptController,(Object)"dont have a receipt");
					
				}
				
			
			catch (SQLException e) {
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
	



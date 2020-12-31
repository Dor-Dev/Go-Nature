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
	private PreparedStatement pstm;
	private ResultSet rs;
	 LocalTime thisTime;
	 Time thisTimeToDB;
	 LocalDate thisDay;
	 Date thisDayToDB;
	 int hours,minutes;

	public ReceiptDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	/**
	 * This is a method by which you can get all the necessary information related to receipts
	 * @param clientMsg
	 * @return
	 */
	public Message parseData(Message clientMsg) {
		try {

			msgFromClient = clientMsg;

			List<String> infoReceipt = (ArrayList<String>) msgFromClient.getObj();

			switch (msgFromClient.getOperationType()) {

			// this case is to update the receipts table with the amount of visitors that already exit
			case UpdateReceiptInfoAfterExit:
				rs = checkReceipt(msgFromClient.getObj());
				if (rs != null) {
					getCurrentTime();

					int currAmount = rs.getInt(10) - Integer.parseInt(infoReceipt.get(2));
					System.out.println("receip amount =" + rs.getInt(10));
					System.out.println("to decrese: " + Integer.parseInt(infoReceipt.get(2)));
					System.out.println("= " + currAmount);
					if (Integer.parseInt(infoReceipt.get(2)) > 0 && currAmount >= 0) {
						
						if(currAmount==0) {
							//if all the visitors for this receipt exit - update the hour of visitexit

						pstm = sqlConnection.connection.prepareStatement("UPDATE  receipts SET currAmountOfVisitorsLeft=? ,visitExit=?  where receiptsID=?");
						pstm.setInt(1, currAmount);
						pstm.setInt(2, hours);
						pstm.setInt(3, Integer.parseInt(infoReceipt.get(0)));
						}
						else {
							// else update only the amount of current visitors
							pstm = sqlConnection.connection.prepareStatement("UPDATE  receipts SET currAmountOfVisitorsLeft=?  where receiptsID=?");
							pstm.setInt(1, currAmount);
							pstm.setInt(2, Integer.parseInt(infoReceipt.get(0)));
							
						}
						

						int res = pstm.executeUpdate();
						if (res == 1) {
							System.out.println("jjjj");
							return new Message(OperationType.UpdateReceiptInfoAfterExit,ClientControllerType.ReceiptController, (Object) "sucsses to update exit");
						}
					}
				
			}
				return new Message(OperationType.UpdateReceiptInfoAfterExit, ClientControllerType.ReceiptController,(Object) "faild to update exit");

			

				// this case is to check the receipts table if there is a order number already
			case CheckReceiptInfo:
				try {
					pstm = sqlConnection.connection.prepareStatement("SELECT * from receipts where parkName=? and orderNumber=?");
					pstm.setString(1, infoReceipt.get(2));
					pstm.setInt(2, Integer.parseInt(infoReceipt.get(0)));
					System.out.println( infoReceipt.get(2));
					System.out.println(infoReceipt.get(0));
					System.out.println("im here1");
					ResultSet rs = pstm.executeQuery();
					if(rs.next()) {
						int receiptID;
						System.out.println("bbbbb");
						int numOfVisitor = rs.getInt(5);
						int actualVisitor= rs.getInt(12);
						int currAmountOfVisitorsLeft= rs.getInt(10);
						if(numOfVisitor>=actualVisitor+Integer.parseInt(infoReceipt.get(1))) {
							System.out.println("im here2");
							currAmountOfVisitorsLeft=currAmountOfVisitorsLeft+Integer.parseInt(infoReceipt.get(1));
							System.out.println(currAmountOfVisitorsLeft);
							actualVisitor+=Integer.parseInt(infoReceipt.get(1));
							System.out.println(actualVisitor);
							receiptID=rs.getInt(1);
							pstm = sqlConnection.connection.prepareStatement("UPDATE  receipts SET currAmountOfVisitorsLeft=? ,actualNumOfVisitors=?  where receiptsID=?");
							pstm.setInt(1,currAmountOfVisitorsLeft);
							pstm.setInt(2,actualVisitor);
							pstm.setInt(3, rs.getInt(1));
					
							int res=pstm.executeUpdate();
							System.out.println("res= " + res);
							if(res==1) {
								
								return new Message(OperationType.UpdateReceipt, ClientControllerType.ReceiptController,(Object)rs.getInt(1));
		
								}
						}
						else {
							
							return new Message(OperationType.UpdateReceipt, ClientControllerType.ReceiptController,(Object)"faild to update");	
						}
					}
					else{
						
						return new Message(OperationType.UpdateReceipt, ClientControllerType.ReceiptController,(Object)"never exist");
					}
					} catch(SQLException e) {}
				break;
				
				// this case is to generate a receipt and update  the receipts table 
			case GenerateReceipt:
				
				getCurrentTime();
				  System.out.println("hour="+hours);

					 infoReceipt = (ArrayList<String>)msgFromClient.getObj();
					String query= "insert into receipts (date,visitEntry, visitExit,numberOfVisitors,type,parkName,orderNumber,visitorID,currAmountOfVisitorsLeft,time,actualNumOfVisitors,cost)" + "values (?,?,?,?,?,?,?,?,?,?,?,?)";
					try {
						pstm = sqlConnection.connection.prepareStatement(query);
						
						pstm.setDate(1, thisDayToDB);
						if(infoReceipt.size()==7) {
						pstm.setInt(2, hours);
						pstm.setInt(3, hours+4);
						pstm.setInt(12, Integer.parseInt(infoReceipt.get(6)));
						}
						else {
							pstm.setInt(2, Integer.parseInt(infoReceipt.get(6)));
							pstm.setInt(3, Integer.parseInt(infoReceipt.get(6))+4);
							pstm.setInt(12, Integer.parseInt(infoReceipt.get(7)));
							
						}
						pstm.setInt(4, Integer.parseInt(infoReceipt.get(1)));
						pstm.setString(5, infoReceipt.get(3));
						pstm.setString(6, infoReceipt.get(0));
						pstm.setInt(7, Integer.parseInt(infoReceipt.get(4)));
						pstm.setInt(8, Integer.parseInt(infoReceipt.get(2)));
						pstm.setInt(9, Integer.parseInt(infoReceipt.get(5)));
						pstm.setTime(10, thisTimeToDB);
						pstm.setInt(11, Integer.parseInt(infoReceipt.get(5)));
						
						pstm.execute();
						//for case that traveler didn't exist in system
						
						 pstm = sqlConnection.connection.prepareStatement("SELECT * from receipts where parkName=? and date=? and time=? and visitorID=?");
						 pstm.setString(1, infoReceipt.get(0));
						 pstm.setDate(2, thisDayToDB);
						 pstm.setTime(3, thisTimeToDB);
						 pstm.setInt(4, Integer.parseInt(infoReceipt.get(2)));
							
							ResultSet rs = pstm.executeQuery();
							System.out.println("rs= " +rs);
							if(rs.next()) { 
								System.out.println("receipt num: " +rs.getInt(1));
								Receipt receipt = new Receipt(rs.getInt(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9),rs.getInt(13));
								return new Message(OperationType.CheckReceiptInfo, ClientControllerType.ReceiptController,(Object) receipt);
							}
							
								System.out.println("no no");
							return new Message(OperationType.CheckReceiptInfo, ClientControllerType.ReceiptController,(Object)"dont have a receipt");
							
						}
						
					
					catch (SQLException e) {
						System.out.println("CATCH");
						e.printStackTrace();
					}
						
				
					break;
			default:
				break;
		
		
			}
		} catch (Exception e) {
		}
		return clientMsg;

	}
	


	/**
	 * this function taking the date and time of today
	 */
	private void getCurrentTime() {

		thisDay = LocalDate.now();
		thisDayToDB = Date.valueOf(thisDay);
		thisTime = LocalTime.now();
		thisTimeToDB = Time.valueOf(thisTime);
		hours = thisTime.getHour();
		minutes = thisTime.getMinute();
		if (minutes > 0) {
			hours += 1;
		}

	}

	/**
	 * this function check if the receipt is exist
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	private ResultSet checkReceipt(Object obj) throws SQLException {
		List<String> infoReceipt = (ArrayList<String>) msgFromClient.getObj();
		try {
			pstm = sqlConnection.connection.prepareStatement("SELECT * from receipts where parkName=? and receiptsID=?");
			pstm.setString(1, infoReceipt.get(1));
			pstm.setInt(2, Integer.parseInt(infoReceipt.get(0)));
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				return rs;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

	


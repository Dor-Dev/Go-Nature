package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Order;
import logic.OrderRequest;

public class OrderDBController {

	private static SqlConnection sqlConnection = null;
	public static final int managerDefultTravelHour = 4;
	public OrderDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Object parseData(Message clientMsg) {


		switch (clientMsg.getOperationType()) {
		case AddOrder:
			return addOrder(clientMsg);
		case OrderCheckDateTime:
			return checkAvailableDateTime(clientMsg);
		case checkAvailableHours:
			return checkAlternativeDates(clientMsg);
		default:
			
			break;
		}
		return null;
	}
	public Object checkAlternativeDates(Message clientMsg) {
		
		OrderRequest request = (OrderRequest)clientMsg.getObj();
		int[] hoursSum = CheckSpecificDate(request.getAskdate());
		return new Message(OperationType.checkAvailableHours, ClientControllerType.OrderController,(Object)hoursSum);
	}

	public Message addOrder(Message clientMsg) {
		PreparedStatement preparedStmt;
		Order newOrder =(Order)clientMsg.getObj();
		System.out.println("Add Order start");
		// the mysql insert statement
		String query = " insert into orders (parkName, arrivalDate, visitorID, paidUp,visitorType,actualNumberOfVisitors,email,status,hourTime,cost,phoneNumber,orderApproved)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,?)";

		// create the mysql insert preparedstatement
		try {
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setString(1, newOrder.getParkName());
			preparedStmt.setString(2, newOrder.getDateTime());
			preparedStmt.setInt(3, newOrder.getVisitorID());
			preparedStmt.setBoolean(4, newOrder.isPaidUp());
			preparedStmt.setString(5, newOrder.getType());
			preparedStmt.setInt(6, newOrder.getNumOfVisitors());
			preparedStmt.setString(7, newOrder.getEmail());
			preparedStmt.setString(8, newOrder.getStatus());
			preparedStmt.setInt(9, newOrder.getHourTime());
			preparedStmt.setInt(10, newOrder.getCost());
			preparedStmt.setString(11, newOrder.getPhoneNumber());
			preparedStmt.setString(12, newOrder.getMsgStatus());
			System.out.println("number:" + newOrder.getPhoneNumber());
			// execute the preparedstatement
			preparedStmt.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Now we will take the orderID from the SQL SERVER (AUTO-INCRESMENT)
		 */
		query = " SELECT MAX(orderID) FROM orders where visitorID =? ";
		try {
			
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setInt(1, newOrder.getVisitorID());
			ResultSet rs = preparedStmt.executeQuery();
			if(rs.next()) {
				newOrder.setOrderID(rs.getInt(1));		//set OrderID to existing order object
			}
				System.out.println(rs.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * return message with the orderID from the SQL
		 */

		return new Message(OperationType.SuccessAddOrder, ClientControllerType.OrderController,(Object)(newOrder));
	}
	
	public Message checkAvailableDateTime(Message clientMsg)
	{ 
		boolean available = true;
		int i;
		int[] hourIndex;
		OrderRequest request = (OrderRequest)clientMsg.getObj();
		int enabledOrders = getHowManyOrdersAllows();
		hourIndex = CheckSpecificDate(request.getAskdate());
		
		/**this conditions check if this number of visitors can enter the park by checking if 
		 * at x hour + visitors > enabled orderd 
		 * the condision check 3 hours before the enter and 3 hours after
		 */
		for(i = 0;i<managerDefultTravelHour;i++)
		{	
			if(hourIndex[request.getHour()+i]+request.getNumOfvisitorAsked()>enabledOrders)
				available = false;
		}
		System.out.println("Available on orderdbcontrooler:"+available);
			return new Message(OperationType.OrderRequestAnswer, ClientControllerType.OrderController,(Object)available);
		
	}

	/**
	 * method to get from database the amount of orders the manager allow
	 * @return
	 */
	private int getHowManyOrdersAllows() {
		int enabledOrders = 0;
		ResultSet rs;
		PreparedStatement preparedStmt = null;
		String query = " SELECT ordersCapacity FROM parks where parkName=?";
		
		try {
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setString(1,"Luna-Park");
			rs = preparedStmt.executeQuery();
			
			
			if(rs.next())
				enabledOrders = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Did'nt find amount of enabled orders in parks");
			e.printStackTrace();
		}
		
		return enabledOrders;
	}
	
	/**
	 * method to get the park occupancy any hour in specipic date
	 * 
	 * @param date
	 * @return array of the occupancy while each cell represents an hour
	 */
	public int[] CheckSpecificDate(LocalDate date)
	{	
		System.out.println(date);
		int i;
		String query;
		int[] hourIndex = new int[24];
		ResultSet rs;
		PreparedStatement preparedStmt = null;
		query = "SELECT  hourTime , SUM(actualNumberOfVisitors) FROM orders WHERE arrivalDate=? group by hourTime";
		try {
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setString(1,date.toString());
			rs = preparedStmt.executeQuery();
			while(rs.next())
			{
				for(i=0;i<managerDefultTravelHour;i++)		// Represent an exact time
					hourIndex[rs.getInt(1)+i]+= rs.getInt(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(i=0;i<24;i++)
			System.out.println(i+" "+hourIndex[i]+ " ");

		return hourIndex;
		
	}

	

}

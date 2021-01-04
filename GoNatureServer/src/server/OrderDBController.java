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
import logic.Order;
import logic.OrderRequest;

public class OrderDBController {

	private static SqlConnection sqlConnection = null;
	public static final int managerDefultTravelHour = 4;
	public static OrderRequest request = null;
	LocalTime thisTime;
	 Time thisTimeToDB;
	 LocalDate thisDay;
	 Date thisDayToDB;
	 int hours,minutes;
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
		case getMyOrders:
			return showMyOrdersTable(clientMsg);
		case CancelOrder:
			return cancelOrder(clientMsg);
		case OrderFinalApproval:
			return orderFinalApprove(clientMsg);
		case GetOutFromWaitingList:
			return getOutFromWaitingList(clientMsg);
		case FindOrder:
			return findOrder(clientMsg);
		default:
			break;
		}
		return null;
	}
	
	/**
	 * This method searches for the visitor's ID  with the relevant date and time in the orders table.
	 * @param clientMsg
	 * @return
	 */
	private Object findOrder(Message clientMsg) {
		List <String> info = (ArrayList<String>) clientMsg.getObj();
		PreparedStatement pstm;
		getCurrentTime();
		 try {
			pstm = sqlConnection.connection.prepareStatement("SELECT * from orders where parkName=? and visitorID=? and arrivalDate=? and hourTime>=? and hourTime<=? and status='Approved'");

			pstm.setString(1, info.get(0));
			pstm.setString(2, info.get(1));
			pstm.setDate(3, thisDayToDB);
			pstm.setInt(4, hours-4);
			pstm.setInt(5, hours);
			
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				int orderID = rs.getInt(1);
				int numOfVisitor = rs.getInt(8);
				Order order = new Order(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getInt(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9),
						rs.getBoolean(10), rs.getInt(11));
				if(numOfVisitor>=Integer.parseInt(info.get(2))) {
					return new Message(OperationType.FindOrder,ClientControllerType.OrderController,(Object)order);
				}
				else {
					return new Message(OperationType.FindOrder,ClientControllerType.OrderController,(Object)"amount is not avilable");
					
				}
				
		 }
		 }catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
			return  new Message(OperationType.FindOrder,ClientControllerType.OrderController,(Object)"the order doesnt exist");
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
	 * The method changes the status of the visitor as soon as a place becomes available for him. 
	 * That it happens as soon as he gets a message that he needs to confirm the exit from the waiting list.
	 * @param clientMsg
	 * @return
	 */
	private Object getOutFromWaitingList(Message clientMsg) {
		Order tmp = (Order)clientMsg.getObj();
		int res = 0;
		String query = "UPDATE orders SET status='Received',msgStatus='Not sent' WHERE visitorID=? and orderID=? and parkName=?";
		PreparedStatement pstm;
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1, tmp.getVisitorID());
			pstm.setInt(2, tmp.getOrderID());
			pstm.setString(3, tmp.getParkName());
			res = pstm.executeUpdate();

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(res==1)
			return new Message(OperationType.WaitingListExitSuccess,ClientControllerType.OrderController,(Object)"You are not in waiting list");
		return null;
	}

	/**
	 * The method approve the order , this is the last stage of order approval
	 * @param clientMsg
	 * @return
	 */
	private Object orderFinalApprove(Message clientMsg) {
		Order tmp = (Order)clientMsg.getObj();
		int res = 0;
		String query = "UPDATE orders SET status='Approved' WHERE visitorID=? and orderID=? and parkName=?";
		PreparedStatement pstm;
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1, tmp.getVisitorID());
			pstm.setInt(2, tmp.getOrderID());
			pstm.setString(3, tmp.getParkName());
			res = pstm.executeUpdate();

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(res==1)
			return new Message(OperationType.ApproveOrderSuccess,ClientControllerType.OrderController,(Object)"The final approval was successful");
		return null;
	}

	/**
	 * The method cancel order by changing the status of the order to 'Canceled'
	 * @param clientMsg
	 * @return
	 */
	private Object cancelOrder(Message clientMsg) {
		Order tmp = (Order)clientMsg.getObj();
		int res = 0;
		String query = "UPDATE orders SET status='Canceled' WHERE visitorID=? and orderID=? and parkName = ?";
		PreparedStatement pstm;
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1, tmp.getVisitorID());
			pstm.setInt(2, tmp.getOrderID());
			pstm.setString(3, tmp.getParkName());
			res = pstm.executeUpdate();

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(res==1)
			return new Message(OperationType.CancelOrderSuccess,ClientControllerType.OrderController,(Object)"Cancel Success");
		return null;
		
	}

	/**
	 * Get the relevant orders for visitor id or subscriber id 
	 * @param clientMsg
	 * @return
	 */
	private Object showMyOrdersTable(Message clientMsg) {
		List<Order> myOrders = new ArrayList<Order>();
		int id = (int) clientMsg.getObj();
		Order tmp;
		PreparedStatement pstm;
		LocalDate thisDay = LocalDate.now();
		Date thisDayToDB=Date.valueOf(thisDay); 
		String query = "SELECT * from orders WHERE visitorID = ? and arrivalDate >= ?";
		
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setInt(1, id);
			pstm.setDate(2, thisDayToDB);

			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				tmp = new Order(rs.getString(2),rs.getInt(4),rs.getInt(9),rs.getInt(8),rs.getString(7),rs.getString(13),rs.getDate(3),rs.getInt(1));
				myOrders.add(tmp);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Message(OperationType.ReturnMyOrders,ClientControllerType.OrderController,(Object)myOrders);
		
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
		String query = " insert into orders (parkName, arrivalDate, visitorID, paidUp,visitorType,actualNumberOfVisitors,email,status,hourTime,cost,phoneNumber,msgStatus)"
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
		request = (OrderRequest)clientMsg.getObj();
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
			preparedStmt.setString(1,request.getParkName());
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
		query = "SELECT  hourTime , SUM(actualNumberOfVisitors) FROM orders WHERE arrivalDate=? AND parkName=? group by hourTime";
		try {
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setString(1,date.toString());
			preparedStmt.setString(2,request.getParkName());
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
		hourIndex[0] = getHowManyOrdersAllows();			//how many orders allow in index 0

		return hourIndex;
		
	}

	

}

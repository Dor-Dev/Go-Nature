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

/**
 * 
 * @author Naor
 *The controller that is responsible for the connection with the data base regarding the orders
 */
public class OrderDBController {



	LocalTime thisTime;
	 Time thisTimeToDB;
	 LocalDate thisDay;
	 Date thisDayToDB;
	 int hours,minutes;

	private static SqlConnection sqlConnection = null;		//singletone 
	public static final int managerDefultTravelHour = 4;	//manager deafult 4 hours stay 
	public static OrderRequest request = null;	//request to check

	
	public OrderDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	/**
	 * This function navigate all the requests from the client
	 * to the asked functions
	 * @param clientMsg
	 * @return Message to client with the relevant object information
	 */
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
		case checkEventDiscount:
			return checkEventDiscount(clientMsg);
		default:
			break;
		}
		return null;
	}
	
	/**
	 * This function check if there is a discount event
	 * The function use the asked date that received from the client to check
	 * @param clientMsg
	 * @return array list, if dont found event discount return int parameter (Zero), 
	 * if found return the amount of the discount and String with the name of the event
	 */
	private Object checkEventDiscount(Message clientMsg) {
		
		int discount = 0;
		String eventName = "";
		PreparedStatement pstm;
		List<String> eventInfo = new ArrayList<String>();
		OrderRequest request = (OrderRequest)clientMsg.getObj();
		String query = "SELECT startDate, endDate,discount,eventName FROM eventrequests WHERE ? between startDate and endDate AND status='Active' AND parkName=?";
		
		try {
			pstm = sqlConnection.connection.prepareStatement(query);
			pstm.setString(1, request.getAskdate().toString());
			pstm.setString(2, request.getParkName());
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				discount = rs.getInt(3);
				eventName = rs.getString(4);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		eventInfo.add(String.valueOf(discount));
		eventInfo.add(eventName);
		return new Message(OperationType.EventDiscountAmount,ClientControllerType.OrderController,eventInfo);
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
		if(res==1) {
			WaitingListMessagesDBController waitingList = new WaitingListMessagesDBController();
			waitingList.notifyTheNextOrderInTheWaitingList(tmp);
			return new Message(OperationType.CancelOrderSuccess,ClientControllerType.OrderController,(Object)"Cancel Success");
		}
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
				tmp = new Order(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getInt(4),rs.getString(5),rs.getString(6),
						rs.getString(7),rs.getInt(8),rs.getInt(9),rs.getBoolean(10), rs.getInt(11),rs.getString(12),rs.getString(13),rs.getInt(14));
				myOrders.add(tmp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Message(OperationType.ReturnMyOrders,ClientControllerType.OrderController,(Object)myOrders);
		
	}

	/**
	 * this method get order request
	 * and return array with the number of visitors each hour at this date
	 * @param clientMsg
	 * @return array with the number of visitors each hour at this order request date
	 */
	public Object checkAlternativeDates(Message clientMsg) {
		
		OrderRequest request = (OrderRequest)clientMsg.getObj();
		int[] hoursSum = CheckSpecificDate(request.getAskdate());
		return new Message(OperationType.checkAvailableHours, ClientControllerType.OrderController,(Object)hoursSum);
	}

	/**
	 * 
	 * the method get message with object of Order, after the check if there is avaiable place.
	 * the order is saved into our order table in the database with all parameters.
	 * after the insert, more one query is running to get the orderID from the database (auto incresment)
	 * and set the value to the Order.
	 * 
	 */
	
	public Message addOrder(Message clientMsg) {
		PreparedStatement preparedStmt;
		Order newOrder =(Order)clientMsg.getObj();
		// the mysql insert statement
		String query = " insert into orders (parkName, arrivalDate, visitorID, paidUp,visitorType,actualNumberOfVisitors,email,status,hourTime,cost,phoneNumber,msgStatus,discount)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,?)";

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
			preparedStmt.setInt(13, newOrder.getDiscount());
			// execute the preparedstatement
			preparedStmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		 // Now we will take the orderID from the SQL SERVER (AUTO-INCRESMENT)
		 
		query = " SELECT MAX(orderID) FROM orders where visitorID =? ";
		try {
			
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setInt(1, newOrder.getVisitorID());
			ResultSet rs = preparedStmt.executeQuery();
			if(rs.next()) {
				newOrder.setOrderID(rs.getInt(1));		//set OrderID to existing order object
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/**
		 * return message with the orderID from the SQL
		 */

		return new Message(OperationType.SuccessAddOrder, ClientControllerType.OrderController,(Object)(newOrder));
	}
	
	/**
	 * this method get object in the Message
	 * object of OrderRequest with Date, hour and num of visitors
	 * and check if available place for the order .
	 * @param clientMsg
	 * @return true/false if hava available place for the num of visitors in the order
	 */
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
		int i;
		String query;
		int[] hourIndex = new int[24];
		ResultSet rs;
		PreparedStatement preparedStmt = null;
		query = "SELECT  hourTime , SUM(actualNumberOfVisitors) FROM orders WHERE arrivalDate=? AND parkName=? AND status<>'Canceled' AND status<>'Waiting list' group by hourTime";
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
			e.printStackTrace();
		}

		hourIndex[0] = getHowManyOrdersAllows();			//how many orders allow in index 0

		return hourIndex;
		
	}

	

}

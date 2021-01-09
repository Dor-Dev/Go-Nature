package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import logic.Order;

/**
 * 
 * @author dana_
 *A class that responsible for finding the next order in line of the waiting list
 *and sending simulation messages to the order owner that informs him
 * that a space became available at the park,that we need his approval within an hour to continue and add the order.
 *The class also responsible for the automatic cancellation of an order after an hour (when a traveler did not gave his approval)
 *
 */

public class WaitingListMessagesDBController {
	private static SqlConnection sqlConnection = null;
	//To Save he date of tomorrow
	public static java.sql.Date tommorow;
	
	
	
	public WaitingListMessagesDBController() {
		//Save he date of tomorrow
		 Date date = new Date();
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 c.add(Calendar.DATE, 1);
		 date=c.getTime();
		 tommorow = new java.sql.Date(date.getTime());
		 
		 //Get SQL connection
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	
/**
 * The method send the next visitor in line with an order in the waiting list a message .
 * The message informs the visitor that a space became available at the park,that we need his approval within an hour to continue and add the order.
 * The method also calls {@link #updateMessageStatus(int, String)} update the message status in the DB to "Sent"
 * After an hour if the visitor did not give his approval to add the order - the order is canceled 
 * @param order The method gets the canceled order from the client
 */
	public void notifyTheNextOrderInTheWaitingList(Order order) {
		ArrayList<Object> nextOrderInWaitingList;
		String messageContent;
		String messageTitle;
		java.sql.Date date = order.getDate();
		int arrivalTime =order.getHourTime();
		String parkName=order.getParkName();
		
		//Date validation
		if (!dateValidation(date))return;
		
		//Calculate the open space (amount of people)
		int [] amountOfVisitorsAvailable=calculateTheOpenSpace(date,parkName,"Received");
		
		//If there is no open space return
		int count=0;
		for(int i=0;i<amountOfVisitorsAvailable.length;i++) {
			if(i>arrivalTime-OrderDBController.managerDefultTravelHour-1 && i<arrivalTime+OrderDBController.managerDefultTravelHour+1)
				count++;
		}
		if(count==0)return;
		
		//Get the next order in the waiting list
		
		nextOrderInWaitingList=selectTheNextOrderInTheWaitingList( "Waiting list","Not sent",date,arrivalTime,amountOfVisitorsAvailable,parkName);
		 //If there isn't next order in line return
		if(nextOrderInWaitingList==null) return;
		int nextOrderID=(int)nextOrderInWaitingList.get(0);
		String nextEmail=(String) nextOrderInWaitingList.get(2);
		int nextAmountOfVisitors=(int)nextOrderInWaitingList.get(3);
		String phoneNumber = (String)nextOrderInWaitingList.get(4);
		
		//Create the message title and content
		messageTitle="A space became available at the park";
		messageContent="Simulation"+"\r\n"+"\r\n"+"Order number " + nextOrderID+ " is now available!\r\n" + 
				"You have 1 hour to confirm your order.\r\n" + 
				"Or else the order will be automatically canceled.\r\n" + 
				"Go to:"+"\r\n"+" Go-Nature system ---> 'My Orders' "+"\r\n"+"and approve your order.\r\n "+
				"\r\n"+"\r\n" +"This message was send to "+nextEmail+" and "+phoneNumber+" .";
		
		JOptionPane.showMessageDialog(null, messageContent,messageTitle,JOptionPane.INFORMATION_MESSAGE);
		updateMessageStatus(nextOrderID, "Sent");
		
		checkWaitingListStatus(order,nextAmountOfVisitors,nextOrderID);
	}
	
/**
 * A method that executes a Runnable in a new thread that waits 1 hour and than checks if the visitor gave his approval to add the order-
 * If the visitor did not give his approval to add the order - the order is canceled (calls {@link #updateOrderStatus(int, String, String)} that updates the order status in the DB to "Canceled") and we start the process to find the next visitor in line
 * The method also checks if the visitor gave his approval - if the amount of visitors of the recent approved order is smaller than the amount of visitors of the canceled order
 * than the process will start again with the difference between those 2 amounts.
 * @param order The next order in line
 * @param amountOfVisitorsOfNextOrder	The amount of visitors of the next order in line
 * @param orderID	The ID of the next order in line
 */
	public void checkWaitingListStatus(Order order,int amountOfVisitorsOfNextOrder,int orderID) {
		
		Thread t= new Thread(new Runnable() {
			
			@Override
			public void run() {
				//Sleep for 1 hour -For the simulation we will change it to several seconds instead of 1 hour so the messages will be send at that certain moment  
				int sleepPeriodTime =1000 *60*60 ;
			    try {
			        Thread.sleep(sleepPeriodTime);
			        //Updates the order status of the orders that were not approved to "Canceled".
			        int ifOrderWasCanceled =updateOrderStatus(orderID,"Canceled","Waiting list");
			        if(ifOrderWasCanceled==1)
			        	notifyTheNextOrderInTheWaitingList(order);
			        else if(order.getNumOfVisitors()-amountOfVisitorsOfNextOrder>0) {
			        	order.setNumOfVisitors(order.getNumOfVisitors()-amountOfVisitorsOfNextOrder);
			        	notifyTheNextOrderInTheWaitingList(order);
			        }			        
			    } catch (InterruptedException ex) {}
				
			}
		});
		t.start();
		
	}
/**
 * The method executes a query that returns the  orderID, hourTime, email, actualNumberOfVisitors, phoneNumber 
 * of all the orders that are in the waiting list for a specific park in a specific date and time range
 * The method calls {@link #checkTheNextOrderInTheWaitingList(ArrayList, int[])} that returns the next order in line
 * @param orderStatus "Waiting list"
 * @param msgStatus	"Not sent"
 * @param arrivalDate The date of the canceled order
 * @param hourTime	The arrival hour of the canceled order
 * @param numberOfVisitors  An array with all the visitors amounts that can enter the park in each hour at a given date 
 * @param parkName	The park name of the park of the canceled order
 * @return	The next order in line
 */

	public ArrayList<Object> selectTheNextOrderInTheWaitingList( String orderStatus,String msgStatus,java.sql.Date arrivalDate, int hourTime, int[] numberOfVisitors, String parkName)  {
		PreparedStatement pstm;
		ArrayList<Object> orderDetails = new ArrayList<>();
		ArrayList<ArrayList<Object>> AllordersDetails = new ArrayList<>();
		try { 
			pstm = sqlConnection.connection.prepareStatement("SELECT orderID, hourTime, email, actualNumberOfVisitors, phoneNumber FROM orders WHERE parkName=? AND arrivalDate=? AND hourTime > ? AND hourTime < ? AND status=?  AND msgStatus=? GROUP BY orderID");
			pstm.setString(1,parkName);
			pstm.setDate(2,  arrivalDate);
			pstm.setInt(3, hourTime-OrderDBController.managerDefultTravelHour-1);
			pstm.setInt(4, hourTime+OrderDBController.managerDefultTravelHour+1);
			pstm.setString(5, orderStatus);
			pstm.setString(6,msgStatus);
			ResultSet rs = pstm.executeQuery();
		
	      // Get the java result set
		    while (rs.next())
		    {
		      orderDetails.add(rs.getInt("orderID"));
		      orderDetails.add(rs.getInt("hourTime"));
		      orderDetails.add(rs.getString("email"));
		      orderDetails.add(rs.getInt("actualNumberOfVisitors"));
		      orderDetails.add(rs.getString("phoneNumber"));
		      AllordersDetails.add(orderDetails);
		      orderDetails = new ArrayList<>();
		    }	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println(AllordersDetails);
		return checkTheNextOrderInTheWaitingList(AllordersDetails,numberOfVisitors);
		
	}
	/**
	 * The Method takes each time the next order in line and checks if the order could exit the waiting list
	 * @param AllordersDetails An ArrayList with all the orders detail of the orders that are in the waiting list for a specific park in a specific date and time range
	 * @param numberOfVisitors	An array with all the visitors amounts that can enter the park in each hour at a given date 
	 * @return The next order in line
	 */
	public  ArrayList<Object> checkTheNextOrderInTheWaitingList(ArrayList<ArrayList<Object>> AllordersDetails,int[] numberOfVisitors){
		boolean canBeAdded=true;
		for(ArrayList<Object> orderDetails:AllordersDetails) {
			int hour=(int)orderDetails.get(1);
			canBeAdded=true;
			for(int i =hour;i < OrderDBController.managerDefultTravelHour+1;i++) {
				if(!canBeAdded)break;
				if((int)orderDetails.get(3)> numberOfVisitors[hour])canBeAdded=false;
			}
			if(canBeAdded)return orderDetails;
		}
		return null;
	}
	/**
	 * 	The method executes select query that return the orders capacity
	 * Than the method converts the amountOfVisitorsEachHour array from an array with all the amounts of visitors in each hour at a given date
	 * to an array with all the visitors amounts that can enter the park in each hour at a given date 
	 * @param arrivalDate The date of the canceled order
	 * @param parkName	The park name of the canceled order
	 * @param orderStatus "Received"
	 * @return	An array with all the visitors amounts that can enter the park in each hour at a given date 
	 */
	public int[] calculateTheOpenSpace(java.sql.Date arrivalDate, String parkName, String orderStatus) {
		PreparedStatement pstm;
		int [] amountOfVisitorsEachHour = CheckSpecificDate(arrivalDate,parkName,orderStatus);
		int ordersCapacity=0;
		try { 
		    
		    pstm = sqlConnection.connection.prepareStatement("SELECT ordersCapacity FROM parks WHERE parkName=? ");
			pstm.setString(1,  parkName);
			ResultSet rs = pstm.executeQuery();
			
	      // Get the java result set
		    if (rs.next())
		    {
		    	ordersCapacity=rs.getInt("ordersCapacity");
		    }	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		for (int i=0;i<amountOfVisitorsEachHour.length;i++) {
			amountOfVisitorsEachHour[i]=ordersCapacity-amountOfVisitorsEachHour[i]>0 ? ordersCapacity-amountOfVisitorsEachHour[i]:0;
		}
		return amountOfVisitorsEachHour;
	}
	
/**
 * Method that returns an array with all the amounts of visitors in each hour at a given date 
 * The method take into consideration the visit period of time that was set by the park manager -  default :4 hours 
 * @param date The date of the canceled order
 * @param parkName The park name of the park of the canceled order
 * @param orderStatus "Received"
 * @return
 */
	public int[] CheckSpecificDate(java.sql.Date date,String parkName,String orderStatus)
	{	
		int i;
		String query;
		int[] hourIndex = new int[24];
		ResultSet rs;
		PreparedStatement preparedStmt = null;
		query = "SELECT  hourTime , SUM(actualNumberOfVisitors) FROM orders WHERE arrivalDate=?  AND parkName=? AND status = ? group by hourTime";
		try {
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setString(1,date.toString());
			preparedStmt.setString(2, parkName);
			preparedStmt.setString(3, orderStatus);
			rs = preparedStmt.executeQuery();
			while(rs.next())
			{
				for(i=0;i<OrderDBController.managerDefultTravelHour;i++)		// Represent an exact time
					hourIndex[rs.getInt(1)+i]+= rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hourIndex;
		
	}
	
/**
 *An order can be canceled till the day before the the arrival date
 *We decided an order can be made two days before the arrival date so there will be an approval process a day before the arrival date 
 * @param arrivalDate The date of the canceled order
 * @return true if the date is valid or else false
 */
	public Boolean dateValidation(java.sql.Date arrivalDate) {
		if(arrivalDate.toString().equals(tommorow.toString())) {
			System.out.println("Invalid date");
			return false;}
		return true;
	}
	
/**
 * A method that execute UPDATE query and updates the message status of a given order
 * @param orderID	The ID of the order to be updated
 * @param newMessageStatus	The status we want it to be
 */
	public static void updateMessageStatus(int orderID,String newMessageStatus) {
		PreparedStatement pstm;
		try {
			pstm = sqlConnection.connection.prepareStatement("UPDATE  orders SET msgStatus=? WHERE orderID=?");
			pstm.setString(1, newMessageStatus);
			pstm.setInt(2, orderID);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
/**
 *  A method that execute UPDATE query and updates the order status of a given order
 * @param orderID	The ID of the order to be updated
 * @param newOrderStatus	The new order status
 * @param unwantedOrderStatus	The old order status
 * @return
 */
	public static int updateOrderStatus(int orderID,String newOrderStatus,String unwantedOrderStatus) {
		PreparedStatement pstm;
		int rs=0;
		try {
			pstm = sqlConnection.connection.prepareStatement("UPDATE  orders SET status=? WHERE orderID=? AND status=?");
			pstm.setString(1, newOrderStatus);
			pstm.setInt(2, orderID);
			pstm.setString(3, unwantedOrderStatus);
			rs =pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;

	}

}

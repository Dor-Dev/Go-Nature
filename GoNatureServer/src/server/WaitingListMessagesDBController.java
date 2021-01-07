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
	 * The method also update the message status in the DB to "Sent"
	 * After an hour if the visitor did not give his approval to add the order - the order is canceled 
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
		int amountOfVisitorsAvailable=calculateTheOpenSpace(date,arrivalTime,parkName,"Received");
		
		//If there is no open space return
		if(amountOfVisitorsAvailable<=0)return;
		
		//Get the next order in the waiting list
		
		nextOrderInWaitingList=selectTheNextOrderInTheWaitingList( "Waiting list","Not sent",date,arrivalTime,amountOfVisitorsAvailable,parkName);
		 //If there isn't next order in line return
		if(nextOrderInWaitingList.isEmpty()) return;
		int nextOrderID=(int)nextOrderInWaitingList.get(0);
		String nextEmail=(String) nextOrderInWaitingList.get(1);
		int nextAmountOfVisitors=(int)nextOrderInWaitingList.get(2);
		String phoneNumber = (String)nextOrderInWaitingList.get(3);
		
		//Create the message title and content
		messageTitle="A space became available at the park";
		messageContent="Simulation"+"\r\n"+"\r\n"+"Order number" + nextOrderID+ " is now available!\r\n" + 
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
	 * If the visitor did not give his approval to add the order - the order is canceled (updates the order status in the DB to "Canceled") and we start the process to find the next visitor in line
	 * The method also checks if the visitor gave his approval - if the amount of visitors of the recent approved order is smaller than the amount of visitors of the canceled order
	 * than the process will start again with the difference between those 2 amounts.
	 */
	public void checkWaitingListStatus(Order order,int amountOfVisitorsOfNextOrder,int orderID) {
		
		Thread t= new Thread(new Runnable() {
			
			@Override
			public void run() {
				//Sleep for 1 hour -For the simulation we will change it to several seconds instead of 1 hour so the messages will be send at that certain moment  
				int sleepPeriodTime =1000 * 60 *60 ;
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
	 * A method that execute SELECT query and returns the orderID, email, actualNumberOfVisitors, phoneNumber of an order
	 * that is scheduled for the given date,that has the given arrival time (specific hour),
	 *  the given message and order statuses and no more than a given amount of visitors.
	 * The method returns the SELECT result as an objects array list
	 */
	public ArrayList<Object> selectTheNextOrderInTheWaitingList( String orderStatus,String msgStatus,java.sql.Date arrivalDate, int hourTime, int numberOfVisitors, String parkName)  {
		PreparedStatement pstm;
		ArrayList<Object> orderDetails = new ArrayList<>();
		try { 
			pstm = sqlConnection.connection.prepareStatement("SELECT MIN(orderID), email, actualNumberOfVisitors, phoneNumber FROM orders WHERE parkName=? AND arrivalDate=? AND hourTime=? AND status=? AND actualNumberOfVisitors<= ? AND msgStatus=? GROUP BY orderID");
			pstm.setString(1,parkName);
			pstm.setDate(2,  arrivalDate);
			pstm.setInt(3, hourTime);
			pstm.setString(4, orderStatus);
			pstm.setInt(5, numberOfVisitors);
			pstm.setString(6,msgStatus);
			ResultSet rs = pstm.executeQuery();
		
	      // Get the java result set
		    if (rs.next())
		    {
		      orderDetails.add(rs.getInt("MIN(orderID)"));
		      orderDetails.add(rs.getString("email"));
		      orderDetails.add(rs.getInt("actualNumberOfVisitors"));
		      orderDetails.add(rs.getString("phoneNumber"));
		    }	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return orderDetails;
		
	}
	
	public int calculateTheOpenSpace(java.sql.Date arrivalDate, int arrivalHour, String parkName, String orderStatus) {
		PreparedStatement pstm;
		int [] amountOfVisitorsEachHour = CheckSpecificDate(arrivalDate,parkName,orderStatus);
		int amountOfTravelers=amountOfVisitorsEachHour[arrivalHour];
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
		return ordersCapacity-amountOfTravelers;
	}
	
	/**
	 * Method that returns an array with all the visitor in each hour at a given date 
	 * The method take into consideration the visit period of time that was set by the park manager -  default :4 hours 
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
	
	/**An order can be canceled till the day before the the arrival date
	*We decided an order can be made two days before the arrival date so there will be an approval process a day before the arrival date 
	**/
	public Boolean dateValidation(java.sql.Date arrivalDate) {
		if(arrivalDate.toString().equals(tommorow.toString())) {
			System.out.println("Invalid date");
			return false;}
		return true;
	}
	
	/**
	 * A method that execute UPDATE query and updates the message status of a given order
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
	 * A method that execute UPDATE query and updates the order status of a given order
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

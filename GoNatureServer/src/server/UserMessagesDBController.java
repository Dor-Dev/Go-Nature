package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import runner.PeriodicallyRunner;

/**
 * 
 * @author dana_
 *A class that responsible for sending remainders messages simulation (with an "approve order" request) .
 *The class also responsible for automatic cancellation of an order after 2 hours (when a traveler hasn't approved his arrival two hours after)
 *If an order is been canceled a message will be send to the traveler
 * *An instance of the class will be created each time the main server is executed , and the main server will call the method {@link #sendReminderMessage()}
 */


public class UserMessagesDBController {
	
	//Create time variables to send reminder message simulation a day before a order is scheduled 
	//And create time variables to send a message if the arrival hasn't been approved two hours later, it will cancellation message
	
	private int targetHour1,targetHour2;
	private int targetMinutes1,targetMinutes2;
	private long delta1,delta2;
	 //Get SQL connection
	private static SqlConnection sqlConnection = SqlConnection.getConnection();

	
	//Reuse of PeriodicallyRunner from external library
	//PeriodicallyRunner is a class that execute a specific runnable at specific starting hour and starting minutes and repeat every delta units of time  
	@SuppressWarnings("unused")
	private PeriodicallyRunner runnerToSendReminderADayBeforeArrival ;
	@SuppressWarnings("unused")
	private PeriodicallyRunner runnerToSendCancelationMsgAfterTwoHours ;
	//To Save he date of tomorrow
	public static java.sql.Date tommorow; 
	
	//The time unit is days because each day at a given time we will run the check and send the suitable messages.
	public static TimeUnit simulationTimeUnit =TimeUnit.DAYS;
		
	
	/**
	 * Constructor
	 * @param targetHour1 The execution hour of the first Runnable
	 * @param targetHour2	The execution hour of the second Runnable
	 * @param targetMinutes1	The execution minutes of the first Runnable
	 * @param targetMinutes2	The execution minutes of the second Runnable
	 * @param delta1	The period of time that will pass between each execution of the first Runnable
	 * @param delta2	The period of time that will pass between each execution of the second Runnable
	 */
	
	public UserMessagesDBController(int targetHour1, int targetHour2, int targetMinutes1,int targetMinutes2, long delta1,long delta2) {
		this.targetHour1 = targetHour1;
		this.targetHour2 = targetHour2;
		this.targetMinutes1 = targetMinutes1;
		this.targetMinutes2 = targetMinutes2;
		this.delta1 = delta1;
		this.delta2=delta2;
		
		//Save he date of tomorrow
		 Date date = new Date();
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 c.add(Calendar.DATE, 1);
		 date=c.getTime();
		 tommorow = new java.sql.Date(date.getTime());
		 
	}

	/**
	 * A method that creates a PeriodicallyRunner and provide it a runnable that create a pop up with the reminder message simulation
	 * PeriodicallyRunner will execute the runnable at specific starting hour and starting minutes and repeat every delta units of time 
	 * The method also updates the message status at the data base to 
	 */
	public void sendReminderMessage()  {
	
	    runnerToSendReminderADayBeforeArrival = new PeriodicallyRunner(targetHour1, targetMinutes1, simulationTimeUnit , delta1, new Runnable() {
			public void run() {
				ArrayList<ArrayList<Object>> ordersList=UserMessagesDBController.selectOrdersdetailsWhoScheduledForTomorrowByTheirAprovalStatus("Received","Not sent");
				for(ArrayList<Object> order : ordersList){
					String title="Confirm your arrival to the park tomorrow";
					String messageContent="Simulation"+"\r\n"+"\r\n"+"Order number "+order.get(0) + " is scheduled for tomorrow, please confirm your arrival.\r\n" + "You have 2 hours to confirm or cancel you arrival – \r\n" + 
			    			"Or else the order will be automatically canceled. \r\n"+"Go to:"+"\r\n"+"Go-Nature system ---> 'My Orders'"+"\r\n"+"and approve your order.\r\n "+"\r\n"+"\r\n" +"This message was send to "+order.get(1)+" and "+order.get(2)+" .";

					JOptionPane.showMessageDialog(null, messageContent,title,JOptionPane.INFORMATION_MESSAGE);
				}
				UserMessagesDBController.updateMessageStatus("Received", "Sent", "Not sent");
			}
	    });		
	    
      runnerToSendCancelationMsgAfterTwoHours = new PeriodicallyRunner(targetHour2, targetMinutes2, simulationTimeUnit , delta2, new Runnable() {
			public void run() {
				ArrayList<ArrayList<Object>> ordersList=UserMessagesDBController.selectOrdersdetailsWhoScheduledForTomorrowByTheirAprovalStatus("Received","Sent");
				for(ArrayList<Object> order : ordersList){
					String title="Your order was canceled";
					String messageContent="Simulation"+"\r\n"+"\r\n"+"Your confirmation period of time has ended-\r\n" + "Order number "+order.get(0)+" has been canceled.\r\n"+"\r\n"+"\r\n" +"This message was send to "+order.get(1)+" and "+order.get(2)+" .";
							
					JOptionPane.showMessageDialog(null, messageContent,title,JOptionPane.INFORMATION_MESSAGE);
				}
				UserMessagesDBController.updateOrderStatus("Canceled", "Received", "Sent");
			}
      });
	}
	
	
/**
 * A method that execute SELECT query that returns the orderID, email, phoneNumber of all the orders which are scheduled for tomorrow and have the given message and order statuses
 * @param orderStatus	The wanted order status
 * @param msgStatus		The wanted message status
 * @return	The SELECT result as an array list of objects array list
 */
	public static ArrayList<ArrayList<Object>> selectOrdersdetailsWhoScheduledForTomorrowByTheirAprovalStatus( String orderStatus,String msgStatus )  {
		PreparedStatement pstm;
		ArrayList<Object> orderDetails = new ArrayList<>();
		ArrayList<ArrayList<Object>> allOrdersDetails= new ArrayList<>();
		try { 
			pstm = sqlConnection.connection.prepareStatement("SELECT orderID, email, phoneNumber FROM orders WHERE arrivalDate=? AND status=? AND msgStatus=?");
			pstm.setDate(1,  tommorow);
			pstm.setString(2, orderStatus);
			pstm.setString(3,msgStatus);
			ResultSet rs = pstm.executeQuery();
		
	      // iterate through the java result set
		    while (rs.next())
		    {
		      orderDetails.add(rs.getInt("orderID"));
		      orderDetails.add(rs.getString("email"));
		      orderDetails.add(rs.getString("phoneNumber"));
		      allOrdersDetails.add(orderDetails);
		      orderDetails=new ArrayList<Object>();
		    }		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return allOrdersDetails;
		
	}
	
	
/**
 * A method that execute UPDATE query and updates  the order status of orders which are scheduled for tomorrow and have the given message and order statuses 
 * @param newOrderStatus The new wanted order status
 * @param oldOrderStatus	The old order status
 * @param messageStatus		The message status of the order
 */
	public static void updateOrderStatus(String newOrderStatus,String oldOrderStatus, String messageStatus) {
		PreparedStatement pstm;
		try {
			pstm = UserMessagesDBController.sqlConnection.connection.prepareStatement("UPDATE  orders SET status=? WHERE arrivalDate=? AND status=? AND msgStatus=?");
			pstm.setString(1, newOrderStatus);
			pstm.setDate(2, UserMessagesDBController.tommorow);
			pstm.setString(3, oldOrderStatus);
			pstm.setString(4, messageStatus);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
/**
 *  * A method that execute UPDATE query and updates the message status of orders which are scheduled for tomorrow and have the given message message and order statuses 
 * @param orderStatus	The order status
 * @param newMessageStatus	The new wanted message status
 * @param oldMessageStatus	The old message status
 */
	public static void updateMessageStatus(String orderStatus,String newMessageStatus, String oldMessageStatus) {
		PreparedStatement pstm;
		try {
			pstm = UserMessagesDBController.sqlConnection.connection.prepareStatement("UPDATE  orders SET msgStatus=? WHERE arrivalDate=? AND status=? AND msgStatus=?");
			pstm.setString(1, newMessageStatus);
			pstm.setDate(2, UserMessagesDBController.tommorow);
			pstm.setString(3, orderStatus);
			pstm.setString(4, oldMessageStatus);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
}

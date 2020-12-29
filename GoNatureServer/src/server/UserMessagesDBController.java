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
 *A class that responsible for the sending remainders messages simulation (with an "approve order" request) .
 *The class also responsible for the automatic cancellation of an order (when a traveler hasn't approved his arrival two hours after)
 *If an order is been canceled a message will be send to the traveler
 */


public class UserMessagesDBController {
	
	//Create time variables to send reminder message simulation a day before a order is scheduled 
	//And create time variables to send a message if the arrival hasn't been approved two hours later, it will cancellation message
	
	private int targetHour1,targetHour2;
	private int targetMinutes1,targetMinutes2;
	private long delta1,delta2;
	private static SqlConnection sqlConnection = null;
	
	//Reuse of PeriodicallyRunner from external library
	//PeriodicallyRunner is a class that execute a specific runnable at specific starting hour and starting minutes and repeat every delta units of time  
	@SuppressWarnings("unused")
	private PeriodicallyRunner runnerToSendReminderADayBeforeArrival ;
	@SuppressWarnings("unused")
	private PeriodicallyRunner runnerToSendCancelationMsgAfterTwoHours ;
	//To Save he date of tomorrow
	public static java.sql.Date tommorow; 
	
	//For the simulation we will initialize the time unit to be minutes so every few minutes the messages will be send (we can't wait 2 hours since the first message is sent)
	public static TimeUnit simulationTimeUnit =TimeUnit.DAYS;
		
	
	
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
		 
		 //Get SQL connection
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * A method that creates a PeriodicallyRunner and provide it a runnable that create a pop up with the reminder message simulation
	 * PeriodicallyRunner will execute the runnable at specific starting hour and starting minutes and repeat every delta units of time 
	 * The method also updates the message status at the data base to 
	 */
	public void sendReminderMessage()  {
	
	    runnerToSendReminderADayBeforeArrival = new PeriodicallyRunner(targetHour1, targetMinutes1, simulationTimeUnit , delta1, new Runnable() {
			public void run() {
				ArrayList<ArrayList<Object>> ordersList=UserMessagesDBController.selectOrdersdetailsWhoScheduledForTomorrowByTheirAprovalStatus("Didn't send yet");
				for(ArrayList<Object> order : ordersList){
					String title="Confirm your arrival to the park tomorrow";
					String messageContent="Simulation"+"\r\n"+"\r\n"+"Order number "+order.get(0) + " is scheduled for tomorrow, please confirm your arrival.\r\n" + "You have 2 hours to confirm or cancel you arrival – \r\n" + 
			    			"Or else the order will be automatically canceled. \r\n"+"\r\n"+"\r\n" +"This message was send to "+order.get(1)+" and "+order.get(2)+" .";

					JOptionPane.showMessageDialog(null, messageContent,title,JOptionPane.INFORMATION_MESSAGE);
				}
				UserMessagesDBController.updateOrdersDetailMessageStatus("Didn't send yet", "The message was sent");
			}
	    });		
	    
      runnerToSendCancelationMsgAfterTwoHours = new PeriodicallyRunner(targetHour2, targetMinutes2, simulationTimeUnit , delta2, new Runnable() {
			public void run() {
				ArrayList<ArrayList<Object>> ordersList=UserMessagesDBController.selectOrdersdetailsWhoScheduledForTomorrowByTheirAprovalStatus("The message was sent");
				for(ArrayList<Object> order : ordersList){
					String title="Your order was canceled";
					String messageContent="Your confirmation period of time has ended-\r\n" + "Order number "+order.get(0)+" has been canceled.\r\n"+"\r\n"+"\r\n" +"This message was send to "+order.get(1)+" and "+order.get(2)+" .";
							
					JOptionPane.showMessageDialog(null, messageContent,title,JOptionPane.INFORMATION_MESSAGE);
				}
				UserMessagesDBController.updateOrdersDetailMessageStatus("The message was sent", "Canceled");
			}
      });
	}
	/**
	 * A method that execute SELECT query and returns the orderID, email, phoneNumber of all the orders which are scheduled for tomorrow and have the given message status
	 */
	public static ArrayList<ArrayList<Object>> selectOrdersdetailsWhoScheduledForTomorrowByTheirAprovalStatus(String orderApprovedStatus )  {
		PreparedStatement pstm;
		ArrayList<Object> orderDetails = new ArrayList<>();
		ArrayList<ArrayList<Object>> allOrdersDetails= new ArrayList<>();
		try { 
			pstm = sqlConnection.connection.prepareStatement("SELECT orderID, email, phoneNumber FROM orders WHERE arrivalDate=? AND orderApproved=?");
			pstm.setDate(1,  tommorow);
			pstm.setString(2, orderApprovedStatus);
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
		    System.out.println(allOrdersDetails);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return allOrdersDetails;
		
	}
	
	/**
	 * A method that execute UPDATE query and updates the message status of orders which are scheduled for tomorrow and have the given message status
	 */
	public static void updateOrdersDetailMessageStatus(String orderApprovedStatusBefore,String orderApprovedStatusAfter) {
		PreparedStatement pstm;
		try {
			pstm = UserMessagesDBController.sqlConnection.connection.prepareStatement("UPDATE  orders SET orderApproved=?  where arrivalDate=? AND orderApproved=?");
			pstm.setString(1, orderApprovedStatusAfter);
			pstm.setDate(2, UserMessagesDBController.tommorow);
			pstm.setString(3, orderApprovedStatusBefore);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
}

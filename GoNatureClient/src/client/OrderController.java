package client;

import common.Message;
import enums.OperationType;
import gui.AddOrderGUIController;
import logic.Order;

	
public class OrderController {
	public static boolean orderCompleted = false;
	private static final int ticketPrice = 100;		//ticket cost
	public static Order recivedOrder = null;
	public static int[] availableSpaces = null;
	public static final int managerDefultTravelHour = 4;
	
	public static void OrderParseData(Message reciveMsg) {
		System.out.println("ORDERCONTROLLERIN!");
		if(reciveMsg.getOperationType().equals(OperationType.SuccessAddOrder))
		{
			
			recivedOrder = (Order) reciveMsg.getObj();
			AddOrderGUIController.newOrder.setOrderID(recivedOrder.getOrderID());
			orderCompleted = true;
			
		}
		if(reciveMsg.getOperationType().equals(OperationType.OrderRequestAnswer))
		{
			System.out.println("OrderController bollean ordercomplete set");
			orderCompleted = (boolean) reciveMsg.getObj();
		}
		
		if(reciveMsg.getOperationType().equals(OperationType.checkAvailableHours))
		{
			System.out.println("WANT NON-TAKENTICKETSARRAY ORDERCONTROLLER");
			availableSpaces = (int[])reciveMsg.getObj();
		}
		
	}
	
	public static int getTicketPrice()
	{
		return ticketPrice;
	}

}

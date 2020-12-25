package client;

import common.Message;
import logic.Order;

	
public class OrderController {
	private static final int ticketPrice = 100;		//ticket cost
	public static Order recivedOrder = null;
	
	public static void OrderParseData(Message reciveMsg) {
		System.out.println(((Order) reciveMsg.getObj()).getOrderID());
		recivedOrder = (Order) reciveMsg.getObj();
		
	}
	
	public static int getTicketPrice()
	{
		return ticketPrice;
	}

}

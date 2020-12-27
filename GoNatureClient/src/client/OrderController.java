package client;

import common.Message;
import enums.OperationType;
import logic.Order;

	
public class OrderController {
	private static final int ticketPrice = 100;		//ticket cost
	public static Order recivedOrder = null;
	
	public static void OrderParseData(Message reciveMsg) {
		if(reciveMsg.getOperationType().equals(OperationType.SuccessAddOrder))
			recivedOrder = (Order) reciveMsg.getObj();
		
	}
	
	public static int getTicketPrice()
	{
		return ticketPrice;
	}

}

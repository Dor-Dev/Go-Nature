package client;

import common.Message;
import logic.Order;

public class OrderController {

	public static Order recivedOrder = null;
	public static void OrderParseData(Message reciveMsg) {
		System.out.println(((Order) reciveMsg.getObj()).getOrderID());
		recivedOrder = (Order) reciveMsg.getObj();
		
	}

}

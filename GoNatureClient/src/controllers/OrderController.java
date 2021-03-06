package controllers;

import java.util.List;

import common.Message;
import gui.AddOrderGUIController;
import gui.MyOrdersGUIController;
import logic.Order;

/**
 * The class manages the order operations, save details of asked orders and
 * saved orders include waiting list details, alternative dates details.
 * 
 * @author Naor0
 *
 */

public class OrderController {
	public static boolean orderCompleted = false;
	private static final int ticketPrice = 100; // ticket cost
	public static Order recivedOrder = null;
	public static int[] availableSpaces = null;
	public static final int managerDefultTravelHour = 4;
	public static int discountDateEvent = 0;
	public static String eventName = "";

	@SuppressWarnings("unchecked")
	public static void OrderParseData(Message reciveMsg) {
		switch (reciveMsg.getOperationType()) {
		case SuccessAddOrder:
			recivedOrder = (Order) reciveMsg.getObj();
			AddOrderGUIController.newOrder.setOrderID(recivedOrder.getOrderID());
			orderCompleted = true;
			break;
		case OrderRequestAnswer:
			orderCompleted = (boolean) reciveMsg.getObj();
			break;
		case checkAvailableHours:
			availableSpaces = (int[]) reciveMsg.getObj();
			break;
		case ReturnMyOrders:
			MyOrdersGUIController.myOrders = (List<Order>) reciveMsg.getObj();
			break;
		case CancelOrderSuccess:
			MyOrdersGUIController.msgFromServer = (String) reciveMsg.getObj();
			break;
		case ApproveOrderSuccess:
			MyOrdersGUIController.msgFromServer = (String) reciveMsg.getObj();
			break;
		case WaitingListExitSuccess:
			MyOrdersGUIController.msgFromServer = (String) reciveMsg.getObj();
			break;
		case EventDiscountAmount:
			List<String> infoEvent = (List<String>) reciveMsg.getObj(); // the list get one or 2 values, if the first
			discountDateEvent = Integer.parseInt(infoEvent.get(0)); // value is zero, no other value, if more than zero
																	// the next value will be String with name of the
																	// event
			if (discountDateEvent != 0)
				eventName = infoEvent.get(1);
			break;

		default:
			break;
		}

	}

	public static int getTicketPrice() {
		return ticketPrice;
	}

}

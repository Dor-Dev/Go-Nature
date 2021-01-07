package controllers;

import java.util.List;

import common.Message;
import enums.OperationType;
import gui.AddOrderGUIController;
import gui.MyOrdersGUIController;
import logic.Order;

	
public class OrderController {
	public static boolean orderCompleted = false;
	private static final int ticketPrice = 100;		//ticket cost
	public static Order recivedOrder = null;
	public static int[] availableSpaces = null;
	public static final int managerDefultTravelHour = 4;
	public static boolean orderExist = false;
	public static int orderID;
	public static OperationType orderType = null;
	public static int discountDateEvent = 0;
	public static String eventName = "";
	
	
	public static void OrderParseData(Message reciveMsg) {
		System.out.println("ORDERCONTROLLERIN!");
		
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
			availableSpaces = (int[])reciveMsg.getObj();
			break;
		case ReturnMyOrders:
			MyOrdersGUIController.myOrders = (List<Order>) reciveMsg.getObj();
			break;
		case CancelOrderSuccess:
			MyOrdersGUIController.msgFromServer = (String)reciveMsg.getObj();
			break;
		case ApproveOrderSuccess:
			MyOrdersGUIController.msgFromServer = (String)reciveMsg.getObj();
			break;
		case WaitingListExitSuccess:
			MyOrdersGUIController.msgFromServer = (String)reciveMsg.getObj();
			break;
		case EventDiscountAmount:
			List<String> infoEvent = (List<String>)reciveMsg.getObj();
			discountDateEvent = Integer.parseInt(infoEvent.get(0));
			if(discountDateEvent!=0)
				eventName = infoEvent.get(1);
			break;
      case FindOrder:
        if(reciveMsg.getObj() instanceof Order) {
			orderExist=true;
			recivedOrder = (Order) reciveMsg.getObj();
			orderType= OperationType.FindOrder;
			}
			else {
				orderExist = false;
				if(((String)reciveMsg.getObj()).equals("amount is not avilable"))
					orderType= OperationType.FaildToUpdate;
					
				else 
					orderType = OperationType.NeverExist;	
      }
        break;
		default:
			break;
		}

	}
	
	public static int getTicketPrice()
	{
		return ticketPrice;
	}

}

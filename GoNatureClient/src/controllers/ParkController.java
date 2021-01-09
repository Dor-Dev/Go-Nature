
package controllers;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import client.ClientController;
import common.Message;
import enums.Discount;
import enums.OperationType;
import logic.Order;
import logic.Park;
import logic.Receipt;
import logic.Subscriber;
import gui.EventsGUIController;
import logic.Event;

public class ParkController {
	public static Park parkConnected= null;
	public static OperationType Parktype= null;
	public static Order order=null;
	public static OperationType ordertype= null;
	public static int occasionalAmount;
	public static Discount disType=null;
	public static Subscriber subscriberConnected = null;
	

	private static LocalDate thisDay;
	private static Date thisDayToDB;
	private static LocalTime thisTime;
	private static int hours;
	private static int minutes;
	
	
	private static void getCurrentTime() {

		thisDay = LocalDate.now();
		thisDayToDB = Date.valueOf(thisDay);
		thisTime = LocalTime.now();
		hours = thisTime.getHour();
		minutes = thisTime.getMinute();
		if (minutes > 0) {
			hours += 1;
	
		}
	}


	@SuppressWarnings("unchecked")
	public static void ParkParseData(Message reciveMsg) {
		
		switch(reciveMsg.getOperationType()) {
		case GetParkInfo:
			parkConnected= (Park) reciveMsg.getObj();
			break;
			
			//case to keep the park
		case ParkInfo:
			parkConnected= (Park) reciveMsg.getObj();
			
			break;
			
			//case to check if the amount of visitors in the park decrese
		case UpdateParkInfo:
			parkConnected= (Park) reciveMsg.getObj();
			Parktype= OperationType.UpdateParkInfo;
			
			break;
		case FailedUpdate:
			Parktype=OperationType.FailedUpdate;
			
			break;
			
			//case to check if the visitor has a order
		case GetOrderInfo:
			getCurrentTime();
			if(reciveMsg.getObj() instanceof Order) {
				order= (Order)reciveMsg.getObj();
				if((order.getStatus().equals("Approved")) &&order.getDate().equals(thisDayToDB) &&order.getHourTime()+4>=hours &&order.getHourTime()<=hours) {
					ordertype=OperationType.GetOrderInfo;
				
				}
				else {
					ordertype= OperationType.NeverExist;
					
				}
			}
			else {
				ordertype= OperationType.NeverExist;
			}
			break;
			
			//case to calculate the amount of occasional and to check if its smaller then difference
		case CheckDifference:
			occasionalAmount= (int)reciveMsg.getObj();
			break;
			
			//case to update to 0 all the visitors that them time to exit past
		case UpdateCurrAmountOfVisitors:
			int num = (int)reciveMsg.getObj();
			parkConnected.setCurrentAmountOfVisitors(num);
			break;
			
			//case to calculate the discount for occasional subscriber
		case OccasionalSubscriber:
			subscriberConnected = (Subscriber)reciveMsg.getObj();
			if (subscriberConnected.getType().equals("instructor")) {
				disType=Discount.GroupDiscount;
				
			}
			else
				disType= Discount.MemberDiscount;
			break;
			
			//case to calculate the discount for occasional visitor
		case OccasionalVisitor:
			disType=Discount.VisitorDiscount;
			break;
			
		case UpdateWasSent:
			Parktype = OperationType.UpdateWasSent;
			break;
		case EventRequestAccepted:
			if (((String) reciveMsg.getObj()).equals("Event request send successfully")) {
				Parktype = OperationType.EventRequestSuccess;
			}
		case EventsToShow:
			if(reciveMsg.getObj() instanceof List<?>)
			EventsGUIController.data = (List<Event>) reciveMsg.getObj();
			break;
			
		case VisitorEnterRequest:
			if(reciveMsg.getObj() instanceof Receipt) {

				Receipt receipt = (Receipt) reciveMsg.getObj();
				ClientController.cardReaderAnswer="Receipt number: "+ receipt.getReceiptID()+ "\n" + "Number of visitors: " + receipt.getNumberOfVisitors()+"\n"+"Park name: " +receipt.getParkName()+"\n"+"Order number: "+receipt.getOrderNumber()+"\n"+"Discount: "+receipt.getDiscount()+"\n"+"Total cost: "+receipt.getCost();
				
			}
			else if(reciveMsg.getObj() instanceof String) {
				String msg = (String) reciveMsg.getObj();
				
					ClientController.cardReaderAnswer=msg;
			
			}
			
			break;
			
		case VisitorExitRequest:
			String msg = (String) reciveMsg.getObj();
			ClientController.cardReaderAnswer=msg;
			break;
			
			
		default:
			break;
		}
		
		
	}


}


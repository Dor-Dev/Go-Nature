package controllers;

import java.util.List;

import common.Message;
import enums.OperationType;
import enums.UserTypes;
import gui.EventsGUIController;
import logic.Event;
import logic.Park;
import logic.Receipt;

public class ParkController {
	public static Park parkConnected;
	public static OperationType Parktype = null;

	public static void ParkParseData(Message reciveMsg) {

		switch (reciveMsg.getOperationType()) {
		case GetParkInfo:
			parkConnected = (Park) reciveMsg.getObj();
			break;
		case ParkInfo:
			parkConnected = (Park) reciveMsg.getObj();
			break;
		case UpdateParkInfo:
			parkConnected = (Park) reciveMsg.getObj();
			Parktype = OperationType.UpdateParkInfo;
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
			
		default:
			break;
		}

	}

}

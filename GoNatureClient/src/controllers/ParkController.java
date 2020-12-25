package controllers;

import common.Message;
import enums.OperationType;
import enums.UserTypes;
import logic.Park;
import logic.Receipt;

public class ParkController {
	public static Park parkConnected= null;
	public static OperationType Parktype= null;
	

	public static void ParkParseData(Message reciveMsg) {
		parkConnected= (Park) reciveMsg.getObj();
		
		switch(reciveMsg.getOperationType()) {
		case GetParkInfo:
			//parkConnected= (Park) reciveMsg.getObj();
			
			break;
			
		case UpdateParkInfo:
			parkConnected= (Park) reciveMsg.getObj();
			Parktype= OperationType.UpdateParkInfo;
			break;
		case UpdateWasSent:
			Parktype = OperationType.UpdateWasSent;
			break;
			
		default:
			break;
		}
		
		
	}

}

package controllers;

import common.Message;
import logic.Park;

/**
 * Controller that receives the message with the wanted information<br>
 * from the server and updates the relevant variables for us to use them.
 *
 */
public class ParkCapacityController {

	//a static variable for saving the received park 
	public static Park chosenPark = null;

	/**
	 * This method is a static method that sends the received message from the server to the client
	 * 
	 * @param reciveMsg - the received object from the server for handling 
	 *
	 */
	public static void ParkCapacityParseData(Message reciveMsg) {
		switch (reciveMsg.getOperationType()) {

		case ShowParkCapacity:
			if (!reciveMsg.getObj().equals("The chosen park doesn't exist"))
					chosenPark = (Park) reciveMsg.getObj();
			break;
		default:
			break;
		}

	}	
}

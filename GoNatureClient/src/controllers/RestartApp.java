package controllers;

import client.ClientController;
import client.MainClient;
import common.Message;
import enums.DBControllerType;
import enums.OperationType;
import gui.RegistrationController;

/**
 * A class that is created with a method for using whenever we need to reset the relevant variables
 *
 */
public class RestartApp {

	/**
	 * A static method that resets every relevant variable that is reachable for many classes to change<br>
	 *  in order to prevent unwanted information from being saved and used inappropriately
	 */
	public static void restartParameters() {
		int id;
		if(VisitorController.subscriberConnected != null) 
			id = VisitorController.subscriberConnected.getVisitorID();
		else if(EmployeeController.employeeConected !=null)
			id =EmployeeController.employeeConected.getEmployeeID();
		else
			id = VisitorController.loggedID;
		// Send request to server for log-out with the id that is connected
		MainClient.clientConsole.accept(new Message(OperationType.UserDisconnected,DBControllerType.LoginDBController,(Object)id));
		//Restart all the static parameters of the client
		VisitorController.subscriberConnected = null;
		VisitorController.loggedID = 0;
		VisitorController.isConnected = false;
		VisitorController.memberNotExist=false;
		EmployeeController.isConnected = false;
		EmployeeController.employeeConected = null;
		ParkController.Parktype = null;
		ClientController.type = null;
		ParkCapacityController.chosenPark=null;
		ParkController.disType = null;
		RegistrationController.setPopUpMsg(null);
		RegistrationController.setMsgReceived(false);
		RegistrationController.setPopUpTitle(null);
		

	}

}

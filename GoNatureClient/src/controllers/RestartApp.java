package controllers;

import client.ClientController;
import client.MainClient;
import common.Message;
import enums.DBControllerType;
import enums.OperationType;
import gui.EventsGUIController;
import gui.ParkCapacityGUIController;
import gui.RegistrationController;

/*
 * A method created for reseting any variables used for saving data received from the database that can't be reset automatically.
 */
public class RestartApp {

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
		ParkCapacityGUIController.setReturnedPark(null);
		ParkController.disType = null;
		RegistrationController.setPopUpMsg(null);
		RegistrationController.setMsgReceived(false);
		RegistrationController.setPopUpTitle(null);

	}

}

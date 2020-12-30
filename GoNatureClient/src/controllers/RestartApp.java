package controllers;

import client.ClientController;
import gui.ParkCapacityGUIController;
import gui.RegistrationController;

/*
 * A method created for reseting any variables used for saving data received from the database that can't be reset automatically.
 */
public class RestartApp {

	public static void restartParameters() {
		VisitorController.subscriberConnected = null;
		VisitorController.loggedID = 0;
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

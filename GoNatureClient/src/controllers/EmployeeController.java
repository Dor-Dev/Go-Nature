package controllers;

import client.ClientController;
import common.Message;
import enums.OperationType;
import enums.UserTypes;
import logic.Employee;

/**
 * The Class is controller for the employee connected details
 * the class is save the role of the employee
 * @author Naor0
 *
 */
public class EmployeeController {
	public static Employee employeeConected = null;
	public static boolean isConnected = false;

	public static void EmployeeParseData(Message reciveMsg) {
		if (!(reciveMsg.getObj() instanceof String)) {
			employeeConected = (Employee) ClientController.returnedValueFromServer;
			switch (reciveMsg.getOperationType()) {
			case EmployeeLogin:
				switch (employeeConected.getRole()) {

				case "Receptionist":
					ClientController.type = UserTypes.entryEmployee;
					break;

				case "Park-manager":
					ClientController.type = UserTypes.parkManager;
					break;

				case "Department-manager":
					ClientController.type = UserTypes.departmentManager;
					break;

				case "Service-representative":
					ClientController.type = UserTypes.serviceEmployee;
					break;

				}
			case ErrorEmployeeLogin:
				break;
			default:
				break;
			}

		}
		if(reciveMsg.getOperationType().equals(OperationType.EmployeeAlreadyLoggedIn)) {
			isConnected = true;
		}
	}

}

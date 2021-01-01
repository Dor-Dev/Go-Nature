package controllers;

import client.ClientController;
import common.Message;
import enums.OperationType;
import enums.UserTypes;
import logic.Employee;

public class EmployeeController {
	public static Employee employeeConected = null;
	public static boolean isConnected = false;

	public static void EmployeeParseData(Message reciveMsg) {
		if (!(reciveMsg.getObj() instanceof String)) {
			System.out.println("HEEELLLLLOOOOO");
			employeeConected = (Employee) ClientController.returnedValueFromServer;
			switch (reciveMsg.getOperationType()) {
			case EmployeeLogin:
				switch (employeeConected.getRole()) {

				case "entry":
					ClientController.type = UserTypes.entryEmployee;
					break;

				case "park manager":
					System.out.println("the employee role is park manager");
					ClientController.type = UserTypes.parkManager;
					break;

				case "department manager":
					ClientController.type = UserTypes.departmentManager;
					break;

				case "service representative":
					ClientController.type = UserTypes.serviceEmployee;
					break;

				}
			case ErrorEmployeeLogin:
				break;
			default:
				System.out.println("DEFUALT EMPLOYEE CASE");
				break;
			}

		}
		if(reciveMsg.getOperationType().equals(OperationType.EmployeeAlreadyLoggedIn)) {
			System.out.println("BBBBYYYYEEEEEE");
			isConnected = true;
		}
	}

}

package client;

import common.UserTypes;
import common.Message;
import gui.LoginGUIController;
import gui.WelcomeGUIController;
import logic.Employee;

public class EmployeeController  {
	public static UserTypes type;

	public static void EmployeeParseData (Message reciveMsg) {
		Employee employee =(Employee)reciveMsg.getObj();
		switch(reciveMsg.getDbControllertype()) {
		case loginDBController:
		switch(employee.getRole()) {
		
		case "entry":
			type = UserTypes.entryEmployee;
			break;
			
		case "park manager":
			System.out.println("the employee role is park manager");
			type = UserTypes.parkManager;
			break;
			
		case "department manager":
			type = UserTypes.departmentManager;
			break;
			
		case "service representative":
			type = UserTypes.serviceEmployee;
			break;
			
		}
		default:
			break;
	}
	}

}

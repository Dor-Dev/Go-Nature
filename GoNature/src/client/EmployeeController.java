package client;

import common.Message;
import logic.Employee;

public class EmployeeController  {

	public static void EmployeeParseData (Message reciveMsg) {
		Employee employee =(Employee)reciveMsg.getObj();
		switch(reciveMsg.getDbControllertype()) {
		case loginDBController:
		switch(employee.getRole()) {
		case "entry":
			System.out.println("the employee role is entry");
			break;
			
		case "park Maneger":
			break;
			
		case "department Manager":
			break;
			
		case "service representative":
			break;
			
		}
		
		default:
			break;
	}
	}

}

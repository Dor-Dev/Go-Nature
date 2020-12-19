package client;

import common.Message;
import gui.LoginGUIController;
import gui.WelcomeGUIController;
import logic.Employee;

public class EmployeeController  {
	public static boolean entryEmployee=false;
	public static boolean parkManager=false;
	public static boolean departmentManager=false;
	public static boolean serviceEmployee=false;

	public static void EmployeeParseData (Message reciveMsg) {
		Employee employee =(Employee)reciveMsg.getObj();
		switch(reciveMsg.getDbControllertype()) {
		case loginDBController:
		switch(employee.getRole()) {
		case "entry":
			serviceEmployee=false;
			parkManager=false;
			departmentManager=false;
			entryEmployee=true;
			break;
			
		case "park manager":
			System.out.println("the employee role is park manager");
			entryEmployee=false;
			serviceEmployee=false;
			departmentManager=false;
			parkManager=true;
			break;
			
		case "department manager":
			entryEmployee=false;
			serviceEmployee=false;
			parkManager=false;
			departmentManager=true;
			break;
			
		case "service representative":
			entryEmployee=false;
			parkManager=false;
			departmentManager=false;
			serviceEmployee=true;
			break;
			
		}
		
		default:
			break;
	}
	}

}

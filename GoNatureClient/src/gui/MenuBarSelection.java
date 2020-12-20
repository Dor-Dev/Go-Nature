package gui;

import java.util.List;

import client.EmployeeController;
import common.UserTypes;
import javafx.scene.control.Label;

public class MenuBarSelection {

	public static void setMenuOptions(List<Label> menuLabels)
	{
		if(EmployeeController.type.equals(UserTypes.entryEmployee))
		{
			
			menuLabels.get(4).setManaged(false);		//Registration
			menuLabels.get(5).setManaged(false);		//Park Details
			menuLabels.get(6).setManaged(false);		//Events
			menuLabels.get(7).setManaged(false);		//ReportsDepartment
			menuLabels.get(8).setManaged(false);		//ReportsManager
			menuLabels.get(9).setManaged(false);		//Park Capacity
			menuLabels.get(10).setManaged(false);		//Requests
		}	
		else if(EmployeeController.type.equals(UserTypes.serviceEmployee))
		{
			menuLabels.get(3).setManaged(false);		//Entrance
			menuLabels.get(5).setManaged(false);		//Park Details
			menuLabels.get(6).setManaged(false);		//Events
			menuLabels.get(7).setManaged(false);		//ReportsDepartment
			menuLabels.get(8).setManaged(false);		//ReportsManager
			menuLabels.get(9).setManaged(false);		//Park Capacity
			menuLabels.get(10).setManaged(false);		//Requests
		}
		else if(EmployeeController.type.equals(UserTypes.parkManager))
		{

			menuLabels.get(3).setManaged(false);		//Entrance
			menuLabels.get(4).setManaged(false);		//Registration
			menuLabels.get(7).setManaged(false);		//ReportsDepartment
			menuLabels.get(9).setManaged(false);		//Park Capacity
			menuLabels.get(10).setManaged(false);		//Requests
		}
		else if(EmployeeController.type.equals(UserTypes.departmentManager))
		{
			menuLabels.get(3).setManaged(false);		//Entrance
			menuLabels.get(4).setManaged(false);		//Registration
			menuLabels.get(5).setManaged(false);		//Park Details
			menuLabels.get(6).setManaged(false);		//Events
			menuLabels.get(8).setManaged(false);		//ReportsManager
		}
	}
}

package gui;

import java.util.List;

import client.ClientController;
import enums.UserTypes;
import javafx.scene.control.Label;
import logic.Employee;
import logic.Subscriber;

/**
 * This class is responsible for which labels will appear on each page depending on the type of user logged on
 * @author Naor0
 *
 */
public class MenuBarSelection {

	/**
	 * This method is responsible for which labels will appear on each page depending on the type of user logged on
	 * @param menuLabels is List with the parameters that came from specific page
	 */
	public static void setMenuOptions(List<Label> menuLabels) {
		if (ClientController.returnedValueFromServer instanceof Employee) {
			menuLabels.get(0).setManaged(false); // Add Order
			menuLabels.get(1).setManaged(false); // My Orders
			if (ClientController.type.equals(UserTypes.entryEmployee)) {

				menuLabels.get(4).setManaged(false); // Registration
				menuLabels.get(5).setManaged(false); // Park Details
				menuLabels.get(6).setManaged(false); // Events
				menuLabels.get(7).setManaged(false); // ReportsDepartment
				menuLabels.get(8).setManaged(false); // ReportsManager
				menuLabels.get(9).setManaged(false); // Park Capacity
				menuLabels.get(10).setManaged(false); // Requests
			} else if (ClientController.type.equals(UserTypes.serviceEmployee)) {

				menuLabels.get(3).setManaged(false); // Entrance
				menuLabels.get(5).setManaged(false); // Park Details
				menuLabels.get(6).setManaged(false); // Events
				menuLabels.get(7).setManaged(false); // ReportsDepartment
				menuLabels.get(8).setManaged(false); // ReportsManager
				menuLabels.get(9).setManaged(false); // Park Capacity
				menuLabels.get(10).setManaged(false); // Requests
			} else if (ClientController.type.equals(UserTypes.parkManager)) {

				menuLabels.get(3).setManaged(false); // Entrance
				menuLabels.get(4).setManaged(false); // Registration
				menuLabels.get(7).setManaged(false); // ReportsDepartment
				menuLabels.get(9).setManaged(false); // Park Capacity
				menuLabels.get(10).setManaged(false); // Requests
			} else if (ClientController.type.equals(UserTypes.departmentManager)) {
				menuLabels.get(3).setManaged(false); // Entrance
				menuLabels.get(4).setManaged(false); // Registration
				menuLabels.get(5).setManaged(false); // Park Details
				menuLabels.get(6).setManaged(false); // Events
				menuLabels.get(8).setManaged(false); // ReportsManager
			}
		} 
		else if (ClientController.returnedValueFromServer instanceof Subscriber) {
		 if (ClientController.type.equals(UserTypes.subscriber) || ClientController.type.equals(UserTypes.instructor)) {
				menuLabels.get(3).setManaged(false); // Entrance
				menuLabels.get(4).setManaged(false); // Registration
				menuLabels.get(5).setManaged(false); // Park Details
				menuLabels.get(6).setManaged(false); // Events
				menuLabels.get(7).setManaged(false); // ReportsDepartment
				menuLabels.get(8).setManaged(false); // ReportsManager
				menuLabels.get(9).setManaged(false); // Park Capacity
				menuLabels.get(10).setManaged(false); // Requests
			}
		}
		 else if (ClientController.type.equals(UserTypes.visitor)|| ClientController.type.equals(UserTypes.VisitorWithOrder)) {
				
				menuLabels.get(2).setManaged(false); // MyProfile
				menuLabels.get(3).setManaged(false); // Entrance
				menuLabels.get(4).setManaged(false); // Registration
				menuLabels.get(5).setManaged(false); // Park Details
				menuLabels.get(6).setManaged(false); // Events
				menuLabels.get(7).setManaged(false); // ReportsDepartment
				menuLabels.get(8).setManaged(false); // ReportsManager
				menuLabels.get(9).setManaged(false); // Park Capacity
				menuLabels.get(10).setManaged(false); // Requests
			}
		
		

	
	}
}
	



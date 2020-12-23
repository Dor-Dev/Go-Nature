package gui;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import client.ClientController;
import controllers.EmployeeController;
import controllers.VisitorController;

import enums.UserTypes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyProfileGUIController {

	@FXML
	private Label mnuAddOrder;

	@FXML
	private Label mnuMyOrders;

	@FXML
	private Label mnuMyProfile;

	@FXML
	private Label mnuParkEntrance;

	@FXML
	private Label mnuRegistration;

	@FXML
	private Label mnuReportsManager;

	@FXML
	private Label mnuEvents;

	@FXML
	private Label mnuParkDetails;

	@FXML
	private Label mnuParkCapacity;

	@FXML
	private Label mnuReportsDepartment;

	@FXML
	private Label mnuRequests;

	@FXML
	private Label lblIdNumber;

	@FXML
	private Label lblFirstName;

	@FXML
	private Label lblLastName;

	@FXML
	private Label lblPhone;

	@FXML
	private Label lblEmail;

	@FXML
	private HBox hboxType;

	@FXML
	private Label lblType;

	@FXML
	private HBox hboxEmployeeNumber;

	@FXML
	private Label lblEmployeeID;

	@FXML
	private HBox hboxEmployeeRole;

	@FXML
	private Label lblEmployeeRole;
	
    @FXML
    private HBox hboxPhone;

	@FXML
	private HBox hboxEmployeeOrganization;

	@FXML
	private Label lblEmployeeOrganization;

	public void show() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/MyProfileGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("My Profile");
			MyProfileGUIController myProfileController = loader.getController();

			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(myProfileController);
			MenuBarSelection.setMenuOptions(menuLabels);
		
			
			if(ClientController.type.equals(UserTypes.subscriber) || ClientController.type.equals(UserTypes.instructor))
				hideEmployeeLabels(myProfileController);
			else
				hideSubscriberLabels(myProfileController);
			
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * this method is for hide the subscriber's labels if the client connected is employee
	 * @param myProfileController
	 */
	private void hideSubscriberLabels(MyProfileGUIController myProfileController) {
		myProfileController.hboxType.setManaged(false);
		myProfileController.hboxPhone.setManaged(false);
		myProfileController.lblEmployeeID.setText(String.valueOf(EmployeeController.employeeConected.getEmployeeID()));
		myProfileController.lblIdNumber.setText(EmployeeController.employeeConected.getUserName());
		myProfileController.lblFirstName.setText(EmployeeController.employeeConected.getFirstName());
		myProfileController.lblLastName.setText(EmployeeController.employeeConected.getLasttName());
		myProfileController.lblEmployeeRole.setText(EmployeeController.employeeConected.getRole());
		myProfileController.lblEmail.setText(EmployeeController.employeeConected.getEmail());
		myProfileController.lblEmployeeOrganization.setText(EmployeeController.employeeConected.getOrganizationAffilation());
		
	}
	
	/**
	 * this method is for hide the employee's labels if the client connected is  subscriber
	 * @param myProfileController
	 */

	private void hideEmployeeLabels(MyProfileGUIController myProfileController) {
		myProfileController.hboxEmployeeNumber.setManaged(false);
		myProfileController.hboxEmployeeOrganization.setManaged(false);
		myProfileController.hboxEmployeeRole.setManaged(false);
		//TODO adding member
		myProfileController.lblIdNumber.setText(String.valueOf(VisitorController.subscriberConnected.getVisitorID()));
		myProfileController.lblEmail.setText(VisitorController.subscriberConnected.getEmail());
		myProfileController.lblType.setText(VisitorController.visitorConnected.getType());
		myProfileController.lblFirstName.setText(VisitorController.subscriberConnected.getFirstName());
		myProfileController.lblLastName.setText(VisitorController.subscriberConnected.getLastName());
		myProfileController.lblPhone.setText(VisitorController.subscriberConnected.getPhone());

		
	}
	

	private List<Label> createLabelList(MyProfileGUIController myProfileController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(myProfileController.mnuAddOrder);
		tempMenuLabels.add(myProfileController.mnuMyOrders);
		tempMenuLabels.add(myProfileController.mnuMyProfile);
		tempMenuLabels.add(myProfileController.mnuParkEntrance);
		tempMenuLabels.add(myProfileController.mnuRegistration);
		tempMenuLabels.add(myProfileController.mnuParkDetails);
		tempMenuLabels.add(myProfileController.mnuEvents);
		tempMenuLabels.add(myProfileController.mnuReportsDepartment);
		tempMenuLabels.add(myProfileController.mnuReportsManager);
		tempMenuLabels.add(myProfileController.mnuParkCapacity);
		tempMenuLabels.add(myProfileController.mnuRequests);
		return tempMenuLabels;
	}

	   @FXML
	    void showMyOrders(MouseEvent event) {
	    	MyOrdersGUIController mo= new MyOrdersGUIController();
	    	((Node)event.getSource()).getScene().getWindow().hide();
	    	mo.show();

	    }

	   @FXML
	    void showAddOrder(MouseEvent event) {
	    	AddOrderGUIController c = new AddOrderGUIController();
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    	c.show();
	    }
	   
	    @FXML
	    void showParkEntrance(MouseEvent event) {
	    	ParkEntranceGUIController pe = new ParkEntranceGUIController();
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    	pe.show();
	    	

	    }
  
	@FXML
	void goToParkDetails(MouseEvent event) {
		ManagerDetailsGUIController mDgc = new ManagerDetailsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		mDgc.show();
	}

    @FXML
    void goToManagerReports(MouseEvent event) {
    	ManagerReportGUIController mRc = new ManagerReportGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mRc.show();
    }

    @FXML
    void goToManagerEvents(MouseEvent event) {
    	EventsGUIController eGc = new EventsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		eGc.show();
    }
    @FXML

    void showParkCapacity(MouseEvent event) {
    	ParkCapacityGUIController pC = new ParkCapacityGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		pC.show();
    }
    

    @FXML
    void showReports(MouseEvent event) {
    	DManagerReportsGUIController rP = new DManagerReportsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		rP.show();
    }

    @FXML
    void showRequests(MouseEvent event) {
    	DManagerRequestsGUIController rQ = new DManagerRequestsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		rQ.show();

    void goToRegisration(MouseEvent event) {
    	RegistrationController c = new RegistrationController();
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
    	c.show();

    }

}

package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.ParkController;
import controllers.RestartApp;
import enums.DBControllerType;
import enums.OperationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Update;
import logic.Validation;

public class ManagerDetailsGUIController {
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
	private Label lblParkName;

	@FXML
	private Label lblVisitorAmount;

	@FXML
	private Label lblAvailableSpace;

	@FXML
	private Label lblVisitorCapacity;

	@FXML
	private Label lblDifference;

	@FXML
	private Label lblHours;

	@FXML
	private TextField txtVisitorCapcity;

	@FXML
	private TextField txtDifference;

	@FXML
	private TextField txtHours;

	@FXML
	private Button btnSendUpdate;

	@FXML
	private Label mnuLogout;

	@FXML
	void goToMainPage(MouseEvent event) {
		RestartApp.restartParameters();
		LoginGUIController login = new LoginGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		login.show();
	}

	@FXML
	void goToEvents(MouseEvent event) {
		EventsGUIController eGc = new EventsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		eGc.show();
	}

	@FXML
	void goToManagerReports(MouseEvent event) {
		ManagerReportGUIController mRc = new ManagerReportGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mRc.show();
	}

	@FXML
	void goToMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();
	}

	/**
	 * Update request for the relevant park according the park manager organization
	 * affiliation. update the visitor capacity,difference between visitors to
	 * orders and estimated time.
	 * 
	 * @param event
	 */
	@FXML
	void sendUpdateRequest(MouseEvent event) {
		String validMsg = eventsFeildsValidation();
		if(!validMsg.equals("OK")) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Please correct the " + validMsg + " field");
			a.setContentText("Validation Error");
			a.setTitle("Validation Error");
			a.showAndWait();
			return;
		}
		String status = "Waiting";
		Update update = new Update(EmployeeController.employeeConected.getOrganizationAffilation(),
				Integer.valueOf(txtVisitorCapcity.getText()), Integer.valueOf(txtDifference.getText()),
				Integer.valueOf(txtHours.getText()), status);
		MainClient.clientConsole.accept(
				new Message(OperationType.SendUpdateRequest, DBControllerType.ParkDBController, (Object) update));
		if (ParkController.Parktype.equals(OperationType.UpdateWasSent)) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("The update has been sent successfully to department manager.");
			a.setContentText("When the update will approve, you will see the new parameters in the left corner.");
			a.setTitle("Update Request");
			a.showAndWait();
		}
	}
	private String eventsFeildsValidation() {
		if(!Validation.onlyDigitsValidation(txtVisitorCapcity.getText()))
			return "Park Capacity";
		if(!Validation.onlyDigitsValidation(txtDifference.getText()))
			return "Difference field";
		if(!Validation.onlyDigitsValidation(txtHours.getText()))
			return "Hours field";
		return "OK";
		
	}
	/*
	 * close popup window button
	 */
	@FXML
	private Button closeButton;

	@FXML
	void closePopUp(ActionEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) closeButton.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	private void setData(ManagerDetailsGUIController mdc) {
		mdc.lblParkName.setText(ParkController.parkConnected.getParkName());
		mdc.lblVisitorAmount.setText(String.valueOf(ParkController.parkConnected.getCurrentAmountOfVisitors()));
		mdc.lblAvailableSpace.setText(String.valueOf(ParkController.parkConnected.getParkCapacity()
				- ParkController.parkConnected.getCurrentAmountOfVisitors()));

		mdc.lblVisitorCapacity.setText(String.valueOf(ParkController.parkConnected.getParkCapacity()));
		mdc.lblDifference.setText(String.valueOf(ParkController.parkConnected.getDifference()));
		mdc.lblHours.setText(String.valueOf(ParkController.parkConnected.getVisitingTime()));
	}

	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ParkManagerDetails.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Details");
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
			ManagerDetailsGUIController managerDetailsController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = managerDetailsController.createLabelList(managerDetailsController);
			MenuBarSelection.setMenuOptions(menuLabels);
			MainClient.clientConsole.accept(new Message(OperationType.GetParkInfo, DBControllerType.ParkDBController,
					(Object) EmployeeController.employeeConected.getOrganizationAffilation()));
			System.out.println("ParkManager: TRY Again");
			setData(managerDetailsController);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private List<Label> createLabelList(ManagerDetailsGUIController managerReportsController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(managerReportsController.mnuAddOrder);
		tempMenuLabels.add(managerReportsController.mnuMyOrders);
		tempMenuLabels.add(managerReportsController.mnuMyProfile);
		tempMenuLabels.add(managerReportsController.mnuParkEntrance);
		tempMenuLabels.add(managerReportsController.mnuRegistration);
		tempMenuLabels.add(managerReportsController.mnuParkDetails);
		tempMenuLabels.add(managerReportsController.mnuEvents);
		tempMenuLabels.add(managerReportsController.mnuReportsDepartment);
		tempMenuLabels.add(managerReportsController.mnuReportsManager);
		tempMenuLabels.add(managerReportsController.mnuParkCapacity);
		tempMenuLabels.add(managerReportsController.mnuRequests);
		return tempMenuLabels;
	}

}

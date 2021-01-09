package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.ParkCapacityController;
import controllers.RestartApp;
import enums.DBControllerType;
import enums.OperationType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Park;

public class ParkCapacityGUIController {

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
	private ComboBox<String> cmbParkName;

	@FXML
	private Label lblParkCapacity;

	@FXML
	private Label lblVisitorsAmount;

	@FXML
    private Label mnuLogout;
	
	 
	

	/**
	 *  This method returns to the main page after the user presses on the "log out" button<br> 
	 * {@link restartParameters()} will be executed in order to reset relevant variables<br>
	 * @param event - the mouse event that occurs when the user clicks on log out
	 */
	@FXML
    void goToMainPage(MouseEvent event) {
	  RestartApp.restartParameters();
	  LoginGUIController login = new LoginGUIController();
	  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	  login.show();
    }
	
	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();

	}

	/**
	 * This method displays the reports page
	 *  after the user presses on the "reports" button in the menu bar<br> 
	 *
	 * @param event - the mouse event that occurs when the user clicks on reports
	 */
	@FXML
	void showReports(MouseEvent event) {
		DManagerReportsGUIController rP = new DManagerReportsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		rP.show();
	}

	/**
	 * This method displays the requests page
	 *  after the user presses on the "requests" button in the menu bar<br> 
	 *
	 * @param event - the mouse event that occurs when the user clicks on requests
	 */
	@FXML
	void showRequests(MouseEvent event) {
		DManagerRequestsGUIController rQ = new DManagerRequestsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		rQ.show();
	}

	@FXML
	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/DManagerCapacity.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Park Capacity");			
			ParkCapacityGUIController parkCapacityController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(parkCapacityController);
			MenuBarSelection.setMenuOptions(menuLabels);
			
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/*initialize the combobox with the different options for park names
	 * setting an action that will happen once we click on one of the options */
	@FXML
	public void initialize() {
		cmbParkName.getItems().removeAll(cmbParkName.getItems());
		cmbParkName.getItems().addAll("Luna-Park","Shipment-park","Tempo-Park");
		cmbParkName.setOnAction(e -> chooseParkAction());
	}
	private List<Label> createLabelList(ParkCapacityGUIController parkCapacityController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(parkCapacityController.mnuAddOrder);
		tempMenuLabels.add(parkCapacityController.mnuMyOrders);
		tempMenuLabels.add(parkCapacityController.mnuMyProfile);
		tempMenuLabels.add(parkCapacityController.mnuParkEntrance);
		tempMenuLabels.add(parkCapacityController.mnuRegistration);
		tempMenuLabels.add(parkCapacityController.mnuParkDetails);
		tempMenuLabels.add(parkCapacityController.mnuEvents);
		tempMenuLabels.add(parkCapacityController.mnuReportsDepartment);
		tempMenuLabels.add(parkCapacityController.mnuReportsManager);
		tempMenuLabels.add(parkCapacityController.mnuParkCapacity);
		return tempMenuLabels;
	}

	/**
	 * This method displays information about the park<br> 
	 *  after the user choses which park he wants to view 
	 */
	@FXML
	 void chooseParkAction() {
		MainClient.clientConsole.accept(new Message(OperationType.ShowParkCapacity,
				DBControllerType.ParkCapacityDBController, (Object) cmbParkName.getValue()));
		
		Park park = ParkCapacityController.chosenPark;
		//displaying the received information
		lblParkCapacity.setText(String.valueOf(park.getParkCapacity()));
		lblVisitorsAmount.setText(String.valueOf(park.getParkCapacity()-park.getCurrentAmountOfVisitors()));
	}


	
}

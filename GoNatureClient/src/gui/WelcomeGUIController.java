package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import controllers.RestartApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeGUIController {

	@FXML
	private Label mnuRequests;
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
	void showAddOrder(MouseEvent event) {
		AddOrderGUIController c = new AddOrderGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		c.show();
	}

	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();

	}

	@FXML
	void showMyOrders(MouseEvent event) {
		MyOrdersGUIController mo = new MyOrdersGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mo.show();

	}

	@FXML
	void showParkEntrance(MouseEvent event) {
		ParkEntranceGUIController pe = new ParkEntranceGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		pe.show();

	}

	/**
	 * This method displays the park capacity page
	 *  after the user presses on the "park capacity" button in the menu bar<br> 
	 *
	 * @param event - the mouse event that occurs when the user clicks on park capacity
	 */
	@FXML
	void showParkCapacity(MouseEvent event) {
		ParkCapacityGUIController pC = new ParkCapacityGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		pC.show();
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

	/**
	 * Change the main window to Events page when click on the Events
	 * 
	 * @param event
	 */
	@FXML
	void goToEventsPage(MouseEvent event) {
		EventsGUIController eGc = new EventsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		eGc.show();
	}

	@FXML
	void goToParkDetails(MouseEvent event) {
		ManagerDetailsGUIController mDgc = new ManagerDetailsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		mDgc.show();
	}

	@FXML
	void goToParkManagerReports(MouseEvent event) {
		ManagerReportGUIController mRc = new ManagerReportGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mRc.show();
	}

	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("WelcomeGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
			primaryStage.setTitle("Go-Nature Welcome");
			WelcomeGUIController welcomeController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(welcomeController);
			MenuBarSelection.setMenuOptions(menuLabels);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private List<Label> createLabelList(WelcomeGUIController welcomeController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(welcomeController.mnuAddOrder);
		tempMenuLabels.add(welcomeController.mnuMyOrders);
		tempMenuLabels.add(welcomeController.mnuMyProfile);
		tempMenuLabels.add(welcomeController.mnuParkEntrance);
		tempMenuLabels.add(welcomeController.mnuRegistration);
		tempMenuLabels.add(welcomeController.mnuParkDetails);
		tempMenuLabels.add(welcomeController.mnuEvents);
		tempMenuLabels.add(welcomeController.mnuReportsDepartment);
		tempMenuLabels.add(welcomeController.mnuReportsManager);
		tempMenuLabels.add(welcomeController.mnuParkCapacity);
		tempMenuLabels.add(welcomeController.mnuRequests);
		return tempMenuLabels;
	}

	@FXML
	void showRegistration(MouseEvent event) {
		RegistrationController c = new RegistrationController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		c.show();
	}

}

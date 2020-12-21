package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.UserTypes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
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

    }

    @FXML
    void showAddOrder(MouseEvent event) {

    }

}

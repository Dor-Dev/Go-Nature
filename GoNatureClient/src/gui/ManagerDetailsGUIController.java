package gui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.media.jfxmediaimpl.platform.Platform;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.ParkController;
import enums.DBControllerType;
import enums.OperationType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    @FXML
    void sendUpdateRequest(MouseEvent event) {
    	List<String> list = new ArrayList<>();
    	String status = "waiting";
    	list.add(txtVisitorCapcity.getText());
    	list.add(txtDifference.getText());
    	list.add(txtHours.getText());
    	list.add(status);
    	MainClient.clientConsole.accept(new Message(OperationType.SendUpdateRequest,DBControllerType.ParkDBController,(Object)list));
    
    }
    
    private void setData(ManagerDetailsGUIController mdc) {
    	mdc.lblParkName.setText(ParkController.parkConnected.getParkName());
    	mdc.lblVisitorAmount.setText(String.valueOf(ParkController.parkConnected.getCurrentAmountOfVisitors()));
    	mdc.lblAvailableSpace.setText(String.valueOf(ParkController.parkConnected.getParkCapacity()-ParkController.parkConnected.getCurrentAmountOfVisitors()));
    	
    	mdc.lblVisitorCapacity.setText(String.valueOf(ParkController.parkConnected.getParkCapacity()));
    	mdc.lblDifference.setText(String.valueOf(ParkController.parkConnected.getDifference()));
    	mdc.lblHours.setText(ParkController.parkConnected.getVisitingTime());
    }
    
    public void show() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ParkManagerDetails.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Details");
			ManagerDetailsGUIController managerDetailsController = loader.getController();	
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = managerDetailsController.createLabelList(managerDetailsController);
			MenuBarSelection.setMenuOptions(menuLabels);
			MainClient.clientConsole.accept(new Message(OperationType.GetParkInfo,DBControllerType.ParkDBController,(Object)EmployeeController.employeeConected.getOrganizationAffilation()));
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

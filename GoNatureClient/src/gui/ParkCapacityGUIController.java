package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();

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
    }



   
    public void show() {
		VBox root;
		Stage primaryStage = new Stage();
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


}

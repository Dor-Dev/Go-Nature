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

public class ParkEntranceGUIController {

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
    	MyProfileGUIController mp= new MyProfileGUIController();
    	((Node)event.getSource()).getScene().getWindow().hide();
    	mp.show();

    }
    
    // TODO a popup message 
    @FXML
    void showOrderReceipt(MouseEvent event) {
    	OrderReceiptPopupGUIController orderReceiptController = new OrderReceiptPopupGUIController();
    	orderReceiptController.show();

    }
    
    @FXML
    void showManualReceipt(MouseEvent event) {

    }
    
    
	 public void show() 
	    {
	    	VBox root;
	    	Stage primaryStage = new Stage();
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("ParkEntranceGUI.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Park Entrance");
				ParkEntranceGUIController parkEntranceController = loader.getController();	
				List<Label> menuLabels = new ArrayList<>();
				menuLabels = createLabelList(parkEntranceController);
				MenuBarSelection.setMenuOptions(menuLabels);
				primaryStage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	    }
	 private List<Label> createLabelList(ParkEntranceGUIController parkEntranceController)
	 {
		 List<Label> tempMenuLabels = new ArrayList<>();
			tempMenuLabels.add(parkEntranceController.mnuAddOrder);
			tempMenuLabels.add(parkEntranceController.mnuMyOrders);
			tempMenuLabels.add(parkEntranceController.mnuMyProfile);
			tempMenuLabels.add(parkEntranceController.mnuParkEntrance);
			tempMenuLabels.add(parkEntranceController.mnuRegistration);
			tempMenuLabels.add(parkEntranceController.mnuParkDetails);
			tempMenuLabels.add(parkEntranceController.mnuEvents);
			tempMenuLabels.add(parkEntranceController.mnuReportsDepartment);
			tempMenuLabels.add(parkEntranceController.mnuReportsManager);
			tempMenuLabels.add(parkEntranceController.mnuParkCapacity);
			tempMenuLabels.add(parkEntranceController.mnuRequests);
			return 	tempMenuLabels;
	 }
	 

}

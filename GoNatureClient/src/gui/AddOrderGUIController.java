package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddOrderGUIController {
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
	    
	 public void show() 
	    {
	    	VBox root;
	    	Stage primaryStage = new Stage();
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/AddOrderGUI.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Add Order");
				AddOrderGUIController addOrderController = loader.getController();
				
				List<Label> menuLabels = new ArrayList<>();
				menuLabels.add(addOrderController.mnuAddOrder);
				menuLabels.add(addOrderController.mnuMyOrders);
				menuLabels.add(addOrderController.mnuMyProfile);
				menuLabels.add(addOrderController.mnuParkEntrance);
				menuLabels.add(addOrderController.mnuRegistration);
				menuLabels.add(addOrderController.mnuParkDetails);
				menuLabels.add(addOrderController.mnuEvents);
				menuLabels.add(addOrderController.mnuReportsDepartment);
				menuLabels.add(addOrderController.mnuReportsManager);
				menuLabels.add(addOrderController.mnuParkCapacity);
				menuLabels.add(addOrderController.mnuRequests);
				MenuBarSelection.setMenuOptions(menuLabels);
				primaryStage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	    }
	 

}

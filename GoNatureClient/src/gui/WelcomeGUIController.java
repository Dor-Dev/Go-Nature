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
	    
	 public void show() 
	    {
	    	VBox root;
	    	Stage primaryStage = new Stage();
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Welcome.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Go-Nature Login");
				WelcomeGUIController welcomeController = loader.getController();
				
				List<Label> menuLabels = new ArrayList<>();
				menuLabels.add(welcomeController.mnuAddOrder);
				menuLabels.add(welcomeController.mnuMyOrders);
				menuLabels.add(welcomeController.mnuMyProfile);
				menuLabels.add(welcomeController.mnuParkEntrance);
				menuLabels.add(welcomeController.mnuRegistration);
				menuLabels.add(welcomeController.mnuParkDetails);
				menuLabels.add(welcomeController.mnuEvents);
				menuLabels.add(welcomeController.mnuReportsDepartment);
				menuLabels.add(welcomeController.mnuReportsManager);
				menuLabels.add(welcomeController.mnuParkCapacity);
				menuLabels.add(welcomeController.mnuRequests);
				MenuBarSelection.setMenuOptions(menuLabels);
				primaryStage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	    }
	 

}

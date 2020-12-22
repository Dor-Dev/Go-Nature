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
    void showAddOrder(MouseEvent event) {
    	AddOrderGUIController c = new AddOrderGUIController();
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
    	c.show();
    }
    
    @FXML
    void showRegistration(MouseEvent event) {
    	RegistrationController c = new RegistrationController();
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
    	c.show();
    }
  
	 public void show() 
	    {
	    	VBox root;
	    	Stage primaryStage = new Stage();
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("WelcomeGUI.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Go-Nature Login");
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
	 private List<Label> createLabelList(WelcomeGUIController welcomeController)
	 {
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
			return 	tempMenuLabels;
	 }
	 

}

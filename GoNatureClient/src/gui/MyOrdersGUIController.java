package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyOrdersGUIController {

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
    private TableColumn<?, ?> colOrderNumber;

    @FXML
    private TableColumn<?, ?> colParkName;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colHour;

    @FXML
    private TableColumn<?, ?> colVisitorsAmount;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colUpdateStatus;

    @FXML
    private TableView<?> tblOrdersTable;

    @FXML
    void showAddOrder(MouseEvent event) {
     	AddOrderGUIController c = new AddOrderGUIController();
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
    	c.show();

    }

    @FXML
    void showMyProfile(MouseEvent event) {
    	MyProfileGUIController mp= new MyProfileGUIController();
    	((Node)event.getSource()).getScene().getWindow().hide();
    	mp.show();

    }
    
    public void show() 
    {
    	VBox root;
    	Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MyOrderGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("My Orders");
			MyOrdersGUIController myOrdersController = loader.getController();	
			fillTable(myOrdersController);
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(myOrdersController);
			MenuBarSelection.setMenuOptions(menuLabels);
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }
 private void fillTable(MyOrdersGUIController myOrdersController) {
		
		
	}

private List<Label> createLabelList(MyOrdersGUIController myOrdersController)
 {
	 List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(myOrdersController.mnuAddOrder);
		tempMenuLabels.add(myOrdersController.mnuMyOrders);
		tempMenuLabels.add(myOrdersController.mnuMyProfile);
		tempMenuLabels.add(myOrdersController.mnuParkEntrance);
		tempMenuLabels.add(myOrdersController.mnuRegistration);
		tempMenuLabels.add(myOrdersController.mnuParkDetails);
		tempMenuLabels.add(myOrdersController.mnuEvents);
		tempMenuLabels.add(myOrdersController.mnuReportsDepartment);
		tempMenuLabels.add(myOrdersController.mnuReportsManager);
		tempMenuLabels.add(myOrdersController.mnuParkCapacity);
		tempMenuLabels.add(myOrdersController.mnuRequests);
		return 	tempMenuLabels;
 }
 

}

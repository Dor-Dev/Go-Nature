package gui;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.sun.jmx.snmp.Timestamp;

import client.ClientController;
import client.MainClient;
import client.OrderController;
import common.Message;
import controllers.VisitorController;
import enums.DBControllerType;
import enums.OperationType;
import enums.UserTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Order;

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

	@FXML
	private ComboBox<String> cmbParkName;

	@FXML
	private ComboBox<String> cmbHour;

	@FXML
	private ComboBox<String> cmbNumOfVisitors;

	@FXML
	private TextField txtEmail;

	@FXML
	private Label lblDiscount;

	@FXML
	private Label lblTotalPrice;

	@FXML
	private Button btnSubmitOrder;

	@FXML
	private RadioButton rdSingleFamily;

	@FXML
	private ToggleGroup visitorType;

	@FXML
	private RadioButton rdGroup;

	@FXML
	private CheckBox cbPayNow;
	
	@FXML
	private DatePicker date;
	
	@FXML
	private Label lblOrderID;

	@FXML
	private Label lblNumVisitors;

	@FXML
	private Label lblPrice;
	
    @FXML
    private Label lblDateTime;



	public void show() {
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
			menuLabels = createLabelList(addOrderController);
			MenuBarSelection.setMenuOptions(menuLabels);
			addOrderController.initializeAddOrder();
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private List<Label> createLabelList(AddOrderGUIController addOrderController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(addOrderController.mnuAddOrder);
		tempMenuLabels.add(addOrderController.mnuMyOrders);
		tempMenuLabels.add(addOrderController.mnuMyProfile);
		tempMenuLabels.add(addOrderController.mnuParkEntrance);
		tempMenuLabels.add(addOrderController.mnuRegistration);
		tempMenuLabels.add(addOrderController.mnuParkDetails);
		tempMenuLabels.add(addOrderController.mnuEvents);
		tempMenuLabels.add(addOrderController.mnuReportsDepartment);
		tempMenuLabels.add(addOrderController.mnuReportsManager);
		tempMenuLabels.add(addOrderController.mnuParkCapacity);
		tempMenuLabels.add(addOrderController.mnuRequests);
		return tempMenuLabels;
	}

	@FXML
	public void initializeAddOrder() {
		/**
		 * Initialize combo box Parks name
		 */
		cmbParkName.getItems().removeAll(cmbParkName.getItems());
		cmbParkName.getItems().addAll("Luna-Park", "Shipment-Park", "Tempo-Park");
		// cmbParkName.getSelectionModel().select("Option B");

		/**
		 * Initialize cmb Visit Hour 10:00 - 17:00
		 */
		cmbHour.getItems().removeAll(cmbHour.getItems());
		cmbHour.getItems().addAll("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00");

		/**
		 * Initialize cmb Num of visitors 1-15
		 */
		cmbNumOfVisitors.getItems().removeAll(cmbNumOfVisitors.getItems());
		cmbNumOfVisitors.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15");

		// rdGroup.setDisable(true);
	}

	/**
	 * update pre-order discount at window in red
	 * 
	 * @param event
	 */
	@FXML
	void updateGroupDiscount(ActionEvent event) {
		if (rdGroup.isSelected())
			lblDiscount.setText("20%");
		if (rdSingleFamily.isSelected())
			lblDiscount.setText("15%");
	}

	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mp = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mp.show();

	}

	@FXML
	void showMyOrders(MouseEvent event) {
		MyOrdersGUIController mo = new MyOrdersGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mo.show();

	}

	@FXML
	void submitOrder(ActionEvent event) {

		/**
		 * userId can be some subscriber or only one who is no have orders, userId will
		 * take the right value we need
		 */
		int userID = VisitorController.loggedID;
		if (VisitorController.loggedID == 0)
			userID = VisitorController.subscriberConnected.getVisitorID();
		/**
		 * Radio button to get the right value the user is chosen
		 */
		RadioButton rd = (RadioButton) visitorType.getSelectedToggle();
		String typeToogleSelected = rd.getText();
		System.out.println(visitorType.getSelectedToggle());
		/*
		 * we use only round hours, so we need to save (int) only the 2 first charcters
		 * from the combo box
		 */
		int hour = Integer.parseInt((cmbHour.getValue().toString().substring(0, 2)));
		Order newOrder = new Order(cmbParkName.getValue(),
				date.getValue().format((DateTimeFormatter.ofPattern("yyyy-MM-dd"))), userID,
				Integer.parseInt((String) cmbNumOfVisitors.getValue().toString()), txtEmail.getText(),
				typeToogleSelected, cbPayNow.isSelected(), hour, 500);
		MainClient.clientConsole
				.accept(new Message(OperationType.AddOrder, DBControllerType.OrderDBController, (Object) newOrder));
		showPopUpWindow();
		
	}
	public void showPopUpWindow()
	{
		Order myOrder = OrderController.recivedOrder;
		VBox root;
    	Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/OrderPopUp.fxml"));
			root = loader.load();
			AddOrderGUIController addOrderController = loader.getController();
			addOrderController.lblOrderID.setText(String.valueOf("Order ID: "+myOrder.getOrderID()));
			addOrderController.lblNumVisitors.setText(String.valueOf("Number of visitors: "+myOrder.getNumOfVisitors()));
			addOrderController.lblPrice.setText(String.valueOf("Total Price: "+myOrder.getCost()));
			addOrderController.lblDateTime.setText("Date & Time: "+
			myOrder.getDateTime()+"  " + myOrder.getHourTime()+":00");
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manual Receipt");
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}


	/*
	 * close popup window button
	 */
	@FXML private javafx.scene.control.Button closeButton;

	@FXML
	   void closeButtonAction(ActionEvent event) {
	    // get a handle to the stage
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}

}

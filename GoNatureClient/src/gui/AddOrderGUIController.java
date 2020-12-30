package gui;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import client.ClientController;
import client.MainClient;
import client.OrderController;
import common.Message;
import controllers.RestartApp;
import controllers.VisitorController;
import enums.DBControllerType;
import enums.OperationType;
import enums.UserTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Order;
import logic.OrderRequest;

public class AddOrderGUIController {

	public static Order newOrder = null;
	public static OrderRequest request = null;
	private static String status = "complete";
	@FXML
	private Label mnuRequests;
	@FXML
	private Label mnuAddOrder;

	@FXML
	private Label mnuMyOrders;

	@FXML
	private TextField txtPhoneEnd;

	@FXML
	private ComboBox<String> cmbPhoneStart;

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
   private Label mnuLogout;
	
	  @FXML
	    void goToMainPage(MouseEvent event) {
		  RestartApp.restartParameters();
		  LoginGUIController login = new LoginGUIController();
		  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		  login.show();
	    }
  
  @FXML
	private Label lblDateTime;

	@FXML
	private DatePicker alternativeDatePicker;

	@FXML
	private ComboBox<String> cmbAlternativeHour;

	@FXML
	private Button btnSubmitAlternativeTime;

	@FXML
	private Label lblAlternativeMsg;


	@FXML
	private Button btnShowAvailableDates;

	@FXML
	private Button btnAddWaitingList;

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

		// Initialize combo box phone first 3 numbers
		cmbPhoneStart.getItems().removeAll(cmbPhoneStart.getItems());
		cmbPhoneStart.getItems().addAll("050", "051", "052", "053", "054", "055", "056", "057", "058", "059");

		/**
		 * set init paynow visible
		 */

		cbPayNow.setVisible(false);
		if (VisitorController.subscriberConnected != null
				&& VisitorController.subscriberConnected.getCreditCard() != null) {
			cbPayNow.setVisible(true);
			System.out.println("HERE");
		}
		/**
		 * set init Discount
		 */
		lblDiscount.setText(String.valueOf(calculateDiscount()) + "%");
		/**
		 * set Group option disable
		 */
		rdGroup.setDisable(true);
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

		if (VisitorController.subscriberConnected != null
				&& VisitorController.subscriberConnected.getType().equals("instructor")) {
			rdGroup.setDisable(false);
			System.out.println("IN");
		}
	}

	/**
	 * update pre-order discount at window in red
	 * 
	 * @param event
	 */
	@FXML
	void updateGroupDiscount(ActionEvent event) {

		/**
		 * this function update the pre-order discount and price this function is run
		 * when changing type/#ofVisitors/PayNow this function also contorol on the
		 * visible of "PAYNOW" CHECK BOX
		 */
		lblDiscount.setText(String.valueOf(calculateDiscount()) + "%");
		if (cmbNumOfVisitors.getValue() != null) {
			lblTotalPrice.setText((String.valueOf(calculatePriceAfterDiscount())) + "¤");
		}

		cbPayNow.setVisible(false);
		if (VisitorController.subscriberConnected != null
				&& VisitorController.subscriberConnected.getCreditCard() != null || rdGroup.isSelected())
			cbPayNow.setVisible(true);
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

		/**
		 * calculate price after discount
		 */

		int finalPrice = calculatePriceAfterDiscount(); // get the ticket price

		/**
		 * Sends request to server to place order
		 */

		newOrder = new Order(cmbParkName.getValue(),
				date.getValue().format((DateTimeFormatter.ofPattern("yyyy-MM-dd"))), userID,
				Integer.parseInt((String) cmbNumOfVisitors.getValue().toString()), txtEmail.getText(),
				typeToogleSelected, cbPayNow.isSelected(), hour, (int) finalPrice,
				cmbPhoneStart.getValue() + txtPhoneEnd.getText(), "Not sent", "Received");
		System.out.println("NEW ORDER CREATED!");
		request = new OrderRequest(date.getValue(), hour, Integer.parseInt(cmbNumOfVisitors.getValue()),cmbParkName.getValue());
		MainClient.clientConsole.accept(
				new Message(OperationType.OrderCheckDateTime, DBControllerType.OrderDBController, (Object) request));
		if (OrderController.orderCompleted) {
			System.out.println(OrderController.orderCompleted);
			executeAddOrderQuery();
		} else
			showFailureAddOrderPopUp();
	}

	private void executeAddOrderQuery() {

		MainClient.clientConsole
				.accept(new Message(OperationType.AddOrder, DBControllerType.OrderDBController, (Object) newOrder));
		simulationPopUp();
		if (newOrder.getStatus().equals("Received"))
			showPopUpWindow();

	}

	private void simulationPopUp() {

		Alert a = new Alert(AlertType.INFORMATION);
		if (newOrder.getStatus().equals("Received")) {
			a.setHeaderText("Order completed");
			a.setContentText("Your orderID: " + newOrder.getOrderID() + "\n" + newOrder.getEmail());
		} else {
			a.setHeaderText("Added to waiting list");
			a.setContentText("Your waiting orderID: " + newOrder.getOrderID() + "\n" + newOrder.getEmail());
		}
		a.setTitle("Simulation");
		a.showAndWait();
	}

	private int calculateDiscount() {
		// calculate discount per ticket
		int discount = 0;
		if (rdSingleFamily.isSelected())
			discount += 15; // price after pre-Order for signle/family
		if (rdGroup.isSelected())
			discount += 25;
		// if visitor is subscriber and not instructor
		if (VisitorController.subscriberConnected != null && !rdGroup.isSelected())
			discount += 20; // price after subscirber discount
		if (cbPayNow.isSelected() && rdGroup.isSelected()) // if instructor and pay now
			discount += 12;
		System.out.println("DIS:" + discount);
		return discount;
	}

	private int calculatePriceAfterDiscount() {
		double finalPrice = OrderController.getTicketPrice();
		System.out.println("STARTC:" + finalPrice);
		System.out.println("NUM:" + Integer.parseInt(cmbNumOfVisitors.getValue().toString()));
		finalPrice *= Integer.parseInt(cmbNumOfVisitors.getValue().toString());
		finalPrice = (finalPrice * (100 - calculateDiscount())) / 100;
		System.out.println("PRICE:" + finalPrice);
		return (int) finalPrice;
	}

	public void showPopUpWindow() {
		Order myOrder = OrderController.recivedOrder;
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/OrderPopUp.fxml"));
			root = loader.load();
			AddOrderGUIController addOrderController = loader.getController();
			addOrderController.lblOrderID.setText(String.valueOf("Order ID: " + myOrder.getOrderID()));
			addOrderController.lblNumVisitors
					.setText(String.valueOf("Number of visitors: " + myOrder.getNumOfVisitors()));
			addOrderController.lblPrice.setText(String.valueOf("Total Price: " + myOrder.getCost()));
			addOrderController.lblDateTime
					.setText("Date & Time: " + myOrder.getDateTime() + "  " + myOrder.getHourTime() + ":00");
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Receipt");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/*
	 * close popup window button
	 */
	@FXML
	private javafx.scene.control.Button closeButton;

	@FXML
	void closeButtonAction(ActionEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) closeButton.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	@FXML
	void AddWaitingList(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();
		newOrder.setStatus("Waiting List");
		executeAddOrderQuery(); // insert order into order DB with status "waiting" (waiting list)
	}

	@FXML
	void ShowAvailableDates(ActionEvent event) {

		VBox root;
		Stage primaryStage = new Stage();

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/AlternativeDatesPopUp.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("No Available Tickets");
			((Node) event.getSource()).getScene().getWindow().hide();
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	public void showFailureAddOrderPopUp() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/NoAvailableSpacesPopUp.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("No Available Spaces at choose time");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@FXML
	void submitAlternativeRequest(ActionEvent event) {

		if (cmbAlternativeHour.getValue() != null) {
			int hour = Integer.parseInt((cmbAlternativeHour.getValue().toString().substring(0, 2)));
			newOrder.setHourTime(hour);
			newOrder.setDatearrival(alternativeDatePicker.getValue().toString());
			MainClient.clientConsole
					.accept(new Message(OperationType.AddOrder, DBControllerType.OrderDBController, (Object) newOrder));
			simulationPopUp();
			showPopUpWindow();
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Error Parameters");
			a.setContentText("fix parametes");
			a.setTitle("Error Parameters");
			a.showAndWait();
		}
	}

	// initilaize hours for our parks
	public void initilaizeCmbHours(ComboBox<String> cmb) {
		int i = 0;
		boolean[] areAvaiableHour = new boolean[24];
		Arrays.fill(areAvaiableHour, Boolean.TRUE); // set all values to true
		int[] avaiableSpacesSum = OrderController.availableSpaces;
		cmb.getItems().removeAll(cmb.getItems());
		for (i = 10; i < 18; i++) {
			for (int j = 0; j < OrderController.managerDefultTravelHour; j++) {
				if (avaiableSpacesSum[i + j] + newOrder.getNumOfVisitors() > avaiableSpacesSum[0]) {		//at index 0 we got how many orders allow
					areAvaiableHour[i] = false;
				}
			}
		}
		for (i = 10; i < 18; i++) {
			if (areAvaiableHour[i] == true)
				cmb.getItems().addAll(i + ":00");
		}
		System.out.println(cmb.getItems().size());
		if (cmb.getItems().size() == 0) {
			lblAlternativeMsg.setText("No Hours for this day");
			lblAlternativeMsg.setStyle("-fx-font-size:14px");
			lblAlternativeMsg.setTextFill(Color.RED);
		} else
			lblAlternativeMsg.setText("");

	}

	@FXML
	void checkHoursForDate(ActionEvent event) {

		LocalDate dateAsked = alternativeDatePicker.getValue();
		OrderRequest request = new OrderRequest(dateAsked, newOrder.getNumOfVisitors());
		System.out.println(dateAsked);
		MainClient.clientConsole.accept(
				new Message(OperationType.checkAvailableHours, DBControllerType.OrderDBController, (Object) request));
		initilaizeCmbHours(cmbAlternativeHour);

	}

}

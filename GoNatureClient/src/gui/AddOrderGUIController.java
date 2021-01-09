package gui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.OrderController;
import controllers.RestartApp;
import controllers.VisitorController;
import enums.DBControllerType;
import enums.OperationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Order;
import logic.OrderRequest;
import logic.Validation;

/**
 * This class add Order to databaseto every client. option to add order to
 * waiting list option to choose alternative dates
 * 
 * @author Naor
 *
 */
public class AddOrderGUIController {

	public static Order newOrder = null;
	public static OrderRequest request = null;

	@FXML
	private CheckBox cbAgreeTerms;

	@FXML
	private Label lblReceivedOrderMsg;

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

	@FXML
	private Label lblGroupTip;

	@FXML
	private Label lblEventDiscount1;

	@FXML
	private Label lblEventDiscount2;

	@FXML
	private Label lblPriceList;

	/**
	 * function to load AddOrder screen
	 */
	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/AddOrderGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Add Order");
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
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

	/**
	 * function that choose the menu to show
	 * 
	 * @param addOrderController
	 * @return Arraylist with the menu options at this page
	 */
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

	/**
	 * Initialize all started screen in add order according to type of user
	 */
	@FXML
	public void initializeAddOrder() {

		// date picker disable typing
		date.getEditor().setDisable(true);

		// set managed false to labal of manager events
		lblEventDiscount1.setManaged(false);
		lblEventDiscount2.setManaged(false);

		// Initialize combo box phone first 3 numbers
		cmbPhoneStart.getItems().removeAll(cmbPhoneStart.getItems());
		cmbPhoneStart.getItems().addAll("050", "051", "052", "053", "054", "055", "056", "057", "058", "059");
		cmbPhoneStart.getSelectionModel().select(0);
		/**
		 * set init paynow visible
		 */

		cbPayNow.setVisible(false);
		if (VisitorController.subscriberConnected != null
				&& VisitorController.subscriberConnected.getCreditCard() != null) {
			cbPayNow.setVisible(true);
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
		cmbHour.getItems().addAll("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00");
		cmbHour.getSelectionModel().select(0);
		/**
		 * Initialize cmb Num of visitors 1-15
		 */
		cmbNumOfVisitors.getItems().removeAll(cmbNumOfVisitors.getItems());
		cmbNumOfVisitors.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15");

		// group tip disable
		lblGroupTip.setVisible(false);

		if (VisitorController.subscriberConnected != null
				&& VisitorController.subscriberConnected.getType().equals("Guide")) {

			rdGroup.setDisable(false);
		} else {
			Tooltip t = new Tooltip();
			t.setText("Only for Guides");
			lblGroupTip.setVisible(true);
			lblGroupTip.setTooltip(t);
		}
		DatePickerDisableDays(date); // disable past dates
	}

	/**
	 * this function get the current date and disable click on past dates and
	 * disable click on the next 2 days
	 * 
	 * @param date
	 */
	private void DatePickerDisableDays(DatePicker date) {
		Callback<DatePicker, DateCell> callB2 = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate today = LocalDate.now();

						setDisable(empty || item.compareTo(today.plusDays(2)) < 0);

					}

				};
			}

		};
		date.setDayCellFactory(callB2);
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

		if (OrderController.discountDateEvent != 0) {
			lblEventDiscount1.setText(date.getValue().toString() + " " + OrderController.eventName);
			lblEventDiscount1.setManaged(true);
			lblEventDiscount1.setVisible(true);
			lblEventDiscount2.setText(OrderController.discountDateEvent + "% off");
			lblEventDiscount2.setManaged(true);
			lblEventDiscount2.setVisible(true);
		} else {
			lblEventDiscount1.setManaged(false);
			lblEventDiscount1.setVisible(false);
			lblEventDiscount2.setManaged(false);
			lblEventDiscount2.setVisible(false);
		}
	}

	/**
	 * menu navigation function show profile screen
	 * 
	 * @param event
	 */
	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mp = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mp.show();

	}

	/**
	 * menu navigation function show my orders screen
	 * 
	 * @param event
	 */
	@FXML
	void showMyOrders(MouseEvent event) {
		MyOrdersGUIController mo = new MyOrdersGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mo.show();

	}

	/**

	 *  This method returns to the main page after the user presses on the "log out" button<br> 
	 * {@link restartParameters()} will be executed in order to reset relevant variables<br>
	 * @param event - the mouse event that occurs when the user clicks on log out

	 */
	@FXML
	void goToMainPage(MouseEvent event) {
		RestartApp.restartParameters();
		LoginGUIController login = new LoginGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		login.show();
	}

	/**
	 * function to valid the fields in the order form
	 * 
	 * @return String with some not vaild parameter
	 */
	public String fieldsValidation() {
		if (Validation.isNull(cmbParkName.getValue()))
			return "Park Name";
		if (Validation.isNull(date.getValue()))
			return "Date";
		if (cmbNumOfVisitors.getValue() == null)
			return "Number Of Visitors";
		if (!Validation.emailValidation(txtEmail.getText()))
			return "Email";
		if (!Validation.phoneValidation(cmbPhoneStart.getValue() + txtPhoneEnd.getText()))
			return "Phone";
		return "OK";
	}

	/**
	 * function to send order request send accept from clien to server the server
	 * check if there is a place available for the order at asked date and time if
	 * no will suggest alternative options by popup
	 * 
	 * @param event
	 */
	@FXML
	void submitOrder(ActionEvent event) {

		/**
		 * validation for the fields, if have error parameters pop a message with ask of
		 * wrong field
		 */
		String ValidMsg = fieldsValidation();
		if (!ValidMsg.equals("OK")) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Please correct the " + ValidMsg + " field");
			a.setContentText("Validation Error");
			a.setTitle("Validation Error");
			a.showAndWait();
			return;
		}
		/**
		 * userId can be some subscriber or only one who is no have orders, userId will
		 * take the right value we need
		 */
		int userID = VisitorController.loggedID;
		if (VisitorController.subscriberConnected != null)
			userID = VisitorController.subscriberConnected.getVisitorID();
		/**
		 * Radio button to get the right value the user is chosen
		 */
		RadioButton rd = (RadioButton) visitorType.getSelectedToggle();
		String typeToogleSelected = rd.getText();
		/*
		 * we use only round hours, so we need to save (int) only the 2 first charcters
		 * from the combo box
		 */
		String[] hour;
		hour = cmbHour.getValue().split(":");
		int hourInteger = Integer.parseInt((hour[0]));

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
				typeToogleSelected, cbPayNow.isSelected(), hourInteger, (int) finalPrice,
				cmbPhoneStart.getValue() + txtPhoneEnd.getText(), "Not sent", "Received", calculateDiscount());
		request = new OrderRequest(date.getValue(), hourInteger, Integer.parseInt(cmbNumOfVisitors.getValue()),
				cmbParkName.getValue());
		MainClient.clientConsole.accept(
				new Message(OperationType.OrderCheckDateTime, DBControllerType.OrderDBController, (Object) request));
		if (OrderController.orderCompleted)
			executeAddOrderQuery();
		else
			showFailureAddOrderPopUp();
	}

	/**
	 * function to excute add order query after check if there is a place at the
	 * asked date time.
	 */
	private void executeAddOrderQuery() {

		MainClient.clientConsole
				.accept(new Message(OperationType.AddOrder, DBControllerType.OrderDBController, (Object) newOrder));
		simulationPopUp();
		if (newOrder.getStatus().equals("Received"))
			showPopUpWindow();

	}

	/**
	 * function to pop simulation pop up The pop-up simulates an SMS/Email message
	 */
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

	/**
	 * calculate discount per ticket The calculation takes into account the type of
	 * visitor
	 * 
	 * @return discount amount
	 */
	private int calculateDiscount() {

		int discount = 0;
		if (OrderController.discountDateEvent != 0)
			discount += OrderController.discountDateEvent;
		if (rdSingleFamily.isSelected())
			discount += 15; // price after pre-Order for signle/family
		if (rdGroup.isSelected())
			discount += 25;
		// if visitor is subscriber and not Guide
		if (VisitorController.subscriberConnected != null && !rdGroup.isSelected())
			discount += 20; // price after subscirber discount
		if (cbPayNow.isSelected() && rdGroup.isSelected()) // if Guide and pay now
			discount += 12;
		return discount;
	}

	/**
	 * function to calculate the final price after discount use the discount
	 * function
	 * 
	 * @return last price for all the order
	 */
	private int calculatePriceAfterDiscount() {
		double finalPrice = OrderController.getTicketPrice();
		finalPrice *= Integer.parseInt(cmbNumOfVisitors.getValue().toString());
		finalPrice = (finalPrice * (100 - calculateDiscount())) / 100;
		return (int) finalPrice;
	}

	/**
	 * this function pop up the order details include orderID that given by the
	 * system
	 */
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

	/**
	 * validation to check in the order details pop up if click on check box that
	 * agree to term (read the important message). only if click on check box the
	 * user can click and success ok button
	 * 
	 * @param event
	 */
	@FXML
	void closeButtonWithTerms(ActionEvent event) {
		if (cbAgreeTerms.isSelected())
			closeButtonAction(event);
		else
			lblReceivedOrderMsg.setText("Must read and confirm the important information");
	}

	/**
	 * the function change the type of order to "Waiting list" and add this order to
	 * the data-base at this status.
	 * 
	 * @param event
	 */
	@FXML
	void AddWaitingList(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();
		newOrder.setStatus("Waiting list");
		executeAddOrderQuery(); // insert order into order DB with status "waiting" (waiting list)

	}

	/**
	 * this function load the "Alternative dates" screen, This screen can only
	 * appear after attempting to book at no possible time
	 * 
	 * @param event
	 */
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
			AddOrderGUIController addOrderController = loader.getController();
			DatePickerDisableDays(addOrderController.alternativeDatePicker);
			((Node) event.getSource()).getScene().getWindow().hide();
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	/**
	 * function to load the alternative options after trying to order at no availble
	 * tickets time
	 */
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

	/**
	 * The function add order with the alternative date and time that choosen by the
	 * visitor (with the other same parameters)
	 * 
	 * @param event
	 */
	@FXML
	void submitAlternativeRequest(ActionEvent event) {

		String[] hour;
		hour = cmbAlternativeHour.getValue().split(":");
		if (cmbAlternativeHour.getValue() != null || alternativeDatePicker.getValue() != null) {
			int hourInteger = Integer.parseInt((hour[0]));
			newOrder.setHourTime(hourInteger);
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
		for (i = 9; i < 18; i++) {
			for (int j = 0; j < OrderController.managerDefultTravelHour; j++) {
				if (avaiableSpacesSum[i + j] + newOrder.getNumOfVisitors() > avaiableSpacesSum[0]) { // at index 0 we //
																										// orders allow
					areAvaiableHour[i] = false;
				}
			}
		}
		for (i = 9; i < 18; i++) {
			if (areAvaiableHour[i] == true)
				cmb.getItems().addAll(i + ":00");
		}
		if (cmb.getItems().size() == 0) {
			lblAlternativeMsg.setText("The entire date is fully booked");
			lblAlternativeMsg.setStyle("-fx-font-size:14px");
			lblAlternativeMsg.setTextFill(Color.RED);
		} else
			lblAlternativeMsg.setText("");
	}

	/**
	 * function to send accept from client to server wich hours are available for
	 * the asked order at specific date and after this call to initilaize the combo
	 * box hours with only the availble hours.
	 * 
	 * @param event
	 */
	@FXML
	void checkHoursForDate(ActionEvent event) {

		LocalDate dateAsked = alternativeDatePicker.getValue();
		OrderRequest request = new OrderRequest(dateAsked, newOrder.getNumOfVisitors());
		MainClient.clientConsole.accept(
				new Message(OperationType.checkAvailableHours, DBControllerType.OrderDBController, (Object) request));
		initilaizeCmbHours(cmbAlternativeHour);

	}

	/**
	 * this function load and open in another window the price list
	 * 
	 * @param event
	 */
	@FXML
	void openPriceList(MouseEvent event) {

		Image priceImage = new Image(getClass().getResourceAsStream("/gui/img/pricelist.jpeg"));
		ImageView priceListImage = new ImageView(priceImage); // same here for winner image
		priceListImage.setFitWidth(370);
		priceListImage.setFitHeight(520);
		Stage priceListStage = new Stage();
		StackPane myLayout2 = new StackPane();
		Scene myScene2 = new Scene(myLayout2, 370, 520);
		Label label2 = new Label("", priceListImage);
		myLayout2.getChildren().add(label2);
		priceListStage.setTitle("Price List");
		priceListStage.setScene(myScene2);
		priceListStage.show();
	}

	/**
	 * The function send accept to server to know if there is a discount event at
	 * specific asked day
	 * 
	 * @param event
	 */
	@FXML
	void checkDiscountEvents(ActionEvent event) {
		if (date.getValue() == null)
			return;
		if (cmbParkName.getValue() == null)
			return;
		OrderRequest request = new OrderRequest(date.getValue(), 0, 0, cmbParkName.getValue());
		MainClient.clientConsole.accept(
				new Message(OperationType.checkEventDiscount, DBControllerType.OrderDBController, (Object) request));
		updateGroupDiscount(event);
	}

}

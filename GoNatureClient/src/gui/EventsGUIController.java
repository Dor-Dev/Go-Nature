package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controllers.RestartApp;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.ParkController;
import enums.DBControllerType;
import enums.OperationType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Event;
import logic.Validation;

/**
 * Class for event FXML , display only for park manager and allows him to send
 * new event request and shows him all the events that do not end yet. When the
 * event request send the table refreshed.
 * 
 * @author dorswisa
 * @param data save all events data as a list.
 *
 */
public class EventsGUIController implements Initializable {
	public static List<Event> data = null;

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
	private TableView<Event> tblEvents;
	@FXML
	private TableColumn<Event, String> colEventName;

	@FXML
	private TableColumn<Event, Date> colStart;

	@FXML
	private TableColumn<Event, Date> colEnd;

	@FXML
	private TableColumn<Event, Integer> colDiscount;

	@FXML
	private TableColumn<Event, String> colStatus;

	@FXML
	private Label mnuReportsDepartment;

	@FXML
	private Label mnuRequests;

	@FXML
	private Label mnuLogout;

	@FXML
	private TextField txtEventName;

	@FXML
	private DatePicker dpStartDate;

	@FXML
	private DatePicker dpEndDate;

	@FXML
	private TextField txtDicount;

	@FXML
	private Button btnEvent;
	@FXML
	private Button closeButton;

	@FXML
	void goToMainPage(MouseEvent event) {
		RestartApp.restartParameters();
		LoginGUIController login = new LoginGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		login.show();
	}

	@FXML
	void goToManagerReports(MouseEvent event) {
		ManagerReportGUIController mRc = new ManagerReportGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mRc.show();
	}

	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();
	}

	@FXML
	void goToParkDetails(MouseEvent event) {
		ManagerDetailsGUIController mDgc = new ManagerDetailsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		mDgc.show();
	}

	@FXML
	void closePopUp(ActionEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) closeButton.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	/**
	 * When the button pressed it send to the server new event request.<br>
	 * Use {@link #eventsFeildsValidation()} for fields validation.
	 * 
	 * @param event When button pressed send to Server Message.
	 * 
	 */
	@FXML
	void sendEventRequest(ActionEvent event) {
		String validMsg = eventsFeildsValidation();
		if (!validMsg.equals("OK")) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Please correct the " + validMsg + " field");
			a.setContentText("Validation Error");
			a.setTitle("Validation Error");
			a.showAndWait();
			return;
		}
		String status = "Waiting";
		LocalDate stDate = dpStartDate.getValue();
		Date startDate = Date.valueOf(stDate);
		LocalDate edDate = dpEndDate.getValue();
		Date endDate = Date.valueOf(edDate);
		String parkName = EmployeeController.employeeConected.getOrganizationAffilation();
		Event eventRequest = new Event(parkName, txtEventName.getText(), startDate, endDate,
				Integer.valueOf(txtDicount.getText()), status);
		MainClient.clientConsole.accept( // send to server event request, the event request will be show to department
											// manager.
				new Message(OperationType.EventRequest, DBControllerType.ParkDBController, (Object) eventRequest));

		// Success pop-up
		if (ParkController.Parktype.equals(OperationType.EventRequestSuccess)) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("The event has been sent successfully");
			a.setContentText("Event request was sent successfully to Department Manager.");
			a.setTitle("Event Request");
			a.showAndWait();
			setData(); // Refresh the event table.
			setStatusColor(); // Set status color of the returned table.


		}

	}

	/**
	 * Check what field is incorrect and returns the field name.
	 * 
	 * @return Name of the field that is incorrect.
	 */
	private String eventsFeildsValidation() {
		if (!Validation.eventNameValidation(txtEventName.getText()))
			return "Event Name";
		if (Validation.isNull(dpStartDate.getValue()))
			return "Start Date";
		if (Validation.isNull(dpEndDate.getValue()))
			return "End Date";
		if (!Validation.discoutValidation(txtDicount.getText()))
			return "Discount";
		return "OK";

	}

	/**
	 * Shows ParkMangerEvents GUI.<br>
	 * Before show the ParkManagerEvents, use {@link #setData()} to send Message to
	 * server and when the data has returned insert the data list to the table.
	 */
	public void show() {

		VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ParkManagerEvents.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Events");
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
			EventsGUIController eventsGUIController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = eventsGUIController.createLabelList(eventsGUIController);

			MenuBarSelection.setMenuOptions(menuLabels);
			eventsGUIController.setData(); // Before the page shows set the table data by sending Message to server and get the events data .
			eventsGUIController.setStatusColor();// Set status color for each row in the table.
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private List<Label> createLabelList(EventsGUIController managerReportsController) {
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

	/**
	 * Initialize the tables columns.<br>
	 * Set status color for each row using
	 * {@link TableColumn#setCellFactory(Callback)}
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

		colEventName.setStyle("-fx-alignment: CENTER");
		colStart.setStyle("-fx-alignment: CENTER");
		colEnd.setStyle("-fx-alignment: CENTER");
		colDiscount.setStyle("-fx-alignment: CENTER");
		colStatus.setStyle("-fx-alignment: CENTER");

		// disable the text field of the date pickers.
		dpStartDate.getEditor().setDisable(true);
		dpEndDate.getEditor().setDisable(true);

		// Disable the irrelevant dates in the date picker.
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate today = LocalDate.now();
						if (dpStartDate.getValue() != null)
							setDisable(empty || item.compareTo(today) < 0
									|| item.isBefore(dpStartDate.getValue().plusDays(1)));
						else {
							setDisable(true);
						}
					}

				};
			}

		};
		dpEndDate.setDayCellFactory(callB);
		// Disable irrelevant dates in the end date picker
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
		dpStartDate.setDayCellFactory(callB2);

	}

	/**
	 * set event table data using
	 * {@link TableView#setItems(javafx.collections.ObservableList)}
	 */
	public void setData() {
		String parkName = EmployeeController.employeeConected.getOrganizationAffilation();
		MainClient.clientConsole.accept( // Send to server request to get all the event data that relevant to
											// organization affiliation of the employee that connected.
				new Message(OperationType.showManagerEvents, DBControllerType.ParkDBController, (Object) parkName));
		tblEvents.setItems(FXCollections.observableArrayList(data));
		colEnd.setSortType(TableColumn.SortType.ASCENDING);
		tblEvents.getSortOrder().add(colEnd);
		tblEvents.sort();

	}


	/**
	 * Set the event status of each row the the relevant color. using
	 * {@link TableColumn#setCellFactory(Callback)} to set status color.
	 */
	private void setStatusColor() {
		// Set color status in each row.
		colStatus.setCellFactory(colStatus -> {
			return new TableCell<Event, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty); // This is mandatory

					if (item == null || empty) { // If the cell is empty
						setText(null);
						setStyle("");
					} else {
						setText(item); // Put the String data in the cell
						Event tmp = getTableView().getItems().get(getIndex());

						if (tmp.getStatus().equals("Waiting")) {
							setTextFill(Color.BLACK);
						}
						if (tmp.getStatus().equals("Active")) {
							setTextFill(Color.GREEN);
						}
						if (tmp.getStatus().equals("Canceled"))
							setTextFill(Color.RED);
					}
				}

			};
		});
	}
}

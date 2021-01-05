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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Event;

public class EventsGUIController implements Initializable {
	public static List<Event> data;

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

	@FXML
	void sendEventRequest(ActionEvent event) {
		String status = "Waiting";
		LocalDate stDate = dpStartDate.getValue();
		Date startDate = Date.valueOf(stDate);
		LocalDate edDate = dpEndDate.getValue();
		Date endDate = Date.valueOf(edDate);
		String parkName = EmployeeController.employeeConected.getOrganizationAffilation();
		Event eventRequest = new Event(parkName, txtEventName.getText(), startDate, endDate,
				Integer.valueOf(txtDicount.getText()), status);
		MainClient.clientConsole.accept(
				new Message(OperationType.EventRequest, DBControllerType.ParkDBController, (Object) eventRequest));
		System.out.println(ParkController.Parktype);
		// Success pop-up
		if (ParkController.Parktype.equals(OperationType.EventRequestSuccess)) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("The event has been sent successfully");
			a.setContentText("Event request was sent successfully to Department Manager.");
			a.setTitle("Event Request");
			a.showAndWait();

		}

	}

	public void show() {

		VBox root;
		Stage primaryStage = new CloseStage();
		String parkName = EmployeeController.employeeConected.getOrganizationAffilation();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ParkManagerEvents.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Events");
			EventsGUIController eventsGUIController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = eventsGUIController.createLabelList(eventsGUIController);
			MenuBarSelection.setMenuOptions(menuLabels);
			MainClient.clientConsole.accept(
					new Message(OperationType.showActiveEvents, DBControllerType.ParkDBController, (Object) parkName));
			eventsGUIController.setData();
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
		colEventName.setStyle("-fx-alignment: CENTER");
		colStart.setStyle("-fx-alignment: CENTER");
		colEnd.setStyle("-fx-alignment: CENTER");
		colDiscount.setStyle("-fx-alignment: CENTER");
		dpStartDate.getEditor().setDisable(true);
		dpEndDate.getEditor().setDisable(true);
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate today = LocalDate.now();
						if (dpStartDate.getValue() != null)
							setDisable(empty || item.compareTo(today) <0
									|| item.isBefore(dpStartDate.getValue().plusDays(1)));
						else {
							setDisable(true);
						}
					}

				};
			}

		};
		dpEndDate.setDayCellFactory(callB);

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

	public void setData() {
		tblEvents.setItems(FXCollections.observableArrayList(data));
	}
}

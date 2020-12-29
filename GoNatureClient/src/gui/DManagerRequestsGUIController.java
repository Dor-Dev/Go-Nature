package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import controllers.RestartApp;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.RequestsController;
import enums.DBControllerType;
import enums.OperationType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Event;
import logic.Update;

public class DManagerRequestsGUIController implements Initializable {
	public static List<Event> eventData;
	public static List<Update> updateData;

	@FXML
	private TableView<Event> tblEventTable;
	@FXML
	private TableColumn<Event, Integer> colERequestNumber;

	@FXML
	private TableColumn<Event, String> colEventName;

	@FXML
	private TableColumn<Event, Date> colStartDate;

	@FXML
	private TableColumn<Event, Date> colEndDate;

	@FXML
	private TableColumn<Event, Integer> colDiscount;

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
	private TableView<Update> tblUpdateTable;
	@FXML
	private ComboBox<String> cmbRequestType;
	@FXML
	private TableColumn<Update, Integer> colRequestNumber;

	@FXML
	private TableColumn<Update, Integer> colParkCapacity;

	@FXML
	private TableColumn<Update, Integer> colDifference;

	@FXML
	private TableColumn<Update, Integer> colVisitingTime;
	@FXML
	private TableColumn<Event, Void> colButtons;

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
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();

	}

	@FXML
	void showParkCapacity(MouseEvent event) {
		ParkCapacityGUIController pC = new ParkCapacityGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		pC.show();
	}

	@FXML
	void showReports(MouseEvent event) {
		DManagerReportsGUIController rP = new DManagerReportsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		rP.show();
	}


	@FXML
	void showRequests(MouseEvent event) {
		DManagerRequestsGUIController rQ = new DManagerRequestsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		rQ.show();
	}


	public void show() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/DManagerRequests.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Department manager Requests");
			DManagerRequestsGUIController dManagerRequestsController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(dManagerRequestsController);
			MenuBarSelection.setMenuOptions(menuLabels);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	/**
	 * create label list for menu bar selction
	 * @param dManagerRequestsController
	 * @return
	 */
	private List<Label> createLabelList(DManagerRequestsGUIController dManagerRequestsController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(dManagerRequestsController.mnuAddOrder);
		tempMenuLabels.add(dManagerRequestsController.mnuMyOrders);
		tempMenuLabels.add(dManagerRequestsController.mnuMyProfile);
		tempMenuLabels.add(dManagerRequestsController.mnuParkEntrance);
		tempMenuLabels.add(dManagerRequestsController.mnuRegistration);
		tempMenuLabels.add(dManagerRequestsController.mnuParkDetails);
		tempMenuLabels.add(dManagerRequestsController.mnuEvents);
		tempMenuLabels.add(dManagerRequestsController.mnuReportsDepartment);
		tempMenuLabels.add(dManagerRequestsController.mnuReportsManager);
		tempMenuLabels.add(dManagerRequestsController.mnuParkCapacity);
		return tempMenuLabels;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**
		 * Initialize combo box Parks name
		 */
		cmbRequestType.getItems().removeAll(cmbRequestType.getItems());
		cmbRequestType.getItems().addAll("Update Requests", "Event Requests");
		/**
		 * Initialize update table column
		 */
		colRequestNumber.setCellValueFactory(new PropertyValueFactory<Update, Integer>("requestNum"));
		colParkCapacity.setCellValueFactory(new PropertyValueFactory<Update, Integer>("capacity"));
		colDifference.setCellValueFactory(new PropertyValueFactory<Update, Integer>("difference"));
		colVisitingTime.setCellValueFactory(new PropertyValueFactory<Update, Integer>("visitingTime"));

		colRequestNumber.setStyle("-fx-alignment: CENTER");
		colParkCapacity.setStyle("-fx-alignment: CENTER");
		colDifference.setStyle("-fx-alignment: CENTER");
		colVisitingTime.setStyle("-fx-alignment: CENTER");

		/**
		 * Initialize event table column
		 */
		colERequestNumber.setCellValueFactory(new PropertyValueFactory<>("requestNum"));
		colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

		colERequestNumber.setStyle("-fx-alignment: CENTER");
		colEventName.setStyle("-fx-alignment: CENTER");
		colStartDate.setStyle("-fx-alignment: CENTER");
		colEndDate.setStyle("-fx-alignment: CENTER");
		colDiscount.setStyle("-fx-alignment: CENTER");

		// hide 2 tables
		tblEventTable.setVisible(false);
		tblUpdateTable.setVisible(false);
		addUpdateConfrimationButtons();
		addEventConfrimationButtons();
		cmbRequestType.setOnAction(e -> chooseRelevantTable());

	}

	private void addUpdateConfrimationButtons() {
		TableColumn<Update, Void> option = new TableColumn<>("");
		Callback<TableColumn<Update, Void>, TableCell<Update, Void>> cellFactory = new Callback<TableColumn<Update, Void>, TableCell<Update, Void>>() {
			@Override
			public TableCell<Update, Void> call(final TableColumn<Update, Void> param) {
				final TableCell<Update, Void> cell = new TableCell<Update, Void>() {

					private final Button btnV = new Button("Approve");
					private final Button btnX = new Button("Decline");

					{
						btnV.setOnAction((ActionEvent event) -> {
							Update tmp = getTableView().getItems().get(getIndex());
							String parkName = EmployeeController.employeeConected.getOrganizationAffilation();
							tmp.setParkName(parkName);
							MainClient.clientConsole.accept(new Message(OperationType.UpdateConfrimation,
									DBControllerType.RequestsDBController, (Object) tmp));
							if (RequestsController.requestType.equals(OperationType.UpdateConfrimation)) {
								Alert a = new Alert(AlertType.INFORMATION);
								a.setHeaderText("The update has been authorized");
								a.setContentText("The park: "+parkName+" parameters was updated successfully");
								a.setTitle("Update Success");
								a.showAndWait();
								showUpdateTable();
							}
						});

						btnX.setOnAction((ActionEvent event) -> {
							Update tmp = getTableView().getItems().get(getIndex());
							MainClient.clientConsole.accept(new Message(OperationType.UpdateDecline,
									DBControllerType.RequestsDBController, (Object) tmp));
							if (RequestsController.requestType.equals(OperationType.UpdateDecline)) {
								Alert a = new Alert(AlertType.INFORMATION);
								a.setHeaderText("The update has been declined");
								a.setContentText("The update was cancel");
								a.setTitle("Update Declined");
								a.showAndWait();
								showUpdateTable();
							}
						});

					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							HBox hBox = new HBox(10, btnV, btnX);
							setGraphic(hBox);
							hBox.setAlignment(Pos.CENTER);
						}
					}

				};

				return cell;
			}
		};
		option.setCellFactory(cellFactory);
		tblUpdateTable.getColumns().add(option);
	}

	private void addEventConfrimationButtons() {
		Callback<TableColumn<Event, Void>, TableCell<Event, Void>> cellFactory = new Callback<TableColumn<Event, Void>, TableCell<Event, Void>>() {
			@Override
			public TableCell<Event, Void> call(final TableColumn<Event, Void> param) {
				final TableCell<Event, Void> cell = new TableCell<Event, Void>() {

					private final Button btnV = new Button("Approve");
					private final Button btnX = new Button("Decline");

					{
						btnV.setOnAction((ActionEvent event) -> {

							Event tmp = getTableView().getItems().get(getIndex());
							MainClient.clientConsole.accept(new Message(OperationType.EventApproval,
									DBControllerType.RequestsDBController, (Object) tmp));
							if (RequestsController.requestType.equals(OperationType.EventActivated)) {
								Alert a = new Alert(AlertType.INFORMATION);
								a.setHeaderText("The event has been activated");
								a.setContentText("The Event was activated");
								a.setTitle("Event Activated");
								a.showAndWait();
								showEventTable();

							}

						});

						btnX.setOnAction((ActionEvent event) -> {
							Event tmp = getTableView().getItems().get(getIndex());
							MainClient.clientConsole.accept(new Message(OperationType.EventDecline,
									DBControllerType.RequestsDBController, (Object) tmp));
							if (RequestsController.requestType.equals(OperationType.EventCanceled)) {
								Alert a = new Alert(AlertType.INFORMATION);
								a.setHeaderText("The event has been canceled");
								a.setContentText("The Event was canceled");
								a.setTitle("Event Decline");
								a.showAndWait();
								showEventTable();
							}
						});

					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							HBox hBox = new HBox(10, btnV, btnX);
							setGraphic(hBox);
							hBox.setAlignment(Pos.CENTER);
						}
					}

				};

				return cell;
			}
		};
		colButtons.setCellFactory(cellFactory);
	}
	/**
	 * choose which table to show according to the combo box selection.
	 */
	private void chooseRelevantTable() {
		if (cmbRequestType.getValue().equals("Update Requests"))
			showUpdateTable();
		if (cmbRequestType.getValue().equals("Event Requests"))
			showEventTable();
	}

	private void showEventTable() {
		String parkName = EmployeeController.employeeConected.getOrganizationAffilation();
		tblEventTable.setVisible(true);
		tblEventTable.setManaged(true);
		tblUpdateTable.setVisible(false);
		tblUpdateTable.setManaged(false);
		MainClient.clientConsole.accept(
				new Message(OperationType.GetEventTable, DBControllerType.RequestsDBController, (Object) parkName));
		if (RequestsController.requestType.equals(OperationType.EventTableArrived))
			setEventData();

	}

	private void setEventData() {
		tblEventTable.setItems(FXCollections.observableArrayList(eventData));
	}

	private void showUpdateTable() {
		String parkName = EmployeeController.employeeConected.getOrganizationAffilation();
		tblUpdateTable.setVisible(true);
		tblUpdateTable.setManaged(true);
		tblEventTable.setVisible(false);
		tblEventTable.setManaged(false);
		MainClient.clientConsole.accept(
				new Message(OperationType.GetUpdateTable, DBControllerType.RequestsDBController, (Object) parkName));
		if (RequestsController.requestType.equals(OperationType.UpdateTableArrived))
			setUpdateData();

	}

	private void setUpdateData() {
		tblUpdateTable.setItems(FXCollections.observableArrayList(updateData));
	}
}

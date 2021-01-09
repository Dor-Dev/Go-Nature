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
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Event;
import logic.Update;

/**
 * Class who controls all the Department manager requests.<br>
 * Choosing from combo box which requests table is showing, then the manager can approve or cancel the request.
 * @author dorswisa
 *
 */
public class DManagerRequestsGUIController implements Initializable {
	/**
	 * Static variable for saving the event data that arrives from server.
	 */
	public static List<Event> eventData;
	/**
	 * Static variable for saving the event data that arrives from server.
	 */
	public static List<Update> updateData;

	@FXML
	private TableView<Event> tblEventTable;
	@FXML
	private TableColumn<Event, String> colEventParkName;
	@FXML
	private TableColumn<Event, String> colEventName;

	@FXML
	private TableColumn<Event, Date> colStartDate;

	@FXML
	private TableColumn<Event, Date> colEndDate;

	@FXML
	private TableColumn<Event, Integer> colDiscount;
	@FXML
	private TableColumn<Event, String> colEventStatus;
	@FXML
	private TableColumn<Event, Void> colEventButtons;

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
    private Label lblRequestTip;

	@FXML
	private TableView<Update> tblUpdateTable;
	@FXML
	private ComboBox<String> cmbRequestType;
	@FXML
	private TableColumn<Update, String> colUpdateParkName;

	@FXML
	private TableColumn<Update, Integer> colParkCapacity;

	@FXML
	private TableColumn<Update, Integer> colDifference;

	@FXML
	private TableColumn<Update, Integer> colVisitingTime;
	@FXML
	private TableColumn<Update, String> colUpdateStatus;
	@FXML
	private TableColumn<Update, Void> colUpdateButtons;

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
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/DManagerRequests.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Requests");
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
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
	 * create label list for menu bar selection.
	 * 
	 * @param dManagerRequestsController controller of the DManagerRequests GUI
	 * @return {@value List} of all the relevant labels we need at Event GUI page. 
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
	/**
	 * Initialize all the controllers of the GUI, Update table, Event Table,ComboBox parkName.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Initialize combo box Parks name
		cmbRequestType.getItems().removeAll(cmbRequestType.getItems());
		cmbRequestType.getItems().addAll("Update Requests", "Event Requests");

		// Initialize update table column

		colUpdateParkName.setCellValueFactory(new PropertyValueFactory<Update, String>("parkName"));
		colParkCapacity.setCellValueFactory(new PropertyValueFactory<Update, Integer>("capacity"));
		colDifference.setCellValueFactory(new PropertyValueFactory<Update, Integer>("difference"));
		colVisitingTime.setCellValueFactory(new PropertyValueFactory<Update, Integer>("visitingTime"));
		colUpdateStatus.setCellValueFactory(new PropertyValueFactory<Update, String>("status"));

		colUpdateParkName.setStyle("-fx-alignment: CENTER");
		colParkCapacity.setStyle("-fx-alignment: CENTER");
		colDifference.setStyle("-fx-alignment: CENTER");
		colVisitingTime.setStyle("-fx-alignment: CENTER");
		colUpdateStatus.setStyle("-fx-alignment: CENTER");

		// Initialize event table column

		colEventParkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
		colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
		colEventStatus.setCellValueFactory(new PropertyValueFactory<Event, String>("status"));

		colEventParkName.setStyle("-fx-alignment: CENTER");
		colEventName.setStyle("-fx-alignment: CENTER");
		colStartDate.setStyle("-fx-alignment: CENTER");
		colEndDate.setStyle("-fx-alignment: CENTER");
		colDiscount.setStyle("-fx-alignment: CENTER");
		colEventStatus.setStyle("-fx-alignment: CENTER");
		
		Tooltip t = new Tooltip();
		t.setText("Choose the relevant requests table.\nThe information presented in the table\nis for all requests whose end date has not passed ");
		lblRequestTip.setTooltip(t);

		// hide 2 tables
		tblEventTable.setVisible(false);
		tblUpdateTable.setVisible(false);
		addUpdateConfrimationButtons();
		addEventConfrimationButtons();
		cmbRequestType.setOnAction(e -> chooseRelevantTable());

	}
	/** 
	 * Adding the colUpdateButtons 2 buttons, one Aprrove button and the other is Decline button.
	 */
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
							MainClient.clientConsole.accept(new Message(OperationType.UpdateConfrimation,
									DBControllerType.RequestsDBController, (Object) tmp));
							if (RequestsController.requestType.equals(OperationType.UpdateConfrimation)) {
								Alert a = new Alert(AlertType.INFORMATION);
								a.setHeaderText("The update has been authorized");
								a.setContentText(
										"The park: " + tmp.getParkName() + " parameters was updated successfully");
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
							Update tmp = getTableView().getItems().get(getIndex());
							HBox hBox1 = new HBox(10, btnV, btnX);
							HBox hBox2 = new HBox();
							if (!tmp.getStatus().equals("Waiting")) {
								setGraphic(hBox2);
								hBox2.setAlignment(Pos.CENTER);
							} else {
								setGraphic(hBox1);
								hBox1.setAlignment(Pos.CENTER);
							}
						}
					}

				};

				return cell;
			}
		};
		colUpdateButtons.setCellFactory(cellFactory);

	}
	/** 
	 * Adding the colEventButtons 2 buttons, one Aprrove button and the other is Decline button.
	 */
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
					//Set the relevant  buttons to the column
					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							Event tmp = getTableView().getItems().get(getIndex());
							HBox hBox1 = new HBox(10, btnV, btnX);
							HBox hBox2 = new HBox();
							if (!tmp.getStatus().equals("Waiting")) {
								setGraphic(hBox2);
								hBox2.setAlignment(Pos.CENTER);
							} else {
								setGraphic(hBox1);
								hBox1.setAlignment(Pos.CENTER);
							}
						}
					}

				};

				return cell;
			}
		};
		colEventButtons.setCellFactory(cellFactory);
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
	/**
	 * Request the events table from Server.<br>
	 * Shows the event table and hide the update table.<br>
	 * Department Manager can view all park manager reports. <br>
	 * {@link #setEventData()} to insert to the event table the data.<br>
	 * {@link #setEventStatusColor()} to set the status color for each row.
	 */
	private void showEventTable() {
		tblEventTable.setVisible(true);
		tblEventTable.setManaged(true);
		tblUpdateTable.setVisible(false);
		tblUpdateTable.setManaged(false);
		MainClient.clientConsole.accept(
				new Message(OperationType.GetEventTable, DBControllerType.RequestsDBController, (Object) null));
		if (RequestsController.requestType.equals(OperationType.EventTableArrived)) {
			setEventData();
			setEventStatusColor();
		}

	}
	/**
	 * Set event table data using {@link TableView#setItems(javafx.collections.ObservableList)}
	 */
	private void setEventData() {
		tblEventTable.setItems(FXCollections.observableArrayList(eventData));
		colStartDate.setSortType(TableColumn.SortType.ASCENDING);
		tblEventTable.getSortOrder().add(colStartDate);
		tblEventTable.sort();
	}
	/**
	 * Request the update parameters table from Server.<br>
	 * Shows the update table and hide the event table.<br>
	 * Department Manager can view all park manager update parameters requests. <br>
	 * {@link #setEventData()} to insert to the update table the data.<br>
	 * 
	 */
	private void showUpdateTable() {
		tblUpdateTable.setVisible(true);
		tblUpdateTable.setManaged(true);
		tblEventTable.setVisible(false);
		tblEventTable.setManaged(false);
		MainClient.clientConsole.accept( //Send Message to server to get the update table from data base.
				new Message(OperationType.GetUpdateTable, DBControllerType.RequestsDBController, (Object) null));
		if (RequestsController.requestType.equals(OperationType.UpdateTableArrived)) {
			if (updateData.size() != 0)
				setUpdateData();
			else {
				tblUpdateTable.setVisible(false);
				tblUpdateTable.setManaged(false);
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("No new update requestes");
				a.setContentText("There are no update requests");
				a.setTitle("Update requests");
				a.showAndWait();
			}
		}

	}
	/**
	 * Set update table data using {@link TableView#setItems(javafx.collections.ObservableList)}
	 */
	private void setUpdateData() {
		tblUpdateTable.setItems(FXCollections.observableArrayList(updateData));
	}

	/**
	 *Set the event status of each row the the relevant color.
	 *using {@link TableColumn#setCellFactory(Callback)} to set status color.
	 */
	private void setEventStatusColor() {
		colEventStatus.setCellFactory(colStatus -> {
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

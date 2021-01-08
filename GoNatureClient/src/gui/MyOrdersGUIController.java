package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.MainClient;
import common.Message;
import controllers.RestartApp;
import controllers.VisitorController;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Order;

/**
 * Controller class which shows to user his own future orders and their status
 * @author dorswisa
 *
 */
public class MyOrdersGUIController implements Initializable {
	public static List<Order> myOrders = null;
	public static String msgFromServer = null;
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
	private TableColumn<Order, Integer> colOrderNumber;

	@FXML
	private TableColumn<Order, String> colParkName;

	@FXML
	private TableColumn<Order, Date> colDate;

	@FXML
	private TableColumn<Order, String> colHour;

	@FXML
	private TableColumn<Order, Integer> colVisitorsAmount;

	@FXML
	private TableColumn<Order, String> colStatus;

	@FXML
	private TableColumn<Order, Void> colButtons;

	@FXML
	private TableView<Order> tblOrdersTable;

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
	void showAddOrder(MouseEvent event) {
		AddOrderGUIController c = new AddOrderGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		c.show();

	}

	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mp = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mp.show();

	}
	/**
	 * Show MyOrder GUI
	 */
	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MyOrderGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature My Orders");
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
			MyOrdersGUIController myOrdersController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(myOrdersController);
			MenuBarSelection.setMenuOptions(menuLabels);

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private List<Label> createLabelList(MyOrdersGUIController myOrdersController) {
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
		return tempMenuLabels;
	}
	/**
	 * initialize my Orders table
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		colParkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colHour.setCellValueFactory(new PropertyValueFactory<>("strHour"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		colVisitorsAmount.setCellValueFactory(new PropertyValueFactory<>("numOfVisitors"));

		colOrderNumber.setStyle("-fx-alignment: CENTER");
		colParkName.setStyle("-fx-alignment: CENTER");
		colDate.setStyle("-fx-alignment: CENTER");
		colHour.setStyle("-fx-alignment: CENTER");
		colStatus.setStyle("-fx-alignment: CENTER");
		colVisitorsAmount.setStyle("-fx-alignment: CENTER");
		showMyOrderTable();

	}
	/**
	 * adding to each row 2 buttons according to the status of the order and the message status from the server 
	 */
	private void addRelevantButtons() {
		Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
			@Override
			public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
				final TableCell<Order, Void> cell = new TableCell<Order, Void>() {

					private final Button btnV = new Button("Approve");
					private final Button btnX = new Button("Decline");
					{	//approve button function, when pressed send to server message to approve the order
						btnV.setOnAction((ActionEvent event) -> {
							Order tmp = getTableView().getItems().get(getIndex());

							if(tmp.getStatus().equals("Received") && tmp.getMsgStatus().equals("Sent")) {
								MainClient.clientConsole.accept(new Message(OperationType.OrderFinalApproval,DBControllerType.OrderDBController,(Object)tmp));
								if(msgFromServer.equals("The final approval was successful")) { //return answer from server which indicates the order was approved, pop-up to user to inform him.
									Alert a = new Alert(AlertType.INFORMATION);
									a.setHeaderText("Order #"+tmp.getOrderID()+" was Approved");
									a.setContentText("We'll be happy to see you on " + tmp.getDate());
									a.setTitle("Order cancel");
									a.showAndWait();
									showMyOrderTable();
								}
							}
							if(tmp.getStatus().equals("Waiting list") && tmp.getMsgStatus().equals("Sent")) {
								MainClient.clientConsole.accept(new Message(OperationType.GetOutFromWaitingList,DBControllerType.OrderDBController,(Object)tmp));
								if(msgFromServer.equals("You are not in waiting list")) {
									Alert a = new Alert(AlertType.INFORMATION);
									a.setHeaderText("Order #"+tmp.getOrderID()+" is not in waiting list anymore");
									a.setContentText("You will get a message day before "+ tmp.getDate());
									a.setTitle("Order cancel");
									a.showAndWait();
									showMyOrderTable();
								}
							}
						});
						//cancel button function , when pressed send to server message to cancel the order.
						btnX.setOnAction((ActionEvent event) -> {
							Order tmp = getTableView().getItems().get(getIndex());
							MainClient.clientConsole.accept(new Message(OperationType.CancelOrder,DBControllerType.OrderDBController,(Object)tmp));
							if(msgFromServer.equals("Cancel Success")) {//returned answer from server which indicates the order was canceled, pop-up to user to inform him.
								Alert a = new Alert(AlertType.INFORMATION);
								a.setHeaderText("Order #"+tmp.getOrderID()+" was canceled");
								a.setContentText("You can not change the cancellation");
								a.setTitle("Order cancel");
								a.showAndWait();
								showMyOrderTable();
							}
						});

					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							Order tmp = getTableView().getItems().get(getIndex());
							HBox hBox1;
							HBox hBox2 = new HBox(btnX);
							//add the relevant buttons to each row according the status and the msgStatus.
							if ((tmp.getStatus().equals("Approved") && tmp.getMsgStatus().equals("Sent"))
									|| (tmp.getStatus().equals("Canceled") && tmp.getMsgStatus().equals("Sent")) || (tmp.getStatus().equals("Canceled") && tmp.getMsgStatus().equals("Not sent"))) {
								return;
							} else if ((tmp.getStatus().equals("Received") && tmp.getMsgStatus().equals("Sent"))
									|| (tmp.getStatus().equals("Waiting list") && tmp.getMsgStatus().equals("Sent"))) {
								hBox1 = new HBox(5,btnV, btnX);
								setGraphic(hBox1);
								hBox1.setAlignment(Pos.BASELINE_RIGHT);
								return;
							} else if ((tmp.getStatus().equals("Received") && tmp.getMsgStatus().equals("Not sent")) || (tmp.getStatus().equals("Waiting list") && tmp.getMsgStatus().equals("Not sent"))) {
								setGraphic(hBox2);
								hBox2.setAlignment(Pos.BASELINE_RIGHT);
								return;

							}

						}
					}

				};

				return cell;
			}
		};
		colButtons.setCellFactory(cellFactory);
	}
	/*
	 * set the status of each row the the relevant color 
	 */
	private void setStatusColor() {
		colStatus.setCellFactory(colStatus -> {
			return new TableCell<Order, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty); // This is mandatory

					if (item == null || empty) { // If the cell is empty
						setText(null);
						setStyle("");
					} else {
						setText(item); // Put the String data in the cell
						Order tmp = getTableView().getItems().get(getIndex());

						if (tmp.getStatus().equals("Received") && tmp.getMsgStatus().equals("Sent")) {
							setTextFill(Color.BLACK);
						}
						if (tmp.getStatus().equals("Received") && tmp.getMsgStatus().equals("Not sent")) {
							setTextFill(Color.BLACK);
						}
						if (tmp.getStatus().equals("Approved"))
							setTextFill(Color.GREEN);
						if (tmp.getStatus().equals("Canceled"))
							setTextFill(Color.RED);
						if (tmp.getStatus().equals("Waiting list"))
							setTextFill(Color.ORANGE);
					}
				}

			};
		});

	}
	/**
	 * Show to the user his own future orders
	 */
	private void showMyOrderTable() {
		int id = VisitorController.loggedID;
		if (VisitorController.subscriberConnected == null) {
			
			MainClient.clientConsole
					.accept(new Message(OperationType.getMyOrders, DBControllerType.OrderDBController, (Object) id));
		} else {
			id = VisitorController.subscriberConnected.getVisitorID();
			MainClient.clientConsole.accept(
					new Message(OperationType.getMyOrders, DBControllerType.OrderDBController, (Object) id));
		}
		if (myOrders != null) {
			tblOrdersTable.setItems(FXCollections.observableArrayList(myOrders));
			colDate.setSortType(TableColumn.SortType.ASCENDING);
			tblOrdersTable.getSortOrder().add(colDate);
			tblOrdersTable.sort();
			setStatusColor();
			addRelevantButtons();
		}
		else{
			tblOrdersTable.setDisable(false);
		}
		
		
	}

}

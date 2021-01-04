package reader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.OrderController;
import controllers.ParkController;
import controllers.ReceiptController;
import enums.DBControllerType;
import enums.Discount;
import enums.OperationType;
import gui.MenuBarSelection;
import gui.ParkEntranceGUIController;
import gui.WelcomeGUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CardReaderController {

	private static CardReaderController cardReaderController = null;

	@FXML
	private ComboBox<String> cmbParkName;

	@FXML
	private TextField txtEnterID;

	@FXML
	private TextField txtEnterAmount;

	@FXML
	private Button btnSendEnter;

	@FXML
	private Label lblEnterAnswer;

	@FXML
	private TextField txtExitID;

	@FXML
	private TextField txtExitAmount;

	@FXML
	private Button btnSendExit;

	@FXML
	private Label lblExitAnswer;

	private static String parkName = null;
	private static String type = null;
	private static int cost = 0;
	private static int numOfVisitors;
	int amount = 0;
	private int orderID = 0;
	List<String> list = new ArrayList<String>();
	private static LocalDate thisDay;
	private static Date thisDayToDB;
	private static LocalTime thisTime;
	private static Time thisTimeToDB;
	private static int hours;
	private static int minutes;
	private final int openEntrance = 10;
	private final int closeEntrance = 17;
	private final int openExit = 10;
	private final int closeExit = 21;
	

	
	/**
	 * This method is activated when you click send, 
	 * it allows the visitor to enter if possible in terms of the number of people in the park,
	 * if he has already received a receipt, or if he has an order (then entered into a receipt system),
	 * or if he is an occasional visitor (then entered into a receipt system).
	 * And finally updates the current amount of visitors to the park.
	 * 
	 * @param event
	 */
	@FXML
	void sendEnterInfo(ActionEvent event) {
		list.removeAll(list);
		amount = Integer.parseInt(txtEnterAmount.getText());
		System.out.println(amount);
		lblEnterAnswer.setText("");
		lblExitAnswer.setText("");

		if (parkName != null) {
			if(checkHoursForEntry()) {
			if (getParkInfo()) {
				checkReceiptInfo();
			}
		}
		}

		else {
			lblEnterAnswer.setText("Choose a park");

		}
	}
	/**
	 * This method searches the visitor's data within the table of receipts in DB.
	 */

	private void checkReceiptInfo() {
		list.add(cmbParkName.getValue());
		list.add(txtEnterID.getText());
		list.add(txtEnterAmount.getText());
		System.out.println("checkReceiptInfo");
		MainReader.clientConsole
				.accept(new Message(OperationType.FindReceipt, DBControllerType.ReceiptDBController, (Object) list));
		if (ReceiptController.receiptType.equals(OperationType.FailedUpdateReceipt)) {
			System.out.println("checkReceiptInfo1");
			System.out.println("the amount of visitors is not ok");
			lblEnterAnswer.setText("can not enter the park");
		} else if (ReceiptController.receiptType.equals(OperationType.SuccessUpdateReceipt)) {
			System.out.println("its ok you can enter");
			lblEnterAnswer.setText("can enter the park");
		} else if (ReceiptController.receiptType.equals(OperationType.NeverExist)) {
			System.out.println(1);
			checkOrderInfo();
			if (OrderController.orderType.equals(OperationType.NeverExist)
					|| OrderController.orderType.equals(OperationType.FindOrder)) {
				System.out.println("qqqqq");
				list.removeAll(list);
				list.add(cmbParkName.getValue());
				list.add(String.valueOf(numOfVisitors));
				list.add(txtEnterID.getText());
				list.add(type);
				list.add(String.valueOf(orderID));
				list.add(txtEnterAmount.getText());
				if (OrderController.orderExist) {
					System.out.println("pppppppp");
					list.add(String.valueOf(OrderController.recivedOrder.getHourTime()));
				}
				list.add(String.valueOf(cost));
				MainReader.clientConsole.accept(new Message(OperationType.GenerateReceipt,
						DBControllerType.ReceiptDBController, (Object) list));
				if (ReceiptController.receiptType.equals(OperationType.CheckReceiptInfo)) {
					System.out.println("i have receipt");
					list.removeAll(list);
					list.add(cmbParkName.getValue());
					list.add(txtEnterAmount.getText());
					MainReader.clientConsole.accept(new Message(OperationType.IncreaseParkVistiors,
							DBControllerType.ParkDBController, (Object) list));
					lblEnterAnswer.setText("can enter the park");
				}

			}
		}
	}

	/**
	 * This method searches for the visitor's data within the order table in DB.
	 */
	private void checkOrderInfo() {
		MainReader.clientConsole
				.accept(new Message(OperationType.FindOrder, DBControllerType.OrderDBController, (Object) list));
		if (OrderController.orderExist) {
			System.out.println(2);
			orderID = OrderController.recivedOrder.getOrderID();
			getTypeOfOrderVisitor();
			numOfVisitors = OrderController.recivedOrder.getNumOfVisitors();
		} else {
			orderID = 0;
			if (OrderController.orderType.equals(OperationType.NeverExist)) {
				System.out.println(3);
				numOfVisitors = Integer.parseInt(txtEnterAmount.getText());
				getTypeOfVisitor();
			} else {
				System.out.println("the amount of visitors is not ok");
				lblEnterAnswer.setText("can not enter the park");
			}

		}

	}

	/**
	 * This method checks the type of visitor
	 */
	private void getTypeOfVisitor() {
		String id = txtEnterID.getText();
		System.out.println(id);
		MainReader.clientConsole
				.accept(new Message(OperationType.TravelerInfo, DBControllerType.ParkDBController, (Object) id));

		if (ParkController.disType.equals(Discount.MemberDiscount)) {
			System.out.println(4);
			type = "member";
			cost = (int) (Integer.parseInt(this.txtEnterAmount.getText()) * OrderController.getTicketPrice() * 0.8);

		}

		else if (ParkController.disType.equals(Discount.VisitorDiscount)) {
			System.out.println(5);
			type = "visitor";
			cost = (int) (Integer.parseInt(this.txtEnterAmount.getText()) * OrderController.getTicketPrice());
		}

		else if (ParkController.disType.equals(Discount.GroupDiscount)) {
			System.out.println(6);
			type = "instructor";
			cost = (int) (Integer.parseInt(this.txtEnterAmount.getText()) * OrderController.getTicketPrice() * 0.9);
		}

	}

	/**
	 * This method searches for information about the park, 
	 * and updates the current amount of visitors to it as of this moment.
	 * 
	 * @return
	 */
	private boolean getParkInfo() {
		MainReader.clientConsole
				.accept(new Message(OperationType.GetParkInfo, DBControllerType.ParkDBController, (Object) parkName));
		MainReader.clientConsole.accept(new Message(OperationType.UpdateCurrAmountOfVisitors,
				DBControllerType.ParkDBController, (Object) ParkController.parkConnected.getParkName()));
		if (!(ParkController.parkConnected.getCurrentAmountOfVisitors() + amount <= ParkController.parkConnected
				.getParkCapacity()) || amount <= 0) {
			System.out.println("cant enter to the park");
			lblEnterAnswer.setText("can not enter the park");
			return false;

		}
		return true;
	}

	/**
	 * This method searches for information about the type of visitor who created an order
	 */
	private void getTypeOfOrderVisitor() {
		if (OrderController.recivedOrder.getType().equals("Single/Family")) {
			MainReader.clientConsole.accept(new Message(OperationType.TravelerInfo, DBControllerType.ParkDBController,
					(Object) String.valueOf(OrderController.recivedOrder.getVisitorID())));
			if (ParkController.disType.equals(Discount.GroupDiscount)
					|| ParkController.disType.equals(Discount.MemberDiscount)) {
				type = "member";

			}

			else {
				type = "visitor";
			}

		} else {
			System.out.println("kkkk");
			type = "instructor";
		}
		cost = OrderController.recivedOrder.getCost();

	}

	/**
	 * This method is activated when you click send,
	 * it allows you to take the visitor out if he has a receipt, 
	 * and update the departure time if all the visitors who came with him left. 
	 * And finally updates the current amount of visitors to the park.
	 * 
	 * @param event
	 */
	@FXML
	void sendExitInfo(ActionEvent event) {

		list.removeAll(list);
		amount = Integer.parseInt(txtExitAmount.getText());
		System.out.println(amount);
		lblEnterAnswer.setText("");
		lblExitAnswer.setText("");

		if (parkName != null) {
			if (amount > 0) {
				if(checkHoursForExit()) {
				list.add(parkName);
				list.add(txtExitID.getText());
				list.add(txtExitAmount.getText());
				MainReader.clientConsole.accept(new Message(OperationType.FindReceiptForExit,
						DBControllerType.ReceiptDBController, (Object) list));
				if (ReceiptController.receiptType.equals(OperationType.FailedUpdateReceipt)
						|| ReceiptController.receiptType.equals(OperationType.NeverExist)) {
					lblExitAnswer.setText("can not exit the park");
				} else {
					list.removeAll(list);
					list.add(parkName);
					list.add(txtExitAmount.getText());
					MainReader.clientConsole.accept(new Message(OperationType.DecreaseParkVistiors,
							DBControllerType.ParkDBController, (Object) list));
					if (ParkController.Parktype.equals(OperationType.UpdateParkInfo)) {
						lblExitAnswer.setText("can exit the park");

					}
				}
			} 
			}
			else {
				lblExitAnswer.setText("can not exit the park");
			}

		} else {
			lblExitAnswer.setText("Choose a park");

		}

	}

	/**
	 * This method activates the card reader page.
	 */
	public void show() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("CardReaderGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Card Reader System");
			cardReaderController = loader.getController();
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	/**
	 * This method initializes the combo box of the park name with the correct data.
	 */
	@FXML
	public void initialize() {
		cmbParkName.getItems().removeAll(cmbParkName.getItems());
		cmbParkName.getItems().addAll("Luna-Park", "Shipment-Park", "Tempo-Park");
		cmbParkName.setOnAction(e -> chooseParkName());
	}

	/**
	 * When choosing the particular park in the combo box, this method works and saves the name of the park.
	 */
	private void chooseParkName() {
		parkName = cmbParkName.getValue();
		System.out.println(parkName);
	}
	
	/**
	 * This method checks whether it is possible to enter the park now
	 * @return
	 */
	private boolean checkHoursForEntry() {
		getCurrentTime();

		if(hours>closeEntrance ||hours<openEntrance) {
			lblEnterAnswer.setText("The entrance hours are over!");
			return false;
		}
		return true;
	}
	
	/**
	 * This method checks whether it is possible to leave the park now
	 * @return
	 */
	private boolean checkHoursForExit() {
		getCurrentTime();

		if(hours>closeExit ||hours<openExit) {
			lblExitAnswer.setText("The exit hours are over!");
			return false;
		}
		return true;
	}
	
	
	/**
	 * this function taking the date and time of now
	 */
	private static void getCurrentTime() {

		thisDay = LocalDate.now();
		thisDayToDB = Date.valueOf(thisDay);
		thisTime = LocalTime.now();
		thisTimeToDB = Time.valueOf(thisTime);
		hours = thisTime.getHour();
		minutes = thisTime.getMinute();
		if (minutes > 0) {
			hours += 1;
	
		}
	}

}

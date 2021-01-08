package reader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.CardReaderRequest;

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
	}

	@FXML
	void sendEnterInfo(ActionEvent event) {

		CardReaderRequest cardReaderRequest= new CardReaderRequest(cmbParkName.getValue()
				, Integer.parseInt(txtEnterID.getText()), Integer.parseInt(txtEnterAmount.getText()));
		MainReader.clientConsole.accept(new Message(OperationType.VisitorEnterRequest,
				DBControllerType.ParkDBController,(Object)cardReaderRequest));
	}

	@FXML
	void sendExitInfo(ActionEvent event) {

	}

}

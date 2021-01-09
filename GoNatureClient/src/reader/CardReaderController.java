package reader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.CardReaderRequest;
import logic.Validation;
import java.io.IOException;
import client.ClientController;
import client.MainClient;
import common.Message;
import enums.DBControllerType;
import enums.OperationType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


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


	/**
	 * This method is activated when you click send, 
	 * it allows the visitor to enter the park: 
	 * if he has already received a receipt, or if he has an order (then entered into a receipt system),
	 * 
	 * And finally updates the current amount of visitors to the park.
	 * 
	 * @param  mouse click event
	 */
	@FXML
	void sendEnterInfo(ActionEvent event) {
		if(validationCardReaderEnter()) {
			return;
		}

		CardReaderRequest cardReaderRequest = new CardReaderRequest(cmbParkName.getValue(),
				Integer.parseInt(txtEnterID.getText()), Integer.parseInt(txtEnterAmount.getText()));
		MainClient.clientConsole.accept(new Message(OperationType.VisitorEnterRequest,
				DBControllerType.ParkDBController, (Object) cardReaderRequest));
		answerFromServer();

	}

	/**
	 * this method bounces the appropriate pop-up for the income or expense of the visitors.
	 */
	public void answerFromServer() {
		Alert a = new Alert(AlertType.INFORMATION);
		String msg = ClientController.cardReaderAnswer;
		System.out.println(msg);
		if(msg.equals("You can not enter the park")) {
			a.setTitle("Sorry!");
			a.setHeaderText(msg);
	
		}
		else if(msg.equals("The receipt is updated, the visitors can enter the park")) {
			a.setTitle("Enjoy!");
			a.setHeaderText(msg);
			
		}
		else if(msg.equals("The entrance hours are over! You can enter the park from 10:00 to 17:00")) {
			a.setTitle("Sorry!");
			a.setHeaderText(msg);
		}
		else if(msg.equals("The visitors can exit the park")){
			a.setTitle("Hope you had fun!");
			a.setHeaderText(msg);
			
		}
		else if (msg.equals("The amount of visitors that want to exit is not match")){
			a.setTitle("Error!");
			a.setHeaderText(msg);
			
		}
		else if(msg.equals("The exit hours are over! The help is on the way.")) {
			a.setTitle("Error!");
			a.setHeaderText(msg);
			
		}
		else {

		a.setTitle("Receipt");
		a.setHeaderText("The order number is valid! you may enter the park.");
		a.setContentText(ClientController.cardReaderAnswer);
		}
		

		
		a.show();
	}

	/**
	 * This method is activated when you click send,
	 * it allows you to take the visitor out if he has a receipt, 
	 * and update the departure time if all the visitors who came with him left. 
	 * And finally updates the current amount of visitors to the park.
	 * 
	 * @param mouse click event
	 */
	@FXML
	void sendExitInfo(ActionEvent event) {
		if(validationCardReaderExit()) {
			return;
		}
		CardReaderRequest cardReaderRequest = new CardReaderRequest(cmbParkName.getValue(),
				Integer.parseInt(txtExitID.getText()), Integer.parseInt(txtExitAmount.getText()));
		MainClient.clientConsole.accept(new Message(OperationType.VisitorExitRequest,
				DBControllerType.ParkDBController, (Object) cardReaderRequest));
		answerFromServer();


	}

	/**
	 * method for validation the information of park name, visitor id and amount of visitors to enter the park
	 * @return true if information is missing.
	 */
	private boolean validationCardReaderEnter() {
		if(Validation.isNull(cmbParkName.getValue()) || ! Validation.onlyDigitsValidation(txtEnterID.getText()) || ! Validation.onlyDigitsValidation(txtEnterAmount.getText())) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * method for validation the information of park name, visitor id and amount of visitors to exit the park
	 * @return true if information is missing.
	 */
	private boolean validationCardReaderExit() {
		if(Validation.isNull(cmbParkName.getValue()) ||! Validation.onlyDigitsValidation(txtExitID.getText()) ||! Validation.onlyDigitsValidation(txtExitAmount.getText())) {
			return true;
		}
		return false;
		
	}

}

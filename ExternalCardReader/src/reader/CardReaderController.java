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
import java.util.ArrayList;
import java.util.List;

import gui.MenuBarSelection;
import gui.WelcomeGUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CardReaderController {

	private static CardReaderController cardReaderController = null;
	@FXML
	private TextField txtEnterReceiptID;

	@FXML
	private TextField txtEnterAmount;

	@FXML
	private Button btnSendEnter;

	@FXML
	private TextField txtExitReceiptID;

	@FXML
	private TextField txtExitAmount;

	@FXML
	private Button btnSendExit;


    @FXML
    private Label lblExitAnswer;


    @FXML
    private Label lblEnterAnswer;


    @FXML
    private ComboBox<String> cmbParkName;

	
	@FXML
	void sendEnterInfo(ActionEvent event) {

		System.out.println("TRU");
	}

	@FXML
	void sendExitInfo(ActionEvent event) {

	}

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
	
	@FXML
	public void initialize() {
		cmbParkName.getItems().removeAll(cmbParkName.getItems());
		cmbParkName.getItems().addAll("Luna-Park", "Shipment-Park", "Tempo-Park");
	}

}

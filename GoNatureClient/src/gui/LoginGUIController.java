package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginGUIController {

	@FXML
	private Label lblLoginErrorMsg;

	@FXML
	private RadioButton rdTraveler;

	@FXML
	private ToggleGroup LoginType;

	@FXML
	private RadioButton rdEmployee;

	@FXML
	private Label lblEnterID;

	@FXML
	private TextField txtLoginID;

	@FXML
	private Label lblPassword;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Button btnLogin;

	@FXML
	public void hideEmployeeParamaters() {
		lblPassword.setManaged(false);
		txtPassword.setManaged(false);
		lblPassword.setVisible(false);
		txtPassword.setVisible(false);
		lblLoginErrorMsg.setVisible(false);
		lblEnterID.setText("Enter ID / Member Number");

	}

	@FXML
	public void showEmployeeParameters() {
		lblPassword.setManaged(true);
		txtPassword.setManaged(true);
		lblPassword.setVisible(true);
		txtPassword.setVisible(true);
		lblLoginErrorMsg.setText(null);
		lblEnterID.setText("User Name");
	}

	@FXML
	void LoginAction(ActionEvent event) {
		lblLoginErrorMsg.setVisible(false);
		List<String> info = new ArrayList<String>();
		info.add(txtLoginID.getText());
		if (rdEmployee.isSelected()) {
			info.add(txtPassword.getText());
			System.out.println("passwords=" + txtPassword.getText());
			System.out.println(info);
			MainClient.clientConsole.accept(
					new Message(OperationType.EmployeeLogin, DBControllerType.LoginDBController, (Object) info));
			if (!EmployeeController.isConnected) {
				if (EmployeeController.employeeConected == null) {
					System.out.println("VALUE");
					lblLoginErrorMsg.setVisible(true);
					lblLoginErrorMsg.setText("incorrect userName or password");
					lblLoginErrorMsg.setTextFill(Color.RED);
					lblLoginErrorMsg.setStyle("-fx-background-color: pink;");
				} else {
					WelcomeGUIController w = new WelcomeGUIController();
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding login window
					w.show();
				}
			} else {

				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("You are already logged in !");
				a.setContentText("Please log-out from the application to enter again.");
				a.setTitle("Login Error");
				a.show();
				RestartApp.restartParameters();// Restart all the static parameters in out system
			}

		}

		else {
			MainClient.clientConsole
					.accept(new Message(OperationType.VisitorLogin, DBControllerType.LoginDBController, (Object) info));
			if (!VisitorController.isConnected) {
				VisitorController.loggedID = Integer.parseInt(info.get(0));
				WelcomeGUIController welcome = new WelcomeGUIController();
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding login window
				welcome.show();
			} else {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("You are already logged in !");
				a.setContentText("Please log-out from the application to enter again.");
				a.setTitle("Login Error");
				a.show();
				RestartApp.restartParameters();// Restart all the static parameters in out system
			}
		}

	}

	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("LoginGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Login");
			LoginGUIController loginController = loader.getController();
			loginController.hideEmployeeParamaters();
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}

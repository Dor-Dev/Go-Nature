package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Validation;

public class LoginGUIController {

	@FXML
	private Label lblLoginErrorMsg;

	@FXML
	private RadioButton rdTraveler;

	@FXML
	private ToggleGroup LoginType;

	@FXML
	private TextField txtUserName;

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
		txtUserName.setManaged(false);
		txtUserName.setVisible(false);
		txtLoginID.setManaged(true);
		txtLoginID.setVisible(true);
		lblPassword.setManaged(false);
		txtPassword.setManaged(false);
		lblPassword.setVisible(false);
		txtPassword.setVisible(false);
		lblLoginErrorMsg.setManaged(false);
		lblLoginErrorMsg.setVisible(false);
		lblEnterID.setText("Enter ID / Member Number");

	}

	@FXML
	public void showEmployeeParameters() {
		txtLoginID.setManaged(false);
		txtLoginID.setVisible(false);
		txtUserName.setManaged(true);
		txtUserName.setVisible(true);
		lblPassword.setManaged(true);
		txtPassword.setManaged(true);
		lblPassword.setVisible(true);
		txtPassword.setVisible(true);
		lblLoginErrorMsg.setText(null);
		lblEnterID.setText("User Name");
	}

	@FXML
	void LoginAction(ActionEvent event) {
		Alert a = new Alert(AlertType.INFORMATION);
		lblLoginErrorMsg.setVisible(false);
		List<String> info = new ArrayList<String>();
		if (rdEmployee.isSelected()) {
			info.add(txtUserName.getText());
			info.add(txtPassword.getText());
			System.out.println("passwords=" + txtPassword.getText());
			System.out.println(info);
			MainClient.clientConsole.accept(
					new Message(OperationType.EmployeeLogin, DBControllerType.LoginDBController, (Object) info));
			if (!EmployeeController.isConnected) {
				if (EmployeeController.employeeConected == null) {
					System.out.println("VALUE");
					lblLoginErrorMsg.setManaged(true);
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

				
				a.setHeaderText("You are already logged in !");
				a.setContentText("Please log-out from the application to enter again.");
				a.setTitle("Login Error");
				a.show();
				RestartApp.restartParameters();// Restart all the static parameters in out system
			}

		}

		else {
			if (!txtLoginID.getStyleClass().contains("success")) {
				lblLoginErrorMsg.setManaged(true);
				lblLoginErrorMsg.setVisible(true);
				lblLoginErrorMsg.setText("You need to put member number(10 digits) or id number(9 digits)!");
				lblLoginErrorMsg.setTextFill(Color.RED);
				lblLoginErrorMsg.setStyle("-fx-background-color: pink;");
				return;
			}
			info.add(txtLoginID.getText());
			MainClient.clientConsole
					.accept(new Message(OperationType.VisitorLogin, DBControllerType.LoginDBController, (Object) info));
			if (!VisitorController.memberNotExist) {
				if (!VisitorController.isConnected) {
					VisitorController.loggedID = Integer.parseInt(info.get(0));
					WelcomeGUIController welcome = new WelcomeGUIController();
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding login window
					welcome.show();
				} else {
					
					a.setHeaderText("You are already logged in !");
					a.setContentText("Please log-out from the application to enter again.");
					a.setTitle("Login Error");
					a.show();
					RestartApp.restartParameters();// Restart all the static parameters in out system
				}
			}
			else {
				
				a.setHeaderText("The member number not exist!");
				a.setContentText("You entered 10 numbers, the member number doesn't exist");
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
			loginController.initValidator();
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public void initValidator() {
		txtLoginID.textProperty().addListener((obs, oldVal, newVal) -> {
			if (Validation.loginValidation(newVal)) {
				txtLoginID.getStyleClass().clear();
				txtLoginID.getStyleClass().addAll("text-field", "text-input");
				txtLoginID.getStyleClass().add("success");
				lblLoginErrorMsg.setManaged(false);
				lblLoginErrorMsg.setVisible(false);

			} else {
				txtLoginID.getStyleClass().clear();
				txtLoginID.getStyleClass().addAll("text-field", "text-input");
				txtLoginID.getStyleClass().add("error");
				lblLoginErrorMsg.setManaged(true);
				lblLoginErrorMsg.setVisible(true);
				lblLoginErrorMsg.setText("You need to put member number(10 digits) or id number(9 digits)!");
				lblLoginErrorMsg.setTextFill(Color.RED);
				lblLoginErrorMsg.setStyle("-fx-background-color: pink;");
			}
		});

	}

}

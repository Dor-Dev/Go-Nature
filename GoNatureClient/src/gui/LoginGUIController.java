package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientConsole;
import client.ClientController;
import client.MainClient;
import common.DBControllerType;
import common.Message;
import common.OperationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    public void hideEmployeeParamaters()
    {
    	lblPassword.setManaged(false);
    	txtPassword.setManaged(false);
    	lblPassword.setVisible(false);
    	txtPassword.setVisible(false);
    	lblLoginErrorMsg.setVisible(false);
    	lblEnterID.setText("Enter ID / Member Number");
  
    }
    @FXML
    public void showEmployeeParameters()
    {
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
    		if(rdEmployee.isSelected()) {
				info.add(txtPassword.getText());
				MainClient.clientConsole.accept(new Message(OperationType.EmployeeLogin,DBControllerType.loginDBController,(Object)info));
				if(ClientController.nullEmployee) {
					lblLoginErrorMsg.setVisible(true);
					lblLoginErrorMsg.setText("incorrect userName or password");
					lblLoginErrorMsg.setTextFill(Color.RED);
					lblLoginErrorMsg.setStyle("-fx-background-color: pink;");
    		
					}
				else {
					WelcomeGUIController w = new WelcomeGUIController();
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding login window
					w.show();
				}
				
    		}
    		// TODO need to finish Traveler connection, check if have orders to show
    		else {
    		MainClient.clientConsole.accept(new Message(OperationType.TravelerLogin,DBControllerType.loginDBController,(Object)info));
    		WelcomeGUIController welcome = new WelcomeGUIController();
    		welcome.show();
    		}
    		
    }
    
    public void show() 
    {
    	VBox root;
    	Stage primaryStage = new Stage();
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

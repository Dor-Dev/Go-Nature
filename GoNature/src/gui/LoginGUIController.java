package gui;

import java.util.ArrayList;
import java.util.List;

import client.ClientConsole;
import client.MainClient;
import common.DBControllerType;
import common.Message;
import common.OperationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;


public class LoginGUIController {

	@FXML
    private RadioButton rdEmployee;

    @FXML
    private RadioButton rdTraveler;

    @FXML
    private ToggleGroup LoginType;

    @FXML
    private Label lblEnterID;

    @FXML
    private TextField txtLoginID;

    @FXML
    private Label lblPassword;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    public void hideEmployeeParamaters()
    {
    	lblPassword.setVisible(false);
    	txtPassword.setVisible(false);
    	lblEnterID.setText("Enter ID / Member Number");
  
    }
    @FXML
    public void showEmployeeParameters()
    {
    	lblPassword.setVisible(true);
    	txtPassword.setVisible(true);
    	lblEnterID.setText("User Name");
    }
    @FXML
    void LoginAction(ActionEvent event) {
    		List<String> info = new ArrayList<String>();
    		info.add(txtLoginID.getText());
    		if(rdEmployee.isSelected());
				info.add(txtPassword.getText());
    		MainClient.clientConsole.accept(new Message(OperationType.TravelerLogin,DBControllerType.loginDBController,(Object)info));
    }
}


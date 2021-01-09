package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ServerGUIController {

	@FXML
	private VBox serverStatus;

	@FXML
	private Label lblConnection;

	@FXML
	private Label lblIpAddress;

	@FXML
	private Label lblHostName;

	@FXML
	private Button btnClose;

	@FXML
	void closeServerWindow(ActionEvent event) {		
	    Stage stage = (Stage) btnClose.getScene().getWindow();
	    stage.close();
	}

	public void setClientStatus(String ip, String host, String status) {
		Platform.runLater(()->{
			lblConnection.setText(status);
			lblHostName.setText(host);
			lblIpAddress.setText(ip);
			if(status.equals("not connected"))
				lblConnection.setTextFill(Color.RED);
			else
				lblConnection.setTextFill(Color.GREEN);
		});	 
	} 

}

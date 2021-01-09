package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This is controller for the Server GUI
 * @author Naor0
 *
 */
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

	/**
	 * This method close the server connection by exit the stage
	 * @param event
	 */
	@FXML
	void closeServerWindow(ActionEvent event) {		
	    Stage stage = (Stage) btnClose.getScene().getWindow();
	    stage.close();
	}

	/**
	 * this method set details of the connection
	 * @param ip
	 * @param host
	 * @param status
	 */
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

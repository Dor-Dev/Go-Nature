package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author dana_
 * A class that is responsible for the pop up of the registration GUI
 */

public class RegistartionPopUpController {
    @FXML
    private Label lblFirstMsg;

    @FXML
    private Label lblSecondMsg;

    @FXML
    private Button btnOK;
	
    void showRegistrationPopUp(String popUpMsg, String memberNumber, String popUpTitle) {
    	VBox root;
    	Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("RegistrationPopUp.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
			primaryStage.setTitle(popUpTitle);
			System.out.println(popUpMsg);
			RegistartionPopUpController registrationPopUpController=loader.getController();
			if(popUpMsg.equals("The member registered successfuly!")) {
				registrationPopUpController.lblSecondMsg.setManaged(true);
				registrationPopUpController.lblSecondMsg.setVisible(true);
				registrationPopUpController.lblFirstMsg.setText(popUpMsg);
				registrationPopUpController.lblSecondMsg.setText("The member number is :"+ memberNumber);
			}else {
				registrationPopUpController.lblSecondMsg.setManaged(false);
				registrationPopUpController.lblSecondMsg.setVisible(false);	
				registrationPopUpController.lblFirstMsg.setText(popUpMsg);
			}
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

    }
    
    @FXML
    void clickOKButton(MouseEvent event) {
	    Stage stage = (Stage) btnOK.getScene().getWindow();
	    stage.close();
    }
    

}

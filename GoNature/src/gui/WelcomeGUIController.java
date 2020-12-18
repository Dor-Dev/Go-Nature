package gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeGUIController {
	
	
	 public void show() 
	    {
	    	VBox root;
	    	Stage primaryStage = new Stage();
			try {
				FXMLLoader loader = new FXMLLoader();
				root = loader.load(getClass().getResource("/gui/Welcome.fxml").openStream());
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Go-Nature Login");
				WelcomeGUIController welcomeController = loader.getController();
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	    }

}

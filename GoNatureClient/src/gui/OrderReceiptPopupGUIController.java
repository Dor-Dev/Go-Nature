package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OrderReceiptPopupGUIController {
	
	 public void show() 
	    {
	    	VBox root;
	    	Stage primaryStage = new Stage();
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("OrderReceiptPopupGUI.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Order Receipt");
				OrderReceiptPopupGUIController OrderReceiptController = loader.getController();	
				//List<Label> menuLabels = new ArrayList<>();
				//menuLabels = createLabelList(parkEntranceController);
				//MenuBarSelection.setMenuOptions(menuLabels);
				primaryStage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

}
}

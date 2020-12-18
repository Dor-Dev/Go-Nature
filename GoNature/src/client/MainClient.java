package client;

import java.io.IOException;

import gui.LoginGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainClient extends Application {
	public static ClientConsole clientConsole;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root;

		try {
			FXMLLoader loader = new FXMLLoader();
			root = loader.load(getClass().getResource("/gui/LoginGUI.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Login");
			LoginGUIController loginController = loader.getController();
			loginController.hideEmployeeParamaters();
			primaryStage.show();
			clientConsole = new ClientConsole("localhost", 5555);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}
}

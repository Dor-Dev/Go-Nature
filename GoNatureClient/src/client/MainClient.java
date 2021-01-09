package client;

import java.io.IOException;

import gui.LoginGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reader.CardReaderController;
import reader.MainReader;

public class MainClient extends Application {
	public static ClientConsole clientConsole;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginGUIController g = new LoginGUIController();
		g.show();
		CardReaderController cardReaderController = new CardReaderController();
		cardReaderController.show();
		clientConsole = new ClientConsole("localhost", 5555);

	}
}

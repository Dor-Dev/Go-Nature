package client;

import gui.LoginGUIController;
import javafx.application.Application;
import javafx.stage.Stage;
import reader.CardReaderController;

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

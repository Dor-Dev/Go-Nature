package reader;

import client.ClientConsole;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainReader extends Application {

	public static ClientConsole clientConsole;
	public static void main(String[] args) {
		launch(args);
	}
 
	@Override
	public void start(Stage primaryStage) throws Exception {
		CardReaderController cardReaderController = new CardReaderController();
		cardReaderController.show();
		clientConsole = new ClientConsole("localhost", 5555);
	

	}
	

}

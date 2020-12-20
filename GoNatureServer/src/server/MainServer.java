package server;

import java.io.IOException;
import gui.ServerGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainServer extends Application {

	VBox root;
	public static void main(String[] args) {
		launch(args);
	}
	@Override 
	public void start(Stage primaryStage) throws Exception {
		try {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/serverGUI.fxml"));
		root =loader.load();
		Scene scene = new Scene(root);
		ServerGUIController controller = loader.getController();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Server");
		primaryStage.show();
		ServerController echoServer = new ServerController(5555,controller);
		try {
			echoServer.listen();
		}
		catch(Exception e) {
			System.out.println("Could not listen");
			
		}

	}catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

}

package server;

import java.io.IOException;
import gui.ServerGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainServer extends Application {
	
	//Set the time variables to send reminder message simulation a day before a order is scheduled - 07:00
	//If the arrival hasn't been approved two hours later, it will cancellation message-09:00
	//For the simulation we will initialize the time unit to be minutes and the suitable variables values so every few minutes the messages will be send
	//(we can't wait 2 hours since the first message is sent)
	private int targetHour1 =7;

	private int targetHour2=9;
	private int targetMinutes1=0;
	private int targetMinutes2=0;
	private long delta1=1,delta2=1;

	private UserMessagesDBController messageSender=new UserMessagesDBController(targetHour1, targetHour2, targetMinutes1, targetMinutes2, delta1,delta2);


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
		 messageSender.sendReminderMessage();
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

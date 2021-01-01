package gui;

import controllers.RestartApp;
import javafx.stage.Stage;

public class CloseStage extends Stage {

	public CloseStage() {
		super();
		this.setOnCloseRequest((event)->{
			RestartApp.restartParameters();
			System.exit(0);
		});
	}
}

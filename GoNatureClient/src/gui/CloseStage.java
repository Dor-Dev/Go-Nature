package gui;

import controllers.RestartApp;
import javafx.stage.Stage;

/**
 * This class restarts parameters before close client connection
 * @author Naor0
 *
 */
public class CloseStage extends Stage {

	public CloseStage() {
		super();
		this.setOnCloseRequest((event)->{
			RestartApp.restartParameters();
			System.exit(0);
		});
	}
}

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;

import common.*;
import controllers.EmployeeController;
import controllers.ParkController;
import controllers.ReceiptController;
import controllers.VisitorController;
import enums.UserTypes;
import logic.Employee;

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ClientController extends AbstractClient {
	public static Object returnedValueFromServer= null ;
	public static boolean awaitResponse=false;
	public static UserTypes type = null;

	//public static UserTypes type =null;
	// Instance variables **********************************************
	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 * 
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ClientController(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *Switch case to find which controller are gonna to take care the request
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		
		Message reciveMsg = (Message) msg;
		returnedValueFromServer = reciveMsg.getObj();
		awaitResponse = false;
		switch (reciveMsg.getControllertype()) {
		case VisitorController:
			VisitorController.visitorParseDate(reciveMsg);
			break;
		case EmployeeController:
			EmployeeController.EmployeeParseData(reciveMsg);
			break;	
		case ParkController:
			ParkController.ParkParseData(reciveMsg);
			break;
		case ReceiptController:
			ReceiptController.receipeParseData(reciveMsg);
			break;
		default:
			break;
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {

		try
	    {
	    	//openConnection();//in order to send more than one message
	       	awaitResponse = true;
	    	sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	      clientUI.display("Could not send message to server: Terminating client."+ e);
	      quit();
	    }
		
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class

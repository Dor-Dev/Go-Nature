package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import gui.ServerGUIController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import common.Message;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class ServerController extends AbstractServer {
	// Class variables *************************************************

	ServerGUIController serverController;

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public ServerController(int port) {
		super(port);
	}

	// Instance methods ************************************************

	public ServerController(int port, ServerGUIController controller) {
		super(port);
		this.serverController = controller;
	}

	/**
	 * This method handles any messages received from the client.
	 * 
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		Message clientMsg = (Message) msg;
		try {
			switch (clientMsg.getDbControllertype()) {

			case LoginDBController:
				LoginDBController loginDBController = new LoginDBController();
				client.sendToClient(loginDBController.parseData(clientMsg));
				break;

			case OrderDBController:
				OrderDBController orderDBController = new OrderDBController();
				client.sendToClient(orderDBController.addOrder(clientMsg));
				break;

			case ParkDBController:
				ParkDBController parkDBcontroller = new ParkDBController();
				client.sendToClient(parkDBcontroller.parseData(clientMsg));
				break;
			case ReceiptDBController:
				ReceiptDBController receiptDBController = new ReceiptDBController();
				client.sendToClient(receiptDBController.parseData(clientMsg));
				break;
        case RegistrationDBController:{
					RegistrationDBController registrationDBController = new RegistrationDBController();
					System.out.println("Server received registaration request ");
					client.sendToClient(registrationDBController.parseData(clientMsg));
          break;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		SqlConnection.getConnection();
		System.out.println("Server listening for connections on port " + getPort());
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {

		serverController.setClientStatus(client.getInetAddress().getHostAddress(),
				client.getInetAddress().getHostName(), "connected");
		new Thread(() -> {
			while (client.isAlive()) {
				try {
					client.join();
				} catch (InterruptedException e) {
				}
			}
			clientDisconnected(client);
		}).start();
	}

	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		serverController.setClientStatus(" ", " ", "not connected");
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		ServerController sv = new ServerController(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class

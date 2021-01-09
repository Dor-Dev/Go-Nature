package controllers;

import client.ClientController;
import common.Message;
import enums.UserTypes;
import logic.Subscriber;

/**
 * The Class is controller for the members and regular visitors connected details
 * the class is save the type of the connected visitors and use them
 * @author Naor0
 *
 */
public class VisitorController {
	public static Subscriber subscriberConnected = null;
	public static int loggedID = 0;
	public static Object disType;
	public static boolean isConnected = false;
	public static boolean memberNotExist = false;

	public static void visitorParseDate(Message msg) {
		switch (msg.getOperationType()) {
		case VisitorWithOrderLogin:
			ClientController.type = UserTypes.VisitorWithOrder;
			break;
		case VisitorLogin:
			ClientController.type = UserTypes.visitor;

			break;

		case SubscriberLogin:
			memberNotExist = false;
			subscriberConnected = (Subscriber) ClientController.returnedValueFromServer;
			if (subscriberConnected.getType().equals("Guide")) {
				ClientController.type = UserTypes.instructor;
			} else
				ClientController.type = UserTypes.subscriber;
			break;
		case VisitorAlreadyLoggedIn:
			isConnected = true;
			break;
		case UserDisconnectedSuccess:
			break;
		case MemberNumberNotExist:
			memberNotExist = true;
			break;
		default:
			break;
		}
	}
	
}

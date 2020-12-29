package controllers;


import client.ClientController;
import common.Message;
import enums.UserTypes;
import logic.Subscriber;


public class VisitorController {
	public static Subscriber subscriberConnected = null;
	public static int loggedID =0;
	public static Object disType;

	public static void visitorParseDate(Message msg) {
		System.out.println("PARSEVISITORCONT");
		switch (msg.getOperationType()) {
		case VisitorWithOrderLogin :
			ClientController.type = UserTypes.VisitorWithOrder;
			break;
		case VisitorLogin:
			ClientController.type = UserTypes.visitor;
			
			break;
			
		case SubscriberLogin:
			subscriberConnected = (Subscriber)ClientController.returnedValueFromServer;
			if (subscriberConnected.getType().equals("instructor")) {
				//System.out.println("come in");
				ClientController.type = UserTypes.instructor;
			}
			else ClientController.type = UserTypes.subscriber;
			break;
			

		default:
			break;
		}
	}
}

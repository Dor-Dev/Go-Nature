package controllers;

import client.ClientController;
import common.Message;
import enums.UserTypes;
import logic.Visitor;

public class VisitorController {
	public static Visitor visitorConnected = null;

	public static void visitorParseDate(Message msg) {
		visitorConnected = (Visitor) ClientController.returnedValueFromServer;
		switch (msg.getOperationType()) {
		case VisitorLogin:
			if (visitorConnected.isMember()) {
				if (visitorConnected.getType().equals("instructor")) {
					ClientController.type = UserTypes.instructor;

				} else {
					ClientController.type = UserTypes.subscriber;
				}
			} else {
				ClientController.type = UserTypes.visitor;
			}
		default:
			break;
		}
	}
}

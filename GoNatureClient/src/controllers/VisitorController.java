package controllers;

import java.util.ArrayList;
import java.util.List;

import client.ClientController;
import common.Message;
import enums.UserTypes;
import logic.Subscriber;
import logic.Visitor;

public class VisitorController {
	public static List<Object>  objConnected = null;
	public static Visitor visitorConnected=null;
	public static Subscriber subscriberConnected = null;

	public static void visitorParseDate(Message msg) {
		objConnected = (ArrayList<Object>) ClientController.returnedValueFromServer;
		
		switch (msg.getOperationType()) {
		case VisitorLogin:
			visitorConnected = (Visitor)objConnected.get(0);
			if (visitorConnected.isMember()) {
				subscriberConnected= (Subscriber)objConnected.get(1);
				
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

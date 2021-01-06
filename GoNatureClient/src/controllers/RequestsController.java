package controllers;

import java.util.List;

import common.Message;
import enums.OperationType;
import gui.DManagerReportsGUIController;
import gui.DManagerRequestsGUIController;
import logic.Event;
import logic.ReportImage;
import logic.Update;

public class RequestsController {
	public static OperationType requestType = null;

	public static void requestsPraseDate(Message reciveMsg) {
		switch (reciveMsg.getOperationType()) {
		case ShowUpdateTable:
			DManagerRequestsGUIController.updateData = (List<Update>) reciveMsg.getObj();
			requestType = OperationType.UpdateTableArrived;
			break;
		case ShowEventTable:
			DManagerRequestsGUIController.eventData = (List<Event>) reciveMsg.getObj();
			requestType = OperationType.EventTableArrived;
			break;
		case EventActivated:
			requestType = OperationType.EventActivated;
			break;
		case EventCanceled:
			requestType = OperationType.EventCanceled;
			break;
		case UpdateConfrimation:
			requestType = OperationType.UpdateConfrimation;
			break;
		case UpdateDecline:
			requestType = OperationType.UpdateDecline;
			break;
		case ReturnReceivedReport:
			DManagerReportsGUIController.reports = (List<ReportImage>) reciveMsg.getObj();
			break;
		default:
			break;

		}

	}

}
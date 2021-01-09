package controllers;

import java.util.List;

import common.Message;
import enums.OperationType;
import gui.DManagerReportsGUIController;
import gui.DManagerRequestsGUIController;
import logic.Event;
import logic.ReportImage;
import logic.Update;

/**
 * Controller who receive the message from server and update the relevant
 * variables.<br>
 * Insert into {@link DManagerRequestsGUIController#updateData} the list comes
 * from server when case is ShowUpdateTable.<br>
 * Insert into {@link DManagerRequestsGUIController#eventData} the list comes
 * from server when case is ShowEventTable.<br>
 * 
 * @author dorswisa
 */
public class RequestsController {
	public static OperationType requestType = null;

	@SuppressWarnings("unchecked")
	public static void requestsPraseDate(Message reciveMsg) {
		switch (reciveMsg.getOperationType()) {
		case ShowUpdateTable: // case which get the update parameters request as a list
			DManagerRequestsGUIController.updateData = (List<Update>) reciveMsg.getObj();
			requestType = OperationType.UpdateTableArrived;
			break;
		case ShowEventTable://// case which get the event requests as a list
			DManagerRequestsGUIController.eventData = (List<Event>) reciveMsg.getObj();
			requestType = OperationType.EventTableArrived;
			break;
		case EventActivated:// case which indicates the event are activated
			requestType = OperationType.EventActivated;
			break;
		case EventCanceled:// case which indicates the event was canceled.
			requestType = OperationType.EventCanceled;
			break;
		case UpdateConfrimation:// case which indicates the update request was approved.
			requestType = OperationType.UpdateConfrimation;
			break;
		case UpdateDecline:// case which indicates the update request was decline
			requestType = OperationType.UpdateDecline;
			break;
		case ReturnReceivedReport:// case which return the list on the reports the park manager was created for
									// department manager.
			DManagerReportsGUIController.reports = (List<ReportImage>) reciveMsg.getObj();
			break;
		default:
			break;

		}

	}

}
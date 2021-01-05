package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Employee;
import logic.Event;
import logic.Update;

public class RequestsDBController {
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;

	public RequestsDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Message parseData(Message clientMsg) {
		PreparedStatement pstm;
		String query;
		msgFromClient = clientMsg;
		switch (msgFromClient.getOperationType()) {
		case GetUpdateTable:
			String parkName = (String) msgFromClient.getObj();
			List<Update> data = new ArrayList<Update>();
			Update tmp;
			query = "SELECT * FROM updateRequests WHERE status = 'Waiting' ";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				ResultSet rs = pstm.executeQuery();
				while (rs.next()) {
					tmp = new Update(rs.getInt(1),rs.getString(6), rs.getInt(2), rs.getInt(3), rs.getInt(4),rs.getString(5));
					data.add(tmp);
				}

				return new Message(OperationType.ShowUpdateTable, ClientControllerType.RequestsController,
						(Object) data);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GetEventTable:
			List<Event> eventData = new ArrayList<Event>();
			LocalDate thisDay = LocalDate.now();
			Date thisDayToDB=Date.valueOf(thisDay); 
			Event tmp2;
			query = "SELECT * FROM eventRequests WHERE endDate >= ?";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setDate(1, thisDayToDB);
				ResultSet rs = pstm.executeQuery();
				while (rs.next()) {
					tmp2 = new Event(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6),rs.getString(7));
					eventData.add(tmp2);
				}
				return new Message(OperationType.ShowEventTable, ClientControllerType.RequestsController,
						(Object) eventData);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case EventApproval:

			Event event = (Event) clientMsg.getObj();
			query = "UPDATE eventRequests SET status='Active' WHERE requestNumber=?";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setInt(1, event.getRequestNum());
				int res = pstm.executeUpdate();
				if (res == 1)
					return new Message(OperationType.EventActivated, ClientControllerType.RequestsController,
							(Object) "the Event was activated");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case EventDecline:
			Event eventCancel = (Event) clientMsg.getObj();
			query = "UPDATE eventRequests SET status='Canceled' WHERE requestNumber=?";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setInt(1, eventCancel.getRequestNum());
				int res = pstm.executeUpdate();
				if (res == 1)
					return new Message(OperationType.EventCanceled, ClientControllerType.RequestsController,
							(Object) "the Event was canceled");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case UpdateConfrimation:
			Update update = (Update) clientMsg.getObj();
			query = "Update parks SET parkCapacity=?,ordersCapacity=?,difference=?  WHERE parkName=?";
			String query2 = "UPDATE updateRequests SET status='Approved' WHERE requestNumber=?";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setInt(1, update.getCapacity());
				pstm.setInt(2, update.getCapacity() - update.getDifference());
				pstm.setInt(3, update.getDifference());
				pstm.setString(4, update.getParkName());

				int res = pstm.executeUpdate();
				pstm = sqlConnection.connection.prepareStatement(query2);
				pstm.setInt(1, update.getRequestNum());
				pstm.executeUpdate();
				if(res==1)
					return new Message(OperationType.UpdateConfrimation, ClientControllerType.RequestsController, (Object)"Update Parametrs approved");
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case UpdateDecline:
			Update cancel = (Update) clientMsg.getObj();
			query = "Update updateRequests SET status='Decline' WHERE requestNumber=?";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setInt(1, cancel.getRequestNum());

				int res = pstm.executeUpdate();
				if(res==1)
					return new Message(OperationType.UpdateDecline, ClientControllerType.RequestsController, (Object)"Update Parametrs declined");
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

		return clientMsg;
	}
}

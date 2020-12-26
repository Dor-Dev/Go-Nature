package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Event;
import logic.Park;
import logic.Subscriber;
import logic.Update;

public class ParkDBController {

	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;

	public ParkDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Message parseData(Message clientMsg) {
		PreparedStatement pstm = null;
		String query;
		msgFromClient = clientMsg;
		switch (msgFromClient.getOperationType()) {
		case GetParkInfo:
			String info;
			info = (String) msgFromClient.getObj();
			try {
				System.out.println("TRY IN LOGIN DB CONTROLLER" + info);
				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, info);
				System.out.println("AFTER QUERY");
				ResultSet rs = pstm.executeQuery();
				// for case that traveler didn't exist in system
				if (rs.next()) {
					System.out.println(rs.getString(1));
					Park park = new Park(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
							rs.getInt(6));
					System.out.println(park);
					return new Message(OperationType.ParkInfo, ClientControllerType.ParkController, (Object) park);
				} else {
					System.out.println("NotFound");
					return null;
				}

			} catch (SQLException e) {
				System.out.println("CATCH");
				e.printStackTrace();
			}
			break;

		case DecreaseParkVistiors:
			List<String> parkInfo = (ArrayList<String>) msgFromClient.getObj();
			try {

				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, parkInfo.get(0));
				ResultSet s = pstm.executeQuery();
				if (s.next()) {
					System.out.println(s.getInt(6));

					System.out.println(parkInfo.get(1));

					int currAmount = s.getInt(6) - Integer.parseInt(parkInfo.get(1));

					pstm = sqlConnection.connection
							.prepareStatement("UPDATE  parks SET currentAmountOfVisitors=?  where parkName=?");
					pstm.setInt(1, currAmount);
					pstm.setString(2, parkInfo.get(0));

					int rs = pstm.executeUpdate();
					if (rs == 1) {
						pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
						pstm.setString(1, parkInfo.get(0));
						System.out.println("AFTER QUERY");
						s = pstm.executeQuery();
						// for case that traveler didn't exist in system
						if (s.next()) {
							System.out.println(s.getString(1));
							Park park = new Park(s.getString(1), s.getInt(2), s.getInt(3), s.getInt(4), s.getInt(5),
									s.getInt(6));
							System.out.println(park);
							return new Message(OperationType.UpdateParkInfo, ClientControllerType.ParkController,
									(Object) park);
						}
					}
				}

			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case IncreaseParkVistiors:
			List<String> parkInfo2 = (ArrayList<String>) msgFromClient.getObj();
			try {

				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, parkInfo2.get(0));
				ResultSet s = pstm.executeQuery();
				if (s.next()) {
					System.out.println(s.getInt(6));
					System.out.println(parkInfo2.get(1));
					int currAmount = s.getInt(6) + Integer.parseInt(parkInfo2.get(1));
					pstm = sqlConnection.connection
							.prepareStatement("UPDATE  parks SET currentAmountOfVisitors=?  where parkName=?");
					pstm.setInt(1, currAmount);
					pstm.setString(2, parkInfo2.get(0));

					int rs = pstm.executeUpdate();
					if (rs == 1) {
						pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
						pstm.setString(1, parkInfo2.get(0));
						System.out.println("AFTER QUERY");
						s = pstm.executeQuery();
						// for case that traveler didn't exist in system
						if (s.next()) {
							System.out.println(s.getString(1));
							Park park = new Park(s.getString(1), s.getInt(2), s.getInt(3), s.getInt(4), s.getInt(5),
									s.getInt(6));
							System.out.println(park);
							return new Message(OperationType.UpdateParkInfo, ClientControllerType.ParkController,
									(Object) park);
						}
					}
				}

			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case TravelerInfo:
			String infoVisitor = (String) msgFromClient.getObj();
			try {
				pstm = sqlConnection.connection.prepareStatement("SELECT * from members where visitorID=?");
				pstm.setString(1, infoVisitor);

				ResultSet rs = pstm.executeQuery();

				if (rs.next()) {
					Subscriber subscriber = null;
					subscriber = new Subscriber(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(13));
					return new Message(OperationType.OccasionalSubscriber, ClientControllerType.VisitorController,
							(Object) subscriber);
				}

				else
					return new Message(OperationType.OccasionalVisitor, ClientControllerType.VisitorController,
							(Object) "not a member");
			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		/**
		 * case to insert the update parameters request to updateRequest table.
		 */
		case SendUpdateRequest:
			Update newUpdate = (Update) msgFromClient.getObj();
			query = "insert into updateRequests(parkCapacity,difference,visitingTime,status,parkName)"
					+ " values(?,?,?,?,?)";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setInt(1, newUpdate.getCapacity());
				pstm.setInt(2, newUpdate.getDifference());
				pstm.setInt(3, newUpdate.getVisitingTime());
				pstm.setString(4, newUpdate.getStatus());
				pstm.setString(5, newUpdate.getParkName());
				// execute the preparedstatement
				pstm.execute();

				return new Message(OperationType.UpdateWasSent, ClientControllerType.ParkController,
						(Object) "Update Sent");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		/**
		 * case to insert new event request to eventRequests table
		 */
		case EventRequest:
			Event newEvent = (Event) msgFromClient.getObj();
			query = "insert into eventRequests(parkName,eventName,startDate,endDate,discount,status)"
					+ " values (?,?,?,?,?,?)";

			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setString(1, newEvent.getParkName());
				pstm.setString(2, newEvent.getEventName());
				pstm.setDate(3, newEvent.getStartDate());
				pstm.setDate(4, newEvent.getEndDate());
				pstm.setInt(5, newEvent.getDiscount());
				pstm.setString(6, newEvent.getStatus());
				pstm.execute();

				return new Message(OperationType.EventRequestAccepted, ClientControllerType.ParkController,
						(Object) "Event request send successfully");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case showActiveEvents:
			List<Event> data = new ArrayList<Event>();
			LocalDate thisday = LocalDate.now();
			Date thisDayToDb = Date.valueOf(thisday);
			Event tmp;
			query = "SELECT * FROM eventRequests WHERE parkName = ? and date(startDate) < date(?) and date(endDate) > date(?) and status='active'";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setString(1, (String)msgFromClient.getObj());
				pstm.setDate(2, thisDayToDb);
				pstm.setDate(3, thisDayToDb);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()) {
					tmp = new Event(rs.getString(3),rs.getDate(4),rs.getDate(5),rs.getInt(6));
					data.add(tmp);
				}
				System.out.println(data.get(0).getEventName());
				return new Message(OperationType.EventsToShow,ClientControllerType.ParkController,(Object)data);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		default:
			break;
		}
		return clientMsg;
	}

}

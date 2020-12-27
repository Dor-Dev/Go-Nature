package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Event;
import logic.Order;
import logic.Park;
import logic.Subscriber;
import logic.Update;

public class ParkDBController {

	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;
	private LocalDate thisDay;
	private Date thisDayToDB;
	private LocalTime thisTime;
	private Time thisTimeToDB;
	private int hours;
	private int minutes;

	public ParkDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Message parseData(Message clientMsg) throws SQLException {
		PreparedStatement pstm;
		String query;
		msgFromClient = clientMsg;
		List<String> parkInfo ;
		String info;
		switch (msgFromClient.getOperationType()) {
		
		// this case get information about the park
		case GetParkInfo:
			
			info = (String) msgFromClient.getObj();
			try {
				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, info);
				System.out.println("AFTER QUERY");
				ResultSet rs = pstm.executeQuery();
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

			// this case dencrease the amount of current visitors in the park
		case DecreaseParkVistiors:
			 parkInfo = (ArrayList<String>) msgFromClient.getObj();
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
				return new Message(OperationType.FailedUpdate, ClientControllerType.ParkController,
						(Object) "can't decrese");
				
			
			}

			 catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

			// this case increase the amount of current visitors in the park
		case IncreaseParkVistiors:
			parkInfo = (ArrayList<String>) msgFromClient.getObj();
			try {

				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, parkInfo.get(0));
				ResultSet s = pstm.executeQuery();
				if (s.next()) {
					System.out.println(s.getInt(6));
					System.out.println(parkInfo.get(1));
					int currAmount = s.getInt(6) + Integer.parseInt(parkInfo.get(1));
					pstm = sqlConnection.connection
							.prepareStatement("UPDATE  parks SET currentAmountOfVisitors=?  where parkName=?");
					pstm.setInt(1, currAmount);
					pstm.setString(2, parkInfo.get(0));
					System.out.println("???????");

					int rs = pstm.executeUpdate();
					if (rs == 1) {
						pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
						pstm.setString(1, parkInfo.get(0));
						System.out.println("AFTER increase park");
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

				System.out.println(8598);
				return new Message(OperationType.FailedUpdate, ClientControllerType.ParkController,
						(Object) "cant increase");

			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

			// this case get information about the visitor to update the relevant discount
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
					
					return new Message(OperationType.OccasionalSubscriber, ClientControllerType.ParkController,
							(Object) subscriber);
				}

				else
					return new Message(OperationType.OccasionalVisitor, ClientControllerType.ParkController,
							(Object) "not a member");
			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

			// this case check the orders table if the order is exist
		case GetOrderInfo:
			List<String> orderInfo = (ArrayList<String>) msgFromClient.getObj();
			try {
				System.out.println("getOrder");
				pstm = sqlConnection.connection.prepareStatement("SELECT * from orders where orderID=? and parkName=?");
				pstm.setInt(1, Integer.parseInt(orderInfo.get(0)));
				pstm.setString(2, orderInfo.get(2));

				ResultSet rs = pstm.executeQuery();

				if (rs.next()) {
					if (rs.getInt(8) >= Integer.parseInt(orderInfo.get(1))) {
						Order order = new Order(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getInt(4),
								rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9),
								rs.getBoolean(10), rs.getInt(11));
						System.out.println("order id= " + order.getOrderID());
						return new Message(OperationType.GetOrderInfo, ClientControllerType.ParkController,
								(Object) order);
					}
				}
				return new Message(OperationType.GetOrderInfo, ClientControllerType.ParkController,
						(Object) "order doesn't exist");

			} catch (SQLException e) {
			}

			break;
			
			// this case check the amount of occasional visitor with the difference 
		case CheckDifference:
			parkInfo =(ArrayList<String>) msgFromClient.getObj();
			getCurrentTime();
			
			System.out.println("cheak occasional amount= ");
			
				pstm = sqlConnection.connection.prepareStatement("SELECT SUM(currAmountOfVisitorsLeft) from receipts where orderNumber=? and parkName=? and date=?");
				pstm.setInt(1, 0);
				pstm.setString(2, parkInfo.get(0));
				pstm.setDate(3, thisDayToDB);
				
				System.out.println("cheak");
				ResultSet s= pstm.executeQuery();
				int occasionalAmount=0;
				if(s.next()) {
					occasionalAmount= s.getInt(1);
					System.out.println("occasional amount1= "+ occasionalAmount);	
				}
				
				System.out.println("occasional amount2= "+ occasionalAmount);	
				return new Message(OperationType.CheckDifference, ClientControllerType.ParkController,(Object)occasionalAmount );
			
			

		
			// this case update the receipts and parks table with the correct current amount of visitors
		case UpdateCurrAmountOfVisitors:
			String parkName = (String)msgFromClient.getObj();
			getCurrentTime();
			System.out.println("888881");
			pstm = sqlConnection.connection.prepareStatement("UPDATE  receipts SET currAmountOfVisitorsLeft=?  where parkName=? and visitExit<=? and date=?");
			pstm.setInt(1, 0);
			pstm.setString(2, parkName);
			pstm.setInt(3, hours);
			pstm.setDate(4, thisDayToDB);
			System.out.println(pstm.executeUpdate());
			
			pstm = sqlConnection.connection.prepareStatement("UPDATE  receipts SET currAmountOfVisitorsLeft=?  where  date<?");
			pstm.setInt(1, 0);
			//pstm.setString(2, parkName);
			pstm.setDate(2, thisDayToDB);
			System.out.println(pstm.executeUpdate());
			
			pstm = sqlConnection.connection.prepareStatement("SELECT SUM(currAmountOfVisitorsLeft) from receipts where parkName=? and date=?");
			pstm.setString(1, parkName);
			pstm.setDate(2, thisDayToDB);
			ResultSet sum= pstm.executeQuery();
			int sumCurr=0;
			if(sum.next()) {
				System.out.println("888882");
				sumCurr= sum.getInt(1);
				System.out.println("sumCurr= "+sumCurr);
			}
			
			pstm = sqlConnection.connection.prepareStatement("UPDATE  parks SET currentAmountOfVisitors=?  where parkName=?");
			pstm.setInt(1, sumCurr);
			pstm.setString(2, parkName);
			
			pstm.executeUpdate();
			System.out.println("8888883");
			return new Message(OperationType.UpdateCurrAmountOfVisitors, ClientControllerType.ParkController,(Object)sumCurr );

		case showActiveEvents:
			List<Event> data = new ArrayList<Event>();
			LocalDate thisday = LocalDate.now();
			Date thisDayToDb = Date.valueOf(thisday);
			Event tmp;
			query = "SELECT * FROM eventRequests WHERE parkName = ? and date(startDate) < date(?) and date(endDate) > date(?) and status='active'";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setString(1, (String) msgFromClient.getObj());
				pstm.setDate(2, thisDayToDb);
				pstm.setDate(3, thisDayToDb);
				ResultSet rs = pstm.executeQuery();
				while (rs.next()) {
					tmp = new Event(rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6));
					data.add(tmp);
				}
				System.out.println(data.get(0).getEventName());
				return new Message(OperationType.EventsToShow, ClientControllerType.ParkController, (Object) data);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			
		default:
			break;
		}
		return clientMsg;
	}
	
	/*
	 * this function taking the date and time of today
	 */
	private void getCurrentTime() {

		thisDay = LocalDate.now();
		thisDayToDB = Date.valueOf(thisDay);
		thisTime = LocalTime.now();
		thisTimeToDB = Time.valueOf(thisTime);
		hours = thisTimeToDB.getHours();
		minutes = thisTimeToDB.getMinutes();
		if (minutes > 0) {
			hours += 1;
	
		}
	}

	

}

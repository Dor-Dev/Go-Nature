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
import logic.CardReaderRequest;
import logic.Event;
import logic.Order;
import logic.Park;
import logic.Receipt;
import logic.Subscriber;
import logic.Update;

/**
 * DB Controller that responsible all the relevant data of park.
 * @author dorswisa
 *
 */
public class ParkDBController {

	private final int firstEntryHour = 9;
	private final int lastEntryHour= 17;
	private final int firstExitHour = 9;
	private final int lastExitHour= 21;
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;
	private LocalDate thisDay;
	private Date thisDayToDB;
	private LocalTime thisTime;
	private Time thisTimeToDB;
	private int hours;
	private int minutes;
	private PreparedStatement pstm;
	ResultSet rs;
	private CardReaderRequest cardReaderRequest = null;
	int res;

	public ParkDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Message parseData(Message clientMsg) throws SQLException {

		String query;
		msgFromClient = clientMsg;

		List<String> parkInfo;
		String info;
		switch (msgFromClient.getOperationType()) {

		// this case get information about the park
		case GetParkInfo:

			info = (String) msgFromClient.getObj();
			try {
				rs = getParkInfo(info);
				if (rs.next()) {
					Park park = returnPark(rs);
					int sumOfVisitors=resetTheAmountOfVisitorsInPark(park.getParkName());
					park.setCurrentAmountOfVisitors(sumOfVisitors);

					return new Message(OperationType.ParkInfo, ClientControllerType.ParkController, (Object) park);
				} else {
					return null;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		// this case dencrease the amount of current visitors in the park
		case DecreaseParkVistiors:
			parkInfo = (ArrayList<String>) msgFromClient.getObj();
			try {
				rs = getParkInfo(parkInfo.get(0));
				if (rs.next()) {
					int currAmount = rs.getInt(6) - Integer.parseInt(parkInfo.get(1));
					res = updateAmountOfCurrAmount(currAmount, parkInfo.get(0));
					if (res == 1) {
						rs = getParkInfo(parkInfo.get(0));
						// for case that traveler didn't exist in system
						if (rs.next()) {
							Park park = returnPark(rs);
							return new Message(OperationType.UpdateParkInfo, ClientControllerType.ParkController,
									(Object) park);
						}
					}
				}
				return new Message(OperationType.FailedUpdate, ClientControllerType.ParkController,
						(Object) "can't decrese");

			}

			catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
			break;

		// this case increase the amount of current visitors in the park
		case IncreaseParkVistiors:
			parkInfo = (ArrayList<String>) msgFromClient.getObj();
			try {
				rs = getParkInfo(parkInfo.get(0));
				if (rs.next()) {
					int currAmount = rs.getInt(6) + Integer.parseInt(parkInfo.get(1));

					res = updateAmountOfCurrAmount(currAmount, parkInfo.get(0));
					if (res == 1) {

						rs = getParkInfo(parkInfo.get(0));
						// for case that traveler didn't exist in system
						if (rs.next()) {
							Park park = returnPark(rs);
							return new Message(OperationType.UpdateParkInfo, ClientControllerType.ParkController,
									(Object) park);
						}
					}
				}

				return new Message(OperationType.FailedUpdate, ClientControllerType.ParkController,
						(Object) "cant increase");

			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
			break;

		// this case get information about the visitor to update the relevant discount
		case TravelerInfo:
			String infoVisitor = (String) msgFromClient.getObj();
			try {

				pstm = sqlConnection.connection.prepareStatement("SELECT * from members where visitorID=?");
				pstm.setInt(1, Integer.parseInt(infoVisitor));

				rs = pstm.executeQuery();

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
				pstm = sqlConnection.connection.prepareStatement("SELECT * from orders where orderID=? and parkName=?");
				pstm.setInt(1, Integer.parseInt(orderInfo.get(0)));
				pstm.setString(2, orderInfo.get(2));

				rs = pstm.executeQuery();

				if (rs.next()) {
					if (rs.getInt(8) >= Integer.parseInt(orderInfo.get(1))) {
						Order order = new Order(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getInt(4),
								rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9),
								rs.getBoolean(10), rs.getInt(11), rs.getInt(14));
						return new Message(OperationType.GetOrderInfo, ClientControllerType.ParkController,
								(Object) order);
					}
				}
				return new Message(OperationType.GetOrderInfo, ClientControllerType.ParkController,
						(Object) "order doesn't exist");

			} catch (SQLException e) {
			}

			break;

		// this case update the receipts and parks table with the correct current amount
		// of visitors
		case UpdateCurrAmountOfVisitors:
			String parkName = (String) msgFromClient.getObj();
			int sumCurr = resetTheAmountOfVisitorsInPark(parkName);
			return new Message(OperationType.UpdateCurrAmountOfVisitors, ClientControllerType.ParkController,
					(Object) sumCurr);
			
		// case who returns a list of all the activated events in a specific park and
		// their date are relevant to the current date.
		case showManagerEvents:
			List<Event> data = new ArrayList<Event>();
			LocalDate thisday = LocalDate.now();
			Date thisDayToDb = Date.valueOf(thisday);
			Event tmp;
			query = "SELECT * FROM eventRequests WHERE parkName = ?  and endDate >= ?";
			try {
				pstm = sqlConnection.connection.prepareStatement(query);
				pstm.setString(1, (String) msgFromClient.getObj());
				pstm.setDate(2, thisDayToDb);
				rs = pstm.executeQuery();
				while (rs.next()) {
					tmp = new Event(rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6),rs.getString(7));
					data.add(tmp);
				}
				return new Message(OperationType.EventsToShow, ClientControllerType.ParkController, (Object) data);

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
				e.printStackTrace();
			}
			break;
			
			/**
			 * this case is for enter card reader to check the order and the receipt
			 */
		case VisitorEnterRequest:
			 cardReaderRequest = (CardReaderRequest) msgFromClient.getObj();
			PreparedStatement pstm, pstm2, pstm3;
			getCurrentTime();
			
			resetTheAmountOfVisitorsInPark(cardReaderRequest.getParkName());
			if(hours < firstEntryHour || hours> lastEntryHour) {
				return new Message(OperationType.VisitorEnterRequest, ClientControllerType.ParkController,
						(Object) "The entrance hours are over! You can enter the park from 09:00 to 17:00");
				
			}
			
			 try {
				pstm = sqlConnection.connection.prepareStatement("SELECT * from orders where parkName=? and visitorID=? and arrivalDate=? and hourTime>=? and hourTime<=? and status='Approved'");

				pstm.setString(1, cardReaderRequest.getParkName());
				pstm.setInt(2, cardReaderRequest.getVisitorID());
				pstm.setDate(3, thisDayToDB);
				pstm.setInt(4, hours-4);
				pstm.setInt(5, hours);
				
				ResultSet rs = pstm.executeQuery();
				while(rs.next()) {
					int orderID = rs.getInt(1);
					int numOfVisitor = rs.getInt(8);
					Order order = new Order(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getInt(4),
							rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9),
							rs.getBoolean(10), rs.getInt(11),rs.getInt(14));
					if(numOfVisitor>=cardReaderRequest.getVisitorAmount()) {
						pstm2 = sqlConnection.connection.prepareStatement("SELECT * from receipts where parkName=? and visitorID=? and orderNumber=? ");
						pstm2.setString(1, cardReaderRequest.getParkName());
						pstm2.setInt(2, cardReaderRequest.getVisitorID());
						pstm2.setInt(3, orderID);
						ResultSet rs2 = pstm2.executeQuery();
						if(rs2.next()) {
							//for case the order is in receipts
							int actualVisitors= rs2.getInt(12);
							int currAmountOfVisitor = rs2.getInt(10);
							int receiptID = rs2.getInt(1);
							if(numOfVisitor >= actualVisitors +cardReaderRequest.getVisitorAmount())
							{
								pstm3 = sqlConnection.connection.prepareStatement("UPDATE  receipts SET currAmountOfVisitorsLeft=? ,actualNumOfVisitors=?  where receiptsID=?");
								pstm3.setInt(1, currAmountOfVisitor + cardReaderRequest.getVisitorAmount());
								pstm3.setInt(2, actualVisitors + cardReaderRequest.getVisitorAmount());
								pstm3.setInt(3, receiptID);
								int res = pstm3.executeUpdate();
								if (res == 1) {
									// update the current amount of visitors in the specific park
									ResultSet parkRes = getParkInfo(cardReaderRequest.getParkName());
									if (parkRes.next()) {
										int currAmount = parkRes.getInt(6) + cardReaderRequest.getVisitorAmount();
										updateAmountOfCurrAmount(currAmount,cardReaderRequest.getParkName());
									}
									//can enter
									return new Message(OperationType.VisitorEnterRequest, ClientControllerType.ParkController,
											(Object) "The receipt is updated, the visitors can enter the park");
								}
							}
							
							
						}
						// for case the order is not exist in receipt table
						else {
							String type = null;
							if(order.getType().equals("Single/Family")){
								pstm = sqlConnection.connection.prepareStatement("SELECT * from members where visitorID=?");
								pstm.setInt(1, order.getVisitorID());
								ResultSet rs1 = pstm.executeQuery();

								if (rs1.next()) {
									type = "member";
								}
								else {
									type= "visitor";
									
								}
							}
							else {
								type = "instructor";
								
							}
							pstm = sqlConnection.connection.prepareStatement("insert into receipts (date,visitEntry, visitExit,numberOfVisitors,type,parkName,orderNumber,visitorID,currAmountOfVisitorsLeft,time,actualNumOfVisitors,cost)"
									+ "values (?,?,?,?,?,?,?,?,?,?,?,?)");
							pstm.setDate(1, thisDayToDB);
							pstm.setInt(2, order.getHourTime());
							pstm.setInt(3, order.getHourTime()+4);
							pstm.setInt(4, order.getNumOfVisitors());
							pstm.setString(5, type);
							pstm.setString(6, order.getParkName());
							pstm.setInt(7, order.getOrderID());
							pstm.setInt(8, order.getVisitorID());
							pstm.setInt(9, cardReaderRequest.getVisitorAmount());
							pstm.setTime(10, thisTimeToDB);
							pstm.setInt(11, cardReaderRequest.getVisitorAmount());
							pstm.setInt(12, order.getCost());
							pstm.execute();
							
							
							pstm = sqlConnection.connection.prepareStatement("SELECT * from receipts where parkName=? and date=? and time=? and visitorID=?");
							pstm.setString(1, order.getParkName());
							pstm.setDate(2, thisDayToDB);
							pstm.setTime(3, thisTimeToDB);
							pstm.setInt(4, order.getVisitorID());

							ResultSet rs3 = pstm.executeQuery();
							
							ResultSet parkRes = getParkInfo(cardReaderRequest.getParkName());
							if (parkRes.next()) {
								int currAmount = parkRes.getInt(6) + cardReaderRequest.getVisitorAmount();
								updateAmountOfCurrAmount(currAmount,cardReaderRequest.getParkName());
							}
							if (rs3.next()) {
								
							Receipt receipt = new Receipt(rs3.getInt(1),order.getNumOfVisitors() , order.getParkName(), order.getOrderID(), order.getCost(), order.getDiscount());
							
							//return the cost and the discount
							return new Message(OperationType.VisitorEnterRequest, ClientControllerType.ParkController,
									(Object) receipt);
						}
						
					
			 
					}
					}
				}
				return new Message(OperationType.VisitorEnterRequest, ClientControllerType.ParkController,
						(Object) "You can not enter the park");
				
			 }
				
			 catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
			/**
			 * this case is for exit card reader to check the receipt
			 */
		case VisitorExitRequest:
		  cardReaderRequest = (CardReaderRequest) msgFromClient.getObj();
			getCurrentTime();
			resetTheAmountOfVisitorsInPark(cardReaderRequest.getParkName());
			if(hours > lastExitHour || hours< firstExitHour) {
				return new Message(OperationType.VisitorExitRequest, ClientControllerType.ParkController,
						(Object) "The exit hours are over! The help is on the way.");
			}
			pstm = sqlConnection.connection.prepareStatement(
					"SELECT * from receipts where parkName=? and visitorID=? and date=? and visitExit>=?");
			pstm.setString(1, cardReaderRequest.getParkName());
			pstm.setInt(2,cardReaderRequest.getVisitorID());
			pstm.setDate(3, thisDayToDB);
			pstm.setInt(4, hours);

			rs = pstm.executeQuery();
			while (rs.next()) {
				int receiptID = rs.getInt(1);
				//int numOfVisitor = rs.getInt(5);
				//int actualVisitor = rs.getInt(12);
				int currAmountOfVisitorsLeft = rs.getInt(10);
				if (currAmountOfVisitorsLeft >= cardReaderRequest.getVisitorAmount()) {
					currAmountOfVisitorsLeft = currAmountOfVisitorsLeft - cardReaderRequest.getVisitorAmount();
					if (currAmountOfVisitorsLeft == 0) {
						pstm = sqlConnection.connection.prepareStatement(
								"UPDATE  receipts SET currAmountOfVisitorsLeft=0, visitExit=? where receiptsID=?");
						pstm.setInt(1, hours);
						pstm.setInt(2, receiptID);
					} else {
						pstm = sqlConnection.connection.prepareStatement(
								"UPDATE  receipts SET currAmountOfVisitorsLeft=? where receiptsID=?");
						pstm.setInt(1, currAmountOfVisitorsLeft);
						pstm.setInt(2, receiptID);
					}
					int res = pstm.executeUpdate();
					if (res == 1) {
						ResultSet parkRes = getParkInfo(cardReaderRequest.getParkName());
						if (parkRes.next()) {
							int currAmount = parkRes.getInt(6) - cardReaderRequest.getVisitorAmount();
							updateAmountOfCurrAmount(currAmount,cardReaderRequest.getParkName());
						}

						return new Message(OperationType.VisitorExitRequest, ClientControllerType.ParkController,
								(Object) "The visitors can exit the park");

					}
				}

			}

				return new Message(OperationType.VisitorExitRequest, ClientControllerType.ParkController,
						(Object) "The amount of visitors that want to exit is not match");
		

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

	/**
	 * this method reset the numbers of visitors in the park (after they left)
	 * @param parkName
	 * @return the amount of visitors that now in the park;
	 */
	private int resetTheAmountOfVisitorsInPark(String parkName) {
		getCurrentTime();
		// update the amount of visitors in receipts from today that the visitExit is
		// over
		try {
			pstm = sqlConnection.connection.prepareStatement(
					"UPDATE  receipts SET currAmountOfVisitorsLeft=?  where parkName=? and visitExit<=? and date=?");
		
		pstm.setInt(1, 0);
		pstm.setString(2, parkName);
		pstm.setInt(3, hours);
		pstm.setDate(4, thisDayToDB);
		pstm.executeUpdate();

		// update the amount of visitors in receipts from lasts day
		pstm = sqlConnection.connection
				.prepareStatement("UPDATE  receipts SET currAmountOfVisitorsLeft=?  where  date<?");
		pstm.setInt(1, 0);
		pstm.setDate(2, thisDayToDB);
		pstm.executeUpdate();

		// check the current amount of visitors in park today
		pstm = sqlConnection.connection
				.prepareStatement("SELECT SUM(currAmountOfVisitorsLeft) from receipts where parkName=? and date=?");
		pstm.setString(1, parkName);
		pstm.setDate(2, thisDayToDB);
		ResultSet sum = pstm.executeQuery();
		int sumCurr = 0;
		if (sum.next()) {
			sumCurr = sum.getInt(1);
		}

		// update the current amount of visitors in park today
		res = updateAmountOfCurrAmount(sumCurr, parkName);
		return sumCurr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}

	/**
	 * this method get information from SQL and create a park object
	 * 
	 * @param s
	 * @return
	 */
	private Park returnPark(ResultSet s) {
		Park park = null;
		try {
			park = new Park(s.getString(1), s.getInt(2), s.getInt(3), s.getInt(4), s.getInt(5), s.getInt(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return park;
	}

	/**
	 * this method update the current amount of visitors in park
	 * 
	 * @param currAmount
	 * @param parkName
	 * @return
	 */
	private int updateAmountOfCurrAmount(int currAmount, String parkName) {
		try {
			pstm = sqlConnection.connection
					.prepareStatement("UPDATE  parks SET currentAmountOfVisitors=?  where parkName=?");
			pstm.setInt(1, currAmount);
			pstm.setString(2, parkName);
			int rs = pstm.executeUpdate();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	/**
	 * this method gets the information of park
	 * 
	 * @param parkName
	 * @return
	 */
	private ResultSet getParkInfo(String parkName) {
		try {
			pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
			pstm.setString(1, parkName);
			ResultSet s = pstm.executeQuery();
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * this function taking the date and time of today
	 */
	private void getCurrentTime() {

		thisDay = LocalDate.now();
		thisDayToDB = Date.valueOf(thisDay);
		thisTime = LocalTime.now();
		thisTimeToDB = Time.valueOf(thisTime);
		hours = thisTime.getHour();
		minutes = thisTime.getMinute();
		if (minutes > 0) {
			hours += 1;

		}
	}

}

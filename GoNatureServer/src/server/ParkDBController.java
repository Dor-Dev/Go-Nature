package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Park;
import logic.Subscriber;

public class ParkDBController {

	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;
	public  ParkDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public Message parseData(Message clientMsg) {
		PreparedStatement pstm;
		msgFromClient = clientMsg;
		switch(msgFromClient.getOperationType()) {
		case GetParkInfo:
			String info;
			info =  (String) msgFromClient.getObj();
			try {
				System.out.println("TRY IN LOGIN DB CONTROLLER" + info);
				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, info);
				System.out.println("AFTER QUERY");
				ResultSet rs = pstm.executeQuery();
				//for case that traveler didn't exist in system
				if(rs.next()) { 
					System.out.println(rs.getString(1));
					Park park = new Park(rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6));
					System.out.println(park);
					return new Message(OperationType.ParkInfo, ClientControllerType.ParkController,(Object)park);
				}
				else {
					System.out.println("NotFound");
					return null;
				}
				
				
			} catch (SQLException e) {
				System.out.println("CATCH");
				e.printStackTrace();
			}
			break;
			
		case DecreaseParkVistiors:
			List<String> parkInfo = (ArrayList<String>)msgFromClient.getObj();
			try {
				
				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, parkInfo.get(0));
				ResultSet s =pstm.executeQuery();
				if(s.next())
				{
					System.out.println(s.getInt(6));
					
					System.out.println(parkInfo.get(1));
					
					int currAmount = s.getInt(6)-Integer.parseInt(parkInfo.get(1));
					
				
					pstm = sqlConnection.connection.prepareStatement("UPDATE  parks SET currentAmountOfVisitors=?  where parkName=?");
					pstm.setInt(1, currAmount);
					pstm.setString(2,parkInfo.get(0));
			
					int rs=pstm.executeUpdate();
					if(rs==1) {
					pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
					pstm.setString(1, parkInfo.get(0));
					System.out.println("AFTER QUERY");
					s = pstm.executeQuery();
				//for case that traveler didn't exist in system
					if(s.next()) { 
						System.out.println(s.getString(1));
						Park park = new Park(s.getString(1),s.getInt(2),s.getInt(3),s.getInt(4),s.getInt(5),s.getInt(6));
						System.out.println(park);
						return new Message(OperationType.UpdateParkInfo, ClientControllerType.ParkController,(Object)park);
				}
			}
			}
			
			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case IncreaseParkVistiors:
			List<String> parkInfo2 = (ArrayList<String>)msgFromClient.getObj();
			try {
				
				pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
				pstm.setString(1, parkInfo2.get(0));
				ResultSet s =pstm.executeQuery();
				if(s.next())
				{
					System.out.println(s.getInt(6));
					System.out.println(parkInfo2.get(1));
					int currAmount = s.getInt(6)+Integer.parseInt(parkInfo2.get(1));
					pstm = sqlConnection.connection.prepareStatement("UPDATE  parks SET currentAmountOfVisitors=?  where parkName=?");
					pstm.setInt(1, currAmount);
					pstm.setString(2,parkInfo2.get(0));
			
					int rs=pstm.executeUpdate();
					if(rs==1) {
					pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
					pstm.setString(1, parkInfo2.get(0));
					System.out.println("AFTER QUERY");
					s = pstm.executeQuery();
				//for case that traveler didn't exist in system
					if(s.next()) { 
						System.out.println(s.getString(1));
						Park park = new Park(s.getString(1),s.getInt(2),s.getInt(3),s.getInt(4),s.getInt(5),s.getInt(6));
						System.out.println(park);
						return new Message(OperationType.UpdateParkInfo, ClientControllerType.ParkController,(Object)park);
				}
			}
 }

			
			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case TravelerInfo:
			String infoVisitor = (String)msgFromClient.getObj();
			try {
			pstm = sqlConnection.connection.prepareStatement("SELECT * from members where visitorID=?");
			pstm.setString(1, infoVisitor);
			
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) { 
				Subscriber subscriber = null;
				subscriber=new Subscriber(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getString(13));
				return new Message(OperationType.OccasionalSubscriber, ClientControllerType.VisitorController,(Object)subscriber);		
			}
			
			else
			return new Message(OperationType.OccasionalVisitor, ClientControllerType.VisitorController,(Object)"not a member");
			}catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case SendUpdateRequest:
			System.out.println("ParkManager Server:Try");
			PreparedStatement preparedStmt;
			List<String> updateInfo = (ArrayList<String>)msgFromClient.getObj();
			String query = "insert into requests(parkCapacity,difference,visitingTime,status)"+ 
							" values(?,?,?,?)";
			try {
				preparedStmt = sqlConnection.connection.prepareStatement(query);
				preparedStmt.setInt(1, Integer.valueOf(updateInfo.get(0)));
				preparedStmt.setInt(2, Integer.valueOf(updateInfo.get(1)));
				preparedStmt.setInt(3, Integer.valueOf(updateInfo.get(2)));
				preparedStmt.setString(4, updateInfo.get(3));
				// execute the preparedstatement
				preparedStmt.execute();
				
				return new Message(OperationType.UpdateWasSent,ClientControllerType.ParkController,(Object)"Update Sent");
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

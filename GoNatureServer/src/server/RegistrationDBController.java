package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;

/**
 * @author dana_
 *A class that is responsible for the queries regards the registration process of members and group instructors
 */
public class RegistrationDBController {
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;
	public RegistrationDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public Message parseData(Message clientMsg) {
		PreparedStatement pstm;
		List<String> info;
		msgFromClient = clientMsg;
		info =  (ArrayList<String>) msgFromClient.getObj();
		switch(msgFromClient.getOperationType()) {
		//Case we register a member with credit card
		case MemberRegistrationCC:{
			try {
					//Check if he already registered
					pstm = sqlConnection.connection.prepareStatement("SELECT * from members where memberNumber=?");
					pstm.setString(1, info.get(0));
					ResultSet rs = pstm.executeQuery();
					if(rs.next()) {
						String Msg="The member already registered";
						return new Message(OperationType.MemberRegistrationCC, ClientControllerType.RegistrationController,(Object)(Msg));
					}
					//If he does not exits at the data base insert the information to the data base and return success message 
					pstm = sqlConnection.connection.prepareStatement("INSERT INTO members (memberNumber,visitorID,firstName,lastName,phoneNumber,email,familyMembers,cardNumber,CVV,cardOwner,expirationMonth,expirationYear,type) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pstm.setInt(1, Integer.parseInt(info.get(0)));
					pstm.setInt(2, Integer.parseInt(info.get(3)));
					pstm.setString(3, info.get(1));
					pstm.setString(4, info.get(2));
					pstm.setString(5, info.get(4));
					pstm.setString(6, info.get(5));
					pstm.setInt(7, Integer.parseInt(info.get(6)));
					pstm.setString(8, info.get(7));
					pstm.setString(9, info.get(9));
					pstm.setString(10, info.get(8));
					pstm.setInt(11, Integer.parseInt(info.get(10)));
					pstm.setInt(12, Integer.parseInt(info.get(11)));
					pstm.setString(13, "Member");
					pstm.executeUpdate();
					String Msg="The member registered successfuly!";
					return new Message(OperationType.MemberRegistrationCC, ClientControllerType.RegistrationController,(Object)Msg);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;	}
		//Case we register a member without credit card
		case MemberRegistration:{
			try {
					//Check if he already registered
					pstm = sqlConnection.connection.prepareStatement("SELECT * from members where memberNumber=?");
					pstm.setString(1, info.get(0));
					ResultSet rs = pstm.executeQuery();
					if(rs.next()) {
						String Msg="The member already registered";
						return new Message(OperationType.MemberRegistration, ClientControllerType.RegistrationController,(Object)(Msg));
					}
					//If he does not exits at the data base insert the information to the data base and return success message 
					pstm = sqlConnection.connection.prepareStatement("INSERT INTO members (memberNumber,visitorID,firstName,lastName,phoneNumber,email,familyMembers,cardNumber,CVV,cardOwner,expirationMonth,expirationYear,type) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pstm.setInt(1, Integer.parseInt(info.get(0)));
					pstm.setInt(2, Integer.parseInt(info.get(3)));
					pstm.setString(3, info.get(1));
					pstm.setString(4, info.get(2));
					pstm.setString(5, info.get(4));
					pstm.setString(6, info.get(5));
					pstm.setInt(7, Integer.parseInt(info.get(6)));
					pstm.setString(8, null);
					pstm.setString(9, null);
					pstm.setString(10, null);
					pstm.setInt(11, 0);
					pstm.setInt(12, 0);
					pstm.setString(13, "Member");
					pstm.executeUpdate();	
					String Msg="The member registered successfuly!";
					return new Message(OperationType.MemberRegistration, ClientControllerType.RegistrationController,(Object)Msg);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;	}
		//Case we register a group instructor with credit card
		case GuideRegistrationCC:{
			try {
					//Check if he already registered
					pstm = sqlConnection.connection.prepareStatement("SELECT * from members where memberNumber=?");
					pstm.setString(1, info.get(0));
					ResultSet rs = pstm.executeQuery();
					if(rs.next()) {
						String Msg="The group instructor already registered";
						return new Message(OperationType.GuideRegistrationCC, ClientControllerType.RegistrationController,(Object)(Msg));
					}
					//If he does not exits at the data base insert the information to the data base and return success message 
					pstm = sqlConnection.connection.prepareStatement("INSERT INTO members (memberNumber,visitorID,firstName,lastName,phoneNumber,email,familyMembers,cardNumber,CVV,cardOwner,expirationMonth,expirationYear,type) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pstm.setInt(1, Integer.parseInt(info.get(0)));
					pstm.setInt(2, Integer.parseInt(info.get(3)));
					pstm.setString(3, info.get(1));
					pstm.setString(4, info.get(2));
					pstm.setString(5, info.get(4));
					pstm.setString(6, info.get(5));
					pstm.setInt(7, 0);
					pstm.setString(8, info.get(6));
					pstm.setString(9, info.get(8));
					pstm.setString(10, info.get(7));
					pstm.setInt(11, Integer.parseInt(info.get(9)));
					pstm.setInt(12, Integer.parseInt(info.get(10)));
					pstm.setString(13, "Guide");

					pstm.executeUpdate();
					String Msg="The group guide registered successfuly!";
					return new Message(OperationType.GuideRegistrationCC, ClientControllerType.RegistrationController,(Object)Msg);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;	}
		//Case we register a group instructor without credit card
		case GuideRegistration:{
			try {
					//Check if he already registered
					pstm = sqlConnection.connection.prepareStatement("SELECT * from members where memberNumber=?");
					pstm.setString(1, info.get(0));
					ResultSet rs = pstm.executeQuery();
					if(rs.next()) {
						String Msg="The group guide already registered";
						return new Message(OperationType.GuideRegistration, ClientControllerType.RegistrationController,(Object)(Msg));
					}
					//If he does not exits at the data base insert the information to the data base and return success message 
					pstm = sqlConnection.connection.prepareStatement("INSERT INTO members (memberNumber,visitorID,firstName,lastName,phoneNumber,email,familyMembers,cardNumber,CVV,cardOwner,expirationMonth,expirationYear,type) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pstm.setInt(1, Integer.parseInt(info.get(0)));
					pstm.setInt(2, Integer.parseInt(info.get(3)));
					pstm.setString(3, info.get(1));
					pstm.setString(4, info.get(2));
					pstm.setString(5, info.get(4));
					pstm.setString(6, info.get(5));
					pstm.setInt(7, 0);
					pstm.setString(8, null);
					pstm.setString(9, null);
					pstm.setString(10, null);
					pstm.setInt(11, 0);
					pstm.setInt(12, 0);
					pstm.setString(13, "Guide");
					pstm.executeUpdate();	
					String Msg="The group instructor registered successfuly!";
					return new Message(OperationType.GuideRegistration, ClientControllerType.RegistrationController,(Object)Msg);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;	}
		default:
			break;
		}
		return null;
	
	}
}
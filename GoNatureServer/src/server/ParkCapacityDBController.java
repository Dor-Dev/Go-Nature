package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Park;

/**
 *A class that is responsible for the queries for getting information about the park for sending it to the department manager
 */
public class ParkCapacityDBController {
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;

	public ParkCapacityDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A method for passing messages from the client to the server
	 * 
	 * @param clientMsg - the message that the client sent 
	 */
	public Object parseData(Message clientMsg) {
		PreparedStatement pstm;
		msgFromClient = clientMsg;
		String info = (String) msgFromClient.getObj();
		try {
			//getting the chosen park from the database
			pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
			pstm.setString(1, info);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				//creating a park object with the details received from the database
				Park p = new Park(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(6));
				//sending the information we got back to the client
				return new Message(OperationType.ShowParkCapacity, ClientControllerType.ParkCapacityController, (Object) (p));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Message(OperationType.ShowParkCapacity, ClientControllerType.ParkCapacityController, (Object)"The chosen park doesn't exist");

	}
}
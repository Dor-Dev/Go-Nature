package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.Message;
import enums.ClientControllerType;
import enums.DBControllerType;
import enums.OperationType;
import logic.Park;

public class ParkCapacityDBController {
	private static Message msgFromClient = null;
	private static SqlConnection sqlConnection = null;

	public ParkCapacityDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Object parseData(Message clientMsg) {
		PreparedStatement pstm;
		msgFromClient = clientMsg;
		String info = (String) msgFromClient.getObj();
		try {
			pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
			pstm.setString(1, info);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				Park p = new Park(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(6));
				return new Message(OperationType.ParkInfo, ClientControllerType.ParkCapacityController, (Object) (p));
			}
		} catch (SQLException e) {}
		return new Message(OperationType.ReceiptInfo, ClientControllerType.ParkCapacityController, null);

	}
}
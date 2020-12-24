package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.Order;

public class OrderDBController {

	private static SqlConnection sqlConnection = null;

	public OrderDBController() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Message addOrder(Message clientMsg) {
		PreparedStatement preparedStmt;
		Order newOrder =(Order)clientMsg.getObj();
		System.out.println("Add Order start");
		// the mysql insert statement
		String query = " insert into orders (parkName, arrivalDate, visitorID, paidUp,visitorType,actualNumberOfVisitors,email,status,hourTime,cost)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		try {
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			preparedStmt.setString(1, newOrder.getParkName());
			preparedStmt.setString(2, newOrder.getDateTime());
			preparedStmt.setInt(3, newOrder.getVisitorID());
			preparedStmt.setBoolean(4, newOrder.isPaidUp());
			preparedStmt.setString(5, newOrder.getType());
			preparedStmt.setInt(6, newOrder.getNumOfVisitors());
			preparedStmt.setString(7, newOrder.getEmail());
			preparedStmt.setString(8, newOrder.getStatus());
			preparedStmt.setInt(9, newOrder.getHourTime());
			preparedStmt.setInt(10, newOrder.getCost());
			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Now we will take the orderID from the SQL SERVER (AUTO-INCRESMENT)
		 */
		query = " SELECT MAX(orderID) FROM orders";
		try {
			preparedStmt = sqlConnection.connection.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			if(rs.next())
				newOrder.setOrderID(rs.getInt(1));		//set OrderID to existing order object
		System.out.println(rs.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * return message with the orderID from the SQL
		 */

		return new Message(OperationType.SuccessAddOrder, ClientControllerType.OrderController,(Object)(newOrder));
	}

}

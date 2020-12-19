package server;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
	public Connection connection = null;
	private static SqlConnection sqlConnection;

	/* Singleton constructor */
	private SqlConnection() {
		if (sqlConnection == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.out.println("Driver definition succeed");
			} catch (Exception ex) {
				/* handle the error */
				System.out.println("Driver definition failed");
			}

			try {
				connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/park?serverTimezone=IST", "root",
						"Aa123456");
				System.out.println("SQL connection succeed");

			} catch (SQLException ex) {/* handle any errors */
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		else {
			System.out.println("only one instance of sql connection");
		}
	}

	/* Get the sql connection */
	public static SqlConnection getConnection() {
		if (sqlConnection == null) {
			sqlConnection = new SqlConnection();
		}
		return sqlConnection;
	}
	


	/*
	 * public static Visitor getVisitor(Object msg) { Message sqlMsg = (Message)msg;
	 * 
	 * Visitor v = null ; PreparedStatement stmt;
	 * 
	 * try { stmt = conn.prepareStatement("SELECT * FROM visitor where id=?");
	 * stmt.setString(1,(String)sqlMsg.getMsg()); ResultSet rs =
	 * stmt.executeQuery();
	 * 
	 * while(rs.next()) v = new
	 * Visitor(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.
	 * getString(5));
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); return null; }
	 * 
	 * return v; } public static String editEmail(Object msg) { Message sqlMsg =
	 * (Message)msg; String str = (String)sqlMsg.getMsg(); String[] dataFromCleint =
	 * str.split(" "); if(dataFromCleint[1].trim().isEmpty()) { return null; } try {
	 * 
	 * PreparedStatement updateflight=
	 * conn.prepareStatement("UPDATE visitor SET email =? WHERE id=?");
	 * updateflight.setString(2, (String)dataFromCleint[0]);
	 * updateflight.setString(1, (String)dataFromCleint[1]);
	 * updateflight.executeUpdate(); } catch (SQLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return
	 * (String)dataFromCleint[1];
	 * 
	 * 
	 * 
	 * }
	 */
}

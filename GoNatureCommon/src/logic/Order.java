package logic;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.sun.jmx.snmp.Timestamp;

public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3680648113244743447L;

	private String type;
	private String parkName;
	private String datearrival;
	private int hourTime;
	private int visitorID;
	private int numOfVisitors;
	private String email;
	private boolean paidUp;
	public String status;
	public int cost;
	private Date date;
	private int orderID;

	public Order(String parkName, String dateTime, int visitorID, int numOfVisitors, String email, String type,
			boolean paidUp, int hourTime, int cost) {
		super();
		this.type = type;
		this.parkName = parkName;
		this.datearrival = dateTime;
		this.numOfVisitors = numOfVisitors;
		this.email = email;
		this.paidUp = paidUp;
		this.visitorID = visitorID;
		this.status = "confirm";
		this.hourTime = hourTime;
		this.cost = cost;

	}
	
	
	public Order(int orderID, String parkName, Date date, int hourTime, String email, String visitorType, String status, int visitorNum, int visitorID, boolean paidUp, int price) {
		super();
		this.type = visitorType;
		this.parkName = parkName;
		this.hourTime = hourTime;
		this.visitorID = visitorID;
		this.numOfVisitors = visitorNum;
		this.email = email;
		this.paidUp = paidUp;
		this.status = status;
		this.cost = price;
		this.date = date;
		this.orderID = orderID;
	}


	
	public String getType() {
		return type;
	}

	public String getParkName() {
		return parkName;
	}

	public String getDateTime() {
		return datearrival;
	}

	public int getNumOfVisitors() {
		return numOfVisitors;
	}

	public String getEmail() {
		return email;
	}

	public boolean isPaidUp() {
		return paidUp;
	}

	public int getVisitorID() {
		return visitorID;
	}

	public String getStatus() {
		return status;
	}

	public int getHourTime() {
		return hourTime;
	}

	public int getCost() {
		return cost;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}



	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getOrderID() {
		return orderID;
	}

}

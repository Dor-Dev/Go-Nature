package logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.sun.jmx.snmp.Timestamp;

public class Order implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3680648113244743447L;
	private int orderID;

	private String type;
	private String parkName;
	private String datearrival;
	private int hourTime;
	private int visitorID;
	private int numOfVisitors;
	private String email;
	private boolean paidUp;
	private String status;
	private int cost;
	private String phoneNumber;
	private String msgStatus;
	public Order(String parkName, String dateTime, int visitorID, int numOfVisitors, String email, String type,
			boolean paidUp, int hourTime, int cost,String phoneNumber,String msgStatus,String status) {
		super();
		this.type = type;
		this.parkName = parkName;
		this.datearrival = dateTime;
		this.numOfVisitors = numOfVisitors;
		this.email = email;
		this.paidUp = paidUp;
		this.visitorID = visitorID;
		this.status = status;
		this.hourTime = hourTime;
		this.cost = cost;
		this.phoneNumber = phoneNumber;
		this.msgStatus = msgStatus;

	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getMsgStatus() {
		return msgStatus;
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
	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public String getDatearrival() {
		return datearrival;
	}

	public void setDatearrival(String datearrival) {
		this.datearrival = datearrival;
	}

	public void setHourTime(int hourTime) {
		this.hourTime = hourTime;
	}


}

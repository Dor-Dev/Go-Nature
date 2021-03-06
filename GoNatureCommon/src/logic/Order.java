package logic;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.sun.jmx.snmp.Timestamp;

/**
 * this is a class that represents the order entity
 * @author amit
 *
 */
public class Order implements Serializable {
	

	private static final long serialVersionUID = 3680648113244743447L;

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
	private Date date;
	private int orderID;
	private int discount;
	private String strHour;

	/**
	 * Constructor for myOrders feature
	 * @param parkName
	 * @param hourTime
	 * @param visitorID
	 * @param numOfVisitors
	 * @param status
	 * @param msgStatus
	 * @param date
	 * @param orderID
	 */
	public Order(int orderID, String parkName, Date date, int hourTime, String email, String visitorType, 
			String status, int visitorNum, int visitorID, boolean paidUp, int price,String phoneNumber, String msgStatus,int discount) {
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
		this.discount =discount;
		this.phoneNumber = phoneNumber;
		this.msgStatus = msgStatus;
		this.strHour = hourTime +":00";
	}

	public Order(String parkName, String dateTime, int visitorID, int numOfVisitors, String email, String type,
			boolean paidUp, int hourTime, int cost,String phoneNumber,String msgStatus,String status, int discount) {
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
		this.discount =discount;

	}

	public int getDiscount() {
		return discount;
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
	
	
	public Order(int orderID, String parkName, Date date, int hourTime, String email, String visitorType, 
			String status, int visitorNum, int visitorID, boolean paidUp, int price,int discount) {
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
		this.discount =discount;
	}


	public String getStrHour() {
		return strHour;
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
	
	public void setNumOfVisitors(int numOfVisitors) {
		this.numOfVisitors = numOfVisitors;
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
	public String getDatearrival() {
		return datearrival;
	}

	public void setDatearrival(String datearrival) {
		this.datearrival = datearrival;
	}

	public void setHourTime(int hourTime) {
		this.hourTime = hourTime;
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

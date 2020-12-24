package logic;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Receipt  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -58351231498756744L;
	
	private int receiptID;
	private Date date;
	private Time visitEntry;
	private Time visitExit;
	private int numberOfVisitors;
	private String type;
	private String parkName;
	private int orderNumber;
	private int visiorID;
	
	public Receipt(int receiptID, Date date, Time visitEntry, Time visitExit, int numberOfVisitors, String type,
			String parkName, int orderNumber, int visiorID) {
		super();
		this.receiptID = receiptID;
		this.date = date;
		this.visitEntry = visitEntry;
		this.visitExit = visitExit;
		this.numberOfVisitors = numberOfVisitors;
		this.type = type;
		this.parkName = parkName;
		this.orderNumber = orderNumber;
		this.visiorID = visiorID;
	}

	public int getReceiptID() {
		return receiptID;
	}

	public void setReceiptID(int receiptID) {
		this.receiptID = receiptID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getVisitEntry() {
		return visitEntry;
	}

	public void setVisitEntry(Time visitEntry) {
		this.visitEntry = visitEntry;
	}

	public Time getVisitExit() {
		return visitExit;
	}

	public void setVisitExit(Time visitExit) {
		this.visitExit = visitExit;
	}

	public int getNumberOfVisitors() {
		return numberOfVisitors;
	}

	public void setNumberOfVisitors(int numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getVisiorID() {
		return visiorID;
	}

	public void setVisiorID(int visiorID) {
		this.visiorID = visiorID;
	}

	


	
	

}

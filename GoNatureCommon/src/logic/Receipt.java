package logic;

import java.io.Serializable;
import java.sql.Date;

public class Receipt  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -58351231498756744L;
	
	private int receiptID;
	private Date date;
	private int visitEntry;
	private int visitExit;
	private int numberOfVisitors;
	private String type;
	private String parkName;
	private int orderNumber;
	private int visiorID;
	private int cost;
	private int  discount;
	
	public Receipt(int receiptID, Date date, int visitEntry, int visitExit, int numberOfVisitors, String type,
			String parkName, int orderNumber, int visiorID,int cost) {
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
		this.cost=cost;
	}
	
	public Receipt(int receiptID,  int numberOfVisitors, String parkName, int orderNumber, int cost , int discount) {
		this.receiptID = receiptID;
		this.numberOfVisitors = numberOfVisitors;
		this.parkName = parkName;
		this.orderNumber = orderNumber;
		this.cost=cost;
		this.discount = discount;
	}
	public int getDiscount() {
		return discount;
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

	public int getVisitEntry() {
		return visitEntry;
	}

	public void setVisitEntry(int visitEntry) {
		this.visitEntry = visitEntry;
	}

	public int getVisitExit() {
		return visitExit;
	}

	public void setVisitExit(int visitExit) {
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

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	


	
	

}

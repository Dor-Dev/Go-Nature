package logic;

import java.io.Serializable;
import java.sql.Date;

/**
 * Class of Event, created for transferring the event data from server to client
 * and for the other way.<br>
 * The event data is used in the tables .
 * 
 * @author dorswisa
 *
 */
public class Event implements Serializable {

	private int requestNum;
	private static final long serialVersionUID = 6561551454070706015L;
	private String parkName;
	private String eventName;
	private Date startDate;
	private Date endDate;
	private int discount;
	private String status;

	/**
	 * @param parkName
	 * @param eventName
	 * @param startDate
	 * @param endDate
	 * @param discount
	 * @param status
	 */
	public Event(String parkName, String eventName, Date startDate, Date endDate, int discount, String status) {
		super();
		this.parkName = parkName;
		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discount = discount;
		this.status = status;
	}
	/**
	 * 
	 * @param eventName
	 * @param startDate
	 * @param endDate
	 * @param discount
	 * @param status
	 */
	public Event(String eventName, Date startDate, Date endDate, int discount, String status) {
		super();
		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discount = discount;
		this.status = status;
	}

	/**
	 * @param requestNum 
	 * @param parkName
	 * @param eventName
	 * @param startDate
	 * @param endDate
	 * @param discount
	 * @param status
	 */
	public Event(int requestNum, String parkName, String eventName, Date startDate, Date endDate, int discount,
			String status) {
		super();
		this.requestNum = requestNum;
		this.parkName = parkName;
		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discount = discount;
		this.status = status;
	}
	/**
	 * @return the requestNum
	 */
	public int getRequestNum() {
		return requestNum;
	}
	/**
	 * @return the parkName
	 */
	public String getParkName() {
		return parkName;
	}
	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @return the discount
	 */
	public int getDiscount() {
		return discount;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param requestNum the requestNum to set
	 */
	public void setRequestNum(int requestNum) {
		this.requestNum = requestNum;
	}
	/**
	 * @param parkName the parkName to set
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


}

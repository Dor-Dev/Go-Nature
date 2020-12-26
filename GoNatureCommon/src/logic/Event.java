package logic;

import java.io.Serializable;
import java.sql.Date;

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6561551454070706015L;
	private String parkName;
	private String eventName;
	private Date startDate;
	private Date endDate;
	private int discount;
	private String status;

	

	public String getParkName() {
		return parkName;
	}

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
	public Event(String eventName, Date startDate, Date endDate, int discount) {
		super();
		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discount = discount;
	}

	public String getEventName() {
		return eventName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getDiscount() {
		return discount;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

}

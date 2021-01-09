package logic;

import java.io.Serializable;
/**
 * Class of Update, created for transferring the update request data from server to client
 * and for the other way.<br>
 * The update data is used in the tables of the requests.
 * @author dorswisa
 */
public class Update implements Serializable{
	
	private static final long serialVersionUID = -8423423767946780101L;
	private int requestNum;
	private String parkName;
	private int capacity;
	private int difference;
	private int visitingTime;
	private String status = null;
	
	/**
	 * @param requestNum
	 * @param parkName
	 * @param capacity
	 * @param difference
	 * @param visitingTime
	 * @param status
	 */
	public Update(int requestNum, String parkName, int capacity, int difference, int visitingTime, String status) {
		super();
		this.requestNum = requestNum;
		this.parkName = parkName;
		this.capacity = capacity;
		this.difference = difference;
		this.visitingTime = visitingTime;
		this.status = status;
	}
	/**
	 * @param parkName
	 * @param capacity
	 * @param difference
	 * @param visitingTime
	 * @param status
	 */
	public Update(String parkName, int capacity, int difference, int visitingTime, String status) {
		super();
		this.parkName = parkName;
		this.capacity = capacity;
		this.difference = difference;
		this.visitingTime = visitingTime;
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
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	/**
	 * @return the difference
	 */
	public int getDifference() {
		return difference;
	}
	/**
	 * @return the visitingTime
	 */
	public int getVisitingTime() {
		return visitingTime;
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
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/**
	 * @param difference the difference to set
	 */
	public void setDifference(int difference) {
		this.difference = difference;
	}
	/**
	 * @param visitingTime the visitingTime to set
	 */
	public void setVisitingTime(int visitingTime) {
		this.visitingTime = visitingTime;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}

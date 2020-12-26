package logic;

import java.io.Serializable;

public class Update implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8423423767946780101L;
	private String parkName;
	private int capacity;
	private int difference;
	private int visitingTime;
	private String status = null;
	
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
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getDifference() {
		return difference;
	}
	public int getVisitingTime() {
		return visitingTime;
	}
	public String getStatus() {
		return status;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public void setDifference(int difference) {
		this.difference = difference;
	}
	public void setVisitingTime(int visitingTime) {
		this.visitingTime = visitingTime;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

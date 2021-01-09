package logic;

import java.io.Serializable;

/**
 * this is a class that represents the park entity
 * @author amit
 *
 */
public class Park implements Serializable {

	private static final long serialVersionUID = -3062676248741754919L;
	
	private String parkName=null;
	private int parkCapacity=0;
	private int orderCapacity=0;
	private int difference=0;
	private int visitingTime;
	private int currentAmountOfVisitors=0;
	
	public Park(String parkName, int parkCapacity, int orderCapacity, int difference, int visitingTime,int currentAmountOfVisitors) {
		this.parkName = parkName;
		this.parkCapacity = parkCapacity;
		this.orderCapacity = orderCapacity;
		this.difference = difference;
		this.visitingTime = visitingTime;
		this.currentAmountOfVisitors = currentAmountOfVisitors;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public int getParkCapacity() {
		return parkCapacity;
	}

	public void setParkCapacity(int parkCapacity) {
		this.parkCapacity = parkCapacity;
	}

	public int getOrderCapacity() {
		return orderCapacity;
	}

	public void setOrderCapacity(int orderCapacity) {
		this.orderCapacity = orderCapacity;
	}

	public int getDifference() {
		return difference;
	}

	public void setDifference(int difference) {
		this.difference = difference;
	}

	public int getVisitingTime() {
		return visitingTime;
	}

	public void setVisitingTime(int visitingTime) {
		this.visitingTime = visitingTime;
	}

	public int getCurrentAmountOfVisitors() {
		return currentAmountOfVisitors;
	}

	public void setCurrentAmountOfVisitors(int currentAmountOfVisitors) {
		this.currentAmountOfVisitors = currentAmountOfVisitors;
	}
	
	@Override
	public String toString() {
		return "Park [parkName=" + parkName + ", parkCapacity=" + parkCapacity + ", orderCapacity=" + orderCapacity
				+ ", difference=" + difference + ", visitingTime=" + visitingTime + ", currentAmountOfVisitors="
				+ currentAmountOfVisitors + "]";
	}

	

}

package logic;

import java.io.Serializable;

public class VisitingReport implements Report , Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3721160153649677017L;
	private String parkName;
	private String date;
	private String type;
	private int [] hours;
	private int [] amountOfVisitors;
	
	public VisitingReport(String parkName, String date, String type, int[] hours, int[] amountOfVisitors) {
		super();
		this.parkName = parkName;
		this.date = date;
		this.type = type;
		this.hours = hours;
		this.amountOfVisitors = amountOfVisitors;
	}

	

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int[] getHours() {
		return hours;
	}


	public void setHours(int[] hours) {
		this.hours = hours;
	}


	public int[] getAmountOfVisitors() {
		return amountOfVisitors;
	}


	public void setAmountOfVisitors(int[] amountOfVisitors) {
		this.amountOfVisitors = amountOfVisitors;
	}


	public void setParkName(String parkName) {
		this.parkName = parkName;
	}




	@Override
	public String getParkName() {
		return parkName;
			
	}

}

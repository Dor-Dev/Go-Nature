package logic;

import java.io.Serializable;

/**
 * This is a class that keeps the data of visiting report
 * @author amit
 *
 */
public class VisitingReport implements Report , Serializable{

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

	/**
	 * @return the parkName
	 */
	@Override
	public String getParkName() {
		return parkName;
	}

	/**
	 * @param parkName the parkName to set
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the hours
	 */
	public int[] getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(int[] hours) {
		this.hours = hours;
	}

	/**
	 * @return the amountOfVisitors
	 */
	public int[] getAmountOfVisitors() {
		return amountOfVisitors;
	}

	/**
	 * @param amountOfVisitors the amountOfVisitors to set
	 */
	public void setAmountOfVisitors(int[] amountOfVisitors) {
		this.amountOfVisitors = amountOfVisitors;
	}

	



}

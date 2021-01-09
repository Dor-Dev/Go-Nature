package logic;

import java.io.Serializable;
import java.sql.Date;

/**
 * A class that represents the usage report with all of it's data that is received from the data base
 */
public class UsageReport implements Report,Serializable {

	
	private static final long serialVersionUID = 5875569985914103432L;
	private Park park;
	private String month;
	private String year;
	private Date[] day;
	private int[] numOfVisitorsInDay;
	
	public UsageReport(Park p, String month, String year, Date[] day, int[] numOfVisitorsInDay) {
		super();
		this.park= p;
		this.month = month;
		this.year = year;
		this.day = day;
		this.numOfVisitorsInDay = numOfVisitorsInDay;
	}


	/**
	 * 
	 * @return the park of the usage report
	 */
	public Park getPark() {
		return park;
	}

	/**
	 * 
	 * @return an array of the days as a calander
	 */
	public Date[] getDay() {
		return day;
	}

	/**
	 * 
	 * @return an array of the amount of visitor for each day
	 */
	public int[] getNumOfVisitorsInDay() {
		return numOfVisitorsInDay;
	}

	/**
	 * 
	 * @return the month of the usage report
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * 
	 * @return the year of the usage report
	 */
	public String getYear() {
		return year;
	}


	/**
	 * 
	 * @return an array with the days that didn't
	 * reach the capacity of the park
	 */
	public Date[] getUnFullDays() {
		for(int i=0;i<31;i++) {
			if(!(numOfVisitorsInDay[i]<park.getParkCapacity()))	
				day[i]=null;
		}
		return day;
		
	}

	/**
	 * @return the park name 
	 * this is an implemented method from the report interface
	 */
	@Override
	public String getParkName() {
		return park.getParkName();
	}
}

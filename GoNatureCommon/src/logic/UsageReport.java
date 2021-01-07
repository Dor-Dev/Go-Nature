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


	public Park getPark() {
		return park;
	}

	
	public Date[] getDay() {
		return day;
	}

	public int[] getNumOfVisitorsInDay() {
		return numOfVisitorsInDay;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}


	
	public Date[] getUnFullDays() {
		for(int i=0;i<31;i++) {
			if(!(numOfVisitorsInDay[i]<park.getParkCapacity()))	
				day[i]=null;
		}
		return day;
		
	}


	@Override
	public String getParkName() {
		return park.getParkName();
	}
}

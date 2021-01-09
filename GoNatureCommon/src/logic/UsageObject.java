package logic;

import java.sql.Date;

/*
 * A class created in order to insert information about the usage report to a table in the displayed screen
 * each variable represents a column 
 */
public class UsageObject {

	private int numOfVIsitors;
	private Date date;
	

	public UsageObject(int numOfVIsitors, Date date) {
		super();
		this.numOfVIsitors = numOfVIsitors;
		this.date = date;
	}


	/**
	 * 
	 * @return the amount of visitors 
	 */
	public int getNumOfVIsitors() {
		return numOfVIsitors;
	}

	/**
	 * 
	 * @return the date of the cancellation report
	 */
	public Date getDate() {
		return date;
	}


}

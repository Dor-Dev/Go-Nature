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


	public int getNumOfVIsitors() {
		return numOfVIsitors;
	}

	public void setNumOfVIsitors(int numOfVIsitors) {
		this.numOfVIsitors = numOfVIsitors;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}

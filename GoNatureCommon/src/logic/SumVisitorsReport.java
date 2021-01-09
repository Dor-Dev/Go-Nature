package logic;

import java.io.Serializable;

/**
 * This is a class that keeps the data of overall report
 * @author amit
 *
 */
public class SumVisitorsReport implements Report , Serializable {

	private static final long serialVersionUID = -8702333225260971786L;
	
	private String parkName;
	private String month;
	private int visitorsAmount;
	private int membersAmount;
	private int groupsAmount;
	private String year;
	
	public SumVisitorsReport(String parkName, String month,String year, int visitorsAmount, int membersAmount, int groupsAmount) {
		super();
		this.parkName = parkName;
		this.month = month;
		this.year=year;
		this.visitorsAmount = visitorsAmount;
		this.membersAmount = membersAmount;
		this.groupsAmount= groupsAmount;
	}
	

	public String getParkName() {
		return parkName;
	}


	public void setParkName(String parkName) {
		this.parkName = parkName;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public int getVisitorsAmount() {
		return visitorsAmount;
	}


	public void setVisitorsAmount(int visitorsAmount) {
		this.visitorsAmount = visitorsAmount;
	}


	public int getMembersAmount() {
		return membersAmount;
	}


	public void setMembersAmount(int membersAmount) {
		this.membersAmount = membersAmount;
	}


	public int getGroupsAmount() {
		return groupsAmount;
	}

	public void setGroupsAmount(int groupsAmount) {
		this.groupsAmount = groupsAmount;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}

}

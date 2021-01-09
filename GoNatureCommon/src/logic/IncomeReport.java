package logic;

import java.io.Serializable;
import java.sql.Date;

/**
 * This is a class that keeps the data of income report
 * @author amit
 *
 */
public class IncomeReport implements Report , Serializable {

	private static final long serialVersionUID = -3658862159815663953L;

	private String parkName;
	private String month;
	private String year;
	private Date [] day;
	private int [] moneyInDay;
	private int totalIncome;
	
	
	
	public IncomeReport(String parkName, String month, String year, Date[] day, int[] moneyInDay , int totalInCome) {
		super();
		this.parkName = parkName;
		this.month = month;
		this.year = year;
		this.day = day;
		this.moneyInDay = moneyInDay;
		this.totalIncome=totalInCome;
		
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



	public void setYear(String year) {
		this.year = year;
	}



	public Date[] getDay() {
		return day;
	}



	public void setDay(Date[] day) {
		this.day = day;
	}



	public int[] getMoneyInDay() {
		return moneyInDay;
	}



	public void setMoneyInDay(int[] moneyInDay) {
		this.moneyInDay = moneyInDay;
	}



	public void setParkName(String parkName) {
		this.parkName = parkName;
	}



	@Override
		public String getParkName() {
		return parkName;
	}



	public int getTotalIncome() {
		return totalIncome;
	}



	public void setTotalIncome(int totalIncome) {
		this.totalIncome = totalIncome;
	}

}

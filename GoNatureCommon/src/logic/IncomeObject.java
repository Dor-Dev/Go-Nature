package logic;

import java.sql.Date;

/**
 * This is a class that keeps the data that we needs for income report table
 * @author amit
 *
 */

public  class IncomeObject {

	private   int  money;
	private   Date  day;
	
	
	
	public IncomeObject(int money, Date day) {
		super();
		this.money = money;
		this.day = day;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money=money;
	}
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	
	
}

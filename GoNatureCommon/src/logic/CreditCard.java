package logic;

import java.io.Serializable;

/**
 * 
 * This class have the details of traveler credit card,
 * credit card is optional for travelers.
 * @author Naor0
 *
 */

public class CreditCard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3762390995657450100L;
	private String cardNumber;
	private String cvv;
	private int monthExp;
	private int yearExp;
	private String cardHolderName;
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public CreditCard(String cardNumber, String cvv, int monthExp, int yearExp, String cardHolderName) {
		super();
		this.cardNumber = cardNumber;
		this.cvv = cvv;
		this.monthExp = monthExp;
		this.yearExp = yearExp;
		this.cardHolderName = cardHolderName;
	}
	public int getMonthExp() {
		return monthExp;
	}
	public void setMonthExp(int monthExp) {
		this.monthExp = monthExp;
	}
	public int getYearExp() {
		return yearExp;
	}
	public void setYearExp(int yearExp) {
		this.yearExp = yearExp;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

}

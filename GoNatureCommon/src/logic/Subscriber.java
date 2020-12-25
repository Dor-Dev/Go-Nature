package logic;

import java.io.Serializable;

public class Subscriber implements Serializable{
	/**
	 * 
	 */

	private static final long serialVersionUID = -416960327558987383L;
	private int ID;
	private int visitorID;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private int familySum; // The total number of family members
	private CreditCard creditCard=null;
	private String type;

	
	public Subscriber(int ID,int visitorID, String firstName, String lastName, String phone, String email, int familySum,String type) {
		this.ID = ID;
		this.visitorID=visitorID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.familySum = familySum;
		this.type=type;
	}


	public Subscriber(int ID, String firstName, String lastName, String phone, String email, int familySum,String type) {
		this.ID = ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;			
		this.email = email;
		this.familySum = familySum;
		this.type=type;
	}
	
	
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getFamilySum() {
		return familySum;
	}
	public void setFamilySum(int familySum) {
		this.familySum = familySum;
	}
	public int getID() {
		return ID;
	}


	public void setID(int ID) {
		this.ID = ID;
	}
		
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	
	public int getVisitorID() {
		return visitorID;
	}


}

package logic;

import java.io.Serializable;

public class Subscriber implements Serializable{

	private static final long serialVersionUID = -416960327558987383L;
	private int ID;
	private int visitorID;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private int familySum; // The total number of family members
	private CreditCard creditCard;
	private String type;

	/**
	 * 
	 * @param ID
	 * @param visitorID
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param email
	 * @param familySum
	 * @param type
	 */
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
	/**
	 * 
	 * @param ID
	 * @param visitorID
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param email
	 * @param familySum
	 * @param type
	 * @param creditCard
	 */
	public Subscriber(int ID,int visitorID, String firstName, String lastName, String phone, String email, int familySum,String type,CreditCard creditCard) {
		this.ID = ID;
		this.visitorID=visitorID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.familySum = familySum;
		this.type=type;
		this.creditCard = creditCard;
	}

	/**
	 * 
	 * @param ID
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param email
	 * @param familySum
	 * @param type
	 */
	public Subscriber(int ID, String firstName, String lastName, String phone, String email, int familySum,String type) {
		this.ID = ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;			
		this.email = email;
		this.familySum = familySum;
		this.type=type;
	}
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}
	/**
	 * @return the visitorID
	 */
	public int getVisitorID() {
		return visitorID;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the familySum
	 */
	public int getFamilySum() {
		return familySum;
	}
	/**
	 * @return the creditCard
	 */
	public CreditCard getCreditCard() {
		return creditCard;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * @param visitorID the visitorID to set
	 */
	public void setVisitorID(int visitorID) {
		this.visitorID = visitorID;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param familySum the familySum to set
	 */
	public void setFamilySum(int familySum) {
		this.familySum = familySum;
	}
	/**
	 * @param creditCard the creditCard to set
	 */
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}



}

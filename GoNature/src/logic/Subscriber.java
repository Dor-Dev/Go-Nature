package logic;

public class Subscriber {
	private int ID;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private int familySum; // The total number of family members
	private CreditCard creditCard;
	public Subscriber(int iD, String firstName, String lastName, String phone, String email, int familySum) {
		super();
		ID = iD;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;			//didnt use credit card!!!
		this.email = email;
		this.familySum = familySum;
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
	
	
	
	

}

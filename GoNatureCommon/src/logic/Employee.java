package logic;

import java.io.Serializable;
/**
 * Class to save Employee data.
 * @author dorswisa
 *
 */
public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1339062107563368430L;
	
	private int employeeID;
	private String firstName;
	private String lasttName;
	private String email;
	private String role;
	private String organizationAffilation; //which park the employee 
	private String userName;
	private String password;

	public Employee(int id,String firstName,String lasttName,String email, String role,String organizationAffilation,String userName,String password) {
		this.employeeID=id;
		this.firstName=firstName;
		this.lasttName=lasttName;
		this.email=email;
		this.role=role;
		this.organizationAffilation=organizationAffilation;
		this.userName=userName;
		this.password=password;
	}

	/**
	 * @return the employeeID
	 */
	public int getEmployeeID() {
		return employeeID;
	}




	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}




	/**
	 * @return the lasttName
	 */
	public String getLasttName() {
		return lasttName;
	}




	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}




	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}




	/**
	 * @return the organizationAffilation
	 */
	public String getOrganizationAffilation() {
		return organizationAffilation;
	}




	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}




	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}




	@Override
	public String toString() {
		return employeeID+ " "+ firstName+ " "+lasttName+ " "+ role+ " "+ organizationAffilation;
		
	}
	
	
}

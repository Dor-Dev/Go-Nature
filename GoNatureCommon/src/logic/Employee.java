package logic;

import java.io.Serializable;

public class Employee implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1339062107563368430L;
	/**
	 * 
	 */
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


	public int getEmployeeID() {
		return employeeID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLasttName() {
		return lasttName;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

	public String getOrganizationAffilation() {
		return organizationAffilation;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	
	@Override
	public String toString() {
		return employeeID+ " "+ firstName+ " "+lasttName+ " "+ role+ " "+ organizationAffilation;
		
	}
	
	
}

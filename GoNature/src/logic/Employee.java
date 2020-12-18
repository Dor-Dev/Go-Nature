package logic;

public class Employee {
	private String employeeID;
	private String firstName;
	private String lasttName;
	private String email;
	private String role;
	private String organizationAffilation;
	private String userName;
	private String password;

	public Employee(String employeeID,String firstName,String lasttName,String email, String role,String organizationAffilation,String userName,String password) {
		this.employeeID=employeeID;
		this.firstName=firstName;
		this.lasttName=lasttName;
		this.email=email;
		this.role=role;
		this.organizationAffilation=organizationAffilation;
		this.userName=userName;
		this.password=password;
	}


	public String getEmployeeID() {
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

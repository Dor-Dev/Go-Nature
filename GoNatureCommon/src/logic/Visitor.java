package logic;

import java.io.Serializable;

public class Visitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4562947978320490571L;
	private int id;
	private String firstName;
	private String surName;
	private boolean isMember;
	private String type;
	/**
	 * @param id
	 * @param firstName
	 * @param surName
	 * @param isMember
	 * @param type
	 */
	public Visitor(int id, String firstName, String surName, boolean isMember, String type) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.surName = surName;
		this.isMember = isMember;
		this.type = type;
	}
	public Visitor(int id) {
		this.id = id;
		this.isMember = false;
		this.type = "visitor";
	}
	
	
	public int getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getSurName() {
		return surName;
	}
	public boolean isMember() {
		return isMember;
	}
	public String getType() {
		return type;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}
	public void setType(String type) {
		this.type = type;
	}
}

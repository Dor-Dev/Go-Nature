package logic;

import java.io.Serializable;

public class CardReaderRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1256722388343686888L;
	private String parkName;
	private int visitorID;
	private int visitorAmount;
	
	public CardReaderRequest(String parkName, int visitorID, int visitorAmount) {
		super();
		this.parkName = parkName;
		this.visitorID = visitorID;
		this.visitorAmount = visitorAmount;
	}

	public String getParkName() {
		return parkName;
	}

	public int getVisitorID() {
		return visitorID;
	}

	public int getVisitorAmount() {
		return visitorAmount;
	}
	
}

package logic;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

public class OrderRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7334228383055411749L;
	LocalDate askedDate;
	int askedHour;
	int numOfvisitorAsked;
	
	public OrderRequest(LocalDate askdate, int hour, int numOfvisitorAsked) {
		super();
		this.askedDate = askdate;
		askedHour = hour;
		this.numOfvisitorAsked = numOfvisitorAsked;
	}
	public OrderRequest(LocalDate askdate, int numOfvisitorAsked) {
		super();
		this.askedDate = askdate;
		this.numOfvisitorAsked = numOfvisitorAsked;
	}
	
	public LocalDate getAskdate() {
		return askedDate;
	}
	public void setAskdate(LocalDate askdate) {
		this.askedDate = askdate;
	}
	public int getHour() {
		return askedHour;
	}
	public void setHour(int hour) {
		askedHour = hour;
	}
	public int getNumOfvisitorAsked() {
		return numOfvisitorAsked;
	}
	
	

}

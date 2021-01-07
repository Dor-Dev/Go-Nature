package logic;

import java.io.Serializable;

/*
 * A class that represents a cancellation report with all of the data received from the database
 */
public class CancellationReport implements Report, Serializable {

	private static final long serialVersionUID = -8530739569402493556L;

	private String parkName;
	private String date;
	private int unfulfilledOrderAmount;
	private int unfulfilledVisitorAmount;
	private int totalOrderAmount;
	private int canceledOrdersCounter;
	private int canceledVisitorsAmount;
	
	public CancellationReport(String parkName, String date,
			int totalOrderAmount, int canceledOrdersCounter,int canceledVisitorsAmount, int unfulfilledOrderAmount, int unfulfilledVisitorAmount) {
		super();
		this.parkName = parkName;
		this.date = date;
		this.unfulfilledOrderAmount = unfulfilledOrderAmount;
		this.unfulfilledVisitorAmount = unfulfilledVisitorAmount;
		this.totalOrderAmount = totalOrderAmount;
		this.canceledOrdersCounter = canceledOrdersCounter;
		this.canceledVisitorsAmount=canceledVisitorsAmount;
	}
	
	
	public String getDate() {
		return date;
	}


	public int getUnfulfilledOrderAmount() {
		return unfulfilledOrderAmount;
	}


	public int getUnfulfilledVisitorAmount() {
		return unfulfilledVisitorAmount;
	}


	public int getTotalOrderAmount() {
		return totalOrderAmount;
	}


	public int getCanceledOrdersCounter() {
		return canceledOrdersCounter;
	}



	@Override
	public String getParkName() {

		return parkName;
	}

	

	public int getCanceledVisitorsAmount() {
		return canceledVisitorsAmount;
	}


}

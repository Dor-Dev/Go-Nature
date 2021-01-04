package logic;

import java.io.Serializable;

public class CancellationReport implements Report, Serializable {

	

	/**
	 * 
	 */
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
		this.setCanceledVisitorsAmount(canceledVisitorsAmount);
	}
	
	
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getUnfulfilledOrderAmount() {
		return unfulfilledOrderAmount;
	}


	public void setUnfulfilledOrderAmount(int unfulfilledOrderAmount) {
		this.unfulfilledOrderAmount = unfulfilledOrderAmount;
	}


	public int getUnfulfilledVisitorAmount() {
		return unfulfilledVisitorAmount;
	}


	public void setUnfulfilledVisitorAmount(int unfulfilledVisitorAmount) {
		this.unfulfilledVisitorAmount = unfulfilledVisitorAmount;
	}


	public int getTotalOrderAmount() {
		return totalOrderAmount;
	}


	public void setTotalOrderAmount(int totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}


	public int getCanceledOrdersCounter() {
		return canceledOrdersCounter;
	}


	public void setCanceledOrdersCounter(int canceledOrdersCounter) {
		this.canceledOrdersCounter = canceledOrdersCounter;
	}


	


	public void setParkName(String parkName) {
		this.parkName = parkName;
	}


	@Override
	public String getParkName() {

		return parkName;
	}

	

	public int getCanceledVisitorsAmount() {
		return canceledVisitorsAmount;
	}


	public void setCanceledVisitorsAmount(int canceledVisitorsAmount) {
		this.canceledVisitorsAmount = canceledVisitorsAmount;
	}

}

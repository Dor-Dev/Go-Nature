package logic;

import java.io.Serializable;

/**
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
	
	/**
	 * 
	 * @return the date of the cancellation report
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * 
	 * @return the amount of the unfulfilled orders
	 */
	public int getUnfulfilledOrderAmount() {
		return unfulfilledOrderAmount;
	}

	/**
	 * 
	 * @return the amount of visitors with the unfulfilled orders
	 */
	public int getUnfulfilledVisitorAmount() {
		return unfulfilledVisitorAmount;
	}

	/**
	 * 
	 * @return the total amount of orders
	 */
	public int getTotalOrderAmount() {
		return totalOrderAmount;
	}

	
	/**
	 * 
	 * @return the amount of canceled orders
	 */
	public int getCanceledOrdersCounter() {
		return canceledOrdersCounter;
	}


	
	/**
	 * @return the park name 
	 * this is an implemented method from the report interface
	 */
	@Override
	public String getParkName() {

		return parkName;
	}

	
	/**
	 * 
	 * @return the amount of visitors with canceled order
	 */
	public int getCanceledVisitorsAmount() {
		return canceledVisitorsAmount;
	}


}

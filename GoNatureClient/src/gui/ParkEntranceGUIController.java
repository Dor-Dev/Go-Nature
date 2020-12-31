package gui;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import client.MainClient;
import client.OrderController;
import common.Message;
import controllers.EmployeeController;
import controllers.ParkController;

import controllers.RestartApp;
import controllers.ReceiptController;

import controllers.VisitorController;
import enums.DBControllerType;
import enums.Discount;
import enums.OperationType;
import enums.UserTypes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ParkEntranceGUIController {

	

    @FXML
    private Button btnOkPopUp;
	@FXML
	private Label lblManualReceiptID;

	@FXML
	private Label lblManualReceiptParkName;

	@FXML
	private Label lblManualReceiptNumOfVisitors;

	@FXML
	private Label lblManualReceiptDiscount;

	@FXML
	private Label lblManualReceiptCost;

	@FXML
	private Label lblOrderReceiptID;
	
	@FXML
    private Label lblOrderReceiptCost;


	@FXML
	private Label lblOrderReceiptParkName;

	@FXML
	private Label lblOrderReceiptOrderID;

	@FXML
	private Label lblOrderReceiptNumOfVisitors;

	@FXML
	private Label lblOrderReceiptDiscount;
	 @FXML
	    private Button btnOKorderReceipt;
	 
	    @FXML
	    private Button btnOKmanualReceipt;

	@FXML
	private Label mnuAddOrder;

	@FXML
	private Label mnuMyOrders;

	@FXML
	private Label mnuMyProfile;

	@FXML
	private Label mnuParkEntrance;

	@FXML
	private Label mnuRegistration;

	@FXML
	private Label mnuReportsManager;

	@FXML
	private Label mnuEvents;

	@FXML
	private Label mnuParkDetails;

	@FXML
	private Label mnuParkCapacity;

	@FXML
	private Label mnuReportsDepartment;

	@FXML
	private Label mnuRequests;

	@FXML
	private Label lblParkName;

	@FXML
	private Label lblCurrentAmountInPark;

	@FXML
	private Label lblTravelerCanEnter;

	@FXML
	private TextField txtReceiptID;

	@FXML
	private TextField txtExitAmount;

	@FXML
	private TextField txtOrderNumber;

	@FXML
	private TextField txtActualAmount;

	@FXML
	private TextField txtTravelerID;

	@FXML
	private TextField txtAmountOfOccasional;


	@FXML
    private Label mnuLogout;
	
	  @FXML
	    void goToMainPage(MouseEvent event) {
		  RestartApp.restartParameters();
		  LoginGUIController login = new LoginGUIController();
		  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		  login.show();
	    }
	  
   /* @FXML
    private ComboBox<String> cmbTypesOccasional;
    */


    @FXML
    private Label lblTitle;

    @FXML
    private Label lblMsg;
    
    private static String visitorID ;
    private static String type = null;
    private static int cost;


    /**
     * this method decrease the amount of visitors in the park after they exit
     * @param event
     */

    
	private static LocalDate thisDay;
	private static Date thisDayToDB;
	private static LocalTime thisTime;
	private static Time thisTimeToDB;
	private static int hours;
	private static int minutes;
	private final int openEntrance = 10;
	private final int closeEntrance = 17;
	
	/**
	 * this function taking the date and time of now
	 */
	private static void getCurrentTime() {

		thisDay = LocalDate.now();
		thisDayToDB = Date.valueOf(thisDay);
		thisTime = LocalTime.now();
		thisTimeToDB = Time.valueOf(thisTime);
		hours = thisTime.getHour();
		minutes = thisTime.getMinute();
		if (minutes > 0) {
			hours += 1;
	
		}
	}
	
	@FXML
	void decreaseAmountOfVisitors(MouseEvent event) {
		List<String> list = new ArrayList<>();
		String receiptID = this.txtReceiptID.getText();
		String AmountOfTravelers = this.txtExitAmount.getText();

		// System.out.println(hour);
		list.add(receiptID);
		list.add(ParkController.parkConnected.getParkName());
		list.add(AmountOfTravelers);
		
		MainClient.clientConsole.accept(new Message(OperationType.UpdateCurrAmountOfVisitors, DBControllerType.ParkDBController, (Object)ParkController.parkConnected.getParkName() ));
		setDataOfPark(this);

		MainClient.clientConsole.accept(new Message(OperationType.UpdateReceiptInfoAfterExit,DBControllerType.ReceiptDBController, (Object) list));

		if (ReceiptController.receiptType.equals(OperationType.SuccessUpdateReceipt)) {

			list.remove(0);
			MainClient.clientConsole.accept(
					new Message(OperationType.DecreaseParkVistiors, DBControllerType.ParkDBController, (Object) list));

			if (ParkController.Parktype.equals(OperationType.UpdateParkInfo))
				setDataOfPark(this);
		} else {
			showPopUp(this,"Failed to update!","invalid receipt number/ amount of visitor");
		}

	}

	/**
	 * this method activate a popUp with the relevant data
	 * @param parkEntranceGUIController
	 * @param string
	 * @param string2
	 */
	private void showPopUp(ParkEntranceGUIController parkEntranceGUIController, String string, String string2) {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("UpdateCapacityInEntryFailed.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("PopUp");
			ParkEntranceGUIController pe = loader.getController();
			pe.lblTitle.setText(string);
			pe.lblMsg.setText(string2);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mp = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mp.show();

	}

	/**
	 * this method show the order receipt popUp after the entry employee put the data 
	 * @param event
	 */
	@FXML
	void showOrderReceipt(MouseEvent event) {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("OrderReceiptPopupGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Order Receipt");
			ParkEntranceGUIController parkEntranceController1 = loader.getController();

			parkEntranceController1.lblOrderReceiptParkName.setText(ParkController.parkConnected.getParkName());
			String orderNumber = this.txtOrderNumber.getText();
			String actualAmount = this.txtActualAmount.getText();
			List<String> list = new ArrayList<>();
			list.add(orderNumber);
			list.add(actualAmount);
			list.add(ParkController.parkConnected.getParkName());
			
			//check if the park is open for entry
			if(!checkHoursForEntry(this))
				return;
			
			
			//update the visitor that need to exit before
			if(!UpdateCurrAmountOfVisitors(this,Integer.parseInt(actualAmount)))
				return;
			
		
			if(!checkOrderInfo(this,list))
				return;
			
	
			//check if the order is  valid
			if (ParkController.ordertype.equals(OperationType.GetOrderInfo)) {
				
				if(!checkReceiptInfo(this,list,actualAmount))
					return;
		
			
					list.removeAll(list);
					list.add(ParkController.parkConnected.getParkName());
					list.add(actualAmount);
					System.out.println("actual" + actualAmount);

						
					MainClient.clientConsole.accept(new Message(OperationType.IncreaseParkVistiors,DBControllerType.ParkDBController, (Object) list));
					
					//if the amount of visitors in park is increase
					if (ParkController.Parktype.equals(OperationType.UpdateParkInfo)) {
						setDataOfPark(this);
	
					list.removeAll(list);
					list.add(ParkController.parkConnected.getParkName());
					list.add(String.valueOf(ParkController.order.getNumOfVisitors()));
					list.add(String.valueOf(ParkController.order.getVisitorID()));
					
					getTypeOfOrderVisitor();
					
					list.add(type);
					list.add(orderNumber);
					list.add(actualAmount);
					list.add(String.valueOf(ParkController.order.getHourTime()));
					list.add(String.valueOf(cost));

					
					MainClient.clientConsole.accept(new Message(OperationType.GenerateReceipt,DBControllerType.ReceiptDBController, (Object) list));
					
					//check if the receipt is created
					if (ReceiptController.receiptType.equals(OperationType.CheckReceiptInfo)) {
						System.out.println("i have receipt");
						parkEntranceController1.lblOrderReceiptID.setText(String.valueOf(ReceiptController.receipt.getReceiptID()));
						parkEntranceController1.lblOrderReceiptOrderID.setText(String.valueOf(ParkController.order.getOrderID()));
						parkEntranceController1.lblOrderReceiptNumOfVisitors.setText(String.valueOf(ParkController.order.getNumOfVisitors()));
						
						putDataInOrderReceipt(this,parkEntranceController1);
			
					primaryStage.show();
					return;
					}
					
					else {
						showPopUp(this, "Failed to update!" ,"invalid order number/ amount of visitor to entry");
						return;
						
					}
				}
			}
			
		}catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	private void getTypeOfOrderVisitor() {
		if(ParkController.order.getType().equals("Single/Family")){
			MainClient.clientConsole.accept(new Message(OperationType.TravelerInfo, DBControllerType.ParkDBController, (Object)String.valueOf(ParkController.order.getVisitorID() )));
		if (ParkController.disType.equals(Discount.GroupDiscount) || ParkController.disType.equals(Discount.MemberDiscount)) {
			type = "member";
			
		}
		
		else {
			type = "visitor";
		}
		
		}
		else {
			type="instructor";
		}
		cost=ParkController.order.getCost();
		
	}

	/**
	 * 	this function checks if the park is open for entry
	 * @param parkEntranceGUIController
	 * @return
	 */
	private boolean checkHoursForEntry(ParkEntranceGUIController parkEntranceGUIController) {
		getCurrentTime();
		if(hours>closeEntrance ||hours<openEntrance) {
			showPopUp(this, "The entrance hours are over!" , "You can enter the park from 10:00 to 17:00");
			return false;
		}
		return true;
	}

	/**
	 * this method update the cost and the discount of the  order receipt for orders visitors
	 * @param parkEntranceGUIController
	 * @param parkEntranceController1
	 */
	private void putDataInOrderReceipt(ParkEntranceGUIController parkEntranceGUIController,
			ParkEntranceGUIController parkEntranceController1) {
		
	
		if (type.equals("instructor")) {
			if (ParkController.order.isPaidUp()) {
				parkEntranceController1.lblOrderReceiptDiscount
						.setText("37% + instructor doesn't need to pay");
				parkEntranceController1.lblOrderReceiptCost.setText(String.valueOf(ParkController.order.getNumOfVisitors()*OrderController.getTicketPrice()*0.63)+ " NIS");
			} else {
				parkEntranceController1.lblOrderReceiptDiscount
						.setText("25% + instructor doesn't need to pay");
				parkEntranceController1.lblOrderReceiptCost.setText(String.valueOf(ParkController.order.getNumOfVisitors()*OrderController.getTicketPrice()*0.75)+ " NIS");
			}
		}
	
		else if (type.equals("member")) {
			parkEntranceController1.lblOrderReceiptDiscount.setText("35%");
			parkEntranceController1.lblOrderReceiptCost.setText(String.valueOf(ParkController.order.getNumOfVisitors()*OrderController.getTicketPrice()*0.65)+ " NIS");
		} else {
			parkEntranceController1.lblOrderReceiptDiscount.setText("15% ");
			parkEntranceController1.lblOrderReceiptCost.setText(String.valueOf(ParkController.order.getNumOfVisitors()*OrderController.getTicketPrice()*0.85)+ " NIS");
		}
		
	}

	/**
	 * this method checks for order if there is a receipt and if the visitor can entry
	 * @param parkEntranceGUIController
	 * @param list
	 * @param actualAmount
	 * @return
	 */
	private boolean checkReceiptInfo(ParkEntranceGUIController parkEntranceGUIController, List<String> list, String actualAmount) {
		MainClient.clientConsole.accept(new Message(OperationType.CheckReceiptInfo, DBControllerType.ReceiptDBController, (Object) list));
		
		//check if some of the visitors for this order already entry and if the amount that want to entry now is impossible
		if (ReceiptController.receiptType.equals(OperationType.FailedUpdateReceipt)) {
			showPopUp(this, "Failed to update!" ,"invalid order number/ amount of visitor to entry");
			return false;
		}
		//check if some of the visitors for this order already entry and if the amount that want to entry now is possible and update the receipt
		if (ReceiptController.receiptType.equals(OperationType.SuccessUpdateReceipt)) {
			String msg="they already paid.\nthe receipt id is:  "+ String.valueOf(ReceiptController.receiptID);
			showPopUp(this, "The visitor can enter!",msg);

			list.removeAll(list);
			list.add(ParkController.parkConnected.getParkName());
			list.add(actualAmount);
			System.out.println("actual" + actualAmount);
			MainClient.clientConsole.accept(new Message(OperationType.IncreaseParkVistiors,DBControllerType.ParkDBController, (Object) list));
			
			//if the amount of visitors in park is increase
			if (ParkController.Parktype.equals(OperationType.UpdateParkInfo)) {
				setDataOfPark(this);
			}

			return false ;
		}
		
		return true;
	}

	/**
	 * this method checks if the order is never exist
	 * @param parkEntranceGUIController
	 * @param list
	 * @return
	 */
	private boolean checkOrderInfo(ParkEntranceGUIController parkEntranceGUIController, List<String> list) {
		MainClient.clientConsole.accept(new Message(OperationType.GetOrderInfo, DBControllerType.ParkDBController, (Object) list));

		//check if the order is not valid
		if(ParkController.ordertype.equals(OperationType.NeverExist)){
			showPopUp(this,  "Failed to update!","invalid order number/ amount of visitor to entry");
			return false;
		}
		return true;
	}
	
	/**
	 * this method show the manual receipt popUp after the entry employee put the data 
	 * @param event
	 */

	@FXML
	void showManualReceipt(MouseEvent event) {
		VBox root;
		Stage primaryStage = new Stage();
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ManualReceiptPopupGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manual Receipt");
			ParkEntranceGUIController parkEntranceController1 = loader.getController();

			parkEntranceController1.lblManualReceiptNumOfVisitors.setText(this.txtAmountOfOccasional.getText());
			parkEntranceController1.lblManualReceiptParkName.setText(ParkController.parkConnected.getParkName());

			int amount = Integer.parseInt(this.txtAmountOfOccasional.getText());
			
			
			//check if the park is open for entry
			if(!checkHoursForEntry(this))
				return;
			
			
			//check if the amount of visitors wants to entry is possible
			if(!UpdateCurrAmountOfVisitors(this,amount))
				return;
	
		
			
			List<String> list = new ArrayList<>();
			list.add(ParkController.parkConnected.getParkName());
			list.add(this.txtAmountOfOccasional.getText());
			
			//check if occasional can entry because the difference
			/*if(!checkOccationalAmount(this,amount,list))
				return;
			*/
		
			visitorID = this.txtTravelerID.getText();
			type = null;
			
			//Calculate the discount and price and show  them
			putDiscountAndPriceToManualReceipt(this,parkEntranceController1);
	
			MainClient.clientConsole.accept(new Message(OperationType.IncreaseParkVistiors, DBControllerType.ParkDBController, (Object) list));
			if (ParkController.Parktype.equals(OperationType.UpdateParkInfo)) {
				setDataOfPark(this);
			list.add(visitorID);
			list.add(type);
			list.add(String.valueOf(0));
			list.add(this.txtAmountOfOccasional.getText());
			list.add(String.valueOf(cost));
			MainClient.clientConsole.accept(
					new Message(OperationType.GenerateReceipt, DBControllerType.ReceiptDBController, (Object) list));
			if (ReceiptController.receiptType.equals(OperationType.CheckReceiptInfo)) {
				parkEntranceController1.lblManualReceiptID
						.setText(String.valueOf(ReceiptController.receipt.getReceiptID()));
			}

			primaryStage.show();

			}
	
		}catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}



	/**
	 * this method update the cost and the discount of the  manual receipt for occasional visitors
	 * @param parkEntranceGUIController
	 * @param parkEntranceController1
	 */
	private void putDiscountAndPriceToManualReceipt(ParkEntranceGUIController parkEntranceGUIController, ParkEntranceGUIController parkEntranceController1) {
		System.out.println(1);
		MainClient.clientConsole.accept(new Message(OperationType.TravelerInfo, DBControllerType.ParkDBController, (Object) visitorID));
		if (ParkController.disType.equals(Discount.GroupDiscount)) {
			System.out.println("instructor");
			parkEntranceController1.lblManualReceiptDiscount.setText("10% + The Instructor needs to pay");
			type = "instructor";
			cost= (int) (Integer.parseInt(this.txtAmountOfOccasional.getText())* OrderController.getTicketPrice() * 0.9);
			
			parkEntranceController1.lblManualReceiptCost.setText(String.valueOf(cost)+ " NIS");
		} else if (ParkController.disType.equals(Discount.MemberDiscount)) {
			parkEntranceController1.lblManualReceiptDiscount.setText("20%");
			type = "member";
			cost= (int) (Integer.parseInt(this.txtAmountOfOccasional.getText())* OrderController.getTicketPrice() * 0.8);
			parkEntranceController1.lblManualReceiptCost.setText(String.valueOf(cost)+ " NIS");
		}

		else {
			parkEntranceController1.lblManualReceiptDiscount.setText("0%");
			type = "visitor";
			cost= (int) (Integer.parseInt(this.txtAmountOfOccasional.getText())* OrderController.getTicketPrice());
			parkEntranceController1.lblManualReceiptCost.setText(String.valueOf(cost)+ " NIS");
		}
		
	}

	/**
	 * this method check the amount of occasional visitors and if its possible to entry more
	 * @param parkEntranceGUIController
	 * @param amount
	 * @param list
	 * @return
	 */
	private boolean checkOccationalAmount(ParkEntranceGUIController parkEntranceGUIController, int amount, List<String> list) {
		MainClient.clientConsole.accept(new Message(OperationType.CheckDifference, DBControllerType.ParkDBController, (Object) list));
		
		if(ParkController.occasionalAmount+ amount > ParkController.parkConnected.getDifference())
		{
			showPopUp(this,  "Failed to update!","invalid amount of visitor to entry");
			return false;
			
		}
		return true;
		
	}
	
	/**
	 * this method update the amount of the visitors that still in park, and check if the amount of visitors wants to entry is possible
	 * @param parkEntranceGUIController
	 * @param amount
	 * @return
	 */

	private boolean UpdateCurrAmountOfVisitors(ParkEntranceGUIController parkEntranceGUIController, int amount) {
		MainClient.clientConsole.accept(new Message(OperationType.UpdateCurrAmountOfVisitors, DBControllerType.ParkDBController, (Object)ParkController.parkConnected.getParkName() ));
		setDataOfPark(this);
		//check if the amount of visitors wants to entry is possible
		if(!(ParkController.parkConnected.getCurrentAmountOfVisitors()+ amount<=ParkController.parkConnected.getParkCapacity())||amount<=0) {
			showPopUp(this,  "Failed to update!","invalid  amount of visitor to entry");
			return false;
		}
		return true;
		
	}

	/**
	 * this method shows the Park Entrance page 
	 */
	public void show() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ParkEntranceGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Park Entrance");
			ParkEntranceGUIController parkEntranceController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(parkEntranceController);
			MenuBarSelection.setMenuOptions(menuLabels);
			MainClient.clientConsole.accept(new Message(OperationType.GetParkInfo, DBControllerType.ParkDBController,
					(Object) EmployeeController.employeeConected.getOrganizationAffilation()));
			System.out.println(55555);
			setDataOfPark(parkEntranceController);

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

    /**
 * this method update the park information if there is any changes
 * @param parkEntranceController
 */
	 private void setDataOfPark(ParkEntranceGUIController parkEntranceController) {
		System.out.println(1010);
		parkEntranceController.lblParkName.setText(ParkController.parkConnected.getParkName());
		System.out.println(ParkController.parkConnected.getParkName());
		parkEntranceController.lblCurrentAmountInPark
				.setText(String.valueOf(ParkController.parkConnected.getCurrentAmountOfVisitors()));
		parkEntranceController.lblTravelerCanEnter.setText(String.valueOf(ParkController.parkConnected.getParkCapacity()
				- ParkController.parkConnected.getCurrentAmountOfVisitors()));
		System.out.println(1010);
	}

	private List<Label> createLabelList(ParkEntranceGUIController parkEntranceController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(parkEntranceController.mnuAddOrder);
		tempMenuLabels.add(parkEntranceController.mnuMyOrders);
		tempMenuLabels.add(parkEntranceController.mnuMyProfile);
		tempMenuLabels.add(parkEntranceController.mnuParkEntrance);
		tempMenuLabels.add(parkEntranceController.mnuRegistration);
		tempMenuLabels.add(parkEntranceController.mnuParkDetails);
		tempMenuLabels.add(parkEntranceController.mnuEvents);
		tempMenuLabels.add(parkEntranceController.mnuReportsDepartment);
		tempMenuLabels.add(parkEntranceController.mnuReportsManager);
		tempMenuLabels.add(parkEntranceController.mnuParkCapacity);
		tempMenuLabels.add(parkEntranceController.mnuRequests);
		return tempMenuLabels;
	}
	
	
	/**
	 * close manual receipt
	 * @param event
	 */
	 @FXML
	    void closePopUp1(MouseEvent event) {
		 Stage stage = (Stage) btnOKmanualReceipt.getScene().getWindow();
		 stage.close();	    
	 }
	 
	 /**
	  * close order receipt
	  * @param event
	  */
	  @FXML
	    void closePopUp2(MouseEvent event) {
		   Stage stage2 = (Stage) btnOKorderReceipt.getScene().getWindow();
		    stage2.close();
	    }
	  

	  /**
	   * close pop Up
	   * @param event
	   */
	    @FXML
	    void closePopUp3(MouseEvent event) {
	    	 Stage stage = (Stage) btnOkPopUp.getScene().getWindow();
			    stage.close();

	    }

}

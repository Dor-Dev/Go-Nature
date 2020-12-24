package gui;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.ParkController;
import controllers.VisitorController;
import enums.DBControllerType;
import enums.Discount;
import enums.OperationType;
import enums.UserTypes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ParkEntranceGUIController {
	
	public static int receiptID =0;


    @FXML
    private Label lblManualReceiptID;

    @FXML
    private Label lblManualReceiptParkName;

    @FXML
    private Label lblManualReceiptNumOfVisitors;

    @FXML
    private Label lblManualReceiptDiscount;

    @FXML
    private Label lblOrderReceiptID;

    @FXML
    private Label lblOrderReceiptParkName;

    @FXML
    private Label lblOrderReceiptOrderID;

    @FXML
    private Label lblOrderReceiptNumOfVisitors;

    @FXML
    private Label lblOrderReceiptDiscount;


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

   /* @FXML
    private ComboBox<String> cmbTypesOccasional;
    */

    @FXML
    void decreaseAmountOfVisitors(MouseEvent event) {
    	List<String> list = new ArrayList<>();
    	String receiptID =this.txtReceiptID.getText();
    	String AmountOfTravelers = this.txtExitAmount.getText();
    	
    	DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        String date =d.format(localDate);
        //System.out.println(date);
     
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String hour= sdf.format(cal.getTime());
       // System.out.println(hour);
        list.add(ParkController.parkConnected.getParkName());
        list.add(AmountOfTravelers);
        MainClient.clientConsole.accept(new Message(OperationType.DecreaseParkVistiors,DBControllerType.ParkDBController,(Object)list));
        
        if(ParkController.Parktype.equals(OperationType.UpdateParkInfo))
        	setDataOfPark(this);
        list.add(receiptID);
        list.add(date);
        list.add(hour);
       //MainClient.clientConsole.accept(new Message(OperationType.UpdateReceipt,DBControllerType.ReceiptDBController,(Object)list));
        

    }

    @FXML
    void showMyProfile(MouseEvent event) {
    	MyProfileGUIController mp= new MyProfileGUIController();
    	((Node)event.getSource()).getScene().getWindow().hide();
    	mp.show();

    }
    
    // TODO a popup message 
    @FXML
    void showOrderReceipt(MouseEvent event) {
    	//TODO increase amount of visitors
    	VBox root;
    	Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("OrderReceiptPopupGUI.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Order Receipt");
			
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}


    }
    
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
			receiptID++;
			parkEntranceController1.lblManualReceiptNumOfVisitors.setText(this.txtAmountOfOccasional.getText());
			parkEntranceController1.lblManualReceiptParkName.setText(ParkController.parkConnected.getParkName());
			parkEntranceController1.lblManualReceiptID.setText(String.valueOf(receiptID));
			List<String> list = new ArrayList<>();
			list.add(ParkController.parkConnected.getParkName());
			list.add(this.txtAmountOfOccasional.getText());
			String visitorID = this.txtTravelerID.getText();
			String type = null;
			MainClient.clientConsole.accept(new Message(OperationType.TravelerInfo,DBControllerType.ParkDBController,(Object)visitorID));
				if(VisitorController.disType.equals(Discount.GroupDiscount))	{
					parkEntranceController1.lblManualReceiptDiscount.setText("10% + The Instructor needs to pay");
					type= "instructor";
				}
				else if(VisitorController.disType.equals(Discount.MemberDiscount)){
					parkEntranceController1.lblManualReceiptDiscount.setText("20%");
					type="member";
				}
			
			else {
				parkEntranceController1.lblManualReceiptDiscount.setText("0%");
				type="visitor";
			}
			
			 MainClient.clientConsole.accept(new Message(OperationType.IncreaseParkVistiors,DBControllerType.ParkDBController,(Object)list));
			 if(ParkController.Parktype.equals(OperationType.UpdateParkInfo))
				 setDataOfPark(this);
			 list.add(visitorID);
			 list.add(type);
			//MainClient.clientConsole.accept(new Message(OperationType.GenerateReceipt,DBControllerType.ReceiptDBController,(Object)list));
			
			
			
			
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}


    }

    
    
	 public void show() 
	    {
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
				MainClient.clientConsole.accept(new Message(OperationType.GetParkInfo,DBControllerType.ParkDBController,(Object)EmployeeController.employeeConected.getOrganizationAffilation()));
				System.out.println(55555);
				setDataOfPark(parkEntranceController);
				
				
				primaryStage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	    }
	 private void setDataOfPark(ParkEntranceGUIController parkEntranceController) {
		System.out.println(1010);
		parkEntranceController.lblParkName.setText(ParkController.parkConnected.getParkName());
		parkEntranceController.lblCurrentAmountInPark.setText(String.valueOf(ParkController.parkConnected.getCurrentAmountOfVisitors()));
		parkEntranceController.lblTravelerCanEnter.setText(String.valueOf(ParkController.parkConnected.getParkCapacity()-ParkController.parkConnected.getCurrentAmountOfVisitors()));
		System.out.println(1010);
	 }
	
	

	private List<Label> createLabelList(ParkEntranceGUIController parkEntranceController)
	 {
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
			return 	tempMenuLabels;
	 }
	 

	 

}

package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import client.MainClient;
import common.Message;
import controllers.RestartApp;
import enums.DBControllerType;
import enums.OperationType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Validation;

/**
 * @author dana_
 *	A method that responsible with controlling the Registration GUI (Registration of member or group instructor by a service representative)
 */

public class RegistrationController {


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
    private RadioButton rdbMember;

    @FXML
    private ToggleGroup registeredType;

    @FXML
    private RadioButton rdbGroupInstractor;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtID;

    @FXML
    private ComboBox<String> cmbPhoneStart;

    @FXML
    private TextField txtPhoneEnd;

    @FXML
    private Button btnRegister;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblFamilyMembers;

    @FXML
    private Label lblRedAslterisk;

    @FXML
    private ComboBox<String> cmbFamilyMembers;

    @FXML
    private CheckBox chkAddCreditCard;

    @FXML
    private Label lblCardNumber;

    @FXML
    private TextField txtCardNumber;

    @FXML
    private Label lblCardOwner;

    @FXML
    private TextField txtCardOwnerName;

    @FXML
    private Label lblCVV;

    @FXML
    private TextField txtCVV;

    @FXML
    private Label lblEperationDate;

    @FXML
    private TextField txtExperationMonth;

    @FXML
    private TextField txtExperationYear;
    
	@FXML
    private Label mnuLogout;
	
	 
    private static String popUpMsg=null;
    private static Boolean msgReceived=false;
    private static String popUpTitle=null;
    private Integer member_number=0 ;

     @FXML
	    void goToMainPage(MouseEvent event) {
		  RestartApp.restartParameters();
		  LoginGUIController login = new LoginGUIController();
		  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		  login.show();
	    }

     /**
      * Method that hides the parameters of number family member at the registration GUI
      * We need to hide it when we register a group instructor
      */
    public void hideFamilyMembersParameters(RegistrationController registrationController)
    {
    	registrationController.lblFamilyMembers.setManaged(false);
    	registrationController.lblRedAslterisk.setManaged(false);
    	registrationController. cmbFamilyMembers.setManaged(false);  
    	registrationController.lblFamilyMembers.setVisible(false);
    	registrationController.lblRedAslterisk.setVisible(false);
    	registrationController. cmbFamilyMembers.setVisible(false);
    }
    
    /**
     * Method that shows the parameters of number family member at the registration GUI
     * We need to show it when we register a member
     */
    public void showFamilyMembersParameters(RegistrationController registrationController)
    {
    	registrationController.lblFamilyMembers.setManaged(true);
    	registrationController.lblRedAslterisk.setManaged(true);
    	registrationController. cmbFamilyMembers.setManaged(true);  
    	registrationController.lblFamilyMembers.setVisible(true);
    	registrationController.lblRedAslterisk.setVisible(true);
    	registrationController. cmbFamilyMembers.setVisible(true);
    }
    

    /**
     * Method that hides the parameters of credit card at the registration GUI
     * We will show the parameters only when the registered user will choose to add credit card (It is optional)
     */

    public void hideCreditCardParamaters(RegistrationController registrationController)
    {
    	registrationController.lblCardNumber.setManaged(false);
    	registrationController.txtCardNumber.setManaged(false);
    	registrationController.lblCardOwner.setManaged(false);
    	registrationController.txtCardOwnerName.setManaged(false);
    	registrationController.lblCVV.setManaged(false);
    	registrationController.txtCVV.setManaged(false);
    	registrationController.lblEperationDate.setManaged(false);
    	registrationController.txtExperationMonth.setManaged(false);
    	registrationController.txtExperationYear.setManaged(false);
    	registrationController.lblCardNumber.setVisible(false);
    	registrationController.txtCardNumber.setVisible(false);
    	registrationController.lblCardOwner.setVisible(false);
    	registrationController.txtCardOwnerName.setVisible(false);
    	registrationController.lblCVV.setVisible(false);
    	registrationController.txtCVV.setVisible(false);
    	registrationController.lblEperationDate.setVisible(false);
    	registrationController.txtExperationMonth.setVisible(false);
    	registrationController.txtExperationYear.setVisible(false);
    }
    
    /**
     * Method that shows the parameters of credit card at the registration GUI
     * We will show the parameters only when the registered user will choose to add credit card (It is optional)
     */

    public void showCreditCardParamaters(RegistrationController registrationController)
    {
    	registrationController.lblCardNumber.setManaged(true);
    	registrationController.txtCardNumber.setManaged(true);
    	registrationController.lblCardOwner.setManaged(true);
    	registrationController.txtCardOwnerName.setManaged(true);
    	registrationController.lblCVV.setManaged(true);
    	registrationController.txtCVV.setManaged(true);
    	registrationController.lblEperationDate.setManaged(true);
    	registrationController.txtExperationMonth.setManaged(true);
    	registrationController.txtExperationYear.setManaged(true);
    	registrationController.lblCardNumber.setVisible(true);
    	registrationController.txtCardNumber.setVisible(true);
    	registrationController.lblCardOwner.setVisible(true);
    	registrationController.txtCardOwnerName.setVisible(true);
    	registrationController.lblCVV.setVisible(true);
    	registrationController.txtCVV.setVisible(true);
    	registrationController.lblEperationDate.setVisible(true);
    	registrationController.txtExperationMonth.setVisible(true);
    	registrationController.txtExperationYear.setVisible(true);
    }
    
    /**
     * A method that registers a user (member\group instructor) when the service representative clicks on the register button
     */
    @FXML
    void registrationAction(MouseEvent event) {
    		HashMap<String, String> hash_map_info = new HashMap<String, String>(); 
    		List<String> info = new ArrayList<String>();
    		if(!Validation.idValidation(txtID.getText())) {
    			if(txtID.getText().equals(""))
    				setPopUpMsg("All fields are requiered");
    			else setPopUpMsg("Invalid ID");
    			RegistartionPopUpController popUp = new RegistartionPopUpController();
        		popUp.showRegistrationPopUp(popUpMsg, (member_number.toString()), popUpTitle);
        		return;
    		}
    		//The function f(X)=X+1000000000 is an injective function - that will promise us unique member number each time
    		member_number=Integer.parseInt(txtID.getText())+1000000000; 
    		//add the info from the GUI to an ArrayList and also to a HashMap by the name of the field
    		info.add(member_number.toString());
    		hash_map_info.put("First Name", txtFirstName.getText());
    		info.add(txtFirstName.getText());
    		hash_map_info.put("Surname",txtSurname.getText());
    		info.add(txtSurname.getText());
    		hash_map_info.put("ID",txtID.getText());
    		info.add(txtID.getText());
    		String phoneNumberStart= cmbPhoneStart.getPromptText();
    		String phoneNumberEnd= txtPhoneEnd.getText();
    		//Connect the strings of the first 3 digits of the phone number and the last 7 digits
    		if(phoneNumberStart!= null && phoneNumberEnd!= null) {
    			phoneNumberStart+=phoneNumberEnd;
    			hash_map_info.put("Phone",phoneNumberStart);
    			info.add(phoneNumberStart);
    		}
    		hash_map_info.put("Email",txtEmail.getText());
    		info.add(txtEmail.getText());
    		if(rdbMember.isSelected()) {
    			hash_map_info.put("Family Members",cmbFamilyMembers.getPromptText());
				info.add(cmbFamilyMembers.getPromptText());
    		}
    		if(chkAddCreditCard.isSelected()) {
    			hash_map_info.put("Card Number",txtCardNumber.getText());
    			info.add(txtCardNumber.getText());
    			hash_map_info.put("Card Owner",txtCardOwnerName.getText());
    			info.add(txtCardOwnerName.getText());
    			hash_map_info.put("CVV",txtCVV.getText());
    			info.add(txtCVV.getText());
    			hash_map_info.put("Experation Month",txtExperationMonth.getText());
    			info.add(txtExperationMonth.getText());
    			hash_map_info.put("Experation Year",txtExperationYear.getText());
    			info.add(txtExperationYear.getText());
    		}
    		//If all the information is valid
    		if(validation(info,hash_map_info)) {
    			//Register a member
    			if(rdbMember.isSelected()) {
    				//It is optional to add credit card details - so we check if we need to register the user with credit card or not
    				if(this.chkAddCreditCard.isSelected())
    					MainClient.clientConsole.accept(new Message(OperationType.MemberRegistrationCC,DBControllerType.RegistrationDBController,(Object)info));
    				else
    					MainClient.clientConsole.accept(new Message(OperationType.MemberRegistration,DBControllerType.RegistrationDBController,(Object)info));
    			}
    			else {
    				//Register a group instructor
    				if(this.chkAddCreditCard.isSelected())
    					//It is optional to add credit card details - so we check if we need to register the user with credit card or not
    					MainClient.clientConsole.accept(new Message(OperationType.GuideRegistrationCC,DBControllerType.RegistrationDBController,(Object)info));
    				else 
    					MainClient.clientConsole.accept(new Message(OperationType.GuideRegistration,DBControllerType.RegistrationDBController,(Object)info));
    			}
    		}else {
    			//If the information is not valid create a pop up with the suitable message
    			setMsgReceived(true);
    			setPopUpTitle("Failed");
    		}
    		//Wait till you get a respond from the server
    		while(msgReceived==false);
    		//create a pop up with the suitable message
    		RegistartionPopUpController popUp = new RegistartionPopUpController();
    		popUp.showRegistrationPopUp(popUpMsg, (member_number.toString()), popUpTitle);
    }
    
	private Boolean validation(List<String> info,HashMap<String, String> hash_map_info ) {
		for( String s : info) {
			if(s.equals("")) {
				setPopUpMsg("All fields are requiered");
				return false;
			}
		}
		if(! Validation.onlyLettersValidation( hash_map_info.get("First Name"))) {
			setPopUpMsg("Invalid First Name");
			return false;
		}
		if(! Validation.onlyLettersValidation(hash_map_info.get("Surname"))) {
			setPopUpMsg("Invalid Surname");
			return false;
		}
		if(!Validation.idValidation(hash_map_info.get("ID"))) {
			setPopUpMsg("Invalid ID number");
			return false;
		}
		if(!Validation.phoneValidation(hash_map_info.get("Phone"))) {
			setPopUpMsg("Invalid phone number");
			return false;
		}
		if(!Validation.emailValidation(hash_map_info.get("Email"))) {
			setPopUpMsg("Invalid Email");
			return false;
		}
		if(this.chkAddCreditCard.isSelected()) {
			if(!Validation.cvvValidation(hash_map_info.get("CVV"))) {
				setPopUpMsg("Invalid CVV");
				return false;
			}
			if(!Validation.cardNumberValidation(hash_map_info.get("Card Number"))) {
				setPopUpMsg("Invalid card number");
				return false;
			}
			if(! Validation.onlyLettersValidation( hash_map_info.get("Card Owner"))) {
				setPopUpMsg("Invalid Owner Name");
				return false;
			}
			if(!Validation.monthExperationValidation(hash_map_info.get("Experation Month"))) {
				setPopUpMsg("Invalid Experation Month");
				return false;								
			}
			if( !Validation.yearExperationValidation(hash_map_info.get("Experation Year"))) {
				setPopUpMsg("Invalid Experation Year");
				return false;								
			}
			
		}
		return true;
		
	}

/**
 * Method that displays the registration GUI
 */
    @FXML
    void show() {
    	VBox root;
		Stage primaryStage = new CloseStage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/RegistrationPage.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Registration");
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
			List<Label> menuLabels = new ArrayList<>();
			RegistrationController registrationController = loader.getController();
			hideCreditCardParamaters(registrationController);
			menuLabels = createLabelList(registrationController);
			MenuBarSelection.setMenuOptions(menuLabels);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

    }
    
    /**
     * Method that create an Array List with the menu labels
     */
	private List<Label> createLabelList(RegistrationController registrationController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(registrationController.mnuAddOrder);
		tempMenuLabels.add(registrationController.mnuMyOrders);
		tempMenuLabels.add(registrationController.mnuMyProfile);
		tempMenuLabels.add(registrationController.mnuParkEntrance);
		tempMenuLabels.add(registrationController.mnuRegistration);
		tempMenuLabels.add(registrationController.mnuParkDetails);
		tempMenuLabels.add(registrationController.mnuEvents);
		tempMenuLabels.add(registrationController.mnuReportsDepartment);
		tempMenuLabels.add(registrationController.mnuReportsManager);
		tempMenuLabels.add(registrationController.mnuParkCapacity);
		tempMenuLabels.add(registrationController.mnuRequests);
		return tempMenuLabels;
	}
	
	
	
	@FXML
	public void initialize() {
		//Initialize combo box phone first 3 numbers
		cmbPhoneStart.getItems().removeAll(cmbPhoneStart.getItems());
		cmbPhoneStart.getItems().addAll("050","051","052","053","054","055","056","057","058","059");

		//Initialize combo box amount of family members 1-15
		cmbFamilyMembers.getItems().removeAll(cmbFamilyMembers.getItems());
		cmbFamilyMembers.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15");

	}
	
	/**
	 * Method that checks if the credit card parameters needs to be hidden or not and acts according to it
	 */
    @FXML
    void showOrHideCreditCardParameters() {
    	if(this.chkAddCreditCard.isSelected()) {
    		showCreditCardParamaters(this);
    	}else hideCreditCardParamaters(this);
    }
    
	/**
	 * Method that checks if the number of family members parameters needs to be hidden or not and acts according to it
	 */
    @FXML
    void showOrHideFamilyMembersParameters() {
    	if(this.rdbGroupInstractor.isSelected()) {
    		hideFamilyMembersParameters(this);
    	}else showFamilyMembersParameters(this);
    }
    
    /**
     * A method that is called when the service representative clicks on 'My Profile' option at the menu
     */
    @FXML
    void showMyProfile(MouseEvent event) {
    	MyProfileGUIController mp= new MyProfileGUIController();
    	((Node)event.getSource()).getScene().getWindow().hide();
    	mp.show();

    }
    
    /**
     * Method that receives messages the suitable from the server
     * @param reciveMsg
     */
    public static void RegistrationParseData(Message reciveMsg) {
    	setPopUpMsg((String) reciveMsg.getObj());
    	setMsgReceived(true);
    	if(popUpMsg.contains("already")) {
    		setPopUpTitle("Failed");
    	}
    	else setPopUpTitle("Succsses");
    	
  	}


	public static void setPopUpMsg(String popUpMsg) {
		RegistrationController.popUpMsg = popUpMsg;
	}


	public static void setMsgReceived(Boolean msgReceived) {
		RegistrationController.msgReceived = msgReceived;
	}


	public static void setPopUpTitle(String popUpTitle) {
		RegistrationController.popUpTitle = popUpTitle;
	}


}

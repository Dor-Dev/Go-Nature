package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ClientController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private Integer member_number ;

     @FXML
	    void goToMainPage(MouseEvent event) {
		  RestartApp.restartParameters();
		  LoginGUIController login = new LoginGUIController();
		  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		  login.show();
	    }

    public void hideFamilyMembersParameters(RegistrationController registrationController)
    {
    	registrationController.lblFamilyMembers.setManaged(false);
    	registrationController.lblRedAslterisk.setManaged(false);
    	registrationController. cmbFamilyMembers.setManaged(false);  
    	registrationController.lblFamilyMembers.setVisible(false);
    	registrationController.lblRedAslterisk.setVisible(false);
    	registrationController. cmbFamilyMembers.setVisible(false);
    }
    
 
    public void showFamilyMembersParameters(RegistrationController registrationController)
    {
    	registrationController.lblFamilyMembers.setManaged(true);
    	registrationController.lblRedAslterisk.setManaged(true);
    	registrationController. cmbFamilyMembers.setManaged(true);  
    	registrationController.lblFamilyMembers.setVisible(true);
    	registrationController.lblRedAslterisk.setVisible(true);
    	registrationController. cmbFamilyMembers.setVisible(true);
    }
    

    

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
    
    @FXML
    void registrationAction(MouseEvent event) {
    		HashMap<String, String> hash_map_info = new HashMap<String, String>(); 
    		List<String> info = new ArrayList<String>();
    		member_number=Integer.parseInt(txtID.getText())+1000000000; //The function f(X)=X+1000000000 is an injective function - that will promise us unique member number each time
    		info.add(member_number.toString());
    		hash_map_info.put("First Name", txtFirstName.getText());
    		info.add(txtFirstName.getText());
    		hash_map_info.put("Surname",txtSurname.getText());
    		info.add(txtSurname.getText());
    		hash_map_info.put("ID",txtID.getText());
    		info.add(txtID.getText());
    		String phoneNumberStart= cmbPhoneStart.getPromptText();
    		String phoneNumberEnd= txtPhoneEnd.getText();
    		if(phoneNumberStart!= null && phoneNumberEnd!= null) {
    			phoneNumberStart+=phoneNumberEnd;
    			hash_map_info.put("Phone",phoneNumberStart);
    			info.add(phoneNumberStart);
    		}else {
    			//Add pop up
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
    		if(validation(info,hash_map_info)) {
    			if(rdbMember.isSelected()) {
    				if(this.chkAddCreditCard.isSelected())
    					MainClient.clientConsole.accept(new Message(OperationType.MemberRegistrationCC,DBControllerType.RegistrationDBController,(Object)info));
    				else
    					MainClient.clientConsole.accept(new Message(OperationType.MemberRegistration,DBControllerType.RegistrationDBController,(Object)info));
    			}
    			else {
    				if(this.chkAddCreditCard.isSelected())
    					MainClient.clientConsole.accept(new Message(OperationType.GuideRegistrationCC,DBControllerType.RegistrationDBController,(Object)info));
    				else MainClient.clientConsole.accept(new Message(OperationType.GuideRegistration,DBControllerType.RegistrationDBController,(Object)info));
    			}
    		}else {
    			setMsgReceived(true);
    			setPopUpTitle("Failed");
    		}
    		while(msgReceived==false);
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
		if(! Pattern.matches("[a-zA-Z]+", hash_map_info.get("First Name"))) {
			setPopUpMsg("Invalid First Name");
			return false;
		}
		if(! Pattern.matches("[a-zA-Z]+", hash_map_info.get("Surname"))) {
			setPopUpMsg("Invalid Surname");
			return false;
		}
		if(hash_map_info.get("ID").length()!=9  || ! Pattern.matches("[0-9]+", hash_map_info.get("ID"))) {
			setPopUpMsg("Invalid ID number");
			return false;
		}
		if(hash_map_info.get("Phone").length()!=10 ||! Pattern.matches("[0-9]+", hash_map_info.get("Phone"))) {
			setPopUpMsg("Invalid phone number");
			return false;
		}
		Pattern emailPattern = Pattern.compile("[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+");
		Matcher m = emailPattern.matcher(hash_map_info.get("Email"));
		if(!m.matches()) {
			setPopUpMsg("Invalid Email");
			return false;
		}
		if(this.chkAddCreditCard.isSelected()) {
			if(hash_map_info.get("CVV").length()<3 || hash_map_info.get("CVV").length()>4 || !Pattern.matches("[0-9]+", hash_map_info.get("CVV"))) {
				setPopUpMsg("Invalid CVV");
				return false;
			}
			if(hash_map_info.get("Card Number").length()!=16 || !Pattern.matches("[0-9]+", hash_map_info.get("Card Number"))) {
				setPopUpMsg("Invalid card number");
				return false;
			}
			if(! Pattern.matches("[a-zA-Z]+", hash_map_info.get("Card Owner"))) {
				setPopUpMsg("Invalid Owner Name");
				return false;
			}
			if(hash_map_info.get("Experation Month").length()!=2 || !Pattern.matches("[0-9]+", hash_map_info.get("Experation Month"))) {
				setPopUpMsg("Invalid Experation Month");
				return false;								
			}
			int tempMonth= Integer.parseInt(hash_map_info.get("Experation Month"));
			if(tempMonth<1 || tempMonth>12) {
				setPopUpMsg("Invalid Experation Month");
				return false;								
			}
			
			if(hash_map_info.get("Experation Year").length()!=2 || !Pattern.matches("[0-9]+", hash_map_info.get("Experation Year"))) {
				setPopUpMsg("Invalid Experation Year");
				return false;	
			}
			int tempYear= Integer.parseInt(hash_map_info.get("Experation Year"));
			if(tempYear<20 || tempYear>30) {
				setPopUpMsg("Invalid Experation Year");
				return false;	
			}
			
		}
		return true;
		
	}


    @FXML
    void show() {
    	VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/RegistrationPage.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Registration");
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
	

    @FXML
    void showOrHideCreditCardParameters() {
    	if(this.chkAddCreditCard.isSelected()) {
    		showCreditCardParamaters(this);
    	}else hideCreditCardParamaters(this);
    }
    
    @FXML
    void showOrHideFamilyMembersParameters() {
    	if(this.rdbGroupInstractor.isSelected()) {
    		hideFamilyMembersParameters(this);
    	}else showFamilyMembersParameters(this);
    }
    
    @FXML
    void showMyProfile(MouseEvent event) {
    	MyProfileGUIController mp= new MyProfileGUIController();
    	((Node)event.getSource()).getScene().getWindow().hide();
    	mp.show();

    }
    
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

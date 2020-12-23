package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

}

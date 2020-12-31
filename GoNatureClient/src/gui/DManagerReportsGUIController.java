package gui;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.ReportController;
import controllers.RestartApp;
import enums.DBControllerType;
import enums.OperationType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.IncomeObject;
import logic.SumVisitorsReport;
import logic.UsageObject;
import logic.VisitingReport;

public class DManagerReportsGUIController {

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
	    private Label mnuLogout;

	    @FXML
	    private ComboBox<String> cmbReportName;

	    @FXML
	    private ComboBox<String> cmbParkName;

	    @FXML
	    private Label lblDate;

	    @FXML
	    private DatePicker datePicker;

	    @FXML
	    private Label lblMonth;

	    @FXML
	    private ComboBox<String> cmbMonth;

	    @FXML
	    private Label lblType;

	    @FXML
	    private ComboBox<String> cmbType;

	    @FXML
	    private Button btnProduceReport;

	    @FXML
	    private VBox vboxReportName;

	    @FXML
	    private Label lblReportName;

	    @FXML
	    private VBox vboxVisiting;

	    @FXML
	    private BarChart<?, ?> barChartVisiting;

	    @FXML
	    private CategoryAxis barChartX;

	    @FXML
	    private NumberAxis barChartY;

		private static String reportName = null;
		private static String parkName = null;
		private static String type = null;
		private static String date = null;
	    private LocalDate thisDay;
	    private Date thisDayToDB ;
	    private int monthInt ;
	    private int dayInt;
	    private int yearInt;
	@FXML
    void goToMainPage(MouseEvent event) {
	  RestartApp.restartParameters();
	  LoginGUIController login = new LoginGUIController();
	  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	  login.show();
    }

	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();

	}

    @FXML
    void showParkCapacity(MouseEvent event) {
    	ParkCapacityGUIController pC = new ParkCapacityGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		pC.show();
    }

   
    @FXML
    void showRequests(MouseEvent event) {
    	DManagerRequestsGUIController rQ = new DManagerRequestsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		rQ.show();
    }

    
    @FXML
	public void initialize() {
		/**
		 * Initialize combo box Report type
		 */
		cmbReportName.getItems().removeAll(cmbReportName.getItems());
		cmbReportName.getItems().addAll("Visiting report", "Canceling report");
		cmbReportName.setOnAction(e -> chooseReportName());
		// cmbParkName.getSelectionModel().select("Option B");

		/**
		 * 
		 * Initialize cmb Months to months of the year
		 */
		cmbMonth.getItems().removeAll(cmbMonth.getItems());
		cmbMonth.getItems().addAll("October 2020","November 2020","December 2020","January 2021", "February 2021", "March 2021", "April 2021", "May 2021", "June 2021", "July 2021", "August 2021","September 2021");
		cmbMonth.setOnAction(e-> chooseMonthAndYear());
		
		
		cmbType.getItems().removeAll(cmbType.getItems());
		cmbType.getItems().addAll("Singles", "Groups", "Members");
		cmbType.setOnAction(e -> chooseTypes());
		
		cmbParkName.getItems().removeAll(cmbParkName.getItems());
		cmbParkName.getItems().addAll("Luna-Park", "Shipment-Park", "Tempo-Park");
		cmbParkName.setOnAction(e -> chooseParkName());
		
		
		

	}


    /**
     * update the park name after the user choose
     */
   
    private void chooseParkName() {
    	parkName= cmbParkName.getValue();
		
	}

    /**
     * update the type of visitors after the user choose
     */
	private void chooseTypes() {
		 if(cmbType.getValue().equals("Singles"))
				 type= "visitor";
		 else if(cmbType.getValue().equals("Groups"))
			 type= "instructor";
		 else if(cmbType.getValue().equals("Members"))
			 type= "member";
		 
		
	}

	private void chooseMonthAndYear() {
		
	}

	/**
	 * update the report name after the user choose
	 */
	private void chooseReportName() {
		reportName  = cmbReportName.getValue();
		
		if(reportName.equals("Visiting report"))
		{
			this.cmbType.setManaged(true);
			this.lblType.setManaged(true);
			this.datePicker.setManaged(true);
			this.lblDate.setManaged(true);
		}
	}
	
	/**
	 * This is a method that pops up the appropriate pop-up and summons the method that produces the appropriate report
	 * @param event
	 */
    @FXML
    void showReportDetails(MouseEvent event) {
    	//setReportDetailsInvisible(this);
    	
    	this.vboxVisiting.setManaged(false);
		this.vboxVisiting.setVisible(false);
    	this.barChartVisiting.setManaged(false);
		this.barChartVisiting.setVisible(false);
		this.barChartX.setManaged(false);
		this.barChartX.setVisible(false);
		this.barChartY.setManaged(false);
		this.barChartY.setVisible(false);
		date =datePicker.getValue().format((DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    	getCurrentDay();
    	System.out.println("htyy");
    	System.out.println(date.substring(0, 4));
    	System.out.println(date.substring(5, 7));
    	Alert a = new Alert(AlertType.INFORMATION);
    	
    	if(Integer.valueOf(date.substring(0, 4))>yearInt||Integer.valueOf(date.substring(5, 7))> monthInt && Integer.valueOf(date.substring(0, 4)) == yearInt) {
    		System.out.println(1155533);
    			a.setHeaderText("The date of production of the report has not yet arrived.");
				a.setContentText("No data available for viewing.");
				a.setTitle("Report Status");
	    		a.showAndWait();
				return;	
    		}
    	else if(Integer.valueOf(date.substring(5, 7))<monthInt&& Integer.valueOf(date.substring(0, 4))==yearInt||Integer.valueOf(date.substring(0, 4))<yearInt) {
			a.setHeaderText("The date of production of the report has passed.");
			a.setContentText("You can view the data that was in it.");
    	}
    	else {
    		a.setHeaderText("Report Status");
			a.setContentText("You can view the existing data of the report.");
    	}
    	
	    		a.setTitle("Report Status");
	    		a.showAndWait();
	    	
    	if(date!=null&&type!=null&&parkName!=null&&reportName.equals("Visiting report")) {
    		showVisitingReport(this);	
    	}

    }

    /**
     * this method create the Visiting report and present it to the manager 
     * @param dManagerReportsGUIController
     */
	private void showVisitingReport(DManagerReportsGUIController dManagerReportsGUIController) {
 		List<String> list = new ArrayList<String>();
		list.add(parkName);
		list.add(date);
		list.add(type);
		MainClient.clientConsole.accept(new Message(OperationType.VisitingReport, DBControllerType.ReportsDBController, (Object)list));
		
		if(ReportController.reportType.equals(OperationType.VisitingReport)) {
			System.out.println("im hereeeeeeeee");
			VisitingReport visitingReport = (VisitingReport) ReportController.report;
			
			this.vboxReportName.setManaged(true);
			this.lblReportName.setManaged(true);
			this.vboxReportName.setVisible(true);
			this.lblReportName.setVisible(true);
			this.vboxVisiting.setManaged(true);
			this.vboxVisiting.setVisible(true);
			this.lblReportName.setManaged(true);
			this.barChartVisiting.setManaged(true);
			this.barChartVisiting.setVisible(true);
			this.barChartX.setManaged(true);
			this.barChartX.setVisible(true);
			this.barChartY.setManaged(true);
			this.barChartY.setVisible(true);
			
			this.lblReportName.setText("Visiting Report for "+cmbType.getValue()+ " in "+ date);
		
			int [] hours = visitingReport.getHours();
			int [] visitors = visitingReport.getAmountOfVisitors();
		
			this.barChartVisiting.getData().clear();
			
			XYChart.Series set1 = new XYChart.Series<>();
			set1.getData().clear();
			for(int i=0;i<hours.length; i++ ) {
				if(hours[i]!=0) {
					System.out.println(hours[i]);
					set1.getData().add(new XYChart.Data<>(String.valueOf(hours[i]+":00"),visitors[i] ));
					
				}
				
			}
	
			
			this.barChartVisiting.getData().add(set1);
			this.barChartVisiting.setBarGap(0);
			this.barChartVisiting.setCategoryGap(50);
			
		}
		
		
	}
	
	/**
	 * This is a method that checks the date of the day and saves it
	 */
	private void getCurrentDay() {
		 thisDay = LocalDate.now();
		 thisDayToDB = Date.valueOf(thisDay);
		 System.out.println(thisDayToDB);
		 monthInt = thisDay.getMonthValue();
		 dayInt = thisDay.getDayOfMonth();
		 yearInt = thisDay.getYear();
		 System.out.println("now is the month "+monthInt);
		 System.out.println(dayInt);
		 System.out.println(thisDay.getYear());
	}
	

	/**
	 * This is the method that activates the page
	 */
	public void show() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/DManagerReports.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Department manager Reports");
			DManagerReportsGUIController dManagerReportsController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			menuLabels = createLabelList(dManagerReportsController);
			MenuBarSelection.setMenuOptions(menuLabels);
			setReportDetailsInvisible(dManagerReportsController);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * This is the method that hides the irrelevant data on the page
	 * @param dManagerReportsController
	 */
	private void setReportDetailsInvisible(DManagerReportsGUIController dManagerReportsController) {
	
		System.out.println("chek2");
		dManagerReportsController.vboxVisiting.setManaged(false);
		dManagerReportsController.vboxVisiting.setVisible(false);
		dManagerReportsController.vboxReportName.setManaged(false);
		dManagerReportsController.vboxReportName.setVisible(false);
		dManagerReportsController.barChartVisiting.setManaged(false);
		dManagerReportsController.barChartVisiting.setVisible(false);
		dManagerReportsController.barChartX.setManaged(false);
		dManagerReportsController.barChartX.setVisible(false);
		dManagerReportsController.barChartY.setManaged(false);
		dManagerReportsController.barChartY.setVisible(false);
		dManagerReportsController.cmbMonth.setManaged(false);
		dManagerReportsController.lblMonth.setManaged(false);
		dManagerReportsController.lblDate.setManaged(false);
		dManagerReportsController.datePicker.setManaged(false);
		dManagerReportsController.lblType.setManaged(false);
		dManagerReportsController.cmbType.setManaged(false);
		
	
	}

	private List<Label> createLabelList(DManagerReportsGUIController dManagerReportsController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(dManagerReportsController.mnuAddOrder);
		tempMenuLabels.add(dManagerReportsController.mnuMyOrders);
		tempMenuLabels.add(dManagerReportsController.mnuMyProfile);
		tempMenuLabels.add(dManagerReportsController.mnuParkEntrance);
		tempMenuLabels.add(dManagerReportsController.mnuRegistration);
		tempMenuLabels.add(dManagerReportsController.mnuParkDetails);
		tempMenuLabels.add(dManagerReportsController.mnuEvents);
		tempMenuLabels.add(dManagerReportsController.mnuReportsDepartment);
		tempMenuLabels.add(dManagerReportsController.mnuReportsManager);
		tempMenuLabels.add(dManagerReportsController.mnuParkCapacity);
		return tempMenuLabels;
	}
}

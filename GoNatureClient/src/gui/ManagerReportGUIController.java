package gui;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.ParkController;
import controllers.ReportController;
import enums.DBControllerType;
import enums.OperationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.UsageObject;
import logic.IncomeObject;
import logic.IncomeReport;
import logic.SumVisitorsReport;
import logic.UsageReport;

public class ManagerReportGUIController {


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
    private ComboBox<String> cmbReport;

    @FXML
    private ComboBox<String> cmbMonths;

    @FXML
    private Button btnMakeReport;

    @FXML
    private VBox vboxReportDetails;

    @FXML
    private Label lblReportName;

    @FXML
    private Label lblMonthYear;

    @FXML
    private BarChart<?, ?> barChartOverall;

    @FXML
    private CategoryAxis barChartX;

    @FXML
    private NumberAxis barChartY;
    @FXML
    private VBox vboxBarChart;
    
    @FXML
    private VBox vboxTbIncome;

    @FXML
    private TableView<IncomeObject> tabIncome;

    @FXML
    private TableColumn<IncomeObject, Date> colDate;

    @FXML
    private TableColumn<IncomeObject, Integer> colIncome;
    
    @FXML
    private HBox hboxTotalIncome;

    private static String cmbName = null;
    
    private static String cmbMon = null;
    
    @FXML
    private Label lblMonthYearMoney;
    
    @FXML
		private TableColumn<UsageObject, Integer> colNumOfVisitors;

		@FXML
		private TableColumn<UsageObject, Date> colUsageDate;
	
		@FXML
		private TableView<UsageObject> tblUsageReportTable;
		
		public static List<UsageObject> UsageReportData ;
	 /*   @FXML
	    private TableView<?> tabIncome;

	    @FXML
	    private TableColumn<Date, Integer> colDate;

	    @FXML
	    private TableColumn<Date, Integer> colIncome;
	    @FXML
	    private VBox vboxTableIncome;
	    */

	
	    
	@FXML
	void goToEventsPage(MouseEvent event) {
		EventsGUIController eGc = new EventsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		eGc.show();
	}

	@FXML
	void goToParkDetails(MouseEvent event) {
		ManagerDetailsGUIController mDgc = new ManagerDetailsGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		mDgc.show();
	}

	@FXML
	void showAddOrder(MouseEvent event) {
		AddOrderGUIController c = new AddOrderGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		c.show();
	}

	@FXML
	void showMyProfile(MouseEvent event) {
		MyProfileGUIController mf = new MyProfileGUIController();
		((Node) event.getSource()).getScene().getWindow().hide();
		mf.show();
	}
	
	@FXML
	public void initialize() {
		/**
		 * Initialize combo box Report type
		 */
		cmbReport.getItems().removeAll(cmbReport.getItems());
		cmbReport.getItems().addAll("Overall", "Usage", "Income");
		cmbReport.setOnAction(e -> chooseReportName());
		// cmbParkName.getSelectionModel().select("Option B");

		/**
		 * 
		 * Initialize cmb Months to months of the year
		 */
		cmbMonths.getItems().removeAll(cmbMonths.getItems());
		cmbMonths.getItems().addAll("October 2020","November 2020","December 2020","January 2021", "February 2021", "March 2021", "April 2021", "May 2021", "June 2021", "July 2021", "August 2021","September 2021");
		cmbMonths.setOnAction(e-> chooseMonthAndYear());
		
		
		// Initialize column of the table for income report
		
		colDate.setCellValueFactory(new PropertyValueFactory<IncomeObject, Date>("day"));
		colIncome.setCellValueFactory(new PropertyValueFactory<IncomeObject, Integer>("money"));
		

		colDate.setStyle("-fx-alignment: CENTER");
		colIncome.setStyle("-fx-alignment: CENTER");
		

		//Initialize the column of the usage report table
		colUsageDate.setCellValueFactory(new PropertyValueFactory<UsageObject,Date>("date"));
		colNumOfVisitors.setCellValueFactory(new PropertyValueFactory<UsageObject,Integer>("numOfVIsitors"));
		
		colUsageDate.setStyle("-fx-alignment: CENTER");
		colNumOfVisitors.setStyle("-fx-alignment: CENTER");

	}
	
	

	private void chooseReportName() {
		setReportDetailsInvisible(this);
		cmbName = cmbReport.getValue();
		System.out.println(cmbReport.getValue());
		
	}

	private void chooseMonthAndYear() {
		setReportDetailsInvisible(this);
		cmbMon = cmbMonths.getValue();
	String monthYear = cmbMonths.getValue();
	if(cmbName.equals("Overall")) {
		getSumReportData(monthYear);
		}
	else if(cmbName.equals("Income")) {
			getIncomeReportData(monthYear);
		
		}
	
	else if(cmbReport.getValue().equals("Usage")){
		getUsageReportData(monthYear);
	}
	
	}
	
	private void getUsageReportData(String monthYear) {
		setReportDetailsInvisible(this);
		List <String> list = new ArrayList<>();
		list =addTheCorrectMonthAndYear(list, monthYear);
		
		MainClient.clientConsole.accept(new Message(OperationType.UsageReport, DBControllerType.ReportsDBController, (Object)list));
		if( ReportController.reportType.equals(OperationType.UsageReport))
		{
			UsageReport usageReport = (UsageReport) ReportController.report;
			
			setReportDetailsVisible(this);
			this.tblUsageReportTable.setVisible(true);
			
			this.lblReportName.setText("Usage Report");
			this.lblMonthYear.setText(monthYear);
			
			
		    barChartOverall.setBarGap(0);
		    barChartOverall.setCategoryGap(50);
		    
		    Date [] day = usageReport.getDay();
		    int [] capacity = usageReport.getNumOfVisitorsInDay();
		    UsageObject [] in= new UsageObject[31];
		    
		    for(int i =0;i<31;i++)
			{
				if(day[i]!=null) {
					System.out.println(day[i]);
					in[i]= new UsageObject(capacity[i], day[i]);
				}
			}
					
	
			
			tblUsageReportTable.setItems(FXCollections.observableArrayList(in));
		   
		}
		
	}
	private void getIncomeReportData(String monthYear) {
		
		
		List <String> list = new ArrayList<>();
		list =addTheCorrectMonthAndYear(list, monthYear);

		this.lblReportName.setManaged(true);
		this.lblMonthYear.setManaged(true);
		
		this.lblReportName.setText("Income Report");
		this.lblMonthYear.setText(monthYear);
		
		
		MainClient.clientConsole.accept(new Message(OperationType.RevenueReport, DBControllerType.ReportsDBController, (Object)list));
		if( ReportController.reportType.equals(OperationType.RevenueReport)) {
			IncomeReport incomeReport = (IncomeReport)  ReportController.report;
			
			Date [] day = incomeReport.getDay();
			 int [] money = incomeReport.getMoneyInDay();
			for(Date d: day) {
				if(d!=null)
					System.out.println(d);
			}
			
			for( int m: money) {
				if(m!=0)
					System.out.println(m);
			}
			
			
			System.out.println("total= "+incomeReport.getTotalIncome());
			this.tabIncome.setVisible(true);
			this.hboxTotalIncome.setVisible(true);
			
			this.lblMonthYearMoney.setText(monthYear + " : "+incomeReport.getTotalIncome()+ " NIS" );
			IncomeObject [] in = new IncomeObject[31];
			//List <IncomeObject> inList = new ArrayList<IncomeObject>();
			
			for(int i =0;i<31;i++)
			{
				if(day[i]!=null) {
					in[i]= new IncomeObject(money[i], day[i]);
				}
			}
					
	
			
			tabIncome.setItems(FXCollections.observableArrayList(in));
		
			
			
		}
		
		
		
	}

	private void getSumReportData(String monthYear) {
		setReportDetailsInvisible(this);
		List <String> list = new ArrayList<>();
		list =addTheCorrectMonthAndYear(list, monthYear);
		
		MainClient.clientConsole.accept(new Message(OperationType.SumVisitorsReport, DBControllerType.ReportsDBController, (Object)list));
		if( ReportController.reportType.equals(OperationType.SumVisitorsReport))
		{
			SumVisitorsReport sumReport = (SumVisitorsReport) ReportController.report;
			System.out.println("visitors= "+ sumReport.getVisitorsAmount());
			System.out.println("members= "+  sumReport.getMembersAmount());
			System.out.println("groups= "+ sumReport.getGroupsAmount());
			
			setReportDetailsVisible(this);
			
			this.lblReportName.setText("Overall Report");
			this.lblMonthYear.setText(monthYear);
			
			this.barChartOverall.getData().clear();
			XYChart.Series set1 = new XYChart.Series<>();
			set1.getData().clear();
			set1.getData().add(new XYChart.Data<>("Singles", sumReport.getVisitorsAmount()));
			set1.getData().add(new XYChart.Data<>("Members", sumReport.getMembersAmount()));
			set1.getData().add(new XYChart.Data<>("Groups", sumReport.getGroupsAmount()));
			
			this.barChartOverall.getData().add(set1);
		
			Node n = barChartOverall.lookup(".data0.chart-bar");
		    n.setStyle("-fx-bar-fill: red");
		    n = barChartOverall.lookup(".data1.chart-bar");
		    n.setStyle("-fx-bar-fill: orange");
		    n = barChartOverall.lookup(".data2.chart-bar");
		    n.setStyle("-fx-bar-fill: green");
	

		    barChartOverall.setBarGap(0);
		    barChartOverall.setCategoryGap(50);
		    
		   
		    
			/*
			this.lblAmountOfSingles.setText(String.valueOf(sumReport.getVisitorsAmount()));
			this.lblAmountOfMembers.setText(String.valueOf(sumReport.getMembersAmount()));
			this.lblAmountOfGroups.setText(String.valueOf(sumReport.getGroupsAmount()));
			*/
		}
		
	}

	private List<String> addTheCorrectMonthAndYear(List<String> list, String monthYear) {
		list.add(EmployeeController.employeeConected.getOrganizationAffilation());
		if(monthYear.equals("December 2020")) {
			list.add(String.valueOf(12));
			list.add("2020");
			}
			else if(monthYear.equals("November 2020")) {
				list.add(String.valueOf(11));
				list.add("2020");
			}
			else if(monthYear.equals("October 2020")) {
				list.add(String.valueOf(10));
				list.add("2020");
			}
			else if(monthYear.equals("January 2021")) {
				list.add(String.valueOf(1));
				list.add("2021");
			}
			else if(monthYear.equals("February 2021")) {
				list.add(String.valueOf(2));
				list.add("2021");
			}
			else if(monthYear.equals("March 2021")) {
				list.add(String.valueOf(3));
				list.add("2021");
			}
			else if(monthYear.equals("April 2021")) {
				list.add(String.valueOf(4));
				list.add("2021");
			}
			else if(monthYear.equals("May 2021")) {
				list.add(String.valueOf(5));
				list.add("2021");
			}
			else if(monthYear.equals("June 2021")) {
				list.add(String.valueOf(6));
				list.add("2021");
			}
			else if(monthYear.equals("July 2021")) {
				list.add(String.valueOf(7));
				list.add("2021");
			}
			else if(monthYear.equals("August 2021")) {
				list.add(String.valueOf(8));
				list.add("2021");
			}
			else if(monthYear.equals("September 2021")) {
				list.add(String.valueOf(9));
				list.add("2021");
			}
		return list;
			
		
		
	}

	private void setReportDetailsVisible(ManagerReportGUIController managerReportGUIController) {
		/*
		managerReportGUIController.vboxReportDetails.setManaged(true);
		managerReportGUIController.hboxGroups.setManaged(true);
		managerReportGUIController.hboxMembers.setManaged(true);
		managerReportGUIController.hboxSingles.setManaged(true);
		*/
		managerReportGUIController.lblMonthYear.setManaged(true);
		managerReportGUIController.lblReportName.setManaged(true);
		managerReportGUIController.barChartOverall.setManaged(true);
		//managerReportGUIController.vboxBarChart.setManaged(true);
		managerReportGUIController.vboxBarChart.setVisible(true);
		managerReportGUIController.hboxTotalIncome.setVisible(false);
		managerReportGUIController.hboxTotalIncome.setManaged(false);
		//managerReportGUIController.barChartOverall.getData().removeAll();
		
	}

	public void show() {
		VBox root;
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ParkManagerReports.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Go-Nature Reports");
			ManagerReportGUIController managerReportsController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			
			menuLabels = managerReportsController.createLabelList(managerReportsController);
			MenuBarSelection.setMenuOptions(menuLabels);
			System.out.println("chek1");
			setReportDetailsInvisible(managerReportsController);
			
			
			
		
			primaryStage.show();
			
	
				
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private void setReportDetailsInvisible(ManagerReportGUIController managerReportsController) {
		/*
		managerReportsController.vboxReportDetails.setManaged(false);
		managerReportsController.hboxGroups.setManaged(false);
		managerReportsController.hboxMembers.setManaged(false);
		managerReportsController.hboxSingles.setManaged(false);
		*/
		
		System.out.println("chek2");
		managerReportsController.lblMonthYear.setManaged(false);
		managerReportsController.lblReportName.setManaged(false);
		managerReportsController.barChartOverall.setManaged(false);
		managerReportsController.barChartOverall.getData().clear();
		managerReportsController.barChartX.setManaged(false);
		managerReportsController.barChartY.setManaged(false);
		//managerReportsController.vboxBarChart.setManaged(false);
		managerReportsController.vboxBarChart.setVisible(false);
		//managerReportsController.vboxTableIncome.setManaged(false);
		System.out.println("chek3");
		managerReportsController.tabIncome.setVisible(false);
		managerReportsController.tblUsageReportTable.setVisible(false);
		
		
	}

	private List<Label> createLabelList(ManagerReportGUIController managerReportsController) {
		List<Label> tempMenuLabels = new ArrayList<>();
		tempMenuLabels.add(managerReportsController.mnuAddOrder);
		tempMenuLabels.add(managerReportsController.mnuMyOrders);
		tempMenuLabels.add(managerReportsController.mnuMyProfile);
		tempMenuLabels.add(managerReportsController.mnuParkEntrance);
		tempMenuLabels.add(managerReportsController.mnuRegistration);
		tempMenuLabels.add(managerReportsController.mnuParkDetails);
		tempMenuLabels.add(managerReportsController.mnuEvents);
		tempMenuLabels.add(managerReportsController.mnuReportsDepartment);
		tempMenuLabels.add(managerReportsController.mnuReportsManager);
		tempMenuLabels.add(managerReportsController.mnuParkCapacity);
		tempMenuLabels.add(managerReportsController.mnuRequests);
		return tempMenuLabels;
	}

}

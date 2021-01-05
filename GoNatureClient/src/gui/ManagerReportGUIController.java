package gui;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.ReportController;
import controllers.RestartApp;
import enums.DBControllerType;
import enums.OperationType;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
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
	private Label mnuLogout;

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
	private static int month;
	private static int year;

	private LocalDate thisDay;
	private Date thisDayToDB;
	private int monthInt;
	private int dayInt;
	private int yearInt;

	@FXML
	private Label lblMonthYearMoney;

	@FXML
	private Label lblTotalIncome;

	@FXML
	private VBox vboxUserReports;
	@FXML
	private TableColumn<UsageObject, Integer> colNumOfVisitors;

	@FXML
	private TableColumn<UsageObject, Date> colUsageDate;

	@FXML
	private TableView<UsageObject> tblUsageReportTable;

	public static List<UsageObject> UsageReportData;

	/*
	 * @FXML private TableView<?> tabIncome;
	 * 
	 * @FXML private TableColumn<Date, Integer> colDate;
	 * 
	 * @FXML private TableColumn<Date, Integer> colIncome;
	 * 
	 * @FXML private VBox vboxTableIncome;
	 */

	@FXML
	void goToMainPage(MouseEvent event) {
		RestartApp.restartParameters();
		LoginGUIController login = new LoginGUIController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		login.show();
	}

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
		cmbMonths.getItems().addAll("October 2020", "November 2020", "December 2020", "January 2021", "February 2021",
				"March 2021", "April 2021", "May 2021", "June 2021", "July 2021", "August 2021", "September 2021");
		cmbMonths.setOnAction(e -> chooseMonthAndYear());

		// Initialize column of the table for income report

		colDate.setCellValueFactory(new PropertyValueFactory<IncomeObject, Date>("day"));
		colIncome.setCellValueFactory(new PropertyValueFactory<IncomeObject, Integer>("money"));

		colDate.setStyle("-fx-alignment: CENTER");
		colIncome.setStyle("-fx-alignment: CENTER");

		// Initialize the column of the usage report table
		colUsageDate.setCellValueFactory(new PropertyValueFactory<UsageObject, Date>("date"));
		colNumOfVisitors.setCellValueFactory(new PropertyValueFactory<UsageObject, Integer>("numOfVIsitors"));

		colUsageDate.setStyle("-fx-alignment: CENTER");
		colNumOfVisitors.setStyle("-fx-alignment: CENTER");

		lblMonthYearMoney.setManaged(false);
		cmbName = null;

		cmbMon = null;

		// btnMakeReport.setOnMouseClicked(e->showPopUp(e));

	}

	/**
	 * update the report name after the user choose
	 */
	private void chooseReportName() {
		setReportDetailsInvisible(this);
		cmbName = cmbReport.getValue();

	}

	/**
	 * update month and year for the report after the user choose
	 */
	private void chooseMonthAndYear() {
		setReportDetailsInvisible(this);
		cmbMon = cmbMonths.getValue();
		String monthYear = cmbMonths.getValue();

	}

	/**
	 * this method create the Usage report and present it to the manager
	 * 
	 * @param monthYear
	 */
	private void getUsageReportData(String monthYear) {
		setReportDetailsInvisible(this);
		List<String> list = new ArrayList<>();
		list = addTheCorrectMonthAndYear(list, monthYear);

		MainClient.clientConsole
				.accept(new Message(OperationType.UsageReport, DBControllerType.ReportsDBController, (Object) list));
		if (ReportController.reportType.equals(OperationType.UsageReport)) {
			UsageReport usageReport = (UsageReport) ReportController.report;

			this.lblReportName.setManaged(true);
			this.lblMonthYear.setManaged(true);

			this.lblReportName.setText("Usage Report");
			this.lblMonthYear.setText(monthYear);

			barChartOverall.setBarGap(0);
			barChartOverall.setCategoryGap(50);

			Date[] day = usageReport.getDay();
			int[] capacity = usageReport.getNumOfVisitorsInDay();
			UsageObject[] in = new UsageObject[31];

			for (int i = 0; i < 31; i++) {
				if (day[i] != null) {
					System.out.println(day[i]);
					in[i] = new UsageObject(capacity[i], day[i]);
				}
			}

			tblUsageReportTable.setItems(FXCollections.observableArrayList(in));
			this.tblUsageReportTable.setManaged(true);
			this.tblUsageReportTable.setVisible(true);
			this.vboxUserReports.setManaged(true);
			this.vboxUserReports.setVisible(true);

		}

	}

	/**
	 * this method create the Income report and present it to the manager
	 * 
	 * @param monthYear
	 */
	private void getIncomeReportData(String monthYear) {
		setReportDetailsInvisible(this);

		List<String> list = new ArrayList<>();
		list = addTheCorrectMonthAndYear(list, monthYear);

		this.lblReportName.setManaged(true);
		this.lblMonthYear.setManaged(true);

		this.lblReportName.setText("Income Report");
		this.lblMonthYear.setText(monthYear);

		MainClient.clientConsole
				.accept(new Message(OperationType.RevenueReport, DBControllerType.ReportsDBController, (Object) list));
		if (ReportController.reportType.equals(OperationType.RevenueReport)) {
			IncomeReport incomeReport = (IncomeReport) ReportController.report;

			Date[] day = incomeReport.getDay();
			int[] money = incomeReport.getMoneyInDay();
			for (Date d : day) {
				if (d != null)
					System.out.println(d);
			}

			for (int m : money) {
				if (m != 0)
					System.out.println(m);
			}

			System.out.println("total= " + incomeReport.getTotalIncome());
			this.hboxTotalIncome.setManaged(true);
			this.hboxTotalIncome.setVisible(true);

			this.lblMonthYearMoney.setText(monthYear + " : " + incomeReport.getTotalIncome() + " NIS");
			IncomeObject[] in = new IncomeObject[31];
			// List <IncomeObject> inList = new ArrayList<IncomeObject>();

			for (int i = 0; i < 31; i++) {
				if (day[i] != null) {
					in[i] = new IncomeObject(money[i], day[i]);
				}
			}

			tabIncome.setItems(FXCollections.observableArrayList(in));

			this.tabIncome.setVisible(true);

			this.tabIncome.setManaged(true);
			this.vboxTbIncome.setManaged(true);
			this.lblMonthYearMoney.setManaged(true);
			this.lblTotalIncome.setManaged(true);

		}

	}

	/**
	 * this method create the Overall report and present it to the manager
	 * 
	 * @param monthYear
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getSumReportData(String monthYear) {
		setReportDetailsInvisible(this);
		List<String> list = new ArrayList<>();
		list = addTheCorrectMonthAndYear(list, monthYear);

		MainClient.clientConsole.accept(
				new Message(OperationType.SumVisitorsReport, DBControllerType.ReportsDBController, (Object) list));
		if (ReportController.reportType.equals(OperationType.SumVisitorsReport)) {
			SumVisitorsReport sumReport = (SumVisitorsReport) ReportController.report;
			System.out.println("visitors= " + sumReport.getVisitorsAmount());
			System.out.println("members= " + sumReport.getMembersAmount());
			System.out.println("groups= " + sumReport.getGroupsAmount());

			this.vboxReportDetails.setManaged(true);
			this.lblMonthYear.setManaged(true);
			this.lblReportName.setManaged(true);

			this.lblReportName.setText("Overall Report");
			this.lblMonthYear.setText(monthYear);

			// this.barChartOverall = new BarChart<>(barChartX, barChartY);
			this.barChartOverall.getData().clear();

			XYChart.Series set1 = new XYChart.Series<>();

			set1.getData().clear();
			set1.getData().add(new XYChart.Data<>("Singles", sumReport.getVisitorsAmount()));
			set1.getData().add(new XYChart.Data<>("Members", sumReport.getMembersAmount()));
			set1.getData().add(new XYChart.Data<>("Groups", sumReport.getGroupsAmount()));

			this.barChartOverall.getData().addAll(set1);

			Node n = barChartOverall.lookup(".data0.chart-bar");
			n.setStyle("-fx-bar-fill: red");
			n = barChartOverall.lookup(".data1.chart-bar");
			n.setStyle("-fx-bar-fill: orange");
			n = barChartOverall.lookup(".data2.chart-bar");
			n.setStyle("-fx-bar-fill: green");

			barChartOverall.setBarGap(0);
			barChartOverall.setCategoryGap(50);
			this.barChartOverall.setManaged(true);
			this.barChartOverall.setVisible(true);
			this.barChartX.setManaged(true);
			this.barChartY.setManaged(true);
			this.vboxBarChart.setManaged(true);
			this.vboxBarChart.setVisible(true);

			/*
			 * this.lblAmountOfSingles.setText(String.valueOf(sumReport.getVisitorsAmount())
			 * );
			 * this.lblAmountOfMembers.setText(String.valueOf(sumReport.getMembersAmount()))
			 * ;
			 * this.lblAmountOfGroups.setText(String.valueOf(sumReport.getGroupsAmount()));
			 */
		}

	}

	/**
	 * this method adding to list the relevant data for sending it to the server for
	 * getting the appropriate information
	 * 
	 * @param list
	 * @param monthYear
	 * @return
	 */
	private List<String> addTheCorrectMonthAndYear(List<String> list, String monthYear) {
		list.add(EmployeeController.employeeConected.getOrganizationAffilation());
		if (monthYear.equals("December 2020")) {
			list.add(String.valueOf(12));
			list.add("2020");
			month = 12;
			year = 2020;
		} else if (monthYear.equals("November 2020")) {
			list.add(String.valueOf(11));
			list.add("2020");
			month = 11;
			year = 2020;
		} else if (monthYear.equals("October 2020")) {
			list.add(String.valueOf(10));
			list.add("2020");
			month = 10;
			year = 2020;
		} else if (monthYear.equals("January 2021")) {
			list.add(String.valueOf(1));
			list.add("2021");
			month = 1;
			year = 2021;
		} else if (monthYear.equals("February 2021")) {
			list.add(String.valueOf(2));
			list.add("2021");
			month = 2;
			year = 2021;
		} else if (monthYear.equals("March 2021")) {
			list.add(String.valueOf(3));
			list.add("2021");
			month = 3;
			year = 2021;
		} else if (monthYear.equals("April 2021")) {
			list.add(String.valueOf(4));
			list.add("2021");
			month = 4;
			year = 2021;
		} else if (monthYear.equals("May 2021")) {
			list.add(String.valueOf(5));
			list.add("2021");
			month = 5;
			year = 2021;
		} else if (monthYear.equals("June 2021")) {
			list.add(String.valueOf(6));
			list.add("2021");
			month = 6;
			year = 2021;
		} else if (monthYear.equals("July 2021")) {
			list.add(String.valueOf(7));
			list.add("2021");
			month = 7;
			year = 2021;
		} else if (monthYear.equals("August 2021")) {
			list.add(String.valueOf(8));
			list.add("2021");
			month = 8;
			year = 2021;
		} else if (monthYear.equals("September 2021")) {
			list.add(String.valueOf(9));
			list.add("2021");
			month = 9;
			year = 2021;
		}
		return list;

	}

	/**
	 * This is the method that activates the page
	 */

	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
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

	/**
	 * This is the method that hides the irrelevant data on the page
	 * 
	 * @param managerReportsController
	 */

	private void setReportDetailsInvisible(ManagerReportGUIController managerReportsController) {
		/*
		 * managerReportsController.vboxReportDetails.setManaged(false);
		 * managerReportsController.hboxGroups.setManaged(false);
		 * managerReportsController.hboxMembers.setManaged(false);
		 * managerReportsController.hboxSingles.setManaged(false);
		 */

		System.out.println("chek2");
		managerReportsController.lblMonthYear.setManaged(false);
		managerReportsController.lblReportName.setManaged(false);
		managerReportsController.barChartOverall.setManaged(false);
		managerReportsController.barChartOverall.getData().clear();
		managerReportsController.barChartOverall.setVisible(false);
		managerReportsController.barChartX.setManaged(false);
		managerReportsController.barChartY.setManaged(false);
		managerReportsController.vboxBarChart.setManaged(false);
		managerReportsController.vboxBarChart.setVisible(false);
		managerReportsController.tabIncome.setVisible(false);
		managerReportsController.tblUsageReportTable.setVisible(false);
		managerReportsController.tabIncome.setManaged(false);
		managerReportsController.vboxTbIncome.setManaged(false);
		managerReportsController.tblUsageReportTable.setManaged(false);
		managerReportsController.lblMonthYearMoney.setManaged(false);
		managerReportsController.lblTotalIncome.setManaged(false);
		managerReportsController.hboxTotalIncome.setManaged(false);
		managerReportsController.hboxTotalIncome.setVisible(false);
		managerReportsController.vboxUserReports.setManaged(false);
		managerReportsController.vboxUserReports.setVisible(false);

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

	/**
	 * This is a method that pops up the appropriate pop-up and summons the method
	 * that produces the appropriate report
	 * 
	 * @param event
	 */
	@FXML
	void showPopUp(MouseEvent event) {

		if (cmbMon != null && cmbName != null) {

			if (cmbName.equals("Overall")) {
				getSumReportData(cmbMon);
				System.out.println("naor");
			} else if (cmbName.equals("Income")) {
				getIncomeReportData(cmbMon);

			}

			else if (cmbReport.getValue().equals("Usage")) {
				getUsageReportData(cmbMon);
			}

			Alert a = new Alert(AlertType.INFORMATION);
			System.out.println(month + " " + year);
			getCurrentDay();

			System.out.println(monthInt);

			if (month < monthInt && year == yearInt || year < yearInt) {
				a.setHeaderText("The date of production of the report has passed.");
				a.setContentText("You can view the data that was in it.");

			} else if (month > monthInt && year == yearInt || year > yearInt) {
				a.setHeaderText("The date of production of the report has not yet arrived.");
				a.setContentText("No data available for viewing.");
				System.out.println(2);
			}

			else if (checkEndOfMonth()) {
				a.setHeaderText("Now it's the end of the month.");
				a.setContentText("The report is finally ready!");

			}

			else {
				a.setHeaderText("The report will be finalized only at the end of the month. ");
				a.setContentText("In the meantime you can view the existing data.");
			}
			a.setTitle("Report Status");
			a.showAndWait();
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
		System.out.println("now is the month " + monthInt);
		System.out.println(dayInt);
		System.out.println(thisDay.getYear());
	}

	/**
	 * this is a method that checks by the number of the month if today is the
	 * end-of-month date
	 * 
	 * @return
	 */
	private boolean checkEndOfMonth() {
		if (monthInt == 1 || monthInt == 3 || monthInt == 5 || monthInt == 7 || monthInt == 8 || monthInt == 10
				|| monthInt == 12) {
			System.out.println(monthInt + " here");
			if (dayInt != 31)
				return false;
		}

		else if (monthInt == 2) {
			if (dayInt != 28)
				return false;
		}

		else if (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11) {
			if (dayInt != 30)
				return false;
		}

		return true;
	}

}

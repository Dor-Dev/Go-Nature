package gui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import client.MainClient;
import common.Message;
import controllers.EmployeeController;
import controllers.ReportController;
import controllers.RestartApp;
import enums.DBControllerType;
import enums.OperationType;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.UsageObject;
import logic.IncomeObject;
import logic.IncomeReport;
import logic.ReportImage;
import logic.SerializableInputStream;
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
	private VBox vBoxReportSnapshot;

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
	private BarChart<String, Integer> barChartOverall;

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
    private Button btnProductToDepartment;

	@FXML
	private HBox hboxTotalIncome;
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
    private Label lblDProduceMsg;
    
	@FXML
	private TableView<UsageObject> tblUsageReportTable;

	public static List<UsageObject> UsageReportData;


	private static String cmbName = null;

	private static String cmbMon = null;
	private static int month;
	private static int year;

	private LocalDate thisDay;
	@SuppressWarnings("unused")
	private Date thisDayToDB;
	private int monthInt;
	private int dayInt;
	private int yearInt;




	/**
	 *  This method returns to the main page after the user presses on the "log out" button<br> 
	 * {@link restartParameters()} will be executed in order to reset relevant variables<br>
	 * @param event - the mouse event that occurs when the user clicks on log out
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

		lblDProduceMsg.setVisible(false);

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



			Date[] day = usageReport.getDay();
			int[] capacity = usageReport.getNumOfVisitorsInDay();
			UsageObject[] in = new UsageObject[31];

			for (int i = 0; i < 31; i++) {
				if (day[i] != null) {
					in[i] = new UsageObject(capacity[i], day[i]);
				}
			}

			tblUsageReportTable.setItems(FXCollections.observableArrayList(in));
			this.tblUsageReportTable.setManaged(true);
			this.tblUsageReportTable.setVisible(true);
			this.vboxUserReports.setManaged(true);
			this.vboxUserReports.setVisible(true);
			this.lblReportName.setManaged(true);
			this.lblReportName.setVisible(true);
			this.btnProductToDepartment.setVisible(true);
			this.btnProductToDepartment.setManaged(true);
			this.lblMonthYear.setVisible(true);
			this.lblMonthYear.setManaged(true);

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
		

			this.hboxTotalIncome.setManaged(true);
			this.hboxTotalIncome.setVisible(true);

			this.lblMonthYearMoney.setText(monthYear + " : " + incomeReport.getTotalIncome() + " NIS");
			IncomeObject[] in = new IncomeObject[31];

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
			this.lblReportName.setManaged(true);
			this.lblReportName.setVisible(true);
			this.btnProductToDepartment.setVisible(true);
			this.btnProductToDepartment.setManaged(true);
			this.lblMonthYear.setVisible(true);
			this.lblMonthYear.setManaged(true);
		}

	}

	/**
	 * this method create the Overall report and present it to the manager
	 * 
	 * @param monthYear
	 */

	private void getSumReportData(String monthYear) {

		setReportDetailsInvisible(this);
		List<String> list = new ArrayList<>();
		list = addTheCorrectMonthAndYear(list, monthYear);

		MainClient.clientConsole.accept(
				new Message(OperationType.SumVisitorsReport, DBControllerType.ReportsDBController, (Object) list));
		if (ReportController.reportType.equals(OperationType.SumVisitorsReport)) {
			SumVisitorsReport sumReport = (SumVisitorsReport) ReportController.report;


			this.vboxReportDetails.setManaged(true);
			this.lblMonthYear.setManaged(true);
			this.lblReportName.setManaged(true);

			this.lblReportName.setText("Overall Report");
			this.lblMonthYear.setText(monthYear);

			this.barChartOverall.getData().clear();

			XYChart.Series<String,Integer> setSingles = new XYChart.Series<>();
			XYChart.Series<String,Integer> setMembers = new XYChart.Series<>();
			XYChart.Series<String,Integer> setGroups = new XYChart.Series<>();

			setSingles.getData().clear();
			setMembers.getData().clear();
			setGroups.getData().clear();
			
			setSingles.getData().add(new XYChart.Data<>("", sumReport.getVisitorsAmount()));
			setSingles.setName("Singles");
			setMembers.getData().add(new XYChart.Data<>("", sumReport.getMembersAmount()));
			setMembers.setName("Members");
			setGroups.getData().add(new XYChart.Data<>("", sumReport.getGroupsAmount()));
			setGroups.setName("Groups");

			this.barChartOverall.getData().add(setSingles);
			this.barChartOverall.getData().add(setMembers);
			this.barChartOverall.getData().add(setGroups);

			barChartOverall.setBarGap(20);
			barChartOverall.setCategoryGap(80);
			this.barChartOverall.setManaged(true);
			this.barChartOverall.setVisible(true);
			this.barChartX.setManaged(true);
			this.barChartY.setManaged(true);
			this.vboxBarChart.setManaged(true);
			this.vboxBarChart.setVisible(true);
			this.lblReportName.setManaged(true);
			this.lblReportName.setVisible(true);
			this.btnProductToDepartment.setVisible(true);
			this.btnProductToDepartment.setManaged(true);
			this.lblMonthYear.setVisible(true);
			this.lblMonthYear.setManaged(true);

			 
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
			primaryStage.getIcons().add(new Image("/gui/img/icon.png"));
			ManagerReportGUIController managerReportsController = loader.getController();
			List<Label> menuLabels = new ArrayList<>();
			managerReportsController.btnProductToDepartment.setVisible(false);
			menuLabels = managerReportsController.createLabelList(managerReportsController);
			MenuBarSelection.setMenuOptions(menuLabels);
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

		managerReportsController.lblDProduceMsg.setVisible(false);
		managerReportsController.lblDProduceMsg.setManaged(false);
		managerReportsController.lblMonthYear.setManaged(false);
		managerReportsController.lblReportName.setManaged(false);
		managerReportsController.barChartOverall.setManaged(false);
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
		managerReportsController.lblReportName.setManaged(false);
		managerReportsController.lblReportName.setVisible(false);
		managerReportsController.btnProductToDepartment.setVisible(false);
		managerReportsController.btnProductToDepartment.setManaged(false);
		managerReportsController.lblMonthYear.setVisible(false);
		managerReportsController.lblMonthYear.setManaged(false);
		
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
		this.btnProductToDepartment.setVisible(true);
		this.btnProductToDepartment.setManaged(true);
		Alert a = new Alert(AlertType.INFORMATION);

		if (cmbMon != null && cmbName != null) {

			if (cmbName.equals("Overall")) {
				getSumReportData(cmbMon);
			} else if (cmbName.equals("Income")) {
				getIncomeReportData(cmbMon);

			}

			else if (cmbReport.getValue().equals("Usage")) {
				getUsageReportData(cmbMon);
			}

			getCurrentDay();

			if (month < monthInt && year == yearInt || year < yearInt) {
				a.setHeaderText("The date of production of the report has passed.");
				a.setContentText("You can view the data that was in it.");

			} else if (month > monthInt && year == yearInt || year > yearInt) {
				a.setHeaderText("The date of production of the report has not yet arrived.");
				a.setContentText("No data available for viewing.");
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
		} else {
			if (cmbName == null) {
				a.setHeaderText("The report name is not valid");
				a.setContentText("Please choose the name of the report first!");
			} else if (cmbMon == null) {
				a.setHeaderText("The report month is not valid");
				a.setContentText("Please choose the month of the report first!");

			}
			a.setTitle("Validation error");
			a.showAndWait();
		}
	}

	/**
	 * This is a method that checks the date of the day and saves it
	 */

	private void getCurrentDay() {
		thisDay = LocalDate.now();
		thisDayToDB = Date.valueOf(thisDay);
		monthInt = thisDay.getMonthValue();
		dayInt = thisDay.getDayOfMonth();
		yearInt = thisDay.getYear();

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

	@FXML
	void popUpReport(ActionEvent event) {

		this.btnProductToDepartment.setVisible(false);
		this.btnProductToDepartment.setManaged(false);
		tblUsageReportTable.setMinHeight(800);	//we need to print all rows so get bigger window
		tabIncome.setMinHeight(800);	//we need to print all rows so get bigger window
		WritableImage snapshot = vBoxReportSnapshot.snapshot(new SnapshotParameters(), null);
		ImageView reportImage = new ImageView(snapshot); 
		Stage reportStage = new Stage();
		StackPane myLayout2 = new StackPane();
		Scene myScene2 = new Scene(myLayout2);
		Label label2 = new Label("", reportImage);
		myLayout2.getChildren().removeAll();
		myLayout2.getChildren().add(label2);
		reportStage.setTitle("Report");
		reportStage.setScene(myScene2);
		reportStage.show();
		tblUsageReportTable.setMinHeight(300);
		tabIncome.setMinHeight(300);
		this.lblDProduceMsg.setVisible(true);
		this.lblDProduceMsg.setManaged(true);
		BufferedImage bImage = SwingFXUtils.fromFXImage(reportImage.getImage(), null);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, "png", outputStream);
			byte[] res = outputStream.toByteArray();
			InputStream inputStream = new ByteArrayInputStream(res);
			SerializableInputStream serializableInputStream = new SerializableInputStream (inputStream);
			LocalDate localDate = LocalDate.now();
			Date date = Date.valueOf(localDate);
			ReportImage report = new ReportImage(cmbName + "-"+cmbMon, serializableInputStream, date,EmployeeController.employeeConected.getOrganizationAffilation());
			MainClient.clientConsole.accept(
					new Message(OperationType.SubmitReport, DBControllerType.ReportsDBController, (Object) report));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

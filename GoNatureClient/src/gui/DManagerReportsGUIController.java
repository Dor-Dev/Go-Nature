package gui;

import java.beans.EventHandler;
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
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.CancellationReport;
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

	@FXML
	private VBox vBoxCancellation;

	@FXML
	private PieChart chrtCancellation;

	@FXML
	private Label lblCanceledData;

	@FXML
	private Label lblUnfulfilledData;
	
	@FXML
	private Label lblTotalOrders;

	private static String reportName = null;
	private static String parkName = null;
	private static String type = null;
	private static String date = null;
	private LocalDate thisDay;
	private Date thisDayToDB;
	private int monthInt;
	private int dayInt;
	private int yearInt;
	private boolean visible=false;
	private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
	/*
	 * This method is used when the user clicks on the log-out button after clicking
	 * the log-out button, every "exposed variable" will be reset in order to
	 * restart the app the re-settng is done by the static method -
	 * "restartParameters"
	 */
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

	/*
	 * A method for initializing the different buttons we are using 
	 */
	@FXML
	public void initialize() {
		
		//Initializing the combo-boxes values
		//to prevent unwanted data from being used after it is no longer needed
		datePicker.setValue(null);
		cmbParkName.setValue(null);
		cmbReportName.setValue(null);
		cmbType.setValue(null);
		
		 //Initialize the report type combo-box 	 
		cmbReportName.getItems().removeAll(cmbReportName.getItems());
		cmbReportName.getItems().addAll("Visiting report", "Cancellation report");
		cmbReportName.setOnAction(e -> chooseReportName());

		//Initialize the visitor type combo-box
		cmbType.getItems().removeAll(cmbType.getItems());
		cmbType.getItems().addAll("Singles", "Groups", "Members");
		cmbType.setOnAction(e -> chooseTypes());

		//Initialize the park name combo-box 
		cmbParkName.getItems().removeAll(cmbParkName.getItems());
		cmbParkName.getItems().addAll("Luna-Park", "Shipment-Park", "Tempo-Park");
		cmbParkName.setOnAction(e -> chooseParkName());

		//setting the action for saving the chosen date
		datePicker.setOnAction(e->chooseDate());
		
		//Initialize the pie chart data and the list that contains it
		pieChartData.clear();		
		chrtCancellation.getData().clear();
		
		//disabling the option the write the date manually in the date-picker
		datePicker.getEditor().setDisable(true);
		
		//setting the pie chart data labels to be empty
		lblCanceledData.setText(null);
		lblUnfulfilledData.setText(null);
		lblTotalOrders.setText(null);
		
	}

	/*
	 * Save the date after the user chooses it.
	 */
	private void chooseDate() {
		if(datePicker.getValue()!=null)
			date = datePicker.getValue().format((DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	/**
	 * Save the park name after the user chooses it.
	 */

	private void chooseParkName() {
		parkName = cmbParkName.getValue();

	}

	/**
	 * Save the type of visitors after the user chooses it.
	 */
	private void chooseTypes() {
		if (cmbType.getValue().equals("Singles"))
			type = "visitor";
		else if (cmbType.getValue().equals("Groups"))
			type = "instructor";
		else if (cmbType.getValue().equals("Members"))
			type = "member";

	}

	/**
	 * save the report name after the user chooses it, The visibility of the buttons
	 * will change according to the chosen report.
	 */
	private void chooseReportName() {
		reportName = cmbReportName.getValue();

		if (reportName.equals("Visiting report")) {
			this.cmbType.setVisible(true);
			this.lblType.setVisible(true);
			this.cmbType.setManaged(true);
			this.lblType.setManaged(true);
			this.datePicker.setManaged(true);
			this.lblDate.setManaged(true);
		} else if (reportName.equals("Cancellation report")) {
			this.cmbType.setManaged(false);
			this.lblType.setManaged(false);
			this.cmbType.setVisible(false);
			this.lblType.setVisible(false);
			this.datePicker.setManaged(true);
			this.lblDate.setManaged(true);
		}
	}

	/**
	 * A method that displays an appropriate pop-up after a date is chosen
	 * considering the report's type and the current date.
	 * 
	 * @param event
	 */
	@FXML
	void showReportDetails(MouseEvent event) {

		this.vboxVisiting.setManaged(false);
		this.vboxVisiting.setVisible(false);
		this.barChartVisiting.setManaged(false);
		this.barChartVisiting.setVisible(false);
		this.barChartX.setManaged(false);
		this.barChartX.setVisible(false);
		this.barChartY.setManaged(false);
		this.barChartY.setVisible(false);
		setCancellationReportInVisible(this);
		//Calling the validation method in order to make sure all of the values have been picked
		validation();
		
		getCurrentDay();// Saving the current date in static variables
		
		// Creating a pop-up that will be used to present information
		Alert a = new Alert(AlertType.INFORMATION);
					
		// The chosen report is the visiting report,
	//	if (reportName.equals("Visiting report")) {
		if (date != null && type != null && parkName != null && reportName.equals("Visiting report")) {
			
			// check if the date has arrived yet by
			// comparing the current day, month and year to the chosen ones.
			// and present a pop-up with the appropriate message
			if (Integer.valueOf(date.substring(0, 4)) > yearInt
					|| Integer.valueOf(date.substring(5, 7)) > monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt
					|| Integer.valueOf(date.substring(5, 7)) == monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt
							&& dayInt < Integer.valueOf(date.substring(8, 10))) {
				a.setHeaderText("The date of production of the report has not yet arrived.");
				a.setContentText("No data available for viewing.");
				a.setTitle("Report Status");
				a.showAndWait();
				return;
			}
			// check if the date has passed by
			// comparing the current day, month and year to the chosen ones.
			// and present a pop-up with the appropriate message
			else if (Integer.valueOf(date.substring(5, 7)) < monthInt
					&& Integer.valueOf(date.substring(0, 4)) == yearInt
					|| Integer.valueOf(date.substring(0, 4)) < yearInt) {
				a.setHeaderText("The date of production of the report has passed.");
				a.setContentText("You can view the data that was in it.");
			} else {
				a.setHeaderText("Report Status");
				a.setContentText("You can view the existing data of the report.");
			}
			a.setTitle("Report Status");
			a.showAndWait();
			//Call the Cancellation Report method in order to display the data
			showVisitingReport(this);
		}
		//the chosen report is cancellation
		else 
			//if (reportName.equals("Cancellation report")) {
			if (date != null && parkName != null && reportName.equals("Cancellation report")) {
				
			// check if the date is the current date
			// comparing the current day, month and year to the chosen ones.
			// and present a pop-up with the appropriate message
			if (Integer.valueOf(date.substring(0, 4)) == yearInt && Integer.valueOf(date.substring(5, 7)) == monthInt
					&& Integer.valueOf(date.substring(8, 10)) == dayInt) {
				a.setHeaderText("Cancellation Report can't be produced at the current day");
				a.setContentText("No data available for viewing.");
				a.setTitle("Report Status");
				a.showAndWait();
				return;
			} 
			// check if the date has arrived yet
			// comparing the current day, month and year to the chosen ones.
			// and present a pop-up with the appropriate message
			else if (Integer.valueOf(date.substring(0, 4)) > yearInt
					|| (Integer.valueOf(date.substring(5, 7)) > monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt)
					|| (Integer.valueOf(date.substring(5, 7)) == monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt
							&& Integer.valueOf(date.substring(8, 10)) > dayInt)) {
				a.setHeaderText("The date of production of the report has not yet arrived.");
				a.setContentText("No data available for viewing.");
				a.setTitle("Report Status");
				a.showAndWait();
				return;
			}
			if(visible == false)
				setCancellationReportVisible(this);
			
			//Call the Cancellation Report method in order to display the data
			showCancellationReport(this);
		}
		
		
	

	}

	/*
	 * A method that displays the cancellation report to the department manager 
	 * with data gotten from the DataBase
	 * it displays -
	 * 1-canceled orders amount
	 * 2-unfulfilled orders amount
	 * out of the total orders in a pie chart.
	 */
	private void showCancellationReport(DManagerReportsGUIController dManagerReports) {
		//a list with the chosen information to send to the server 
				List<String> list = new ArrayList<String>();
				list.add(parkName);
				list.add(date);
				MainClient.clientConsole.accept(
						new Message(OperationType.CancellationReport, DBControllerType.ReportsDBController, (Object) list));
				//after receiving valid information from the server - display it to the screen
				if (ReportController.reportType.equals(OperationType.CancellationReport)) {
					
					
					
					//Save the received report 
					CancellationReport cancellationReport = (CancellationReport) ReportController.report;
					
					//if the cancellation report is visible 
					//make it invisible 
					if (cancellationReport.getTotalOrderAmount()==0) {
						if(visible == true)
							setCancellationReportInVisible(dManagerReports);
						// Creating a pop-up for alerting there is not information to be displayed
						Alert a = new Alert(AlertType.INFORMATION);
						a.setHeaderText("There is no available information at the chosen production date.");
						a.setContentText("Please, choose a different date.");
						a.setTitle("Report Status");
						a.showAndWait();
						return;
					}
					else {//if the cancellation report is invisible 
						//make it visible 
						if(visible == false)
							setCancellationReportVisible(dManagerReports);
						dManagerReports.lblReportName.setText("Cancellation Report at " + date);
						setPieChart(dManagerReports, cancellationReport.getCanceledOrdersCounter(),
								cancellationReport.getUnfulfilledOrderAmount(),
								cancellationReport.getTotalOrderAmount()-cancellationReport.getCanceledOrdersCounter()-cancellationReport.getUnfulfilledOrderAmount());
						//Displaying additional information about the orders
						dManagerReports.lblTotalOrders.setText("Total orders amount - " + cancellationReport.getTotalOrderAmount());
						dManagerReports.lblTotalOrders.setVisible(true);
						dManagerReports.lblTotalOrders.setManaged(true);
						dManagerReports.lblCanceledData.setText("Canceled orders amount - " + cancellationReport.getCanceledOrdersCounter()
								+ " (Visitors amount - " + cancellationReport.getCanceledVisitorsAmount() + ")");
						dManagerReports.lblUnfulfilledData
								.setText("Unfulfilled orders amount - " + cancellationReport.getUnfulfilledOrderAmount()
										+ " (Visitors amount - " + cancellationReport.getUnfulfilledVisitorAmount() + ")");
						
					}
					
					
					
					
				}

	}

	
	/**
	 * this method create the Visiting report and present it to the manager
	 * 
	 * @param dManagerReportsGUIController
	 */
	private void showVisitingReport(DManagerReportsGUIController dManagerReportsGUIController) {
		List<String> list = new ArrayList<String>();
		list.add(parkName);
		list.add(date);
		list.add(type);
		MainClient.clientConsole
				.accept(new Message(OperationType.VisitingReport, DBControllerType.ReportsDBController, (Object) list));

		if (ReportController.reportType.equals(OperationType.VisitingReport)) {
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

			this.lblReportName.setText("Visiting Report for " + cmbType.getValue() + " in " + date);

			int[] hours = visitingReport.getHours();
			int[] visitors = visitingReport.getAmountOfVisitors();

			this.barChartVisiting.getData().clear();

			XYChart.Series set1 = new XYChart.Series<>();
			set1.getData().clear();
			for (int i = 0; i < hours.length; i++) {
				if (hours[i] != 0) {
					System.out.println(hours[i]);
					set1.getData().add(new XYChart.Data<>(String.valueOf(hours[i] + ":00"), visitors[i]));

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
		System.out.println("now is the month " + monthInt);
		System.out.println(dayInt);
		System.out.println(thisDay.getYear());
	}

	/**
	 * This is the method that activates the page
	 */
	public void show() {
		VBox root;
		Stage primaryStage = new CloseStage();
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
	 * 
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
		dManagerReportsController.lblDate.setManaged(false);
		dManagerReportsController.datePicker.setManaged(false);
		dManagerReportsController.lblType.setManaged(false);
		dManagerReportsController.cmbType.setManaged(false);
		dManagerReportsController.vBoxCancellation.setManaged(false);
		dManagerReportsController.chrtCancellation.setManaged(false);
		dManagerReportsController.vBoxCancellation.setVisible(false);
		dManagerReportsController.chrtCancellation.setVisible(false);
		dManagerReportsController.lblCanceledData.setVisible(false);
		dManagerReportsController.lblUnfulfilledData.setVisible(false);
		dManagerReportsController.lblTotalOrders.setVisible(false);
		dManagerReportsController.lblCanceledData.setManaged(false);
		dManagerReportsController.lblUnfulfilledData.setManaged(false);
		dManagerReportsController.lblTotalOrders.setManaged(false);
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
	
	/*
	 * A method that checks every time if any of the combo-boxes values weren't chosen
	 * and displays an appropriate pop-up
	 */
	private void validation() {
		String report = this.cmbReportName.getValue();
		String park = this.cmbParkName.getValue();
		LocalDate date = this.datePicker.getValue();
		String visitorType = this.cmbType.getValue(); 
		Alert a = new Alert(AlertType.INFORMATION);	
		 
		if(report==null) {
			String boxes = "";
			if(park==null) {
				boxes+="'Report-Name'\n";
				boxes+="'Park-Name'\n";		
			}else 
				boxes+="'Report-Name' ";
			if(!boxes.equals("")) {
			a.setHeaderText("Error, the following boxes aren't picked yet!\n Please choose the values before clicking on produce report.");
			a.setContentText( boxes);
			a.setTitle("Report Status");
			a.showAndWait();
			return;	}
		}else if(report.equals("Cancellation report")){
			String boxes = "";
			
			if(park == null)
				boxes += "'Park-Name'\n";
			
			if(date == null)
				boxes+="'Date'\n";
			
			if(!boxes.equals("")) {
				a.setHeaderText("Error, the following boxes aren't picked yet!\n Please choose the values before clicking on 'Produce Report'.");
				a.setContentText( boxes);
				a.setTitle("Report Status");
				a.showAndWait();
				return;
				}
		}else if(report.equals("Visiting report")) {
			String boxes = "";
			
			if(park == null)
				boxes += "'Park-Name'\n";
			
			if(date == null) 
				boxes+="'Date'\n";
			
			if(visitorType == null)
				boxes+="'Type'\n";
			
			if(!boxes.equals("")) {
			a.setHeaderText("Error, the following boxes aren't picked yet!\n Please choose the values before clicking on 'Produce Report'.");
			a.setContentText( boxes );
			a.setTitle("Report Status");
			a.showAndWait();
			return;
			}
		}else
			return;
		
	}
	
	/*
	 * A method used to set the Cancellation report GUI to be visible 
	 */
	private void setCancellationReportInVisible(DManagerReportsGUIController dManagerReports) {
		//A boolean variable that saves whether the cancellation report is visible or not
		//in this case it's visible 
		visible = false;
		dManagerReports.chrtCancellation.setData(null);
		dManagerReports.vboxReportName.setManaged(false);
		dManagerReports.lblReportName.setManaged(false);
		dManagerReports.vboxReportName.setVisible(false);
		dManagerReports.lblReportName.setVisible(false);
		dManagerReports.lblReportName.setManaged(false);

		dManagerReports.vBoxCancellation.setManaged(false);
		dManagerReports.vBoxCancellation.setVisible(false);

		dManagerReports.chrtCancellation.setManaged(false);
		dManagerReports.chrtCancellation.setVisible(false);

		dManagerReports.lblCanceledData.setVisible(false);
		dManagerReports.lblUnfulfilledData.setVisible(false);
		dManagerReports.lblTotalOrders.setVisible(false);
		dManagerReports.lblCanceledData.setManaged(false);
		dManagerReports.lblUnfulfilledData.setManaged(false);
		dManagerReports.lblTotalOrders.setManaged(false);
		
		
	}

	/*
	 * A method used to set the Cancellation report GUI to be invisible 
	 */
	private void setCancellationReportVisible(DManagerReportsGUIController dManagerReports) {
		//A boolean variable that saves whether the cancellation report is visible or not
		//in this case it's visible 
		visible = true;
		
		dManagerReports.vboxReportName.setManaged(true);
		dManagerReports.lblReportName.setManaged(true);
		dManagerReports.vboxReportName.setVisible(true);
		dManagerReports.lblReportName.setVisible(true);
		dManagerReports.lblReportName.setManaged(true);

		dManagerReports.vBoxCancellation.setManaged(true);
		dManagerReports.vBoxCancellation.setVisible(true);

		dManagerReports.chrtCancellation.setManaged(true);
		dManagerReports.chrtCancellation.setVisible(true);

		dManagerReports.lblCanceledData.setVisible(true);
		dManagerReports.lblUnfulfilledData.setVisible(true);
		dManagerReports.lblTotalOrders.setVisible(true);
		dManagerReports.lblCanceledData.setManaged(true);
		dManagerReports.lblUnfulfilledData.setManaged(true);
		dManagerReports.lblTotalOrders.setManaged(true);
	}
	
	/*
	 * a method used to insert data to the pie chart
	 */
	private void setPieChart(DManagerReportsGUIController dManagerReports, int canceledOrders, int unfulfilledOrders, int restOfOrders) {
		pieChartData.clear();
		
		pieChartData.add(new PieChart.Data("Rest of orders", restOfOrders));
		pieChartData.add(new PieChart.Data("Canceled orders", canceledOrders));
		pieChartData.add(new PieChart.Data("Unfulfilled orders", unfulfilledOrders));
		
		//setting the percentage of each slice in the pie chart
		pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat
				(data.getName(), " - ",data.pieValueProperty().multiply(100 / (canceledOrders+unfulfilledOrders+restOfOrders)), "%")));
	
		
		dManagerReports.chrtCancellation.setData(pieChartData);
	}
	}


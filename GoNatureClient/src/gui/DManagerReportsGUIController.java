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
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.CancellationReport;
import logic.Order;
import logic.ReportImage;
import logic.VisitingReport;

public class DManagerReportsGUIController {
	private static DManagerReportsGUIController dManagerReportsController = null;
	public static List<ReportImage> reports = null;

	@FXML
	private VBox vboxReceivedReports;

	@FXML
	private TableView<ReportImage> tblReports;

	@FXML
	private TableColumn<ReportImage, Integer> colReportID;

	@FXML
	private TableColumn<ReportImage, String> colParkName;

	@FXML
	private TableColumn<ReportImage, String> colReportName;

	@FXML
	private TableColumn<ReportImage, Date> colDate;

	@FXML
	private TableColumn<ReportImage, Void> colView;
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
	private Label lblChooseParkName;

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
		cmbReportName.getItems().addAll("Visiting report", "Cancellation report", "Park Manager's Reports");
		cmbReportName.setOnAction(e -> chooseReportName());
		// cmbParkName.getSelectionModel().select("Option B");

		cmbType.getItems().removeAll(cmbType.getItems());
		cmbType.getItems().addAll("Singles", "Groups", "Members");
		cmbType.setOnAction(e -> chooseTypes());

		cmbParkName.getItems().removeAll(cmbParkName.getItems());
		cmbParkName.getItems().addAll("Luna-Park", "Shipment-Park", "Tempo-Park");
		cmbParkName.setOnAction(e -> chooseParkName());

		// init the Table view of reports
		colReportID.setCellValueFactory(new PropertyValueFactory<>("reportID"));
		colParkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colReportName.setCellValueFactory(new PropertyValueFactory<>("reportName"));

		colReportID.setStyle("-fx-alignment: CENTER");
		colParkName.setStyle("-fx-alignment: CENTER");
		colDate.setStyle("-fx-alignment: CENTER");
		colReportName.setStyle("-fx-alignment: CENTER");

	}

	/**
	 * Add View button to each row in the table
	 */
	private void addRelevantButton() {
		Callback<TableColumn<ReportImage, Void>, TableCell<ReportImage, Void>> cellFactory = new Callback<TableColumn<ReportImage, Void>, TableCell<ReportImage, Void>>() {
			@Override
			public TableCell<ReportImage, Void> call(final TableColumn<ReportImage, Void> param) {
				final TableCell<ReportImage, Void> cell = new TableCell<ReportImage, Void>() {

					private final Button btnView = new Button("View");
					{
						// Action when press on View button
						btnView.setOnAction((ActionEvent event) -> {
							ReportImage report = getTableView().getItems().get(getIndex());
						});

					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {

							HBox hBox2 = new HBox(btnView);
							setGraphic(hBox2);
							hBox2.setAlignment(Pos.BASELINE_RIGHT);
							return;

						}

					}

				};

				return cell;

			}
		};
		colView.setCellFactory(cellFactory);
	}

	/**
	 * update the park name after the user choose
	 */

	private void chooseParkName() {
		parkName = cmbParkName.getValue();

	}

	/**
	 * update the type of visitors after the user choose
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
	 * update the report name after the user choose
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
			this.datePicker.setVisible(true);
			this.lblDate.setVisible(true);
			this.cmbParkName.setManaged(true);
			this.cmbParkName.setVisible(true);
			this.btnProduceReport.setVisible(true);
			this.lblChooseParkName.setVisible(true);
		} else if (reportName.equals("Cancellation report")) {
			this.cmbType.setManaged(false);
			this.lblType.setManaged(false);
			this.cmbType.setVisible(false);
			this.lblType.setVisible(false);
			this.datePicker.setManaged(true);
			this.datePicker.setVisible(true);
			this.lblDate.setManaged(true);
			this.lblDate.setVisible(true);
			this.cmbParkName.setManaged(true);
			this.cmbParkName.setVisible(true);
			this.btnProduceReport.setVisible(true);
			this.lblChooseParkName.setVisible(true);
		} else if (reportName.equals("Park Manager's Reports")) {
			this.cmbType.setManaged(false);
			this.lblType.setManaged(false);
			this.cmbType.setVisible(false);
			this.lblType.setVisible(false);
			this.datePicker.setManaged(false);
			this.lblDate.setManaged(false);
			this.datePicker.setVisible(false);
			this.lblDate.setVisible(false);
			this.cmbParkName.setManaged(false);
			this.cmbParkName.setVisible(false);
			this.btnProduceReport.setVisible(false);
			this.lblChooseParkName.setVisible(false);
			setReportDetailsInvisible(dManagerReportsController);
			dManagerReportsController.vboxReceivedReports.setManaged(true);
			dManagerReportsController.vboxReceivedReports.setVisible(true);
			dManagerReportsController.tblReports.setVisible(true);
			dManagerReportsController.tblReports.setManaged(true);
			showReceivedReportsTable();

		}
	}

	/**
	 * Send message to server to get all the reports for the department manager
	 */
	private void showReceivedReportsTable() {
		MainClient.clientConsole.accept(new Message(OperationType.GetReceivedReportsTable,
				DBControllerType.RequestsDBController, (Object) "Get Received Reports"));
		if (reports != null) {
			tblReports.setItems(FXCollections.observableArrayList(reports));
			addRelevantButton();
		}

	}

	/**
	 * This is a method that pops up the appropriate pop-up and summons the method
	 * that produces the appropriate report
	 * 
	 * @param event
	 */
	@FXML
	void showReportDetails(MouseEvent event) {
		// setReportDetailsInvisible(this);

		this.vboxVisiting.setManaged(false);
		this.vboxVisiting.setVisible(false);
		this.barChartVisiting.setManaged(false);
		this.barChartVisiting.setVisible(false);
		this.barChartX.setManaged(false);
		this.barChartX.setVisible(false);
		this.barChartY.setManaged(false);
		this.barChartY.setVisible(false);
		this.vBoxCancellation.setManaged(false);
		this.vBoxCancellation.setVisible(false);
		this.chrtCancellation.setManaged(false);
		this.chrtCancellation.setVisible(false);

		date = datePicker.getValue().format((DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		getCurrentDay();
		System.out.println(date.substring(0, 4));
		System.out.println(date.substring(5, 7));
		System.out.println(Integer.valueOf(date.substring(8, 10)));
		Alert a = new Alert(AlertType.INFORMATION);
		if (reportName.equals("Visiting report")) {
			if (Integer.valueOf(date.substring(0, 4)) > yearInt
					|| Integer.valueOf(date.substring(5, 7)) > monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt
					|| Integer.valueOf(date.substring(5, 7)) == monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt
							&& dayInt < Integer.valueOf(date.substring(8, 10))) {
				System.out.println(1155533);
				a.setHeaderText("The date of production of the report has not yet arrived.");

				a.setContentText("No data available for viewing.");
				a.setTitle("Report Status");
				a.showAndWait();
				return;
			} else if (Integer.valueOf(date.substring(5, 7)) < monthInt
					&& Integer.valueOf(date.substring(0, 4)) == yearInt
					|| Integer.valueOf(date.substring(0, 4)) < yearInt) {
				a.setHeaderText("The date of production of the report has passed.");
				a.setContentText("You can view the data that was in it.");
			} else {

				a.setHeaderText("Report Status");
				a.setContentText("You can view the existing data of the report.");
			}
		}

		else if (reportName.equals("Cancellation report")) {

			if (Integer.valueOf(date.substring(0, 4)) == yearInt && Integer.valueOf(date.substring(5, 7)) == monthInt
					&& Integer.valueOf(date.substring(8, 10)) == dayInt) {
				System.out.println("today sint good");
				a.setHeaderText("Cancellation Report can't be produced at the current day");
				a.setContentText("No data available for viewing.");
				a.setTitle("Report Status");
				a.showAndWait();
				return;
			} else if (Integer.valueOf(date.substring(0, 4)) > yearInt
					|| (Integer.valueOf(date.substring(5, 7)) > monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt)
					|| (Integer.valueOf(date.substring(5, 7)) == monthInt
							&& Integer.valueOf(date.substring(0, 4)) == yearInt
							&& Integer.valueOf(date.substring(8, 10)) > dayInt)) {
				System.out.println(1155533);
				a.setHeaderText("The date of production of the report has not yet arrived.");
				a.setContentText("No data available for viewing.");
				a.setTitle("Report Status");
				a.showAndWait();
				return;
			} else {

				a.setHeaderText("Report Status");
				a.setContentText("You can view the existing data of the report.");
			}
		}
		a.setTitle("Report Status");
		a.showAndWait();

		if (date != null && type != null && parkName != null && reportName.equals("Visiting report")) {
			showVisitingReport(this);
		} else if (date != null && parkName != null && reportName.equals("Cancellation report")) {
			showCancellationReport(this);

		}

	}

	private void showCancellationReport(DManagerReportsGUIController dManagerReportsGUIController) {

		this.lblCanceledData.setText("");
		this.lblUnfulfilledData.setText("");
		this.lblTotalOrders.setText("");
		List<String> list = new ArrayList<String>();
		list.add(parkName);
		list.add(date);
		MainClient.clientConsole.accept(
				new Message(OperationType.CancellationReport, DBControllerType.ReportsDBController, (Object) list));
		if (ReportController.reportType.equals(OperationType.CancellationReport)) {
			System.out.println("cancellation selected");
			CancellationReport cancellationReport = (CancellationReport) ReportController.report;
			this.vboxReportName.setManaged(true);
			this.lblReportName.setManaged(true);
			this.vboxReportName.setVisible(true);
			this.lblReportName.setVisible(true);
			this.lblReportName.setManaged(true);

			this.vBoxCancellation.setManaged(true);
			this.vBoxCancellation.setVisible(true);

			this.chrtCancellation.setManaged(true);
			this.chrtCancellation.setVisible(true);

			this.lblReportName.setText("Cancellation Report at " + date);

			if (cancellationReport.getCanceledOrdersCounter() == 0
					&& cancellationReport.getUnfulfilledOrderAmount() == 0) {
				this.chrtCancellation.setManaged(false);
				this.chrtCancellation.setVisible(false);
				this.lblTotalOrders.setText("There is no available information for this date!");
			} else {

				this.chrtCancellation.setManaged(true);
				this.chrtCancellation.setVisible(true);
				this.lblTotalOrders.setText("");

				ObservableList<PieChart.Data> pieCancellationData = FXCollections.observableArrayList(
						new PieChart.Data("Canceled orders", cancellationReport.getCanceledOrdersCounter()),
						new PieChart.Data("Unfulfilled orders", cancellationReport.getUnfulfilledOrderAmount()),
						new PieChart.Data("Rest of orders",
								cancellationReport.getTotalOrderAmount()
										- cancellationReport.getUnfulfilledOrderAmount()
										- cancellationReport.getCanceledOrdersCounter())

				);

				pieCancellationData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " - ",
						data.pieValueProperty().multiply(100 / cancellationReport.getTotalOrderAmount()), "%")));
				this.chrtCancellation.setData(pieCancellationData);
				this.lblTotalOrders.setText("Total orders amount - " + cancellationReport.getTotalOrderAmount());
				this.lblCanceledData.setText("Canceled orders amount - " + cancellationReport.getCanceledOrdersCounter()
						+ " (Visitors amount - " + cancellationReport.getCanceledVisitorsAmount() + ")");
				this.lblUnfulfilledData
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
			dManagerReportsController = loader.getController();
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
		dManagerReportsController.vboxReceivedReports.setManaged(false);
		dManagerReportsController.vboxReceivedReports.setVisible(false);
		dManagerReportsController.tblReports.setVisible(false);
		dManagerReportsController.tblReports.setManaged(false);

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

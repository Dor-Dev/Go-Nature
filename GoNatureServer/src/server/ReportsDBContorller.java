package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import common.Message;
import enums.ClientControllerType;
import enums.OperationType;
import logic.CancellationReport;
import logic.IncomeReport;
import logic.Order;
import logic.Park;
import logic.ReportImage;
import logic.SerializableInputStream;
import logic.SumVisitorsReport;
import logic.UsageReport;
import logic.VisitingReport;

public class ReportsDBContorller {

	private static LocalDate thisDay;
	private static Date thisDayToDB;
	private static Date startMonthToDB, endMonthToDB;
	private static SqlConnection sqlConnection = null;
	private static LocalDate startMonth, endMonth;
	private static String month;
	private static String year;

	public ReportsDBContorller() {
		try {
			sqlConnection = SqlConnection.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Message parseData(Message clientMsg) {
		PreparedStatement pstm;
		ResultSet rs;
		List<String> info;
		int i = 0;

		System.out.println("try 4");

		switch (clientMsg.getOperationType()) {

		case SubmitReport:
			PreparedStatement preparedStmt;
			ReportImage report =(ReportImage)clientMsg.getObj();
			System.out.println("REPORT DB START");
			SerializableInputStream serInput = (SerializableInputStream) report.getReportImage();
			// the mysql insert statement
			String query = " insert into issuedreports (date, reportname, image,parkname )"
					+ " values (?, ?, ?, ?)";

			// create the mysql insert preparedstatement
			try {
				preparedStmt = sqlConnection.connection.prepareStatement(query);
				preparedStmt.setDate(1, report.getDate());
				preparedStmt.setString(2, report.getReportName());
				preparedStmt.setBlob(3, report.getReportImage(),serInput.getData().length);
				preparedStmt.setString(4, report.getParkName());
				// execute the preparedstatement
				preparedStmt.execute();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new Message(OperationType.SubmitReport, ClientControllerType.ReportController,
					(Object) "Success");

		case SumVisitorsReport:
			String[] types = new String[3];

			int[] amount = new int[3];

			int visitorsAmount = 0;
			int membersAmount = 0;
			int groupsAmount = 0;
			System.out.println("try 5");

			info = (ArrayList<String>) clientMsg.getObj();
			month = info.get(1);
			year = info.get(2);
			getCurrentTime();
			try {

				pstm = sqlConnection.connection.prepareStatement(
						"SELECT type, SUM(actualNumOfVisitors) from receipts where parkName=? and  date<=? and date>=? group by type");
				pstm.setString(1, info.get(0));
				pstm.setDate(2, endMonthToDB);
				pstm.setDate(3, startMonthToDB);
				rs = pstm.executeQuery();
				System.out.println(rs);
				System.out.println(startMonthToDB);
				System.out.println("sumVisotor1");
				for (i = 0; i < 3; i++) {
					types[i] = "?";
					System.out.println("??");
				}
				i = 0;
				while (rs.next()) {
					System.out.println(22);
					types[i] = rs.getString(1);
					amount[i] = rs.getInt(2);
					i++;
				}
			} catch (SQLException e) {
			}

			for (i = 0; i < 3; i++) {
				if (types[i].equals("member")) {
					membersAmount = amount[i];
				} else if (types[i].equals("visitor")) {
					visitorsAmount = amount[i];
				} else if (types[i].equals("instructor")) {
					groupsAmount = amount[i];
				}

			}

			System.out.println("try6");

			SumVisitorsReport sumVisitorsReport = new SumVisitorsReport(info.get(0), month, year, visitorsAmount,
					membersAmount, groupsAmount);

			System.out.println("visitors= " + visitorsAmount);
			System.out.println("members= " + membersAmount);
			return new Message(OperationType.SumVisitorsReport, ClientControllerType.ReportController,
					(Object) sumVisitorsReport);
			
		case RevenueReport:
			System.out.println("RevenueReport1");

			info = (ArrayList<String>) clientMsg.getObj();
			month = info.get(1);
			year = info.get(2);
			getCurrentTime();
			System.out.println("RevenueReport2");
			try {
				pstm = sqlConnection.connection.prepareStatement(
						"SELECT date, SUM(cost) from receipts where parkName=? and  date<=? and date>=? group by date");
				pstm.setString(1, info.get(0));
				pstm.setDate(2, endMonthToDB);
				pstm.setDate(3, startMonthToDB);
				rs = pstm.executeQuery();
				Date[] day = new Date[31];
				int[] moneyInDay = new int[31];
				i = 0;
				int totalInCome = 0;

				System.out.println("RevenueReport3");

				while (rs.next()) {
					System.out.println(22);
					day[i] = rs.getDate(1);
					moneyInDay[i] = rs.getInt(2);
					i++;

				}

				pstm = sqlConnection.connection
						.prepareStatement("SELECT SUM(cost) from receipts where parkName=? and  date<=? and date>=?");
				pstm.setString(1, info.get(0));
				pstm.setDate(2, endMonthToDB);
				pstm.setDate(3, startMonthToDB);
				rs = pstm.executeQuery();

				if (rs.next()) {
					totalInCome = rs.getInt(1);
					System.out.println(totalInCome);
				}
				System.out.println("RevenueReport4");

				IncomeReport incomeReport = new IncomeReport(info.get(0), month, year, day, moneyInDay, totalInCome);
				return new Message(OperationType.RevenueReport, ClientControllerType.ReportController,
						(Object) incomeReport);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;

		case UsageReport:

			info = (ArrayList<String>) clientMsg.getObj();
			month = info.get(1);
			year = info.get(2);
			getCurrentTime();
			try {
				pstm = sqlConnection.connection.prepareStatement(
						"SELECT date, SUM(actualNumOfVisitors) from receipts where parkName=? and  date<=? and date>=? group by date");
				pstm.setString(1, info.get(0));
				pstm.setDate(2, endMonthToDB);
				pstm.setDate(3, startMonthToDB);
				rs = pstm.executeQuery();
				Date[] day = new Date[31];
				int[] numOfVIsitorsInDay = new int[31];
				i = 0;
				int totalVisitorNum = 0;
				Park park = null;
				while (rs.next()) {
					System.out.println(22);
					day[i] = rs.getDate(1);
					numOfVIsitorsInDay[i] = rs.getInt(2);
					i++;

				}
				try {
					pstm = sqlConnection.connection.prepareStatement("SELECT * from parks where parkName=?");
					pstm.setString(1, info.get(0));
					rs = pstm.executeQuery();
					if (rs.next()) {
						park = new Park(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
								rs.getInt(6));
					}
				} catch (SQLException e) {
				}
				UsageReport usageReport = new UsageReport(park, month, year, day, numOfVIsitorsInDay);
				System.out.println(usageReport.getParkName());
				return new Message(OperationType.UsageReport, ClientControllerType.ReportController,
						(Object) usageReport);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		case VisitingReport:
			info = (ArrayList<String>) clientMsg.getObj();

			try {
				pstm = sqlConnection.connection.prepareStatement(
						"SELECT visitEntry, SUM(actualNumOfVisitors) from receipts where parkName=? and  date=? and type=? group by visitEntry");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				pstm.setString(3, info.get(2));
				rs = pstm.executeQuery();
				i = 0;
				int[] hours = new int[8];
				int[] amountOfVisitors = new int[8];

				while (rs.next()) {
					System.out.println(22);
					hours[i] = rs.getInt(1);
					amountOfVisitors[i] = rs.getInt(2);
					i++;
				}

				VisitingReport visitingReport = new VisitingReport(info.get(0), info.get(1), info.get(2), hours,
						amountOfVisitors);
				System.out.println(visitingReport.getParkName());
				return new Message(OperationType.VisitingReport, ClientControllerType.ReportController,
						(Object) visitingReport);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		case CancellationReport:
			System.out.println("cancelll");
			info = (ArrayList<String>) clientMsg.getObj();
			month = info.get(0);
			year = info.get(1);

			// getCurrentTime();
			try {

				pstm = sqlConnection.connection
						.prepareStatement("SELECT  * FROM  receipts  WHERE  parkName=? and date=? ");

				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				rs = pstm.executeQuery();
				System.out.println("after this");
				int unfulfilledOrderCounter = 0;
				int unfulfilledVisitorsAmount = 0;
				List<Integer> approvedOrders = new ArrayList<Integer>();
				while (rs.next()) {
					System.out.println(rs.getString(7) + rs.getInt(1));
					approvedOrders.add(rs.getInt(8));
				}
				pstm = sqlConnection.connection.prepareStatement(
						"SELECT  * FROM  orders  WHERE  parkName=? and arrivalDate=? and status = ?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				pstm.setString(3, "Approved");

				rs = pstm.executeQuery();
				System.out.println("mid query");
				while (rs.next()) {
					System.out.println("id" + rs.getInt(1));
					if (!(approvedOrders.contains(rs.getInt(1)))) {
						unfulfilledOrderCounter++;
						unfulfilledVisitorsAmount += rs.getInt(7);
					}
				}

				System.out.println("mary");
				pstm = sqlConnection.connection
						.prepareStatement("SELECT * from orders where parkName=? and arrivalDate=? and status = ?");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				pstm.setString(3, "Canceled");
				rs = pstm.executeQuery();
				visitorsAmount = 0;
				int canceledOrderCounter = 0;
				int canceledVisitorsAmount = 0;
				while (rs.next()) {
					System.out.println("order -" + rs.getInt(1));
					System.out.println("amount -" + rs.getInt(7));
					canceledOrderCounter++;
					canceledVisitorsAmount += rs.getInt(7);
				}

				pstm = sqlConnection.connection
						.prepareStatement("SELECT * from orders where parkName=? and arrivalDate=? ");
				pstm.setString(1, info.get(0));
				pstm.setString(2, info.get(1));
				rs = pstm.executeQuery();
				int totalOrderCounter = 0;
				while (rs.next()) {
					System.out.println((rs.getInt(1)));
					totalOrderCounter++;
				}
				CancellationReport cancellationReport = new CancellationReport(info.get(0), info.get(1),
						totalOrderCounter, canceledOrderCounter, canceledVisitorsAmount, unfulfilledOrderCounter,
						unfulfilledVisitorsAmount);

				return new Message(OperationType.CancellationReport, ClientControllerType.ReportController,
						(Object) cancellationReport);
			} catch (SQLException e) {
			}
			break;
			
		default:
			break;

		}

		return clientMsg;
	}

	/**
	 * this function taking the date and time of start and end of specific month
	 */
	private static void getCurrentTime() {

		System.out.println(month);
		System.out.println(year);

		String monthToDb = month;

		if (Integer.parseInt(month) < 10) {
			monthToDb = "0" + month;

		}
		int monthInt = Integer.parseInt(month);
		System.out.println(monthInt);
		if (monthInt == 1 || monthInt == 3 || monthInt == 5 || monthInt == 7 || monthInt == 8 || monthInt == 10
				|| monthInt == 12) {
			System.out.println(monthInt + " here");
			endMonth = LocalDate.parse(year + "-" + monthToDb + "-31");
			System.out.println(endMonth);
		}

		else if (monthInt == 2)
			endMonth = LocalDate.parse(year + "-" + monthToDb + "-28");

		else if (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11)
			endMonth = LocalDate.parse(year + "-" + monthToDb + "-30");

		System.out.println(12345);
		startMonth = LocalDate.parse(year + "-" + monthToDb + "-01");

		startMonthToDB = Date.valueOf(startMonth);
		endMonthToDB = Date.valueOf(endMonth);
		System.out.println(startMonth);
		System.out.println(endMonth);

	}

}

package controllers;

import common.Message;
import enums.OperationType;
import logic.CancellationReport;
import logic.IncomeReport;
import logic.Report;
import logic.SumVisitorsReport;
import logic.UsageReport;
import logic.VisitingReport;

/**
 * This class manages the details of each report in the database include
 * department manager reports and, park manager reports, the class use the
 * returend parameters that recieved from the Server and use them for the
 * client.
 * 
 * @author Naor0
 *
 */
public class ReportController {

	public static Report report = null;
	public static OperationType reportType = null;

	public static void reportParseData(Message reciveMsg) {

		switch (reciveMsg.getOperationType()) {

		// case to check the status of receipt
		case SumVisitorsReport:
			report = (SumVisitorsReport) reciveMsg.getObj();
			reportType = OperationType.SumVisitorsReport;
			break;

		case RevenueReport:
			report = (IncomeReport) reciveMsg.getObj();
			reportType = OperationType.RevenueReport;
			break;

		case UsageReport:
			report = (UsageReport) reciveMsg.getObj();
			reportType = OperationType.UsageReport;
			break;

		case VisitingReport:
			report = (VisitingReport) reciveMsg.getObj();
			reportType = OperationType.VisitingReport;
			break;

		case CancellationReport:
			report = (CancellationReport) reciveMsg.getObj();
			reportType = OperationType.CancellationReport;
			break;

		default:
			break;

		}
	}
}

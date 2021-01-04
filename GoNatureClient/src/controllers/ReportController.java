package controllers;

import common.Message;
import enums.OperationType;
import logic.CancellationReport;
import logic.IncomeReport;
import logic.Report;
import logic.SumVisitorsReport;
import logic.UsageReport;
import logic.VisitingReport;

public class ReportController {
	
	public static Report report = null;
	public static OperationType reportType = null;
	
	
	public static void reportParseData(Message reciveMsg) {
	
	
	switch(reciveMsg.getOperationType()) {
	
	//case to check the status of receipt
	case SumVisitorsReport:
		report= (SumVisitorsReport) reciveMsg.getObj();
		reportType=OperationType.SumVisitorsReport;
		break;
		
		
	case RevenueReport:
		report = (IncomeReport) reciveMsg.getObj();
		reportType= OperationType.RevenueReport;
		break;
		
	case UsageReport:
		report = (UsageReport) reciveMsg.getObj();
		System.out.println("Usage report controller");
		reportType= OperationType.UsageReport;
		break;
		
	case VisitingReport:
		report = (VisitingReport)reciveMsg.getObj();
		System.out.println("reportController");
		reportType=OperationType.VisitingReport;
		
	case CancellationReport:
		report = (CancellationReport)reciveMsg.getObj();
		System.out.println("reportController");
		reportType=OperationType.CancellationReport;
		
	break;
	
	
	default:
		break;
	
			
		
	

}
	}
}


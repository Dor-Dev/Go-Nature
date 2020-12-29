package controllers;

import common.Message;
import enums.OperationType;
import logic.Order;
import logic.Receipt;

public class ReceiptController {
	public static Receipt receipt = null;
	public static OperationType receiptType = null;
	
	
	public static void receipeParseData(Message reciveMsg) {
	
		String msg;
	switch(reciveMsg.getOperationType()) {
	
	//case to check the status of receipt
	case CheckReceiptInfo:
		if(reciveMsg.getObj() instanceof Receipt) {
	receipt= (Receipt) reciveMsg.getObj();
	receiptType = OperationType.CheckReceiptInfo;
		}
		else {
			receiptType=OperationType.NeverExist;
			
		}
	break;
	
	//case to update the amount of visitors after exit in the receipt
	case UpdateReceiptInfoAfterExit:
		 msg = (String)reciveMsg.getObj();
		if( msg.equals("sucsses to update exit")) {
			receiptType= OperationType.SuccessUpdateReceipt;
		}
		else {
			receiptType= OperationType.FailedUpdateReceipt;
		}
		break;
		
		// case to update the  amount of visitors (in entry)
	case UpdateReceipt:
		msg = (String)reciveMsg.getObj();
			
		if( msg.equals("sucsses to update")) {
		receiptType= OperationType.SuccessUpdateReceipt;
		System.out.println("receiptType = success");
		}
		
		else  if(msg.equals("faild to update")) {
			System.out.println("failedddd");
			receiptType= OperationType.FailedUpdateReceipt;
			
		}
			
		else {
			System.out.println("receiptType = never");
			receiptType= OperationType.NeverExist;
		}
		break;
		
	
	default:
		break;
	}
	}
	
	

}

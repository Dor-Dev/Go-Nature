package controllers;

import common.Message;
import enums.OperationType;
import logic.Receipt;

public class ReceiptController {
	public static Receipt receipt = null;
	public static OperationType receiptType = null;
	
	public static void receipeParseData(Message reciveMsg) {
	receipt= (Receipt) reciveMsg.getObj();
	
	switch(reciveMsg.getOperationType()) {
	
	case ReceiptInfo:
	receipt= (Receipt) reciveMsg.getObj();
	receiptType = OperationType.ReceiptInfo;
	break;
	default:
		break;
	}
	}
	
	

}

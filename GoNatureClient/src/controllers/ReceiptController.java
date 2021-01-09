package controllers;

import common.Message;
import enums.OperationType;
import logic.Receipt;

/**
 * This class manages the details of each receipt in the database include order receipts
 * and manual receipts, the class use the returend parameters that recieved
 * from the Server and use them for the client.
 * 
 * @author Naor0
 *
 */
public class ReceiptController {
	public static Receipt receipt = null;
	public static OperationType receiptType = null;
	public static int receiptID = 0;

	public static void receipeParseData(Message reciveMsg) {

		String msg;
		switch (reciveMsg.getOperationType()) {

		// case to check the status of receipt
		case CheckReceiptInfo:
			if (reciveMsg.getObj() instanceof Receipt) {
				receipt = (Receipt) reciveMsg.getObj();
				receiptType = OperationType.CheckReceiptInfo;
			} else
				receiptType = OperationType.NeverExist;

			break;

		// case to update the amount of visitors after exit in the receipt
		case UpdateReceiptInfoAfterExit:
			msg = (String) reciveMsg.getObj();
			if (msg.equals("sucsses to update exit"))
				receiptType = OperationType.SuccessUpdateReceipt;

			else
				receiptType = OperationType.FailedUpdateReceipt;

			break;

		// case to update the amount of visitors (in entry)
		case UpdateReceipt:
			// if the update successes the object is the number of the receipt
			if (reciveMsg.getObj() instanceof Integer) {
				receiptType = OperationType.SuccessUpdateReceipt;
				receiptID = (int) reciveMsg.getObj();

			} else {
				msg = (String) reciveMsg.getObj();
				if (msg.equals("sucsses to update"))
					receiptType = OperationType.SuccessUpdateReceipt;

				else if (msg.equals("faild to update"))
					receiptType = OperationType.FailedUpdateReceipt;
				else
					receiptType = OperationType.NeverExist;
			}
			break;

		default:
			break;
		}
	}

}

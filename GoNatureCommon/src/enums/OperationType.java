package enums;

public enum OperationType {

	//login enums
	EmployeeLogin,ErrorEmployeeLogin, VisitorLogin, VisitorWithOrderLogin, SubscriberLogin, 
	
	//park entrance enums
	GetParkInfo, ParkInfo, UpdateParkInfo, DecreaseParkVistiors, UpdateReceipt, IncreaseParkVistiors,
	TravelerInfo, OccasionalSubscriber, OccasionalVisitor, GenerateReceipt, CheckReceiptInfo, UpdateReceiptInfo, 
	SuccessUpdateReceipt, FailedUpdateReceipt, GetOrderInfo, UpdateReceiptInfoAfterExit, NeverExist, FailedUpdate, CheckDifference, UpdateCurrAmountOfVisitors;
}

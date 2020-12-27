package enums;

public enum OperationType {
	// Login Enums
	EmployeeLogin, ErrorEmployeeLogin, VisitorLogin, VisitorWithOrderLogin, SubscriberLogin,


	//login enums
	EmployeeLogin,ErrorEmployeeLogin, VisitorLogin, VisitorWithOrderLogin, SubscriberLogin, 
	
	// Park Enmus
	GetParkInfo, ParkInfo, UpdateParkInfo, DecreaseParkVistiors, UpdateReceipt, IncreaseParkVistiors, TravelerInfo,
	OccasionalSubscriber, OccasionalVisitor, GenerateReceipt,  CheckReceiptInfo, AddOrder, SuccessAddOrder,
	MemberRegistration, MemberRegistrationCC, GuideRegistration, GuideRegistrationCC, UpdateReceiptInfo, 
	SuccessUpdateReceipt, FailedUpdateReceipt, GetOrderInfo, UpdateReceiptInfoAfterExit, NeverExist, FailedUpdate, CheckDifference, UpdateCurrAmountOfVisitors,
	// Update Paramters enums
	SendUpdateRequest, UpdateWasSent
	// Events enums
	, EventRequest,EventRequestAccepted, EventError, EventRequestSuccess, showActiveEvents, EventsToShow, EventsReady;
}

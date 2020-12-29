

package enums;

public enum OperationType {
	// Login Enums
	EmployeeLogin, ErrorEmployeeLogin, VisitorLogin, 
	VisitorWithOrderLogin, SubscriberLogin,


	//Order Enums
	OrderRequestAnswer,AddOrder, SuccessAddOrder, 
	OrderCheckDateTime, checkAvailableHours,

	
	// Park Enmus
	GetParkInfo, ParkInfo, UpdateParkInfo, DecreaseParkVistiors, UpdateReceipt, IncreaseParkVistiors, TravelerInfo,
	OccasionalSubscriber, OccasionalVisitor, GenerateReceipt,  CheckReceiptInfo, 
	MemberRegistration, MemberRegistrationCC, GuideRegistration, GuideRegistrationCC, UpdateReceiptInfo, 
	SuccessUpdateReceipt, FailedUpdateReceipt, GetOrderInfo, UpdateReceiptInfoAfterExit, NeverExist, FailedUpdate, CheckDifference, UpdateCurrAmountOfVisitors,
	// Update Paramters enums
	SendUpdateRequest, UpdateWasSent
	// Events enums
	, EventRequest,EventRequestAccepted, EventError, EventRequestSuccess, showActiveEvents, EventsToShow, EventsReady, 
	//Requests enums
	GetUpdateTable, ShowUpdateTable, UpdateTableArrived, GetEventTable, EventTableArrived, ShowEventTable, EventApproval, EventActivated, EventDecline, EventCanceled, UpdateConfrimation, UpdateDecline;
	//reports
	SumVisitorsReport, RevenueReport, UsageReport;
}


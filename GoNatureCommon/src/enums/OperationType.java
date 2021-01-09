


package enums;

public enum OperationType {
	// Login Enums
	EmployeeLogin, ErrorEmployeeLogin, VisitorLogin, 
	VisitorWithOrderLogin, SubscriberLogin,VisitorAlreadyLoggedIn,EmployeeAlreadyLoggedIn,UserDisconnected,UserDisconnectedSuccess,
	MemberNumberNotExist,

	//Order Enums
	OrderRequestAnswer,AddOrder, SuccessAddOrder, 
	OrderCheckDateTime, checkAvailableHours, checkEventDiscount, EventDiscountAmount,
	
	//MyOrder Enums
	getMyOrders,ReturnMyOrders, OrderFinalApproval,GetOutFromWaitingList, CancelOrder,
	CancelOrderSuccess, ApproveOrderSuccess,WaitingListExitSuccess,
	
	// Park Enmus
	GetParkInfo, ParkInfo, UpdateParkInfo, DecreaseParkVistiors, UpdateReceipt, IncreaseParkVistiors, TravelerInfo,
	OccasionalSubscriber, OccasionalVisitor, GenerateReceipt,  CheckReceiptInfo, ShowParkCapacity,
	MemberRegistration, MemberRegistrationCC, GuideRegistration, GuideRegistrationCC, UpdateReceiptInfo, 
	SuccessUpdateReceipt, FailedUpdateReceipt, GetOrderInfo, UpdateReceiptInfoAfterExit, NeverExist, FailedUpdate, CheckDifference, UpdateCurrAmountOfVisitors,
	
	// Update Paramters enums
	SendUpdateRequest, UpdateWasSent,
	
	// Events enums
	EventRequest,EventRequestAccepted, EventError, EventRequestSuccess, showManagerEvents, EventsToShow, EventsReady, 
	
	//Requests enums
	GetUpdateTable, ShowUpdateTable, UpdateTableArrived, GetEventTable, EventTableArrived, ShowEventTable, 
	EventApproval, EventActivated, EventDecline, EventCanceled, UpdateConfrimation, UpdateDecline,
	
	//reports

	SumVisitorsReport, RevenueReport, UsageReport, ReceiptInfo ,VisitingReport,CancellationReport, SubmitReport,GetReceivedReportsTable,
	ReturnReceivedReport,
	
	// card reader

	 VisitorEnterRequest, VisitorExitRequest;

}



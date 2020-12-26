package enums;

public enum OperationType {
	// Login Enums
	EmployeeLogin, ErrorEmployeeLogin, VisitorLogin, VisitorWithOrderLogin, SubscriberLogin,

	// Park Enmus
	GetParkInfo, ParkInfo, UpdateParkInfo, DecreaseParkVistiors, UpdateReceipt, IncreaseParkVistiors, TravelerInfo,
	OccasionalSubscriber, OccasionalVisitor, GenerateReceipt, ReceiptInfo, AddOrder, SuccessAddOrder,
	MemberRegistration, MemberRegistrationCC, GuideRegistration, GuideRegistrationCC,
	// Update Paramters enums
	SendUpdateRequest, UpdateWasSent
	// Events enums
	, EventRequest,EventRequestAccepted, EventError, EventRequestSuccess, showActiveEvents, EventsToShow, EventsReady;
}

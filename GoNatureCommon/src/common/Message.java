package common;

import java.io.Serializable;

/**
 * Object that will use to communicate between the Client and the Server.
 * Implements <code>Serializable</code> so could be sent by Socket
 */
public class Message implements Serializable {



	private static final long serialVersionUID = 1L;

	// Type of the operation we want from the server to make.
    private OperationType operationType;

    // Which controller on the server should make the operation.
    private DBControllerType dbControllertype;

    // If the operation that returns from the server succeeded\failed.
    private ReturnMessageType returnMessageType;
    
    private Object obj;

	

	public OperationType getOperationType() {
		return operationType;
	}



	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}



	public DBControllerType getDbControllertype() {
		return dbControllertype;
	}



	public void setDbControllertype(DBControllerType dbControllertype) {
		this.dbControllertype = dbControllertype;
	}



	public Object getObj() {
		return obj;
	}



	public void setObj(Object obj) {
		this.obj = obj;
	}



	public Message(OperationType operationType, DBControllerType dbControllertype,Object obj) {
		super();
		this.operationType = operationType;
		this.dbControllertype = dbControllertype;
		this.obj = obj;
	}
    
    
    


	


}
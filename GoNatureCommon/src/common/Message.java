package common;

import java.io.Serializable;

import enums.ClientControllerType;
import enums.DBControllerType;
import enums.OperationType;
import enums.ReturnMessageType;

/**
 * Object that will use to communicate between the Client and the Server.
 * Implements <code>Serializable</code> so could be sent by Socket
 */
public class Message implements Serializable {





	/**
	 * 
	 */
	private static final long serialVersionUID = -7240040463603651727L;

	// Type of the operation we want from the server to make.
    private OperationType operationType;

    // Which controller on the server should make the operation.
    private DBControllerType dbControllerType;
    
    //Which controller on the client should do the operation
    private ClientControllerType controllerType;

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
		return dbControllerType;
	}
	
	public ClientControllerType getControllertype() {
		return controllerType;
	}



	public void setDbControllertype(DBControllerType dbControllertype) {
		this.dbControllerType = dbControllertype;
	}



	public Object getObj() {
		return obj;
	}



	public void setObj(Object obj) {
		this.obj = obj;
	}


	/**
	 * Constructor for Message from client to server
	 * @param operationType
	 * @param dbControllertype
	 * @param obj
	 */
	public Message(OperationType operationType, DBControllerType dbControllertype,Object obj) {
		super();
		this.operationType = operationType;
		this.dbControllerType = dbControllertype;
		this.obj = obj;
	}
	
	/**
	 * Constructor for Message from server to client
	 * @param operationType
	 * @param controllerType
	 * @param obj
	 */
    public Message(OperationType operationType,ClientControllerType controllerType,Object obj) {
    	this.controllerType = controllerType;
    	this.operationType = operationType;
    	this.obj = obj;
    }



    


	


}
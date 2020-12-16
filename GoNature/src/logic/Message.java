package logic;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object object;
	private Command cmd;
	public Object getMsg() {
		return object;
	}
	public void setMsg(Object msg) {
		this.object = msg;
	}
	public Command getCmd() {
		return cmd;
	}
	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}
	public Message(Object msg, Command cmd) {
		super();
		this.object = msg;
		this.cmd = cmd;
	};

}

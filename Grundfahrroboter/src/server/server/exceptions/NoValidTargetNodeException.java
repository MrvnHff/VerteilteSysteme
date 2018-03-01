package server.server.exceptions;

@SuppressWarnings("serial")
public class NoValidTargetNodeException extends RuntimeException {

	public NoValidTargetNodeException() {
		super();
	}
	
	public NoValidTargetNodeException(String message) {
		super(message);
	}
}

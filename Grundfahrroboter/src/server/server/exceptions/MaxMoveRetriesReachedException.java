package server.server.exceptions;

public class MaxMoveRetriesReachedException extends RuntimeException {
	
	public MaxMoveRetriesReachedException() {
		super();
	}
	
	public MaxMoveRetriesReachedException(String message) {
		super(message);
	}	
	
}

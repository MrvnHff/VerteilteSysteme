package server.server.exceptions;

public class MaximumWorkersReachedException extends RuntimeException {
	
	public MaximumWorkersReachedException() {
		super();
	}
	
	public MaximumWorkersReachedException(String message) {
		super(message);
	}	
	
}

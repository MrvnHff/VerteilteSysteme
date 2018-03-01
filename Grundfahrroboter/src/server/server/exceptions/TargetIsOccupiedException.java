package server.server.exceptions;

public class TargetIsOccupiedException extends RuntimeException {
	
	public TargetIsOccupiedException() {
		super();
	}
	
	public TargetIsOccupiedException(String message) {
		super(message);
	}	
	
}

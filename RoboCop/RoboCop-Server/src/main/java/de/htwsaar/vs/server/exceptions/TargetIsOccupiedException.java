package de.htwsaar.vs.server.exceptions;

public class TargetIsOccupiedException extends RuntimeException {
	
	public TargetIsOccupiedException() {
		super();
	}
	
	public TargetIsOccupiedException(String message) {
		super(message);
	}	
	
}

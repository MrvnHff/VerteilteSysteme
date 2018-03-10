package server.server.exceptions;

public class NoValidNodeIdException extends RuntimeException {
	
	public NoValidNodeIdException() {
		super();
	}
	
	public NoValidNodeIdException(String message) {
		super(message);
	}
}

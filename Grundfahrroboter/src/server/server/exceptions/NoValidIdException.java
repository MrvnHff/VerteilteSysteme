package server.server.exceptions;

public class NoValidIdException extends RuntimeException {

	public NoValidIdException() {
		super();
	}
	
	public NoValidIdException(String message) {
		super(message);
	}
}

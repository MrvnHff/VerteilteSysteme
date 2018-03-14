package server.server.exceptions;

public class NoPathToDestinationException extends RuntimeException {

	public NoPathToDestinationException() {
		super();
	}
	
	public NoPathToDestinationException(String message) {
		super(message);
	}
}

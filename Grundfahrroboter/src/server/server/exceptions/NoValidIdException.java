package server.server.exceptions;

/**
 * Fehlermeldung fuer den Fall, dass es sich um eine ungeultige ID handelt.
 */
public class NoValidIdException extends RuntimeException {

	public NoValidIdException() {
		super();
	}
	
	public NoValidIdException(String message) {
		super(message);
	}
}

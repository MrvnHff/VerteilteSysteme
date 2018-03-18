package server.server.exceptions;

/**
 * Fehlermeldung fuer den Fall, dass der Zielknote ungueltig ist.
 */
@SuppressWarnings("serial")
public class NoValidTargetNodeException extends RuntimeException {

	public NoValidTargetNodeException() {
		super();
	}
	
	public NoValidTargetNodeException(String message) {
		super(message);
	}
}

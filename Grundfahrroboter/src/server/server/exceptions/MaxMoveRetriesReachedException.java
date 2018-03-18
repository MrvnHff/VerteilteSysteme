package server.server.exceptions;


/**
 * Fehlermeldung fuer den Fall, dass die maximale Zahl an Vorwaertsfahrversuchen erreicht wurde und dieser Vorgang abgebrochen wird.
 */
public class MaxMoveRetriesReachedException extends RuntimeException {
	
	public MaxMoveRetriesReachedException() {
		super();
	}
	
	public MaxMoveRetriesReachedException(String message) {
		super(message);
	}	
	
}

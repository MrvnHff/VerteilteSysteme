package server.server.exceptions;


/**
 * Fehlermeldung fuer den Fall, dass die maximale Zahl an Vorwaertsfahrversuchen erreicht wurde und dieser Vorgang abgebrochen wird.
 */
public class MaxMoveRetriesReachedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8910611502419791799L;

	public MaxMoveRetriesReachedException() {
		super();
	}
	
	public MaxMoveRetriesReachedException(String message) {
		super(message);
	}	
	
}

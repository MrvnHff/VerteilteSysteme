package server.server.exceptions;

/**
 * Fehlermeldung fuer den Fall, dass die maximale Anzahl an Workern erreicht ist, und keiner mehr hinzugefuegt werden kann.
 */
public class MaximumWorkersReachedException extends RuntimeException {
	
	public MaximumWorkersReachedException() {
		super();
	}
	
	public MaximumWorkersReachedException(String message) {
		super(message);
	}	
	
}

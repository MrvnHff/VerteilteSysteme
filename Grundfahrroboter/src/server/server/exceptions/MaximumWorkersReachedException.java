package server.server.exceptions;

/**
 * Fehlermeldung fuer den Fall, dass die maximale Anzahl an Workern erreicht ist, und keiner mehr hinzugefuegt werden kann.
 */
public class MaximumWorkersReachedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2942505335749079841L;

	public MaximumWorkersReachedException() {
		super();
	}
	
	public MaximumWorkersReachedException(String message) {
		super(message);
	}	
	
}

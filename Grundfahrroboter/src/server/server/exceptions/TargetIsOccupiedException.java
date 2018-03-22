package server.server.exceptions;


/**
 * Fehlermeldung fuer den Fall, dass der Knoten bereits besetzt ist, auf den gefahren werden soll.
 * @author admin
 *
 */
public class TargetIsOccupiedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2857088497872853950L;

	public TargetIsOccupiedException() {
		super();
	}
	
	public TargetIsOccupiedException(String message) {
		super(message);
	}	
	
}

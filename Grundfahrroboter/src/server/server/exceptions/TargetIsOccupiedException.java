package server.server.exceptions;


/**
 * Fehlermeldung fuer den Fall, dass der Knoten bereits besetzt ist, auf den gefahren werden soll.
 * @author admin
 *
 */
public class TargetIsOccupiedException extends RuntimeException {
	
	public TargetIsOccupiedException() {
		super();
	}
	
	public TargetIsOccupiedException(String message) {
		super(message);
	}	
	
}

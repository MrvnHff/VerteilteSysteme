package server.server.exceptions;


/**
 * Fehlermeldung fuer den Fall, dass es aktuell keinen Pfad zum Zielknoten existiert.
 */
public class NoPathToDestinationException extends RuntimeException {

	public NoPathToDestinationException() {
		super();
	}
	
	public NoPathToDestinationException(String message) {
		super(message);
	}
}

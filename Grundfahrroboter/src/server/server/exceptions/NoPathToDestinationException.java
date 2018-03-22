package server.server.exceptions;


/**
 * Fehlermeldung fuer den Fall, dass es aktuell keinen Pfad zum Zielknoten existiert.
 */
public class NoPathToDestinationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3077352585066603005L;

	public NoPathToDestinationException() {
		super();
	}
	
	public NoPathToDestinationException(String message) {
		super(message);
	}
}

package server.server.exceptions;

/**
 * Fehlermeldung fuer den Fall, dass es sich um eine ungeultige ID handelt.
 */
public class NoValidIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6553144184454926570L;

	public NoValidIdException() {
		super();
	}
	
	public NoValidIdException(String message) {
		super(message);
	}
}

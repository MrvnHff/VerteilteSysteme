package server.server.exceptions;

/**
 * Fehlermeldung fuer den Fall, dass der Knoten keine Teilmenge des Graphen ist.
 */
public class NoValidNodeIdException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1564814941847270587L;

	public NoValidNodeIdException() {
		super();
	}
	
	public NoValidNodeIdException(String message) {
		super(message);
	}
}

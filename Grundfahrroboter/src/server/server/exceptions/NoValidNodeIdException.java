package server.server.exceptions;

/**
 * Fehlermeldung fuer den Fall, dass der Knoten keine Teilmenge des Graphen ist.
 */
public class NoValidNodeIdException extends RuntimeException {
	
	public NoValidNodeIdException() {
		super();
	}
	
	public NoValidNodeIdException(String message) {
		super(message);
	}
}

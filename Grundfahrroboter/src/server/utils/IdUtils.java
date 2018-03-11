package server.utils;

import server.server.exceptions.NoValidIdException;

/**
 * Bietet Methoden an für den Umgang mit Id's
 * @author Mathias Wittling
 *
 */
public abstract class IdUtils {

	/**
	 * Erstellt aus der Id ein array mit den entsprechendne Zahlenwerten
	 * @param cellId Id aus der die zahlenwerte extrahiert werden sollen
	 * @return array mit den entsprechenden Zahlenwerten
	 */
	public static int[] extractCoordinates(String cellId) {
		if(isValidId(cellId)) {
			String strCoordinates[];
			int	intCoordinates[] = new int[2];
			strCoordinates = cellId.split("/");
			intCoordinates[0] =  Integer.parseInt(strCoordinates[0]);
			intCoordinates[1] =  Integer.parseInt(strCoordinates[1]);
			return intCoordinates;
		} else {
			throw new NoValidIdException(cellId);
		}
	}
	
	/**
	 * Erstellt aus den übergebenen Zahlenwerten die entsprechende Id
	 * @param row Zeilenposition
	 * @param col Spaltenposition
	 * @return nodeId
	 */
	public static String createIdString(int row, int col) {
		return row + "/" + col;
	}
	
	/**
	 * Überprüft ob der übergebene String eine gültige Knoten Id darstellt
	 * @param nodeId Id die überprüft werden soll
	 * @return true falls gültig, false wenn nicht
	 */
	public static boolean isValidId(String nodeId) {
		return nodeId.matches("[0-9]*/[0-9]*");
	}
}


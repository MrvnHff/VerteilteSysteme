package server.utils;

/**
 * Bietet Methoden an f�r den Umgang mit Id's
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
		String strCoordinates[];
		int	intCoordinates[] = new int[2];
		strCoordinates = cellId.split("/");
		intCoordinates[0] =  Integer.parseInt(strCoordinates[0]);
		intCoordinates[1] =  Integer.parseInt(strCoordinates[1]);
		return intCoordinates;
	}
	
	/**
	 * Erstellt aus den �bergebenen Zahlenwerten die entsprechende Id
	 * @param row Zeilenposition
	 * @param col Spaltenposition
	 * @return nodeId
	 */
	public static String createIdString(int row, int col) {
		return row + "/" + col;
	}
	
	/**
	 * �berpr�ft ob der �bergebene String eine g�ltige Knoten Id darstellt
	 * @param nodeId Id die �berpr�ft werden soll
	 * @return true falls g�ltig, false wenn nicht
	 */
	public static boolean isValidNodeId(String nodeId) {
		//FIXME muss noch implementiert werden
		return true;
	}
}


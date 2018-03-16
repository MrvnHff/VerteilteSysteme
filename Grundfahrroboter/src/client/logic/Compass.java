package logic;

import sensors.Gyrosensor;

/**
 * Die Klasse bestimmt die Ausrichtung des Roboters und stellt über den Gyrosensor einen Kompas da.
 * @author Lennart
 * @version 10.10.2017 
 */
public class Compass {
	
	final static String NORTH = "NORTH";
	final static String EAST = "EAST";
	final static String SOUTH = "SOUTH";
	final static String WEST = "WEST";
	
	/**
	 * Errechnet über den Gyrosensor die Ausrichtung.
	 */
	public static String getDirection(Gyrosensor g) {
		int degree = (360 + g.getValue()) % 360;		
		String direction = "";
		if (degree > 315 || degree <= 45) {
			direction = NORTH;
		}else if (degree > 45 || degree <= 135) {
			direction = EAST;
		}else if (degree > 135 || degree <= 225) {
			direction = SOUTH;
		}else if (degree > 225 || degree <= 315) {
			direction = WEST;
		}		
		return direction;
	}
}

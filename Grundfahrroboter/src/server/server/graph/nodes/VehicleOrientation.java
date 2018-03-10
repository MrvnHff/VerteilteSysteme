package server.server.graph.nodes;

/**
 * Ausrichtung eines Fahrzeugs
 * @author Mathias Wittling
 *
 */
public enum VehicleOrientation {
	WEST,
	NORTH,
	EAST,
	SOUTH;
	
	private static VehicleOrientation[]  vals = values();
	
	/**
	 * @return successor
	 */
	public VehicleOrientation turnRight() {
		return vals[(this.ordinal()+1) % (vals.length)];
	}

	/**
	 * @return predecessor
	 */
	public VehicleOrientation turnLeft() {
		return vals[((this.ordinal() - 1 + vals.length) % (vals.length))];
	}
	
	/**
	 * Berechnet den Abstand zwischen dem eigenen und dem Übergebenen Wert
	 * @param orientation Wert zu dem der Abstand berechnet werden soll
	 * @return abstand (-3) bis (+3)
	 */
	public int getDifference(VehicleOrientation orientation) {
		return (this.ordinal() - orientation.ordinal()) * (-1);
	}
}

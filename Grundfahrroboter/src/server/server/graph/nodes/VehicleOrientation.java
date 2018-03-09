package server.server.graph.nodes;

public enum VehicleOrientation {
	WEST,
	NORTH,
	EAST,
	SOUTH;
	
	private static VehicleOrientation[]  vals = values();
	
	public VehicleOrientation turnRight() {
		return vals[(this.ordinal()+1) % (vals.length)];
	}

	public VehicleOrientation turnLeft() {
		return vals[((this.ordinal() - 1 + vals.length) % (vals.length))];
	}
	
	public int getDifference(VehicleOrientation orientation) {
		return (this.ordinal() - orientation.ordinal()) * (-1);
	}
}

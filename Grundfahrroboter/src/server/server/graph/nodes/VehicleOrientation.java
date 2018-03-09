package server.server.graph.nodes;

public enum RobotOrientation {
	WEST,
	NORTH,
	EAST,
	SOUTH;
	
	private static RobotOrientation[]  vals = values();
	
	public RobotOrientation turnRight() {
		return vals[(this.ordinal()+1) % (vals.length)];
	}

	public RobotOrientation turnLeft() {
		return vals[((this.ordinal() - 1 + vals.length) % (vals.length))];
	}
	
	public int getDifference(RobotOrientation orientation) {
		return (this.ordinal() - orientation.ordinal()) * (-1);
	}
}

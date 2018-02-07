package de.htwsaar.vs.server.graph.nodes;

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
		return vals[Math.abs((this.ordinal()-1) % (vals.length))];
	}
}

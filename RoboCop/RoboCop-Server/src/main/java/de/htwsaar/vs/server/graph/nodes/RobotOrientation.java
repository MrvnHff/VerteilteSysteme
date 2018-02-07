package de.htwsaar.vs.server.graph.nodes;

public enum RobotOrientation {
	EAST,
	NORTH,
	WEST,
	SOUTH;
	
	private static RobotOrientation[]  vals = values();
	
	public RobotOrientation turnRight() {
		return vals[this.ordinal()+1 % vals.length];
	}
}

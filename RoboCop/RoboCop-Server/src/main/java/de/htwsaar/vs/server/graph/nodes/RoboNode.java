package de.htwsaar.vs.server.graph.nodes;

public class RoboNode {
	private String robotId;
	private Orientation orientation;
	
	public RoboNode() {
		robotId = null;
		orientation = null;
	}
	
	public RoboNode(String robotId, Orientation orientation) {
		this.robotId = robotId;
		this.orientation = orientation;
	}
	
	@Override
	public String toString() {
		return robotId + "|"+ orientation;
	}
}

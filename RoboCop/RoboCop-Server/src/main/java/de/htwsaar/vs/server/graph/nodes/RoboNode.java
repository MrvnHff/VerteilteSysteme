package de.htwsaar.vs.server.graph.nodes;

public class RoboNode {
	private String nodeId;
	private String robotId;
	private Orientation orientation;
	
	public RoboNode() {
		nodeId = null;
		robotId = null;
		orientation = null;
	}
	
	public RoboNode(String nodeId, String robotId, Orientation orientation) {
		this.nodeId = nodeId;
		this.robotId = robotId;
		this.orientation = orientation;
	}
	
	public RoboNode(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getNodeId() {
		return this.nodeId;
	}
	
	@Override
	public String toString() {
		return robotId + "|"+ orientation;
	}
}

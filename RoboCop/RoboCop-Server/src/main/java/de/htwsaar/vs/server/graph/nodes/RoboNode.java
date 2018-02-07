package de.htwsaar.vs.server.graph.nodes;

public class RoboNode {
	private String nodeId;
	private String robotId;
	private RobotOrientation orientation;
	
	public RoboNode() {
		nodeId = null;
		robotId = null;
		orientation = null;
	}
	
	public RoboNode(String nodeId, String robotId, RobotOrientation orientation) {
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
	
	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}
	
	public void setOrientation(RobotOrientation orientation) {
		this.orientation = orientation;
	}
	
	public boolean isEmpty() {
		if(robotId == null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return robotId + "|"+ orientation;
	}
}

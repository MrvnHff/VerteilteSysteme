package server.server.graph.nodes;

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
	
	public String getRobotId() {
		return this.robotId;
	}
	
	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}
	
	public void setOrientation(RobotOrientation orientation) {
		this.orientation = orientation;
	}
	
	public RobotOrientation getOrientation() {
		return this.orientation;
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

	public void turnLeft() {
		orientation = orientation.turnLeft();
		System.out.println(this.orientation);
		
	}

	public void turnRight() {
		orientation = orientation.turnRight();
		System.out.println(this.orientation);
	}
}

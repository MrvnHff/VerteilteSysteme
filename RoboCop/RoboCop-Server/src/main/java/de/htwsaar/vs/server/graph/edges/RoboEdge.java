package de.htwsaar.vs.server.graph.edges;

import org.jgrapht.graph.DefaultEdge;

@SuppressWarnings("serial")
public class RoboEdge extends DefaultEdge {
	
	private String robotId;
	
	public RoboEdge() {
		super();
		robotId = "";
	}
	
	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}
	
	public String getRobotId() {
		return robotId;
	}
	
}

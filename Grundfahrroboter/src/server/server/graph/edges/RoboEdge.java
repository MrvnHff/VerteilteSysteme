package server.server.graph.edges;

import org.jgrapht.graph.DefaultEdge;

import server.server.graph.nodes.RobotOrientation;
/**
 * Erweiterung des Standardkante um das Attribut robotId
 * @author Mathias
 *
 */
@SuppressWarnings("serial")
public class RoboEdge extends DefaultEdge {
	
	private String robotId;
	private RobotOrientation orientation;
	
	public RoboEdge() {
		super();
		robotId = "";
		orientation = null;
	}
	
	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}
	
	public String getRobotId() {
		return robotId;
	}
	
}

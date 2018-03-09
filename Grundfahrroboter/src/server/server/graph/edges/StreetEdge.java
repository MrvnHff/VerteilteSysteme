package server.server.graph.edges;

import org.jgrapht.graph.DefaultEdge;

import server.server.graph.nodes.VehicleOrientation;
/**
 * Erweiterung des Standardkante um das Attribut vehicleId
 * @author Mathias
 *
 */
@SuppressWarnings("serial")
public class StreetEdge extends DefaultEdge {
	
	private String vehicleId;
	private VehicleOrientation orientation;
	
	public StreetEdge() {
		super();
		vehicleId = "";
		orientation = null;
	}
	
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public String getVehicleId() {
		return vehicleId;
	}
	
}

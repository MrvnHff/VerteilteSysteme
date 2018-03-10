package server.server.graph.nodes;

/**
 * Knotentyp zur verwendung im StreetGraph
 * Verwaltet KnotenId, vehicleId und die Orientierung des vehicels
 * @author Mathias Wittling
 *
 */
public class StreetNode {
	private String nodeId;
	private String vehicleId;
	private VehicleOrientation orientation;
	
	public StreetNode() {
		nodeId = null;
		vehicleId = null;
		orientation = null;
	}
	
	public StreetNode(String nodeId, String vehicleId, VehicleOrientation orientation) {
		this.nodeId = nodeId;
		this.vehicleId = vehicleId;
		this.orientation = orientation;
	}
	
	public StreetNode(String nodeId) {
		this();
		this.nodeId = nodeId;
	}
	
	/**
	 * Dreht die Orientierung nach links
	 */
	public void turnLeft() {
		orientation = orientation.turnLeft();
		System.out.println(this.orientation);
	}

	/**
	 * Dreht die Orientierung nach rechts
	 */
	public void turnRight() {
		orientation = orientation.turnRight();
		System.out.println(this.orientation);
	}
	
	/**
	 * Prüft ob dem Knoten eine vehicleId zugeordnet ist
	 * @return true falls keine Id zugeordnet, false wenn doch
	 */
	public boolean isEmpty() {
		if(vehicleId == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getNodeId() {
		return this.nodeId;
	}
	
	public String getVehicleId() {
		return this.vehicleId;
	}
	
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public void setOrientation(VehicleOrientation orientation) {
		this.orientation = orientation;
	}
	
	public VehicleOrientation getOrientation() {
		return this.orientation;
	}
	
	@Override
	public String toString() {
		return nodeId + "|" + vehicleId + "|"+ orientation;
	}

	
}

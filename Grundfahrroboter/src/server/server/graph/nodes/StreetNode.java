package server.server.graph.nodes;

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
		this.nodeId = nodeId;
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
	
	public boolean isEmpty() {
		if(vehicleId == null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return vehicleId + "|"+ orientation;
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

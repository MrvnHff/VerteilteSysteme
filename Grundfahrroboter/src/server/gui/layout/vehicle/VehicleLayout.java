package server.gui.layout.vehicle;

import server.gui.graph.Cell;
import server.gui.graph.Graph;
import server.gui.layout.base.Layout;
import javafx.scene.transform.Rotate;

/**
 * Zur Positionierung von Fahrezug Knoten
 * @author Mathias Wittling
 *
 */
public class VehicleLayout extends Layout{
	
	private Graph graph;
	
	public VehicleLayout(Graph graph) {
		this.graph = graph;
	}
	
	/**
	 * Does nothing
	 */
	public void execute() {
		
	}
	
	/**
	 * Bewegt einen Fahrzeugknoten zu einem normalen Knoten
	 * @param vehicleId Id des zu bewegenden Fahrzeugsknoten
	 * @param cellId Id zu dem der Fahrzeugknoten bewegt werden soll
	 */
	public void moveVehicleTo(String vehicleId, String cellId) {
		Cell cell = graph.getModel().getCell(cellId);
		Cell vehicle = graph.getModel().getVehicleCell(vehicleId);
		vehicle.relocate(cell.getLayoutX(), cell.getLayoutY());
	}
	
	/**
	 * Dreht einen Fahrzeugknoten um eine Gradzahl
	 * @param vehicleId Id des zu drehenden Fahrezeugsknoten
	 * @param angle Gradzahl um die gedreht werden soll
	 */
	public void rotate(String vehicleId, int angle) {
		Cell cell = graph.getModel().getVehicleCell(vehicleId);
		cell.getTransforms().add(new Rotate(angle, 26, 25));
		
	}

}

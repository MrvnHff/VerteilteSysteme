package server.gui.layout.vehicle;

import java.util.List;
import server.gui.cells.VehicleCell;
import server.gui.graph.Cell;
import server.gui.graph.Graph;
import server.gui.layout.base.Layout;
import javafx.scene.transform.Rotate;

public class VehicleLayout extends Layout{
	
	private Graph graph;
	
	public VehicleLayout(Graph graph) {
		this.graph = graph;
	}
	
	//does nothing at the moment
	public void execute() {
		List<VehicleCell> cells = graph.getModel().getAllVehicleCells();
	
		for(Cell cell: cells) {
			
		}
	}
	
	public void moveVehicleTo(String vehicleId, String cellId) {
		Cell cell = graph.getModel().getCell(cellId);
		Cell vehicle = graph.getModel().getVehicleCell(vehicleId);
		vehicle.relocate(cell.getLayoutX(), cell.getLayoutY());
	}
	
	public void rotate(String vehicleId, int angle) {
		Cell cell = graph.getModel().getVehicleCell(vehicleId);
		cell.getTransforms().add(new Rotate(angle, 26, 25));
		
	}

}

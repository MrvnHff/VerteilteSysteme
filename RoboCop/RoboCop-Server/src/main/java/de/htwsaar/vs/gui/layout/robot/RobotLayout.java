package de.htwsaar.vs.gui.layout.robot;

import java.util.List;
import de.htwsaar.vs.gui.cells.RobotCell;
import de.htwsaar.vs.gui.graph.Cell;
import de.htwsaar.vs.gui.graph.Graph;
import de.htwsaar.vs.gui.layout.base.Layout;
import javafx.scene.transform.Rotate;

public class RobotLayout extends Layout{
	
	private Graph graph;
	
	public RobotLayout(Graph graph) {
		this.graph = graph;
	}
	
	//does nothing at the moment
	public void execute() {
		List<RobotCell> cells = graph.getModel().getAllRobotCells();
	
		for(Cell cell: cells) {
			
		}
	}
	
	public void moveRobotTo(String robotId, String cellId) {
		Cell cell = graph.getModel().getCell(cellId);
		Cell robot = graph.getModel().getRobotCell(robotId);
		robot.relocate(cell.getLayoutX(), cell.getLayoutY());
	}
	
	public void rotate(String robotId, int angle) {
		Cell cell = graph.getModel().getRobotCell(robotId);
		cell.getTransforms().add(new Rotate(angle, 26, 25));
		
	}

}

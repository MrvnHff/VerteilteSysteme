package de.htwsaar.vs.gui.layout.robot;

import java.util.List;
import java.util.Map;

import de.htwsaar.vs.gui.graph.Cell;
import de.htwsaar.vs.gui.graph.Graph;
import de.htwsaar.vs.gui.layout.base.Layout;

public class RobotLayout extends Layout{
	
	private Graph graph;
	
	public RobotLayout(Graph graph) {
		this.graph = graph;
	}
	
	public void execute() {
		
	}
	
	public void moveRobotTo(String robotId, String cellId) {
		Cell cell = graph.getModel().getCell(cellId);
		Cell robot = graph.getModel().getRobotCell(robotId);
		robot.relocate(cell.getLayoutX(), cell.getLayoutY());
	}

}

package de.htwsaar.vs.gui.layout.grid;

import java.util.List;

import de.htwsaar.vs.gui.graph.Cell;
import de.htwsaar.vs.gui.graph.Graph;
import de.htwsaar.vs.gui.layout.base.Layout;

public class GridLayout extends Layout{
	
	private Graph graph;
	
	private int scale;
	
	public GridLayout(Graph graph) {
		this.graph = graph;
		this.scale = 1;
	}
	
	public GridLayout(Graph graph, int scale) {
		this(graph);
		this.scale = scale;
	}
	
	public void execute() {
		List<Cell> cells = graph.getModel().getAllCells();
		int coordinates[];
		
		for(Cell cell: cells) {
			coordinates = extractCoordinates(cell.getCellId());
			cell.relocate(coordinates[1] * scale, coordinates[0] * scale);
		}
	}
	
	public int[] extractCoordinates(String cellId) {
		String strCoordinates[];
		int	intCoordinates[] = new int[2];
		strCoordinates = cellId.split("/");
		intCoordinates[0] =  Integer.parseInt(strCoordinates[0]);
		intCoordinates[1] =  Integer.parseInt(strCoordinates[1]);
		return intCoordinates;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getscale() {
		return this.scale;
	}
	
}

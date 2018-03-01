package server.gui.layout.grid;

import java.util.List;

import server.gui.graph.Cell;
import server.gui.graph.Graph;
import server.gui.layout.base.Layout;
import server.utils.IdUtils;

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
			coordinates = IdUtils.extractCoordinates(cell.getCellId());
			cell.relocate(coordinates[1] * scale, coordinates[0] * scale);
		}
	}
	
	
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getscale() {
		return this.scale;
	}
	
}

package de.htwsaar.vs.server.graph;

import java.util.Collection;
import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import de.htwsaar.vs.server.graph.edges.RoboEdge;
import de.htwsaar.vs.server.graph.nodes.RoboNode;

public class RoboGraph {
	
	SimpleGraph<String, RoboEdge> roadGraph; 
	HashMap<String, RoboNode> roboNodeMap;
	int columnCount;
	int rowCount;
	
	public RoboGraph() {
		roadGraph = new SimpleGraph<String, RoboEdge>(RoboEdge.class);
		roboNodeMap = new HashMap<String, RoboNode>();
		columnCount = 0;
		rowCount = 0;
	}
	
	public RoboGraph(int row, int col) {
		this();
		
		columnCount = col;
		rowCount = row;
		
		for(int x = 0; x < row; x++) {
			for(int y = 0; y < col; y++) {
				addNode(x + "/" + y);
			}
		}
		
		for(int i = 0; i < row - 1 ; i++) {
    		for(int j = 0; j < col - 1 ; j++) {
    			addEdge(i + "/" + j, i + "/" + (j+1));
    			addEdge(i + "/" + j, (i+1) + "/" + j);
    		}
    	}
		
		for(int n = 0; n < col-1; n++) {
			addEdge((row-1) + "/" + n, (row-1) + "/" + (n+1));    			
		}
		for(int m = 0; m < row-1; m++) {
			addEdge(m + "/" + (col-1), (m+1) + "/" + (col-1));
		}
	}
	
	
	public void addNode(String nodeId) {
		roadGraph.addVertex(nodeId);
		roboNodeMap.put(nodeId, new RoboNode(nodeId));
	}
	
	public void addEdge(String source, String target) {
		roadGraph.addEdge(source, target);
	}
	
	public RoboNode getNode(String nodeId) {
		return roboNodeMap.get(nodeId);
	}
	
	public Collection<RoboNode> getAllNodes() {
		return roboNodeMap.values();
	}
	
	public int getColumnCount() {
		return columnCount;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public String toString() {
		return roadGraph.toString();
	}
	
}

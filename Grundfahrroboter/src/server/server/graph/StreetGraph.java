package server.server.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import server.server.exceptions.NoValidTargetNodeException;
import server.server.exceptions.TargetIsOccupiedException;
import server.server.graph.edges.StreetEdge;
import server.server.graph.nodes.StreetNode;
import server.server.graph.nodes.VehicleOrientation;
import server.utils.IdUtils;

public class StreetGraph {
	
	private SimpleGraph<String, StreetEdge> roadGraph; 
	private HashMap<String, StreetNode> streetNodeMap;
	private int columnCount;
	private int rowCount;
	
	public StreetGraph() {
		roadGraph = new SimpleGraph<String, StreetEdge>(StreetEdge.class);
		streetNodeMap = new HashMap<String, StreetNode>();
		columnCount = 0;
		rowCount = 0;
	}
	
	public StreetGraph(int row, int col) {
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
	

	public Set<StreetEdge> getAllEdges() {
		return roadGraph.edgeSet();
	}
	
	public Set<StreetEdge> getEdgesOf(String node) {
		return roadGraph.edgesOf(node);
	}
	
	public String getEdgeSource(StreetEdge edge) {
		return roadGraph.getEdgeSource(edge);
	}
	
	public String getEdgeTarget(StreetEdge edge) {
		return roadGraph.getEdgeTarget(edge);
	}
	
	public void addNode(String nodeId) {
		roadGraph.addVertex(nodeId);
		streetNodeMap.put(nodeId, new StreetNode(nodeId));
	}
	
	public void addEdge(String source, String target) {
		roadGraph.addEdge(source, target);
	}
	
	public StreetNode getNode(String nodeId) {
		return streetNodeMap.get(nodeId);
	}
	
	public StreetEdge getEdge(String source, String target) {
		return roadGraph.getEdge(source, target);
	}
	
	public Collection<StreetNode> getAllNodes() {
		return streetNodeMap.values();
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
	
	public String addVehicle(String vehicleId) {
		String position = findFreePosition();
		streetNodeMap.get(position).setVehicleId(vehicleId);
		streetNodeMap.get(position).setOrientation(VehicleOrientation.NORTH);
		return position;
	}

	private String findFreePosition() {
		StreetNode node;
		Iterator<StreetNode> it = streetNodeMap.values().iterator();
		while(it.hasNext()) {
			node = it.next();
			if(node.isEmpty()) {
				return node.getNodeId();
			}
		}
		return null;
	}
	
	public void removeVehicle(String vehicleId) {
		String nodeId = getVehiclePosition(vehicleId);
		streetNodeMap.get(nodeId).setVehicleId(null);
		streetNodeMap.get(nodeId).setOrientation(null);
		return;
	}
	
	public String getVehiclePosition(String vehicleId) {
		StreetNode node = getVehicleById(vehicleId);
		return node.getNodeId();
	}
	
	private StreetNode getVehicleById(String vehicleId) {
		StreetNode node;
		Iterator<StreetNode> it = streetNodeMap.values().iterator();
		while(it.hasNext()) {
			node = it.next();
			try {
				if(node.getVehicleId().equals(vehicleId)) {
					return node;
				}
			} catch(NullPointerException e) {} //Muss abgefangen werden, fall die  Vehicle ID NULL ist
		}
		return null;
	}

	public void turnVehicleLeft(String vehicleId) {
		StreetNode node = getVehicleById(vehicleId);
		node.turnLeft();
	}

	public void turnVehicleRight(String vehicleId) {
		StreetNode node = getVehicleById(vehicleId);
		node.turnRight();
	}
	
	private StreetNode findNodeInFrontOfVehicle(StreetNode node) {
		String targetNodeId;
		int coordinates[] = IdUtils.extractCoordinates(node.getNodeId());
		switch(node.getOrientation()) {
		case WEST:
			coordinates[1] -= 1;
			break;
		case NORTH:
			coordinates[0] -= 1;
			break;
		case EAST:
			coordinates[1] += 1;
			break;
		case SOUTH:
			coordinates[0] += 1;
		}
		targetNodeId = IdUtils.createIdString(coordinates[0], coordinates[1]);
		return getNode(targetNodeId);
	}
	
	/**
	 * Liefert die benötigte Drehung um das Fahrzeug in Richtung des Zielknoten auszurichten. Nur für benachbarte Knoten geeignet
	 * @param vehicleId Id des Fahrzeugs
	 * @param destination ID des Zielknotens
	 * @return benötigte drehung, -1 für einmal links, 1 für einmal rechts, 2 für zweimal rechts und so weiter
	 */
	public int getNeededRotation(String vehicleId, String destination) {
		VehicleOrientation neededOrientation;
		VehicleOrientation nodeOrientation;
		
		StreetNode node = getVehicleById(vehicleId);
		String nodeId = node.getNodeId();
		nodeOrientation = node.getOrientation();
		if(nodeId.equals(destination)) {
			return 0;
		}
		
		int Node[] = IdUtils.extractCoordinates(nodeId);
		int Dest[] = IdUtils.extractCoordinates(destination);
		
		if((Node[0] + 1) == Dest[0] && Node[1] == Dest[1]) {
			neededOrientation = VehicleOrientation.SOUTH;
		} else if((Node[0] - 1) == Dest[0] && Node[1] == Dest[1]) {
			neededOrientation = VehicleOrientation.NORTH;
		} else if(Node[0] == Dest[0] && (Node[1] - 1) == Dest[1]) {
			neededOrientation = VehicleOrientation.WEST;
		} else {
			neededOrientation = VehicleOrientation.EAST;
		}
		return nodeOrientation.getDifference(neededOrientation);
	}
	
	public String moveVehicleForward(String vehicleId) {
		StreetNode sourceNode = getVehicleById(vehicleId);
		StreetNode targetNode = findNodeInFrontOfVehicle(sourceNode);
		if(targetNode == null) {
			throw new NoValidTargetNodeException();
		} else if(!targetNode.isEmpty()) {
			throw new TargetIsOccupiedException();
		} else {
			sourceNode.setVehicleId(null);
			targetNode.setVehicleId(vehicleId);
			targetNode.setOrientation(sourceNode.getOrientation());
			return targetNode.getNodeId();
		}
	}

	public List<String> getShortesPath(String vehicleId, String destination) {
		StreetNode sourceNode = getVehicleById(vehicleId);
		String start = sourceNode.getNodeId();
		List<StreetNode> exceptions = new ArrayList<StreetNode>();
		exceptions.add(sourceNode);
		exceptions.add(getNode(destination));
		StreetGraph tempGraph = creatUnoccupiedGraph(exceptions);
		StreetNode node = tempGraph.getNode(destination);
		if(node == null) {
			throw new NoValidTargetNodeException();
		}
		return tempGraph.executeDijkstraShortestPathAlgo(start, destination);
	}
	
	private List<String> executeDijkstraShortestPathAlgo(String start, String destination) {
		DijkstraShortestPath<String, StreetEdge> shortestPathAlgo = new DijkstraShortestPath<String, StreetEdge>(roadGraph);
		GraphPath<String, StreetEdge> path = shortestPathAlgo.getPath(start, destination);
		return path.getVertexList();
	}

	private StreetGraph creatUnoccupiedGraph(List<StreetNode> exceptions) {
		List<StreetNode> occupiedNodes = this.getListOfOccupiedNodes();
		occupiedNodes.removeAll(exceptions);
		StreetGraph tempGraph = new StreetGraph(this.getRowCount(), this.getColumnCount());
		tempGraph.deleteNodes(occupiedNodes);
		return tempGraph;
	}

	private void deleteNodes(List<StreetNode> nodes) {
		for(StreetNode node: nodes) {
			deleteNode(node.getNodeId());
		}
	}

	private void deleteNode(String nodeId) {
		roadGraph.removeVertex(nodeId);
		this.streetNodeMap.remove(nodeId);
	}

	private List<StreetNode> getListOfOccupiedNodes() {
		Collection<StreetNode> nodes = getAllNodes();
		List<StreetNode> occupiedNodes = new LinkedList<StreetNode>();
		for(StreetNode node: nodes) {
			if(!node.isEmpty()) {
				occupiedNodes.add(node);
			}
		}
		return occupiedNodes;		
	}
	
	public boolean isNodeEmpty(String nodeId) {
		StreetNode node = getNode(nodeId);
		return node.isEmpty();
	}
	
}

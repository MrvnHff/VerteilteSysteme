package de.htwsaar.vs.server.graph;

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

import de.htwsaar.vs.server.exceptions.NoValidTargetNodeException;
import de.htwsaar.vs.server.exceptions.TargetIsOccupiedException;
import de.htwsaar.vs.server.graph.edges.RoboEdge;
import de.htwsaar.vs.server.graph.nodes.RoboNode;
import de.htwsaar.vs.server.graph.nodes.RobotOrientation;
import de.htwsaar.vs.utils.IdUtils;

public class RoboGraph {
	
	private SimpleGraph<String, RoboEdge> roadGraph; 
	private HashMap<String, RoboNode> roboNodeMap;
	private int columnCount;
	private int rowCount;
	
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
	

	public Set<RoboEdge> getAllEdges() {
		return roadGraph.edgeSet();
	}
	
	public Set<RoboEdge> getEdgesOf(String node) {
		return roadGraph.edgesOf(node);
	}
	
	public String getEdgeSource(RoboEdge edge) {
		return roadGraph.getEdgeSource(edge);
	}
	
	public String getEdgeTarget(RoboEdge edge) {
		return roadGraph.getEdgeTarget(edge);
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
	
	public RoboEdge getEdge(String source, String target) {
		return roadGraph.getEdge(source, target);
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
	
	public String addRobot(String robotId) {
		String position = findFreePosition();
		RoboNode node = roboNodeMap.get(position);
		node.setRobotId(robotId);
		node.setOrientation(RobotOrientation.NORTH);
		return position;
	}

	private String findFreePosition() {
		RoboNode node;
		Iterator<RoboNode> it = roboNodeMap.values().iterator();
		while(it.hasNext()) {
			node = it.next();
			if(node.isEmpty()) {
				return node.getNodeId();
			}
		}
		return null;
	}
	
	public String getRobotPosition(String robotId) {
		RoboNode node = getRobotById(robotId);
		return node.getNodeId();
	}
	
	private RoboNode getRobotById(String robotId) {
		RoboNode node;
		Iterator<RoboNode> it = roboNodeMap.values().iterator();
		while(it.hasNext()) {
			node = it.next();
			if(node.getRobotId() == robotId) {
				return node;
			}
		}
		return null;
	}

	public void turnRobotLeft(String robotId) {
		RoboNode node = getRobotById(robotId);
		node.turnLeft();
	}

	public void turnRobotRight(String robotId) {
		RoboNode node = getRobotById(robotId);
		node.turnRight();
	}
	
	private RoboNode findNodeInFrontOfRobot(RoboNode node) {
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
	 * Liefert die benötigte drehung um den Robotor in Richtung des Zielknoten auszurichten. Nur für benachbarte Knoten geeignet
	 * @param robotId Id des roboters
	 * @param destination id des Zielknotens
	 * @return benötigte drehung, -1 für einmal links, 1 für einmal rechts, 2 für zweimal rechts und so weiter
	 */
	public int getNeededRotation(String robotId, String destination) {
		RobotOrientation neededOrientation;
		RobotOrientation nodeOrientation;
		
		RoboNode node = getRobotById(robotId);
		String nodeId = node.getNodeId();
		nodeOrientation = node.getOrientation();
		if(nodeId.equals(destination)) {
			return 0;
		}
		
		int Node[] = IdUtils.extractCoordinates(nodeId);
		int Dest[] = IdUtils.extractCoordinates(destination);
		
		if((Node[0] + 1) == Dest[0] && Node[1] == Dest[1]) {
			neededOrientation = RobotOrientation.SOUTH;
		} else if((Node[0] - 1) == Dest[0] && Node[1] == Dest[1]) {
			neededOrientation = RobotOrientation.NORTH;
		} else if(Node[0] == Dest[0] && (Node[1] - 1) == Dest[1]) {
			neededOrientation = RobotOrientation.WEST;
		} else {
			neededOrientation = RobotOrientation.EAST;
		}
		return nodeOrientation.getDifference(neededOrientation);
	}
	
	public String moveRobotForward(String robotId) {
		RoboNode sourceNode = getRobotById(robotId);
		RoboNode targetNode = findNodeInFrontOfRobot(sourceNode);
		if(targetNode == null) {
			throw new NoValidTargetNodeException();
		} else if(!targetNode.isEmpty()) {
			throw new TargetIsOccupiedException();
		} else {
			sourceNode.setRobotId(null);
			targetNode.setRobotId(robotId);
			targetNode.setOrientation(sourceNode.getOrientation());
			return targetNode.getNodeId();
		}
	}

	public List<String> getShortesPath(String robotId, String destination) {
		RoboNode sourceNode = getRobotById(robotId);
		String start = sourceNode.getNodeId();
		RoboGraph tempGraph = creatUnoccupiedGraph(sourceNode);
		return tempGraph.executeDijkstraShortestPathAlgo(start, destination);
	}
	
	private List<String> executeDijkstraShortestPathAlgo(String start, String destination) {
		DijkstraShortestPath<String, RoboEdge> shortestPathAlgo = new DijkstraShortestPath<String, RoboEdge>(roadGraph);
		GraphPath<String, RoboEdge> path = shortestPathAlgo.getPath(start, destination);
		return path.getVertexList();
	}

	private RoboGraph creatUnoccupiedGraph(RoboNode exception) {
		List<RoboNode> occupiedNodes = this.getListOfOccupiedNodes();
		occupiedNodes.remove(exception);
		RoboGraph tempGraph = new RoboGraph(this.getRowCount(), this.getColumnCount());
		tempGraph.deleteNodes(occupiedNodes);
		return tempGraph;
	}

	private void deleteNodes(List<RoboNode> nodes) {
		for(RoboNode node: nodes) {
			deleteNode(node.getNodeId());
		}
	}

	private void deleteNode(String nodeId) {
		roadGraph.removeVertex(nodeId);
		this.roboNodeMap.remove(nodeId);
	}

	private List<RoboNode> getListOfOccupiedNodes() {
		Collection<RoboNode> nodes = getAllNodes();
		List<RoboNode> occupiedNodes = new LinkedList<RoboNode>();
		for(RoboNode node: nodes) {
			if(!node.isEmpty()) {
				occupiedNodes.add(node);
			}
		}
		return occupiedNodes;		
	}
	
	public boolean isNodeEmpty(String nodeId) {
		RoboNode node = getNode(nodeId);
		return node.isEmpty();
	}
	
}

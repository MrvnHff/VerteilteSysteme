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

import server.server.exceptions.NoValidNodeIdException;
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
	
	/**
	 * Erstellt einen leeren Graph ohne Knoten und Kanten
	 */
	public StreetGraph() {
		roadGraph = new SimpleGraph<String, StreetEdge>(StreetEdge.class);
		streetNodeMap = new HashMap<String, StreetNode>();
		columnCount = 0;
		rowCount = 0;
	}
	
	/**
	 * Erstellt einen GridGraph
	 * @param row Anzahl an Zeilen
	 * @param col Anzahl an Spalten
	 */
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
	
	/**
	 * Fügt einen Knoten ein
	 * @param nodeId Id des Knoten, muss dem Muster row/collumn entsprechen
	 */
	public void addNode(String nodeId) {
		if(IdUtils.isValidId(nodeId)) {
			roadGraph.addVertex(nodeId);
			streetNodeMap.put(nodeId, new StreetNode(nodeId));
		} else {
			throw new NoValidNodeIdException(nodeId);
		}
	}
	
	/**
	 * Fügt eine ungerichtet Kante in den Graph ein
	 * @param source StartKnoten
	 * @param target Zielknoten
	 */
	public void addEdge(String source, String target) {
		roadGraph.addEdge(source, target);
	}
	
	/**
	 * Gibt zurück ob sich auf dem Knoten ein Roboter befindet oder nicht
	 * @param nodeId zu überprüfende Knoten Id
	 * @return true wenn der Knoten leer ist, false wenn er belegt ist
	 */
	public boolean isNodeEmpty(String nodeId) {
		StreetNode node = getNode(nodeId);
		return node.isEmpty();
	}
	
	public boolean nodeExists(String nodeId) {
		StreetNode node = getNode(nodeId);
		if(node == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Fügt einen Roboter an einer freien Stelle in Richtung Norden, zum Graph hinzu
	 * @param vehicleId Id des Roboters
	 * @return Knoten id an dem der Roboter hinzugefügt wurde
	 */
	public String addVehicle(String vehicleId) {
		String position = findFreePosition();
		streetNodeMap.get(position).setVehicleId(vehicleId);
		streetNodeMap.get(position).setOrientation(VehicleOrientation.NORTH);
		return position;
	}
	
	/**
	 * Sucht einen Knoten der nicht von einem Roboter belegt ist
	 * @return KnotenId eines freien Knotens oder null wenn keiner gefunden wurde
	 */
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
	
	/**
	 * Entfernt einen Roboter aus dem Graphen
	 * @param vehicleId Id des zu entfernenden Roboters
	 */
	public void removeVehicle(String vehicleId) {
		String nodeId = getVehiclePosition(vehicleId);
		streetNodeMap.get(nodeId).setVehicleId(null);
		streetNodeMap.get(nodeId).setOrientation(null);
	}
	
	/**
	 * Löscht eine Liste von Knoten aus dem Graph
	 * @param nodes Liste von zu löschende Knoten
	 */
	private void deleteNodes(List<StreetNode> nodes) {
		for(StreetNode node: nodes) {
			deleteNode(node.getNodeId());
		}
	}

	/**
	 * Löscht einen einzelnen Knoten
	 * @param nodeId Id des zu löschenden Knoten
	 */
	private void deleteNode(String nodeId) {
		roadGraph.removeVertex(nodeId);
		this.streetNodeMap.remove(nodeId);
	}
	
	//###################################################
	//# 	Bewegungsmethoden							#
	//###################################################

	/**
	 * Dreht den Roboter um 90° nach links
	 * @param vehicleId Id des zu drehenden Roboters
	 */
	public void turnVehicleLeft(String vehicleId) {
		StreetNode node = getVehicleById(vehicleId);
		node.turnLeft();
	}

	/**
	 * Dreht den Roboter um 90° nach rechts
	 * @param vehicleId Id des zu drehenden Roboters
	 */
	public void turnVehicleRight(String vehicleId) {
		StreetNode node = getVehicleById(vehicleId);
		node.turnRight();
	}
	
	/**
	 * Findet den Knoten auf den der Roboter auf dem übergebenen Knoten ausgerichtet ist
	 * @param node Knoten auf dem sich der Roboter befindet
	 * @return Knoten Id des Knoten auf den der Roboter ausgerichtet ist
	 */
	private StreetNode findNodeInFrontOfVehicle(StreetNode node) {
		//TODO überprüfung einfügen ob sich auf dem Knoten überhaupt ein Roboter befindet 
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
		
		//TODO umbenennen und evtl. auslagern
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
	
	/**
	 * Bewegt den Roboter einen Knoten nach vorne
	 * @param vehicleId Roboter der Bewegt werden soll
	 * @return Knoten Id auf dem sich der Roboter nach der bewegung befindet
	 */
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
	
	//###################################################
	//# 	Shortest-Path Methoden						#
	//###################################################

	/**
	 * Findet einen kürzesten Pfad von einem Roboter zu einem Zielknoten
	 * @param vehicleId Id des Roboters
	 * @param destination KnotenId des ZielKnoten
	 * @return Liste von KnotenId's von der Position des Roboters bis zum Zielknoten
	 */
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
	
	/**
	 * Wendet den Dijkstra ShortestPath Algoritmus auf den Graphen an, mit den übergebenen start und zielknoten
	 * @param start Id des Startknotens
	 * @param destination Id des Zielknotens
	 * @return Liste von KnotenId die den Pfad darstellen
	 */
	private List<String> executeDijkstraShortestPathAlgo(String start, String destination) {
		DijkstraShortestPath<String, StreetEdge> shortestPathAlgo = new DijkstraShortestPath<String, StreetEdge>(roadGraph);
		GraphPath<String, StreetEdge> path = shortestPathAlgo.getPath(start, destination);
		return path.getVertexList();
	}

	/**
	 * Kopiert den aktuellen graphen und entfernt alle besetzten Knoten aus dem Graph, 
	 * ausgenommen die Knoten die in einer Liste übergeben werden
	 * @param exceptions Knoten die nicht aus dem Graph entfernt werden sollen
	 * @return kopierter und modifizierter Graph
	 */
	private StreetGraph creatUnoccupiedGraph(List<StreetNode> exceptions) {
		List<StreetNode> occupiedNodes = this.getListOfOccupiedNodes();
		occupiedNodes.removeAll(exceptions);
		//TODO echte Kopie vom Graph erstellen
		StreetGraph tempGraph = new StreetGraph(this.getRowCount(), this.getColumnCount());
		tempGraph.deleteNodes(occupiedNodes);
		return tempGraph;
	}
	
	//###################################################
	//# 	           Getter/Setter					#
	//###################################################
	
	/**
	 * Erstellt eine Liste von Knoten die von Robotern belegt sind
	 * @return Liste mit belegten Knoten
	 */
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
	
	public StreetNode getNode(String nodeId) {
		return streetNodeMap.get(nodeId);
	}
	
	public StreetEdge getEdge(String source, String target) {
		return roadGraph.getEdge(source, target);
	}
	
	public Collection<StreetNode> getAllNodes() {
		return streetNodeMap.values();
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

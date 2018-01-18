package de.htwsaar.vs.server;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import de.htwsaar.vs.server.graph.RoboGraph;
import de.htwsaar.vs.server.graph.nodes.Orientation;
import de.htwsaar.vs.server.graph.nodes.RoboNode;

public class Server {
	
	RoboGraph roboGraph;
	
	public Server() {
		roboGraph = new RoboGraph(3, 3);		
	}
	
	public RoboGraph getRoboGraph() {
		return this.roboGraph;
	}
	
	
	public static void main(String[] args) {
        Server server = new Server();
    }
}

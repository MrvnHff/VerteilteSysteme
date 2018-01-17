package de.htwsaar.vs.server;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import de.htwsaar.vs.server.graph.nodes.Orientation;
import de.htwsaar.vs.server.graph.nodes.RoboNode;

public class Server {
	SimpleGraph<RoboNode, DefaultEdge> roadMap; 
	
	public Server() {
		roadMap = new SimpleGraph<RoboNode, DefaultEdge>(DefaultEdge.class);
		RoboNode test1 = new RoboNode("test1", Orientation.NORTH);
		RoboNode test2 = new RoboNode("test2", Orientation.SOUTH);
		roadMap.addVertex(test1);
		roadMap.addVertex(test2);
		roadMap.addEdge(test1, test2);
		System.out.println(roadMap.toString());
	}
	
	public static void main(String[] args) {
        Server server = new Server();
    }
}

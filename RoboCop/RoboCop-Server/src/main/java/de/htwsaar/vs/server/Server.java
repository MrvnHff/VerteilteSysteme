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
		roboGraph = new RoboGraph(2, 3);
		/*roboGraph.addNode("0/0");
		roboGraph.addNode("0/1");
		roboGraph.addNode("1/0");
		roboGraph.addNode("1/1");
		roboGraph.addEdge("0/0", "0/1");
		roboGraph.addEdge("0/1", "1/1");
		roboGraph.addEdge("1/1", "1/0");
		roboGraph.addEdge("1/0", "0/0");*/
		
	}
	
	public RoboGraph getRoboGraph() {
		return this.roboGraph;
	}
	
	
	public static void main(String[] args) {
        Server server = new Server();
    }
}

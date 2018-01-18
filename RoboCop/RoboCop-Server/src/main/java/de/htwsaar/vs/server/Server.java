package de.htwsaar.vs.server;

import de.htwsaar.vs.server.graph.RoboGraph;

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

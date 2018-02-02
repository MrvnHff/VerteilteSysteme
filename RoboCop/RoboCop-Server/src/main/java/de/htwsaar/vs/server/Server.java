package de.htwsaar.vs.server;

import java.rmi.registry.Registry;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import de.htwsaar.vs.server.graph.RoboGraph;
import de.htwsaar.vs.server.graph.nodes.Orientation;
import de.htwsaar.vs.server.graph.nodes.RoboNode;

public class Server {
	private String serverLog;
	private Registry registry;
	private String robotLog;
	private boolean robotStatus;
	private WorkerImplement worker;
	
	private RoboGraph roboGraph;
	
	/**
	 * Konstruktor
	 */
	public Server() {
		roboGraph = new RoboGraph(3, 3);		
	}
	
	/**
	 * Gibt den RoboGraph objekt zurück.
	 * @return roboGraph
	 */
	public RoboGraph getRoboGraph() {
		return this.roboGraph;
	}
	
	
	/**
	 * Die Methode gibt den log des Roboters zurück
	 * @return robotLog
	 */
	public String getRobotLog() {
        return this.robotLog;
    }
	
    /**
     * Stellt den Art des Logs des Roboters fest.
     * @return robotStatus
     */
    public boolean isRobotStatus() {
        return this.robotStatus;
    }
    
	/**
	 * Zum Starten des Servers
	 */
	public void startServer() {
		
	}
	
	/**
	 * Zum Stoppen des Servers
	 */
	public void stopServer() {
		
	}
	
	/**
	 * Zum Schreiben Des logs des Servers.
	 * @param log
	 */
    public void printServerLog(String log){
    	this.serverLog = "-> "+log;
    }
    
    /**
	 * Zum Schreiben Des logs des Roboters.
	 * @param log
	 */
    public void printRobotLog(String log, boolean status){
    	this.robotStatus = status;
    	this.robotLog = "-> "+log;
    }

	
	
	public static void main(String[] args) {
        Server server = new Server();
    }
}

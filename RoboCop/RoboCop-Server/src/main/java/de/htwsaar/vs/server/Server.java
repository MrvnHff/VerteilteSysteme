package de.htwsaar.vs.server;

import java.rmi.registry.Registry;

import de.htwsaar.vs.gui.Gui;
import de.htwsaar.vs.server.graph.RoboGraph;

public class Server {
	private Gui gui;
	
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
	 * Fügt neuen Roboter zum RoboGraph hinzu und in die GUI ein
	 * @param robotId Id des neuen Roboters
	 */
	private void addRobot(String robotId) {
		String position;
		position = roboGraph.addRobot(robotId);
		gui.addRobot(robotId, position);
	}
	
	public void turnRobotLeft(String robotId) {
		roboGraph.turnRobotLeft(robotId);
		//toDo: methode die die bewegung an den physichen roboter kommuniziert
	}
	
	public void turnRobotRight(String robotId) {
		roboGraph.turnRobotRight(robotId);
		//toDo: methode die die bewegung an den physichen roboter kommuniziert
	}
	
	public void setGui(Gui gui) {
		this.gui = gui;
		//Wird momentan hier nur zum testen hinzugefügt. die Roboter sollen später natürlich dynamisch hinzugefügt werden sobald sie sich mit dem Server verbinden
		addRobot("George");
		addRobot("Jane");
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

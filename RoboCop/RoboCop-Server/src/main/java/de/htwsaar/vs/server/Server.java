package de.htwsaar.vs.server;

import java.rmi.registry.Registry;
import java.util.List;

import de.htwsaar.vs.gui.Gui;
import de.htwsaar.vs.server.graph.RoboGraph;

public class Server implements ServerInterface{
	private Gui gui;
	
	private String serverLog;
	private Registry registry;
	private String robotLog;
	private boolean robotFlag;
	private boolean serverFlag;
	
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
	
	/**
	 * Dreht den Roboter im RoboGraph und gibt den Befehl an den physischen roboter weiter
	 */
	public void turnRobotLeft(String robotId) {
		roboGraph.turnRobotLeft(robotId);
		//toDo: methode die die bewegung an den physichen roboter kommuniziert
	}
	
	
	public void turnRobotLeftGui(String robotId) {
		turnRobotLeft(robotId);
		gui.turnRobotLeft(robotId);
	}
	
	/**
	 * Dreht den Roboter im RoboGraph und gibt den Befehl an den physischen roboter weiter
	 */
	public void turnRobotRight(String robotId) {
		roboGraph.turnRobotRight(robotId);
		//toDo: methode die die bewegung an den physichen roboter kommuniziert
	}
	
	public void turnRobotRightGui(String robotId) {
		turnRobotRight(robotId);
		gui.turnRobotRight(robotId);
	}
	
	public String moveRobotForward(String robotId) {
		String destination = roboGraph.moveRobotForward(robotId);
		//toDo: methode die die bewegung an den physichen roboter kommuniziert
		return destination;
	}
	
	public void moveRobotForwardGui(String robotId) {
		String destination = moveRobotForward(robotId);
		gui.setRobotPosition(robotId, destination);
		//wartezeit dient nur dazu, das man den Pfad in der gui nachverfolgen kann
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void driveRobotTo(String robotId, String destination) {
		String position = roboGraph.getRobotPosition(robotId);
		List<String> path = roboGraph.getShortesPath(robotId, destination);
		path.remove(0);
		//Der folgende algorithmus ist arbeit für einen eigenen Thread/Worker, sodass die pfade gleichzeitg für mehrere roboter abgearbeitet werden können
		for(String nodeId: path) {
			
			int rotationsNeeded = roboGraph.getNeededRotation(robotId, nodeId);
			switch(rotationsNeeded) {
				case 1: turnRobotRightGui(robotId);
						break;
				case 2: turnRobotRightGui(robotId);
						turnRobotRightGui(robotId);
						break;
				case -1:turnRobotLeftGui(robotId);
						break;
				case -2:turnRobotLeftGui(robotId);
						break;
				default:break;
			}
			moveRobotForwardGui(robotId);
		}
	}
	
	public void setGui(Gui gui) {
		this.gui = gui;
		//Wird momentan hier nur zum testen hinzugefügt. die Roboter sollen später natürlich dynamisch hinzugefügt werden sobald sie sich mit dem Server verbinden
		addRobot("George");
		addRobot("Jane");
		gui.addServerTextMessage("Hello World");
		gui.addRobotTextMessage("George", "Hello George");
		gui.addRobotTextMessage("Jane", "Hello Jane");
	}
	
	/**
	 * Gibt den RoboGraph objekt zurück.
	 * @return roboGraph
	 */
	public RoboGraph getRoboGraph() {
		return this.roboGraph;
	}
	
	public boolean getServerFlag() {
		// TODO Auto-generated method stub
		return this.serverFlag;
	}

	
	public String getServerLog() {
		// TODO Auto-generated method stub
		return this.serverLog;
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
    public boolean getRobotFlag() {
        return this.robotFlag;
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
    private void printServerLog(String serverLog, boolean serverFlag){
    	this.serverLog = serverLog;
    	this.serverFlag = serverFlag;
    }
    
    /**
     * Zum Schreiben Des logs des Roboters.
     * @param log
     * @param flag
     */
    public void printRobotLog(String robotLog, boolean robotFlag){
    	this.robotFlag = robotFlag;
    	this.robotLog = robotLog;
    }

	
	
	public static void main(String[] args) {
        Server server = new Server();
    }
	
}

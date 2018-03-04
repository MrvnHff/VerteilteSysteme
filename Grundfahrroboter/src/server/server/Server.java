package server.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import client.RoboServerInterface;
import server.gui.Gui;
import server.server.exceptions.TargetIsOccupiedException;
import server.server.graph.RoboGraph;

public class Server implements ServerInterface{
	private Gui gui;
	
	private String serverLog;
	private String roboErrorLog;
	private String roboLog;
	private Registry registry;
	private RoboServerInterface robo;
	
	private RoboGraph roboGraph;
	
	/**
	 * Konstruktor
	 */
	public Server() {
		roboGraph = new RoboGraph(3, 3);
		/*try {
			registry = LocateRegistry.getRegistry("192.168.178.26", 55555);
			robo = (RoboServerInterface) registry.lookup("Robo2");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}*/
		
	}

	/**
	 * gibt die Fehler des Roboters zur�ck.
	 * @return roboErrorLog
	 */
	public String getRoboErrorLog() {
		return this.roboErrorLog;
	}
	
	/**
	 * gibt den Log des Roboters zur�ck
	 * @return roboLog
	 */
	public String getRoboLog() {
		return this.roboLog;
	}
	
	/**
	 * Diese Methode schreibt Fehler kommend vom Roboter.
	 * @param roboErrorLog
	 */
	public void printRoboErrorLog(String roboErrorLog) {
		this.roboErrorLog = roboErrorLog;
	}
	
	/**
	 * Zum Schreiben von Log(Meldungen) des Roboters.
	 * @param roboLog
	 */
	public void printRoboLog(String roboLog) {
		this.roboLog = roboLog;
	}


	/**
	 * Gibt den Log des Servers zur�ck.
	 * @return serverLog
	 */
	public String getServerLog() {
		return this.serverLog;
	}


	/**
	 * Zum Schreiben von Server logs
	 * @param serverLog
	 */
	public void printServerLog(String serverLog) {
		this.serverLog = serverLog;
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
	
	/**
	 * 
	 * @param robotId
	 */
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
	
	/**
	 * 
	 * @param robotId
	 */
	public void turnRobotRightGui(String robotId) {
		turnRobotRight(robotId);
		gui.turnRobotRight(robotId);
	}
	
	/**
	 * 
	 * @param robotId
	 */
	public String moveRobotForward(String robotId) {
		String destination = roboGraph.moveRobotForward(robotId);
		//toDo: methode die die bewegung an den physichen roboter kommuniziert
		return destination;
	}
	
	/**
	 * 
	 * @param robotId
	 */
	public void moveRobotForwardGui(String robotId) {
		try {
				String destination = moveRobotForward(robotId);
				gui.setRobotPosition(robotId, destination);
		} catch (TargetIsOccupiedException e) {
			System.out.println(e);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException f) {
				// TODO Auto-generated catch block
				f.printStackTrace();
			}
			moveRobotForwardGui(robotId);
		}
		
		//wartezeit dient nur dazu, das man den Pfad in der gui nachverfolgen kann
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param robotId
	 * @param destination
	 */
	public void driveRobotTo(String robotId, String destination) {
		String position = roboGraph.getRobotPosition(robotId);
		List<String> path = roboGraph.getShortesPath(robotId, destination);
		
		
		
		//Der folgende algorithmus ist arbeit für einen eigenen Thread/Worker, sodass die pfade gleichzeitg für mehrere roboter abgearbeitet werden können
		while(path.size() > 1) {
			String nodeId = path.get(1);
			if((path.size() == 2) && (nodeId == destination)) {
				gui.addRobotTextMessage(robotId, "bääähhh");
			}
			
			int rotationsNeeded = roboGraph.getNeededRotation(robotId, nodeId);
			turnRobot(rotationsNeeded, robotId);
			
			moveRobotForwardGui(robotId);
			path = roboGraph.getShortesPath(robotId, destination);
		}
	}
	
	/**
	 * 
	 * @param rotationsNeeded
	 * @param robotId
	 */
	private void turnRobot(int rotationsNeeded, String robotId) {
		switch(rotationsNeeded) {
			case 1: turnRobotRightGui(robotId);
					break;
			case 2: turnRobotRightGui(robotId);
					turnRobotRightGui(robotId);
					break;
			case 3: turnRobotLeftGui(robotId);
					break;
			case -1:turnRobotLeftGui(robotId);
					break;
			case -2:turnRobotLeftGui(robotId);
					turnRobotLeftGui(robotId);
					break;
			case -3:turnRobotRightGui(robotId);
					break;
			default:break;
		}
	}
	
	/**
	 * 
	 * @param gui
	 */
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
	
	
	public static void main(String[] args) {
        Server server = new Server();
    }
	
}

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
	private String robotLog;
	private boolean robotFlag;
	private boolean serverFlag;
	
	private ListenerInterface listener;
	private Worker[] worker;
	private int anzahl;
	
	private RoboGraph roboGraph;
	
	private final static int MAXWORKER = 4;
	
	/**
	 * Konstruktor
	 */
	public Server() {
		roboGraph = new RoboGraph(3, 3);
		listener = new Listener(this, MAXWORKER);
		worker = new Worker[MAXWORKER];
		anzahl = 0;
	}
	
	public void addWorker(Worker worker, int position) {
		this.worker[position] = worker;
		anzahl++;
	}
	
	public void removeWorker(String workerName) {
		int i = findWorker(workerName);
		worker[i] = null;
	}
	
	private int findEmpty() {
		for (int i = 0; i < MAXWORKER; i++) {
			if (worker[i] == null) {return i;}
		}
		return -1;
	}
	
	public int getNextFreeWorkerNumber() {
		return findEmpty();
	}
	
	private int findWorker(String workerName) {
		int i = 0;
		for (i = 0; i < MAXWORKER; i++) {
			if (worker[i].getWorkerName() == workerName) {return i;}
		}
		return -1;
	}
	
	private int findRobot(String robotName) {
		int i = 0;
		for (i = 0; i < MAXWORKER; i++) {
			if (worker[i].getRoboName() == robotName) {return i;}
		}
		return -1;
	}
	
	public boolean isAllowedToAddWorker() {
		if (anzahl < MAXWORKER) {
			return true;
		}
		return false;
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
	
	/*###############################################
	 * # 	Funktionen zum Steuern der Roboter		#
	 * ##############################################
	 */
	
	/**
	 * Dreht den Roboter im RoboGraph und gibt den Befehl an den physischen roboter weiter
	 */
	public void turnRobotLeft(String robotId) {
		roboGraph.turnRobotLeft(robotId);
		//worker[findRobot(robotId)].turnLeft();
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
		//worker[findRobot(robotId)].turnRight();
	}
	
	public void turnRobotRightGui(String robotId) {
		turnRobotRight(robotId);
		gui.turnRobotRight(robotId);
	}
	
	public String moveRobotForward(String robotId) {
		String destination = roboGraph.moveRobotForward(robotId);
		//TODO Geschwindigkeit nicht fest an Roboter �bergeben
		//worker[findRobot(robotId)].driveNextPoint(50);
		return destination;
	}
	
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
		
		// TODO Wartezeit dient nur dazu, das man den Pfad in der gui nachverfolgen kann
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
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
	
	public void setGui(Gui gui) {
		this.gui = gui;
		//TODO roboter in der GUI registrieren
		//Wird momentan hier nur zum testen hinzugefügt. die Roboter sollen später natürlich dynamisch hinzugefügt werden sobald sie sich mit dem Server verbinden
		//addRobot("George");
		//addRobot("Jane");
		//gui.addServerTextMessage("Hello World");
		//gui.addRobotTextMessage("George", "Hello George");
		//gui.addRobotTextMessage("Jane", "Hello Jane");
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
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	public void stopServer() {
		try {
			listener.stopListener();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < MAXWORKER; i++) {
			if (worker[i] != null) {
				try {
					worker[i].closeConnection();
				} catch (RemoteException | NotBoundException e) {}
			}
		}
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

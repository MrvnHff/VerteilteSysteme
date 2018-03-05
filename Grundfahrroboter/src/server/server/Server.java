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
	private int maxWorker;
	
	private RoboGraph roboGraph;
	
	private int port;
	
	private final static int STD_MAXWORKER = 4;
	private final static int STD_LISTENER_PORT = 55555;
	private final static int STD_GRAPH_ROWS = 3;
	private final static int STD_GRAPH_COMLUMNS = 3;
	
	/**
	 * Standard-Konstruktor erzeugt einen Standard-Graphen (3x3),
	 * startet einen Listener am Standard Port (55555) an,
	 * legt den Standardwert f¸r die maximale Workerzahl fest (4)
	 */
	public Server() {
		this(STD_GRAPH_ROWS, STD_GRAPH_COMLUMNS, STD_LISTENER_PORT, STD_MAXWORKER);
	}
	
	/**
	 * Konstruktor der einen Server mit den angegebenen Parametern startet
	 * @param graphRows Anzahl der vertikalen Knoten des Graphen
	 * @param graphColumns Anzahl der horizontalen Knoten des Graphen
	 * @param port Port, an dem der Listener gestartet werden soll
	 * @param maxWorker Maximale Anzahl an Worker (und somit auch an registrierbaren Robotern)
	 */
	public Server(int graphRows, int graphColumns, int port, int maxWorker) {
		roboGraph = new RoboGraph(graphRows, graphColumns);
		worker = new Worker[maxWorker];
		this.maxWorker = maxWorker;
		anzahl = 0;
		this.port= port;
	}
	
	/**
	 * Registriert einen neuen Worker beim Server und f¸gt den Roboter dem Graphen hinzu
	 * @param worker Das Worker Objekt
	 * @param position Die Position, an die der Worker geschrieben werden soll
	 */
	public void addWorker(Worker worker, int position) {
		this.worker[position] = worker;
		anzahl++;
		addRobot(this.worker[position].getRoboName());
	}
	
	/**
	 * Entfernt den Worker und den Roboter aus dem Graphen
	 * @param workerName
	 */
	public void removeWorker(String workerName) {
		int i = findWorker(workerName);
		removeRobot(this.worker[i].getRoboName());
		worker[i] = null;
		i--;
		
	}

	
	public int getNextFreeWorkerNumber() {
		for (int i = 0; i < maxWorker; i++) {
			if (worker[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	private int findWorker(String workerName) {
		int i = 0;
		for (i = 0; i < maxWorker; i++) {
			if (worker[i].getWorkerName() == workerName) {return i;}
		}
		return -1;
	}
	
	private int findRobot(String robotName) {
		int i = 0;
		for (i = 0; i < maxWorker; i++) {
			if (worker[i].getRoboName() == robotName) {return i;}
		}
		return -1;
	}
	
	public boolean isAllowedToAddWorker() {
		return(anzahl < maxWorker);
	}
	
	/**
	 * F√ºgt neuen Roboter zum RoboGraph hinzu und in die GUI ein
	 * @param robotId Id des neuen Roboters
	 */
	private void addRobot(String robotId) {
		String position;
		position = roboGraph.addRobot(robotId);
		gui.addRobot(robotId, position);
	}
	
	private void removeRobot(String robotId) {
		roboGraph.removeRobot(robotId);
		gui.removeRobot(robotId);
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
		//TODO Geschwindigkeit nicht fest an Roboter ¸bergeben
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
		
		
		
		//Der folgende algorithmus ist arbeit f√ºr einen eigenen Thread/Worker, sodass die pfade gleichzeitg f√ºr mehrere roboter abgearbeitet werden k√∂nnen
		while(path.size() > 1) {
			String nodeId = path.get(1);
			if((path.size() == 2) && (nodeId == destination)) {
				gui.addRobotTextMessage(robotId, "b√§√§√§hhh");
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
		//Wird momentan hier nur zum testen hinzugef√ºgt. die Roboter sollen sp√§ter nat√ºrlich dynamisch hinzugef√ºgt werden sobald sie sich mit dem Server verbinden
		//addRobot("George");
		//addRobot("Jane");
		//gui.addServerTextMessage("Hello World");
		//gui.addRobotTextMessage("George", "Hello George");
		//gui.addRobotTextMessage("Jane", "Hello Jane");
	}
	
	/**
	 * Gibt den RoboGraph objekt zur√ºck.
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
	 * Die Methode gibt den log des Roboters zur√ºck
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
		listener = new Listener(this, port);
	}
	
	/**
	 * Zum Stoppen des Servers
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	public void stopServer() {
		try {
			listener.stopListener();
			listener = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < maxWorker; i++) {
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
    
    /**
     * F¸gt eine Meldung in das Textfeld eines Roboters in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param robotId Die ID des Roboters
     * @param message Die Meldung die hinzugef¸gt werden soll
     */
    public void addRobotTextMessage(String robotId, String message) {
    	if(gui != null) {
    		gui.addRobotTextMessage(robotId, message);
    	} else {
    		System.out.println("Keine GUI registriert. Meldung: " + robotId + ": " + message);
    	}
    }
    
    
    /**
     * F¸r eine Meldung in das Textfeld des Servers in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param message Die Nachricht die hinzugef¸gt werden soll
     */
    public void addServerTextMessage(String message) {
    	if(gui != null) {
    		gui.addServerTextMessage(message);
    	} else {
    		System.out.println("Keine GUI registriert. Servermeldung: " + message);
    	}
    }

	
	
    /**
	 * Main-Methode, um den Server als Standalone ohne GUI zu starten.
	 * @param args Parameterliste: Leer um einen Server mit den Standardparametern zu starten, sonst: rowCount columnCount portNumber maxWorkers
	 */
	public static void main(String[] args) {
        if(args.length == 0) {
        	Server server = new Server();
        } else {
       		try {
       			int rows = Integer.parseInt(args[0]);
       			int columns = Integer.parseInt(args[1]);
       			int port = Integer.parseInt(args[2]);
       			int workerMax = Integer.parseInt(args[3]);
       			Server server = new Server(rows, columns, port, workerMax);
       		} catch (ArrayIndexOutOfBoundsException e) {
       			System.out.println("Keinen Server gestartet. Zu wenige Parameter ¸bergeben!");
       		} catch (NumberFormatException e) {
       			System.out.println("Keinen Server gestartet. Parameter in ung¸ltigem Format eingegeben!");
       		} finally {
       			System.out.println("Zu ¸bergebende Parameterliste: rowCount columnCount portNumber maxWorkers");
       		} 	
        }
    }
	
}

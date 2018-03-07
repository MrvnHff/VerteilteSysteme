package server.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<String, Thread> robotMode = new HashMap<String, Thread>();
	
	private int port;
	
	private final static int STD_MAXWORKER = 4;
	private final static int STD_LISTENER_PORT = 55555;
	private final static int STD_GRAPH_ROWS = 3;
	private final static int STD_GRAPH_COMLUMNS = 3;
	
	/**
	 * Standard-Konstruktor erzeugt einen Standard-Graphen (3x3),
	 * startet einen Listener am Standard Port (55555) an,
	 * legt den Standardwert fï¿½r die maximale Workerzahl fest (4)
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
		this.maxWorker = maxWorker;
		worker = new Worker[maxWorker];
		anzahl = 0;
		this.port= port;
	}
	
	/**
	 * Registriert einen neuen Worker beim Server und fï¿½gt den Roboter dem Graphen hinzu
	 * @param worker Das Worker Objekt
	 * @param position Die Position, an die der Worker geschrieben werden soll
	 * @throws Exception Wird geworfen, wenn die versucht wird mehr worker als zulässig hinzuzufügen
	 */
	public void addWorker(String roboName, String roboIp, int roboPort) throws Exception {
		// Wenn ein Worker zu dieser Roboter ID existiert, beende diesen.
		if(isAddWorkerAllowed()) {
			int existingWorkerPosition = findWorkerToRobotId(roboName) ; 
			if(existingWorkerPosition >= 0) {
				removeWorker(existingWorkerPosition);
			}
			int nextFreePos = getNextFreeWorkerNumber();
			this.worker[nextFreePos] = new Worker(this, "Worker_" + nextFreePos, roboName, roboIp, roboPort, (this.port + nextFreePos + 1));
			anzahl++;
			addRobot(roboName);
		} else {
			throw new Exception("Maximale Anzahl an Worker erreicht. Worker wurde nicht gestartet.");
		}
	}
	
	/**
	 * Entfernt den Worker und den Roboter aus dem Graphen anhand des Worker Namens
	 * @param workerName
	 */
	public void removeWorker(String robotId) {
		int i = findWorkerToRobotId(robotId);
		removeWorker(i); //FIXME kontrollieren
	}
	
	/**
	 * Entfernt den Worker und den Roboter aus dem Graphen anhand der Worker Position im Array
	 * @param position Index des Worker Arrays, an welcher der Worker entfernt werden soll
	 */
	private void removeWorker(int position) { //FIXME kontrollieren
		//TODO Fehlerbehandlung fï¿½r ungï¿½ltigen Parameter
		//FIXME Testen ob das funktioniert, wenn es von addWorker aufgerufen wird
		//Entfernen des Roboters aus dem Graphen
		//Anhalten des Workers
		try {
			worker[position].closeConnection();
			removeRobot(this.worker[position].getRoboName());
			worker[position] = null;
			anzahl--;
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Server: Fehler bei removeRobot() " + e);
		}		
	}

	
	private int getNextFreeWorkerNumber() {
		for (int i = 0; i < maxWorker; i++) {
			if (worker[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	/*
	private int findWorker(String workerName) {
		int i = 0;
		for (i = 0; i < maxWorker; i++) {
			try {
				if (worker[i].getWorkerName() == workerName) {
					return i;
				}
			}catch(NullPointerException e) {
				//Wird geworfen, falls worker[i] = null ist
				//keine weitere Behandlung notwendig
			}
		}
		return -1;
	}*/
	
	/**
	 * Sucht den Worker im Array zum Roboter Namen
	 * @param robotName Name des Roboters
	 * @return Index der Fundstelle, wenn nicht vorhanden -1
	 */
	private int findWorkerToRobotId(String robotName) {
		for (int i = 0; i < maxWorker; i++) {
			try {
				if (worker[i].getRoboName().equals(robotName)) {
					return i;
				}
			} catch(NullPointerException e) {
				//Wird geworfen, falls worker[i] = null ist
				//keine weitere Behandlung notwendig
			}
		}
		return -1;
	}
	
	public boolean isAddWorkerAllowed() {
		return(anzahl < maxWorker);
	}
	
	/**
	 * FÃ¼gt neuen Roboter zum RoboGraph hinzu und in die GUI ein
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
		if(isRobotInAutoMode(robotId)) {
			deactivateAutoDst(robotId);
		}
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
		try {
			worker[findWorkerToRobotId(robotId)].turnLeft();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		try {
			worker[findWorkerToRobotId(robotId)].turnRight();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void turnRobotRightGui(String robotId) {
		turnRobotRight(robotId);
		gui.turnRobotRight(robotId);
	}
	
	public String moveRobotForward(String robotId) {
		String destination = roboGraph.moveRobotForward(robotId);
		//TODO Geschwindigkeit nicht fest an Roboter ï¿½bergebe		
		try {
			worker[findWorkerToRobotId(robotId)].driveNextPoint(50);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	//###################################################
	//# 	Automatische Steuerung der Roboter			#
	//###################################################
	
	public String generateRndDestination() {
		int x;
		int y;
		x = (int) (Math.random()* roboGraph.getColumnCount());
		y = (int) (Math.random()* roboGraph.getRowCount());
		
		return (x + "/" + y);
	}
	
	/**
	 * Startet einen Thread, der dem angegebenen Roboter in einer Endlosschleife driveRobotTo 
	 * mit zufällig generierten Zielen ausführt.
	 * @param robotId Die ID des Roboters
	 */
	public void activateAutoDst(String robotId) {		
		if(!isRobotInAutoMode(robotId)) {
			// Starte Thread für Roboter Auto Destination Mode
			Thread t = new Thread( new AutoDestinationThread(robotId, this));
			t.run();
			robotMode.put(robotId, t);
			addRobotTextMessage(robotId, "Automodus gestartet in Thread " + t.getName());
		} else {
			addRobotTextMessage(robotId, "Roboter ist bereits in AUTO-MODE");
		}
		return ; 
	}
	
	/**
	 * Beendet den Thread der dem Roboter automatisch neue Ziele zuweist
	 * @param robotId Die ID des Roboters
	 */
	public void deactivateAutoDst(String robotId) {
		if(isRobotInAutoMode(robotId)) {
			Thread t = robotMode.get(robotId);
			t.interrupt();
			robotMode.remove(robotId);
			addRobotTextMessage(robotId, "AUTO-MODE beendet (hoffentlich)");
		} else {
			addRobotTextMessage(robotId, "Roboter ist bereits in MAN-MODE");
		}
		return ;
	}
	
	/**
	 * Überprüft, ob der Roboter im Auto-Modus ist
	 * @param robotId Die ID des Roboters, der geprüft werden soll
	 * @return True, wenn der AUTO-Mode aktive ist, sonst false
	 */
	public boolean isRobotInAutoMode(String robotId) {
		// Frage Wert in der Verwaltung ab
		//true falls AUTO aktiv, sonst false
		if(robotMode.get(robotId) == null) {
			return false;
		}
		return true;
	}
	
	
	public void driveRobotTo(String robotId, String destination) {
		String position = roboGraph.getRobotPosition(robotId);
		List<String> path = roboGraph.getShortesPath(robotId, destination);
		
		
		
		//Der folgende algorithmus ist arbeit fÃ¼r einen eigenen Thread/Worker, sodass die pfade gleichzeitg fÃ¼r mehrere roboter abgearbeitet werden kÃ¶nnen
		while(path.size() > 1) {
			String nodeId = path.get(1);
			if((path.size() == 2) && (nodeId == destination)) {
				gui.addRobotTextMessage(robotId, "bÃ¤Ã¤Ã¤hhh");
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
	}
	
	/**
	 * Gibt den RoboGraph objekt zurÃ¼ck.
	 * @return roboGraph
	 */
	public RoboGraph getRoboGraph() {
		return this.roboGraph;
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
	public void stopServer() { //FIXME stopServer kontrollieren
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
     * Fï¿½gt eine Meldung in das Textfeld eines Roboters in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param robotId Die ID des Roboters
     * @param message Die Meldung die hinzugefï¿½gt werden soll
     */
    public void addRobotTextMessage(String robotId, String message) {
    	if(gui != null) {
    		gui.addRobotTextMessage(robotId, message);
    	} else {
    		System.out.println("Keine GUI registriert. Meldung: " + robotId + ": " + message);
    	}
    }
    
    
    /**
     * Fï¿½r eine Meldung in das Textfeld des Servers in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param message Die Nachricht die hinzugefï¿½gt werden soll
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
        	server.startServer();
        } else {
       		try {
       			int rows = Integer.parseInt(args[0]);
       			int columns = Integer.parseInt(args[1]);
       			int port = Integer.parseInt(args[2]);
       			int workerMax = Integer.parseInt(args[3]);
       			Server server = new Server(rows, columns, port, workerMax);
       			server.startServer();
       		} catch (ArrayIndexOutOfBoundsException e) {
       			System.out.println("Keinen Server gestartet. Zu wenige Parameter ï¿½bergeben!");
       		} catch (NumberFormatException e) {
       			System.out.println("Keinen Server gestartet. Parameter in ungï¿½ltigem Format eingegeben!");
       		} finally {
       			System.out.println("Zu ï¿½bergebende Parameterliste: rowCount columnCount portNumber maxWorkers");
       		} 	
        }
    }
	
}

package server.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.gui.Gui;
import server.server.exceptions.MaximumWorkersReachedException;
import server.server.exceptions.TargetIsOccupiedException;
import server.server.graph.RoboGraph;


public class Server implements ServerInterface{
	
	//###################################################
	//# 	Klassenattribute							#
	//###################################################
	
	private Gui gui;
		
	private Listener listener;
	private Worker[] worker;
	private int anzahl;
	private int maxWorker;
	
	private RoboGraph roboGraph;
	private Map<String, Boolean> robotMode = new HashMap<String, Boolean>();
	
	private int port;
	
	private final static int STD_MAXWORKER = 4;
	private final static int STD_LISTENER_PORT = 55555;
	private final static int STD_GRAPH_ROWS = 3;
	private final static int STD_GRAPH_COMLUMNS = 3;

	
	//###################################################
	//# 	Konstruktoren								#
	//###################################################
	
	/**
	 * Standard-Konstruktor erzeugt einen Standard-Graphen (3x3),
	 * startet einen Listener am Standard Port (55555) an,
	 * legt den Standardwert f�r die maximale Workerzahl fest (4)
	 */
	public Server() {
		this(STD_GRAPH_ROWS, STD_GRAPH_COMLUMNS, STD_LISTENER_PORT, STD_MAXWORKER);
	}
	
	/**
	 * Konstruktor der einen Server mit den angegebenen Parametern erstellt
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
	
	
	//###################################################
	//# 	Methoden f�r die GUI / mit GUI-Aufrufen		#
	//###################################################
	
	
	
	/**
	 * Registriert die GUI beim Server, damit der Server GUI-Funktionen aufrufen kann
	 * @param gui
	 */
	public void setGui(Gui gui) {
		this.gui = gui;
	}
	
	/**
	 * Gibt den RoboGraph objekt zurück.
	 * @return roboGraph
	 */
	public RoboGraph getRoboGraph() {
		return this.roboGraph;
	}
	
    
	/**
	 * <b>Startet den Server</b>
	 * (Startet den Listener und somit auch die Erreichbarkeit im Netzwerk)
	 */
	public void startServer() {
		listener = new Listener(this, port);
	}
	
	/**
	 * <b>Zum Stoppen des Servers.</b>
	 * (Beenden den Listener und alle Worker und sie somit auch nicht mehr im Netzwerk erreichbar)
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	public void stopServer() { 
		listener.stopListener();
		listener = null;

		for (int i = 0; i < maxWorker; i++) {
			if (worker[i] != null) {
				removeWorker(worker[i].getRoboId());
			}
		}
	}
	

    
    /**
     * F�gt eine Meldung in das Textfeld eines Roboters in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param robotId Die ID des Roboters
     * @param message Die Meldung die hinzugef�gt werden soll
     */
    public void addRobotTextMessage(String robotId, String message) {
    	if(gui != null) {
    		gui.addRobotTextMessage(robotId, message);
    	} else {
    		System.out.println("Keine GUI registriert. Meldung: " + robotId + ": " + message);
    	}
    }
    
    
    /**
     * F�r eine Meldung in das Textfeld des Servers in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param message Die Nachricht die hinzugef�gt werden soll
     */
    public void addServerTextMessage(String message) {
    	if(gui != null) {
    		gui.addServerTextMessage(message);
    	} else {
    		System.out.println("Keine GUI registriert. Servermeldung: " + message);
    	}
    }

	
	
	//###################################################
	//# 	Worker-Methoden								#
	//###################################################
	
	
	/**
	 * Registriert einen neuen Worker beim Server und f�gt den Roboter dem Graphen hinzu
	 * @param worker Das Worker Objekt
	 * @param position Die Position, an die der Worker geschrieben werden soll
	 */
	public synchronized void addWorker(String roboName, String roboIp, int roboPort) {
		// Wenn ein Worker zu dieser Roboter ID existiert, beende diesen.
		if(isAddWorkerAllowed()) {
			int existingWorkerPosition = findWorkerToRobotId(roboName) ; 
			if(existingWorkerPosition >= 0) {
				removeWorker(roboName);
			}
			int nextFreePos = getNextFreeWorkerNumber();
			this.worker[nextFreePos] = new Worker(this, "Worker_" + nextFreePos, roboName, roboIp, roboPort, (this.port + nextFreePos + 1));
			anzahl++;
			
			
			//Fahrzeug dem Graphen und der GUI hinzuf�gen
			String position;
			position = roboGraph.addRobot(roboName);
			gui.addRobot(roboName, position);
		} else {
			throw new MaximumWorkersReachedException("Maximale Anzahl an Worker erreicht. Worker wurde nicht gestartet.");
		}
	}
	
	
	
	
	//TODO removeWorker in removeRobot umbenennen 
	/**
	 * Beendet den Worker und den entfernt den Roboter aus dem Graphen anhand der ID des Roboters
	 * @param workerName
	 */
	public void removeWorker(String robotId) {
		// Aus GUI und StreetGraph entfernen
		roboGraph.removeRobot(robotId);
		gui.removeRobot(robotId);
		//FIXME Mathias: kann ich vom Server aus den Roboter aus dem AUTO-Mode nehmen, so dass der zugeh�rige Thread auch beendet wird?
		if(isRobotInAutoMode(robotId)) {
			deactivateAutoDst(robotId);
		}
		
		// Worker anhalten und dereferenzieren
		int position = findWorkerToRobotId(robotId);
		worker[position].closeConnection();
		worker[position] = null;
	}
	

	
	/**
	 * Gibt die n�chste freie Nummer im Worker Array zur�ck.
	 * @return Die n�chste freie Nummer, wenn noch eine frei ist, sonst -1.
	 */
	private int getNextFreeWorkerNumber() {
		for (int i = 0; i < maxWorker; i++) {
			if (worker[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	
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
	
	
	/**
	 * Pr�ft ob die maximale Anzahl an Worker erreicht ist.
	 * @return true falls noch Worker hinzugef�gt werden k�nnen, sonst false
	 */
	public boolean isAddWorkerAllowed() {
		return(anzahl < maxWorker);
	}


	
	//###################################################
	//# 	AUTO-Modus Methoden							#
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
	 * mit zuf�llig generierten Zielen ausf�hrt.
	 * @param robotId Die ID des Roboters
	 */
	public void activateAutoDst(String robotId) {
		robotMode.put(robotId, true);
		while(isRobotInAutoMode(robotId)) {
			String destination = generateRndDestination();
			gui.setRobotDestinationTextField(robotId, destination);
			driveRobotTo(robotId, destination);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				//Keine weitere Behandlung notwendig
			}
			addRobotTextMessage(robotId, "Ziel erreicht");
		} 
	}
	
	/**
	 * Beendet den Thread der dem Roboter automatisch neue Ziele zuweist
	 * @param robotId Die ID des Roboters
	 */
	public void deactivateAutoDst(String robotId) {
		this.robotMode.remove(robotId);
	}
	
	/**
	 * �berpr�ft, ob der Roboter im Auto-Modus ist
	 * @param robotId Die ID des Roboters, der gepr�ft werden soll
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
	
	
	//###################################################
	//# 	Fahr- und Pfadlogik							#
	//###################################################
	
	
	public void driveRobotTo(String robotId, String destination) {
		String position = roboGraph.getRobotPosition(robotId); //TODO Variable Position notwendig??
		List<String> path = roboGraph.getShortesPath(robotId, destination);
		
		
		
		//Der folgende algorithmus ist arbeit für einen eigenen Thread/Worker, sodass die pfade gleichzeitg für mehrere roboter abgearbeitet werden können
		while(path.size() > 1) {
			String nodeId = path.get(1);
			if((path.size() == 2) && (nodeId == destination)) {
				gui.addRobotTextMessage(robotId, "bääähhh");
			}
			
			int rotationsNeeded = roboGraph.getNeededRotation(robotId, nodeId);
			turnRobot(rotationsNeeded, robotId);
			
			moveRobotForward(robotId);
			path = roboGraph.getShortesPath(robotId, destination);
		}
	}
	
	
	//###################################################
	//# 	Fahrmethoden f�r die Fahrzeuge				#
	//###################################################
	
	/**
	 * 
	 * @param rotationsNeeded
	 * @param robotId
	 */
	private void turnRobot(int rotationsNeeded, String robotId) {
		switch(rotationsNeeded) {
			case 1: turnRobotRight(robotId);
					System.out.println("Dreh soll: rechts");
					break;
			case 2: turnRobotRight(robotId);
					turnRobotRight(robotId);
					System.out.println("Dreh soll: rechts");
					System.out.println("Dreh soll: rechts");
					break;
			case 3: turnRobotLeft(robotId);
					System.out.println("Dreh soll: links");
					break;
			case -1:turnRobotLeft(robotId);
					System.out.println("Dreh soll: links");
					break;
			case -2:turnRobotLeft(robotId);
					turnRobotLeft(robotId);
					System.out.println("Dreh soll: links");
					System.out.println("Dreh soll: links");
					break;
			case -3:turnRobotRight(robotId);
					System.out.println("Dreh soll: rechts");
					break;
			default:break;
		}
	}
	
	
	/**
	 * Gibt den Drehbefehl an den Roboter weiter und �ndert anschlie�end die Ausrichtung im GUI und im Stra�en-Graphen.
	 * @param robotId ID des Fahrzeuges, das gedreht werden soll.
	 */
	public void turnRobotLeft(String robotId) {
		
		try {
			worker[findWorkerToRobotId(robotId)].turnLeft();
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei turnVehicleLeft remote Aufruf, " + 
					"Fahrzeugdrehung abgebrochen.");
			e.printStackTrace();
			//return; //Wenn Fehler beim Remote, drehe nicht im Graphen oder in der GUI
		}		
		roboGraph.turnRobotLeft(robotId);
		gui.turnRobotLeft(robotId);
	}
	
	/**
	 * Dreht den Roboter im RoboGraph und gibt den Befehl an den physischen roboter weiter
	 */
	public void turnRobotRight(String robotId) {
		try {
			worker[findWorkerToRobotId(robotId)].turnRight();
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei turnVehicleRight remote Aufruf, " + 
					"Fahrzeugdrehung abgebrochen.");
			e.printStackTrace();
			//return; //Wenn Fehler beim Remote, drehe nicht im Graphen oder in der GUI
		}
		roboGraph.turnRobotRight(robotId);
		gui.turnRobotRight(robotId);
	}
	

	
	/**
	 * 
	 * @param robotId
	 */
	public String moveRobotForward(String robotId) {
		String destination;
		try {		
			destination = roboGraph.moveRobotForward(robotId);
		} catch(TargetIsOccupiedException e) {
			System.out.println("Server: Zielknoten besetzt. N�chster Versuch kommt.");
			// 2 Sekunden warten, bis zum erneuten versuch
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				// Keine Fehlerbehandlung notwendig
			}
			destination = moveRobotForward(robotId);
			return destination; // Rausspringen, bei ausgel�ster Exception, sonst beim Rekursiven aufl�sen zu viele Befehle
		}
		
		try {
			//TODO Roboter keine Feste Geschwindigkeit �bergeben
			worker[findWorkerToRobotId(robotId)].driveNextPoint(20);
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei moveVehicleForward remote Aufruf, " + 
					"Fahrzeugbewegung abgebrochen.");
			e.printStackTrace();
			//return destination; //Wenn Fehler beim Remote, drehe nicht im Graphen oder in der GUI
			//TODO Bewegung auf den Graphen r�ckg�ngig machen, bei RemoteException, um auf den vorherigen Stand zu kommen, wie bei turn-Befehlen?
		}
		
		gui.setRobotPosition(robotId, destination);
		return destination;
	}
		
	

    
	//###################################################
	//# 	Main Methode								#
	//###################################################
	
	
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
       			System.out.println("Keinen Server gestartet. Zu wenige Parameter �bergeben!");
       		} catch (NumberFormatException e) {
       			System.out.println("Keinen Server gestartet. Parameter in ung�ltigem Format eingegeben!");
       		} finally {
       			System.out.println("Zu �bergebende Parameterliste: rowCount columnCount portNumber maxWorkers");
       		} 	
        }
    }
	
}

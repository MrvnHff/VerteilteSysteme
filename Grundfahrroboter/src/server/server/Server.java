package server.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import server.gui.Gui;
import server.gui.GuiInterface;
import server.server.exceptions.MaximumWorkersReachedException;
import server.server.exceptions.TargetIsOccupiedException;
import server.server.graph.StreetGraph;


public class Server implements ServerInterface{
	
	//###################################################
	//# 	Klassenattribute							#
	//###################################################
	
	private GuiInterface gui;
		
	private Listener listener;
	//private Worker[] worker;
	//private int anzahl; //Wird obsolet, da man die Anzahl direkt aus der Map abfragen kann.
	private int maxWorker;
	//<String> = vehicleId, <Worker> = referenz auf den Worker
	private Map<String, Worker> workerMap = new HashMap<String, Worker>();;
	
	private StreetGraph streetGraph;
	
	// <String> = vehicleId, <Boolean> = true, wenn Roboter im AUTO-Mode. Roboter die nicht in AUTO sind, werden aus der Map gel�scht
	private Map<String, Boolean> vehicleMode = new HashMap<String, Boolean>();
	
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
	 * @param maxWorker Maximale Anzahl an Worker (und somit auch an registrierbaren Fahrzeugen)
	 */
	public Server(int graphRows, int graphColumns, int port, int maxWorker) {
		streetGraph = new StreetGraph(graphRows, graphColumns);
		this.maxWorker = maxWorker;
		//worker = new Worker[maxWorker];
		//anzahl = 0;
		//workerMap = new HashMap<String, Worker>();
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
	 * Gibt den StreetGraph zur�ck.
	 * @return streetGraph Der Stra�engraph
	 */
	public StreetGraph getStreetGraph() {
		return this.streetGraph;
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
		/*
		for (int i = 0; i < maxWorker; i++) {
			if (worker[i] != null) {
				removeWorker(worker[i].getVehicleId());
			}
		}*/
		
		for(String vehicleId : workerMap.keySet()) {
			removeWorker(vehicleId);
		}
	}
	

    
    /**
     * F�gt eine Meldung in das Textfeld eines Fahrzeuges in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param vehicleId Die ID des Fahrzeuges
     * @param message Die Meldung die hinzugef�gt werden soll
     */
    public void addVehicleTextMessage(String vehicleId, String message) {
    	if(gui != null) {
    		gui.addVehicleTextMessage(vehicleId, message);
    	} else {
    		System.out.println("Keine GUI registriert. Meldung: " + vehicleId + ": " + message);
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
	 * Registriert einen neuen Worker beim Server und f�gt das Fahrzeug dem Stra�engraphen und der GUI hinzu
	 * @param worker Das Worker Objekt
	 * @param position Die Position, an die der Worker geschrieben werden soll
	 */
	public synchronized void addWorker(String vehicleId, String vehicleIp, int vehiclePort) {
		
		if(isAddWorkerAllowed()) {
			/*int existingWorkerPosition = findWorkerToVehicleId(vehicleId) ; 
			if(existingWorkerPosition >= 0) { // Wenn ein Worker zu dieser FahrzeugID existiert, beende diesen.
				removeWorker(vehicleId);
			}
			int nextFreePos = getNextFreeWorkerNumber();
			this.worker[nextFreePos] = new Worker(this, "Worker_" + nextFreePos, vehicleId, vehicleIp, vehiclePort, (this.port + nextFreePos + 1));
			anzahl++;
			*/
			
			if(workerMap.containsKey(vehicleId)) { //Erst l�schen, falls vorhanden
				removeWorker(vehicleId);
			}
			int nextFreeNumber = getNextFreeWorkerNumber();
			workerMap.put(vehicleId, new Worker(this, "Worker_" + nextFreeNumber, vehicleId, vehicleIp, vehiclePort, (this.port + nextFreeNumber)));
			
			//Fahrzeug dem Graphen und der GUI hinzuf�gen
			String position;
			position = streetGraph.addVehicle(vehicleId);
			gui.addVehicle(vehicleId, position);
		} else {
			throw new MaximumWorkersReachedException("Maximale Anzahl an Worker erreicht. Worker wurde nicht gestartet.");
		}
	}
	
	
	
	
	/**
	 * Beendet den Worker und den entfernt das Fahrzeug aus dem Graphen und der GUI anhand der ID des Fahrzeugs
	 * @param vehicleId
	 */
	public synchronized void removeWorker(String vehicleId) {
		// Aus GUI und StreetGraph entfernen
		streetGraph.removeVehicle(vehicleId);
		//gui.removeVehicle(vehicleId); //FIXME kann nicht aufgerufen werden, da es sich beim aufrufen nicht um einen FX-Thread handelt
		//FIXME Mathias: kann ich vom Server aus den Roboter aus dem AUTO-Mode nehmen, so dass der zugeh�rige Thread auch beendet wird?
		if(isVehicleInAutoMode(vehicleId)) {
			deactivateAutoDst(vehicleId);
		}
		/*
		// Worker anhalten und dereferenzieren
		int position = findWorkerToVehicleId(vehicleId);
		worker[position].closeConnection();
		worker[position] = null;
		*/
		workerMap.get(vehicleId).closeConnection();
		workerMap.remove(vehicleId);
	}
	

	
	/**
	 * Gibt die n�chste freie Nummer im Worker Array zur�ck.
	 * @return Die n�chste freie Nummer, wenn noch eine frei ist, sonst -1.
	 */
	private int getNextFreeWorkerNumber() {
		List<Integer> usedNumbers = new ArrayList<Integer>();
		for(Worker worker : workerMap.values()) {
			usedNumbers.add(worker.getWorkerPort() - this.port);
		}
		
		Collections.sort( usedNumbers );
		
		int number = 1;
		for(int i : usedNumbers) {
			if(number != i) {
				return number;
			}
			number++;
		}
		return number;
		
		/*for (int i = 0; i < maxWorker; i++) {
			if (worker[i] == null) {
				return i;
			}
		}
		return -1;*/
	}
	
	
	/*
	 * Sucht den Worker im Array zur Fahrzeug ID
	 * @param vehicleId Name (ID) des Fahrzeuges
	 * @return Index der Fundstelle, wenn nicht vorhanden -1
	 *
	private int findWorkerToVehicleId(String vehicleId) {
		for (int i = 0; i < maxWorker; i++) {
			try {
				if (worker[i].getVehicleId().equals(vehicleId)) {
					return i;
				}
			} catch(NullPointerException e) {
				//Wird geworfen, falls worker[i] = null ist
				//keine weitere Behandlung notwendig
			}
		}
		return -1;
	}*/
	
	
	/**
	 * Pr�ft ob die maximale Anzahl an Worker erreicht ist.
	 * @return true falls noch Worker hinzugef�gt werden k�nnen, sonst false
	 */
	public boolean isAddWorkerAllowed() {
		//return(anzahl < maxWorker);
		return(workerMap.size() < maxWorker);
	}


	
	//###################################################
	//# 	AUTO-Modus Methoden							#
	//###################################################
	
	public String generateRndDestination() {
		int x;
		int y;
		x = (int) (Math.random()* streetGraph.getColumnCount());
		y = (int) (Math.random()* streetGraph.getRowCount());
		
		return (x + "/" + y);
	}
	
	/**
	 * Startet einen Thread, der dem angegebenen Fahrzeug in einer Endlosschleife driveVehicleTo() 
	 * mit zuf�llig generierten Zielen ausf�hrt.
	 * @param vehicleId Die ID des Fahrzeugs
	 */
	public void activateAutoDst(String vehicleId) {
		vehicleMode.put(vehicleId, true);
		while(isVehicleInAutoMode(vehicleId)) {
			String destination = generateRndDestination();
			gui.setVehicleDestinationTextField(vehicleId, destination);
			driveVehicletTo(vehicleId, destination);
			addVehicleTextMessage(vehicleId, "Ziel erreicht");
			// Warte 2 Sekunden, wenn am Ziel angekommen, dann wieder von vorne
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				//Keine weitere Behandlung notwendig
			}
		} 
	}
	
	/**
	 * Beendet den Thread der dem Fahrzeug automatisch neue Ziele zuweist
	 * @param vehicleId Die ID des Fahrzeugs
	 */
	public void deactivateAutoDst(String vehicleId) {
		this.vehicleMode.remove(vehicleId);
	}
	
	/**
	 * �berpr�ft, ob das Fahrzeug im Auto-Modus ist
	 * @param vehicleId Die ID des Fahrzeuges, der gepr�ft werden soll
	 * @return True, wenn der AUTO-Mode aktiv ist, sonst false
	 */
	public boolean isVehicleInAutoMode(String vehicleId) {
		// Frage Wert in der Verwaltung ab
		//true falls AUTO aktiv, sonst false
		if(vehicleMode.get(vehicleId) == null) {
			return false;
		}
		return true;
	}
	
	
	//###################################################
	//# 	Fahr- und Pfadlogik							#
	//###################################################
	
	
	public void driveVehicletTo(String vehicleId, String destination) {
		String position = streetGraph.getVehiclePosition(vehicleId); //TODO Mathias: Variable Position notwendig??
		List<String> path = streetGraph.getShortesPath(vehicleId, destination);
		
		
		
		//Der folgende algorithmus ist arbeit für einen eigenen Thread/Worker, sodass die pfade gleichzeitg für mehrere Fahrzeuge abgearbeitet werden können
		while(path.size() > 1) {
			String nodeId = path.get(1);
			if((path.size() == 2) && (nodeId == destination)) {
				addVehicleTextMessage(vehicleId, "bääähhh");
			}
			
			int rotationsNeeded = streetGraph.getNeededRotation(vehicleId, nodeId);
			turnVehicle(rotationsNeeded, vehicleId);
			
			moveVehicleForward(vehicleId);
			path = streetGraph.getShortesPath(vehicleId, destination);
		}
	}
	
	
	//###################################################
	//# 	Fahrmethoden f�r die Fahrzeuge				#
	//###################################################
	
	/**
	 * 
	 * @param rotationsNeeded
	 * @param vehicleId
	 */
	private void turnVehicle(int rotationsNeeded, String vehicleId) {
		switch(rotationsNeeded) {
			case 1: turnVehicleRight(vehicleId);
					break;
			case 2: turnVehicleRight(vehicleId);
					turnVehicleRight(vehicleId);
					break;
			case 3: turnVehicleLeft(vehicleId);
					break;
			case -1:turnVehicleLeft(vehicleId);
					break;
			case -2:turnVehicleLeft(vehicleId);
					turnVehicleLeft(vehicleId);
					break;
			case -3:turnVehicleRight(vehicleId);
					break;
			default:break;
		}
	}
	
	
	/**
	 * Gibt den Drehbefehl an das Fahrzeug weiter und �ndert anschlie�end die Ausrichtung im GUI und im Stra�en-Graphen.
	 * @param vehicleId ID des Fahrzeuges, das gedreht werden soll.
	 */
	public void turnVehicleLeft(String vehicleId) {
		
		try {
			//worker[findWorkerToVehicleId(vehicleId)].turnLeft();
			workerMap.get(vehicleId).turnLeft();
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei turnVehicleLeft remote Aufruf, " + 
					"Fahrzeugdrehung abgebrochen.");
			e.printStackTrace();
			//return; //Wenn Fehler beim Remote, drehe nicht im Graphen oder in der GUI
		}		
		streetGraph.turnVehicleLeft(vehicleId);
		gui.turnVehicleLeft(vehicleId);
	}
	
	/**
	 * Dreht das Fahrzeug im StreetGraph, in der GUI und gibt den Befehl an das Remote-Fahrzeug weiter
	 */
	public void turnVehicleRight(String vehicleId) {
		try {
			//worker[findWorkerToVehicleId(vehicleId)].turnRight();
			workerMap.get(vehicleId).turnRight();
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei turnVehicleRight remote Aufruf, " + 
					"Fahrzeugdrehung abgebrochen.");
			e.printStackTrace();
			//return; //Wenn Fehler beim Remote, drehe nicht im Graphen oder in der GUI
		}
		streetGraph.turnVehicleRight(vehicleId);
		gui.turnVehicleRight(vehicleId);
	}
	

	
	/**
	 * 
	 * @param vehicleId
	 */
	public String moveVehicleForward(String vehicleId) {
		String destination;
		try {		
			destination = streetGraph.moveVehicleForward(vehicleId);
		} catch(TargetIsOccupiedException e) {
			System.out.println("Server: Zielknoten besetzt. N�chster Versuch kommt.");
			// 4 Sekunden warten, bis zum erneuten versuch
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				// Keine Fehlerbehandlung notwendig
			}
			destination = moveVehicleForward(vehicleId);
			return destination; // Rausspringen, bei ausgel�ster Exception, sonst beim Rekursiven aufl�sen zu viele Befehle
		}
		
		try {
			//TODO Fahrzeug keine Feste Geschwindigkeit �bergeben
			//worker[findWorkerToVehicleId(vehicleId)].driveNextPoint(20);
			workerMap.get(vehicleId).driveNextPoint(20);
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei moveVehicleForward remote Aufruf, " + 
					"Fahrzeugbewegung abgebrochen.");
			e.printStackTrace();
			//return destination; //Wenn Fehler beim Remote, drehe nicht im Graphen oder in der GUI
			//TODO Bewegung auf den Graphen r�ckg�ngig machen, bei RemoteException, um auf den vorherigen Stand zu kommen, wie bei turn-Befehlen?
		}
		
		gui.setVehiclePosition(vehicleId, destination);
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

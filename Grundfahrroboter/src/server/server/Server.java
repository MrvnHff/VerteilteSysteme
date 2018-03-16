package server.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import server.gui.Gui;
import server.gui.GuiInterface;
import server.server.exceptions.MaxMoveRetriesReachedException;
import server.server.exceptions.MaximumWorkersReachedException;
import server.server.exceptions.NoPathToDestinationException;
import server.server.exceptions.TargetIsOccupiedException;
import server.server.graph.StreetGraph;


public class Server implements ServerInterface{
	
	//###################################################
	//# 	Klassenattribute							#
	//###################################################
	private static final String CONFIG_FILENAME = "resources/config/serverStart";
	
	private GuiInterface gui;
		
	private Listener listener;
	private int maxWorker;
	//<String> = vehicleId, <Worker> = Referenz auf den Worker
	private Map<String, Worker> workerMap = new HashMap<String, Worker>();;
	
	private StreetGraph streetGraph;
	
	// <String> = vehicleId, <Boolean> = true, wenn Roboter im AUTO-Mode. Roboter die nicht in AUTO sind, werden aus der Map gelöscht
	private Map<String, Boolean> vehicleMode = new HashMap<String, Boolean>();
	
	private int port;
	
	private static ResourceBundle configuration = ResourceBundle.getBundle(CONFIG_FILENAME);
	
	//Geschwindigkeit der Fahrzeuge beim Vorwärtsfahren in %
	private final static int VEHICLE_SPEED = Integer.parseInt(configuration.getString("VehicleSpeed"));
	
	//Anzahl maximaler Versuche vorwärts auf ein belegtes Feld zu fahren
	private final static int MAX_RETRIES = Integer.parseInt(configuration.getString("MaxMoveRetries"));
	
	//###################################################
	//# 	Konstruktoren								#
	//###################################################
	
	/**
	 * Standard-Konstruktor erzeugt einen Standard-Graphen (3x3),
	 * startet einen Listener am Standard Port (55555) an,
	 * legt den Standardwert fï¿½r die maximale Workerzahl fest (4)
	 */
	public Server() {
		this(Integer.parseInt(configuration.getString("GraphRows")), 
				Integer.parseInt(configuration.getString("GraphColumns")),
				Integer.parseInt(configuration.getString("Port")), 
				Integer.parseInt(configuration.getString("MaxVehicles")));
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
		this.port= port;
	}
	
	
	//###################################################
	//# 	Methoden für die GUI / mit GUI-Aufrufen		#
	//###################################################
	
	
	
	/**
	 * Registriert die GUI beim Server, damit der Server GUI-Funktionen aufrufen kann
	 * @param gui
	 */
	public void setGui(Gui gui) {
		this.gui = gui;
	}
	
	/**
	 * Gibt den StreetGraph zurück.
	 * @return streetGraph Der Straßengraph
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
		
		for(String vehicleId : workerMap.keySet()) {
			removeWorker(vehicleId);
		}
	}
	

    
    /**
     * Fï¿½gt eine Meldung in das Textfeld eines Fahrzeuges in der GUI hinzu.
     * Ist keine GUI registriert, wird die Ausgabe auf die Kommandozeile umgelenkt.
     * @param vehicleId Die ID des Fahrzeuges
     * @param message Die Meldung die hinzugefï¿½gt werden soll
     */
    public void addVehicleTextMessage(String vehicleId, String message) {
    	if(gui != null) {
    		gui.addVehicleTextMessage(vehicleId, message);
    	} else {
    		System.out.println("Keine GUI registriert. Meldung: " + vehicleId + ": " + message);
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
    
    public boolean nodeExists(String nodeId) {
		return streetGraph.nodeExists(nodeId);
	}

	
	
	//###################################################
	//# 	Worker-Methoden								#
	//###################################################
	
	
	/**
	 * Registriert einen neuen Worker beim Server und fï¿½gt das Fahrzeug dem Straßengraphen und der GUI hinzu
	 * @param worker Das Worker Objekt
	 * @param position Die Position, an die der Worker geschrieben werden soll
	 */
	public synchronized void addWorker(String vehicleId, String vehicleIp, int vehiclePort) {
		
		if(isAddWorkerAllowed()) {
			if(workerMap.containsKey(vehicleId)) { //Erst löschen, falls vorhanden
				removeWorker(vehicleId);
			}
			int nextFreeNumber = getNextFreeWorkerNumber();
			workerMap.put(vehicleId, new Worker(this, "Worker_" + nextFreeNumber, vehicleId, vehicleIp, vehiclePort, (this.port + nextFreeNumber)));
			
			//Fahrzeug dem Graphen und der GUI hinzufügen
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
		// FIXME Worker wird nicht entfern, wenn der RemoteHost nicht mehr erreichbar ist und eine Exception ausgelöst wird.
		streetGraph.removeVehicle(vehicleId);
		gui.removeVehicle(vehicleId);

		if(isVehicleInAutoMode(vehicleId)) {
			deactivateAutoDst(vehicleId);
		}

		workerMap.get(vehicleId).closeConnection();
		workerMap.remove(vehicleId);
	}
	

	
	/**
	 * Gibt die nächste freie Nummer im Worker Array zurück.
	 * @return Die nächste freie Nummer, wenn noch eine frei ist, sonst -1.
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
	}
	

	
	/**
	 * Prüft ob die maximale Anzahl an Worker erreicht ist.
	 * @return true falls noch Worker hinzugefügt werden können, sonst false
	 */
	public boolean isAddWorkerAllowed() {
		//return(anzahl < maxWorker);
		return(workerMap.size() < maxWorker);
	}


	
	//###################################################
	//# 	AUTO-Modus Methoden							#
	//###################################################
	
	public String generateRndDestination() {
		int x, y;
		do {
			x = (int) (Math.random()* streetGraph.getColumnCount());
			y = (int) (Math.random()* streetGraph.getRowCount());
		} while(!streetGraph.isNodeEmpty(x + "/" + y));
		
		return (x + "/" + y);
	}
	
	/**
	 * Startet einen Thread, der dem angegebenen Fahrzeug in einer Endlosschleife driveVehicleTo() 
	 * mit zufï¿½llig generierten Zielen ausfï¿½hrt.
	 * @param vehicleId Die ID des Fahrzeugs
	 */
	public void activateAutoDst(String vehicleId) {
		vehicleMode.put(vehicleId, true);
		while(isVehicleInAutoMode(vehicleId)) {
			String destination;
			do {
				destination = generateRndDestination();
			}while(!streetGraph.isNodeEmpty(destination)); //Neues Ziel, falls Knoten besetzt, soll Deadlocks in AUTO verringern
			
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
	 * ï¿½berprï¿½ft, ob das Fahrzeug im Auto-Modus ist
	 * @param vehicleId Die ID des Fahrzeuges, der geprï¿½ft werden soll
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
		boolean pathExists = false;
		List<String> path = null;
		while(!pathExists){
			try {
				path = streetGraph.getShortesPath(vehicleId, destination);
				pathExists = true;
			} catch (NoPathToDestinationException nptde) {
				pathExists = false;
				System.err.println(nptde.getMessage() + "Neuer Pfad wird gleich ermittelt.");
				try {
					Thread.sleep(2000);					// Wartezeit, bis ein neuer Pfad ermittelt wird.
				} catch (InterruptedException ie) { } 	// Kein Handling notwendig
			}
		}
		while(path.size() > 1) {
			String nodeId = path.get(1);
			if((path.size() == 2) && (nodeId == destination)) {
				addVehicleTextMessage(vehicleId, "bÃ¤Ã¤Ã¤hhh");
			}
			
			int rotationsNeeded = streetGraph.getNeededRotation(vehicleId, nodeId);
			turnVehicle(rotationsNeeded, vehicleId);
			try {
				moveVehicleForward(vehicleId);
				path = streetGraph.getShortesPath(vehicleId, destination);
			} catch(MaxMoveRetriesReachedException mmrre) {
				System.err.println("Server: Knoten für Fahrzeug " + vehicleId + " zu lange besetzt. Neue Route berechnen");
				addVehicleTextMessage(vehicleId, "Neue Route berechnen...");
				path.clear(); //Bestehende Liste leeren, neue wird beim Wiederaufruf erstellt.
				driveVehicletTo(vehicleId, destination);
			}
		
		}
	}
	
	
	//###################################################
	//# 	Fahrmethoden für die Fahrzeuge				#
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
			case -2:turnVehicleRight(vehicleId);
					turnVehicleRight(vehicleId);
					break;
			case -3:turnVehicleRight(vehicleId);
					break;
			default:break;
		}
	}
	
	
	/**
	 * Gibt den Drehbefehl an das Fahrzeug weiter und ändert anschließend die Ausrichtung im GUI und im Straßen-Graphen.
	 * @param vehicleId ID des Fahrzeuges, das gedreht werden soll.
	 */
	public void turnVehicleLeft(String vehicleId) {
		try {
			workerMap.get(vehicleId).turnLeft();
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei turnVehicleLeft remote Aufruf, " + 
					"Fahrzeugdrehung abgebrochen.");
			e.printStackTrace();
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
		}
		streetGraph.turnVehicleRight(vehicleId);
		gui.turnVehicleRight(vehicleId);
	}
	
	/**
	 * Bewegt das Fahrezeug auf der Datenstruktur und der Gui nach vorne
	 * @param vehicleId
	 */
	public String moveVehicleForward(String vehicleId) {
		String destination;
		try {		
			destination = streetGraph.moveVehicleForward(vehicleId);
		} catch(TargetIsOccupiedException e) {
			destination = moveVehicleForwardRetry(vehicleId, 1);
		}
		
		try {
			workerMap.get(vehicleId).driveNextPoint(VEHICLE_SPEED);
		} catch (RemoteException e) {
			System.err.println("Server: Fehler bei moveVehicleForward remote Aufruf, " + 
					"Fahrzeugbewegung abgebrochen.");
			e.printStackTrace();
		}
		
		gui.setVehiclePosition(vehicleId, destination);
		return destination;
	}
	
	private String moveVehicleForwardRetry(String vehicleId, int retry) {		
		String destination;

		if(retry <= MAX_RETRIES) {
			System.out.println("Server: Zielknoten für Fahrzeug " + vehicleId + " besetzt. Versuch " + retry);
			// 4 Sekunden warten, bis zum erneuten versuch
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) { } // Keine Fehlerbehandlung notwendig
			
			try {		
				destination = streetGraph.moveVehicleForward(vehicleId);
			} catch(TargetIsOccupiedException e) {				
				destination = moveVehicleForwardRetry(vehicleId, ++retry);
			}
			return destination;
		} else {
			System.err.println("Server: Für Roboter " + vehicleId + " Vorwärtsfahrt abgebrochen. Maximale Versuche erreicht.");
			addVehicleTextMessage(vehicleId, "Vorwärtsfahrt abgebrochen.");
			throw new MaxMoveRetriesReachedException("Roboter " + vehicleId + "hat maximale Anzahl an Fahrversuchen vorwärts erreicht");
		}
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
       			System.out.println("Keinen Server gestartet. Zu wenige Parameter ï¿½bergeben!");
       		} catch (NumberFormatException e) {
       			System.out.println("Keinen Server gestartet. Parameter in ungï¿½ltigem Format eingegeben!");
       		} finally {
       			System.out.println("Zu ï¿½bergebende Parameterliste: rowCount columnCount portNumber maxWorkers");
       		} 	
        }
    }

	
	
}

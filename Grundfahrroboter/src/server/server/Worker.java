package server.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import client.client.RemoteVehicleInterface;

/**
 * Der Worker, ueber den die Kommunikation zwischen Server und verbundenem Roboter laeuft. Beim Aufruf sind die
 * Parameter zu beachten, mit denen der Worker laufen soll. Nach dem Starten wird automatisch versucht eine 
 * Verbindung mit dem angebenen Fahrzeug aufzubauen.
 * 
 */
public class Worker extends Thread implements WorkerInterface{
	private WorkerInterface stub;
	private RemoteVehicleInterface vehicle;
	private Registry registryW;
	private Registry registryV;
	private Registry returnOfCreateRegistry;
	private Server server;
	
	private String workerIp;
	private int workerPort;
	private String workerId;
	private String vehicleIp;
	private int vehiclePort;
	private String vehicleId;
	
	/**
	 * Legt einen Worker mit den übergebenen Parametern an. Der Thread startet sich dann automatisch selbst.
	 * @param server Referenz auf den dahinterstehenden Server.
	 * @param workerId Name des Workers
	 * @param vehicleId ID des Fahrzeugs
	 * @param vehicleIp IP des Fahrzeugs
	 * @param vehiclePort Port des Fahrzeugs
	 * @param workerPort Port des Workers, an dem der Worker laufen soll
	 */
	public Worker(Server server, String workerId, String vehicleId, String vehicleIp, int vehiclePort, int workerPort) {
		this.workerId = workerId;
		this.workerPort = workerPort;
		this.vehicleIp = vehicleIp;
		this.vehicleId = vehicleId;
		this.vehiclePort = vehiclePort;
		this.server = server;
		try {
			this.workerIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.print("Worker: Fehler beim auslesen der Host-IP: " + e);
		}
		
		
		start();
	}
	
	/**
	 * Beim aufrufen, wird der Worker an der RMI-Schnittstelle angelegt und sucht 
	 * anschließend nach dem Fahrzeug im Netzwerk. Wurde dieses gefunden, wird das
	 * Fahrzeug auf diesen Worker Konfiguriert und ein Datenaustausch ist in beide
	 * Richtungen moeglich.
	 */
	public void run() {
		try {
			registerWorker();		
			registerVehicleInWorker();	
			registerWorkerInVehicle();
		} catch (Exception e) {			
			System.err.println("Worker: Fehler in der run() Methode aufgefangen.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt die ID des Fahrzeugs zurueck, fuer die der Worker laeuft.
	 * @return Die ID des Fahrzeugs
	 */
	public String getVehicleId() {
		return vehicleId;
	}
	
	
	/**
	 * Gibt die IP-Adresse zurueck, fuer die der Worker laeuft.
	 * @return die IP des Fahrzeugs 
	 */
	public String getVehicleIp() {
		return this.vehicleIp;
	}
	
	
	/**
	 * Gibt den Port des Fahrzeuges zurueck, ueber den dieses erreichbar ist.
	 * @return Der Port des Fahrzeugs
	 */
	public int getVehiclePort() {
		return this.vehiclePort;
	}
	
	
	/**
	 * Gibt die ID des Workers zurueck.
	 * @return Die ID des Workers
	 */
	public String getWorkerId() {
		return workerId;
	}
	
	
	/**
	 * Gibt den Port des Worker zurueck, unter dem dieser im Netzwerk erreichbar ist.
	 * @return Der Port des Workers.
	 */
	public int getWorkerPort() {
		return this.workerPort;
	}
	
	
	/**
	 * Gibt die IP-Adresse des Workers zurueck, unter der dieser im Netzwerk zu finden ist.
	 * @return Die IP-Adresse der Workers.
	 */
	public String getWorkerIp() {
		return this.workerIp;
	}
	

	//Javadoc-Kommentar im Interface
	//remoteReachable
	@Override
	public void printStatus(String s) throws RemoteException {
		System.out.println("Statusmeldung: " + s);	
		server.addVehicleTextMessage(vehicleId, s);
	}

	//Javadoc-Kommentar im Interface
	//remoteReachable
	@Override
	public void printError(String s) throws RemoteException {
		System.out.println("Fehlermeldung: " + s);		
		server.addVehicleTextMessage(vehicleId, s);
	}

	/**
	 * Melde sich am Netzwerk an (Port öffnen)
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	private void registerWorker() throws RemoteException, AlreadyBoundException {
		stub = (WorkerInterface) UnicastRemoteObject.exportObject(this, 0);
		returnOfCreateRegistry = LocateRegistry.createRegistry(workerPort);
	    registryW = LocateRegistry.getRegistry(workerPort);
	    registryW.bind(workerId, stub);
	    System.out.println("Worker: " + workerId + " bereit!");
	}
	
	
	/**
	 * Sucht nach dem Fahrzeug im Netzwerk und speichert sich dieses ab, um darauf zugreifen zu können.
	 * @throws AccessException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws AlreadyBoundException
	 */
	private void registerVehicleInWorker() throws AccessException, RemoteException, NotBoundException, AlreadyBoundException {        
	    registryV = LocateRegistry.getRegistry(vehicleIp, vehiclePort);
		vehicle = (RemoteVehicleInterface) registryV.lookup(vehicleId);		
		System.out.println("Worker: " + workerId + " verbunden mit Fahrzeug " + vehicleId + "!");		
	}
	
	
	/**
	 * Meldet diesen Worker beim Fahrzeug an.
	 */
	private void registerWorkerInVehicle() {
		// Diesen Worker beim Fahrzeug anmelden
		try {
			vehicle.registerWorkerInVehicle(workerId, workerIp, workerPort);
		} catch (RemoteException e) {
			System.out.println("Worker: Fehler beim Aufruf registerWorkerInVehicle() " + e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Ruft die closeConnection() des Fahrzeugs auf und und beendet den eigenen Worker-Thread.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void closeConnection() {
		
		try {
			vehicle.closeConnection();			// Programm auf Fahrzeug beenden
			this.vehicle = null;				// Dereferenzieren
		} catch (RemoteException e) {
			System.err.println("Worker: " + this.workerId + " Fehler beim Versuch Fahrzeug zu beenden. Fahrzeug: " + this.vehicleId);
			e.printStackTrace();
		}
		
		try {
			registryW.unbind(workerId);	// Das Binding des Workers zum Remote-Objekt-Stub aufheben
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Worker: " + this.workerId + " Fehler beim unbind.");
			e.printStackTrace();
		}
				
		try { 				// Den Port wieder freigeben 
			UnicastRemoteObject.unexportObject(returnOfCreateRegistry, true);
		} catch (NoSuchObjectException e) {
			System.err.println("Worker: " + this.workerId + " Fehler beim unexport.");
			e.printStackTrace();
		}
		server.addServerTextMessage("Worker: " + this.workerId + " beendet. (Fahrzeug war: " +  this.vehicleId + ")");
		
		this.vehicle = null;		//Dereferenzieren, damit der Garbage Collector die Objekte aufsammelt
		this.server = null;
		
		System.out.println("Worker: " + this.workerId + " beendet. (Fahrzeug war: " +  this.vehicleId + ")");
		
	}
	
	
	//Javadoc-Kommentar im Interface
	//remoteReachable
	public void quitFromVehicle() {
		server.removeWorker(this.vehicleId);
	}
	
	/**
	 * Dreht das verbundene Fahrzeug um 90° nach links.
	 * @throws RemoteException
	 */
	public void turnLeft() throws RemoteException {
		vehicle.turnLeft();
	}

	
	/**
	 * Dreht das verbundene Fahrzeug um 90° nach rechts.
	 * @throws RemoteException
	 */
	public void turnRight() throws RemoteException {
		vehicle.turnRight();
		
	}

	/**
	 * Haelt das verbundene Fahrzeug an.
	 * @throws RemoteException
	 */
	public void stopDrive() throws RemoteException {
		vehicle.stopDrive();
	}

	/**
	 * Faehrt das verbundene Fahrzeug zum Knoten, der in geradeaus Richtung davor liegt.
	 * @param speed Geschwindigkeit in %, mit der das Fahrzeug fahren soll.
	 * @throws RemoteException
	 */
	public void driveNextPoint(int speed) throws RemoteException {
		vehicle.driveNextPoint(speed);
	}

	/**
	 * Ruft den aktuellen Status des verbundenen Fahrzeugs ab.
	 * @return Die Statusmeldung des Fahrzeugs
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException {
		return vehicle.getStatus();
	}
	
	/**
	 * Ruft die aktuelle Fehlermeldung beim Fahrzeug ab.
	 * @return Die Fehlermeldung des Fahrzheugs.
	 * @throws RemoteException
	 */
	public String getError() throws RemoteException {
		return vehicle.getError();
	}	
}

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

import client.RemoteVehicleInterface;

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
	
	public void run() {
		try {
			registerWorker();		
			registerVehicleInWorker();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getVehicleId() {
		return vehicleId;
	}
	
	public String getVehicleIp() {
		return this.vehicleIp;
	}
	
	public int getVehiclePort() {
		return this.vehiclePort;
	}
	
	public String getWorkerId() {
		return workerId;
	}
	
	public int getWorkerPort() {
		return this.workerPort;
	}
	
	public String getWorkerIp() {
		return this.workerIp;
	}
	

	// TODO setWay umbenennen in etwas sinnvolles (Worker + Interface + VehicleServer) + Javadoc Kommentar anpassen
	//Javadoc-Kommentar im Interface
	//remoteReachable
	@Override
	public void setWay(String point1, String point2) throws RemoteException {
		System.out.println("Statusabruf: " + vehicle.getStatus());
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
	    /*stub = (WorkerInterface) UnicastRemoteObject.exportObject(this, 0); 
        try { 
            registryW = LocateRegistry.createRegistry(workerPort); 
        } catch (ExportException e) {} 
        registryW = LocateRegistry.getRegistry(workerPort); 
        registryW.bind(workerId, stub);*/ 
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
		// Diesen Worker beim Fahrzeug anmelden
		vehicle.registerWorkerInVehicle(workerId, workerIp, workerPort);
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

		this.vehicle = null;		//Dereferenzieren, damit der Garbage Collector die Objekte aufsammelt
		this.server = null;
		
		System.out.println("Worker: " + this.workerId + " beendet. (Fahrzeug war: " +  this.vehicleId + ")");
	}


	public void turnLeft() throws RemoteException {
		vehicle.turnLeft();
	}

	public void turnRight() throws RemoteException {
		vehicle.turnRight();
		
	}

	public void stopDrive() throws RemoteException {
		vehicle.stopDrive();
	}

	public void driveNextPoint(int speed) throws RemoteException {
		vehicle.driveNextPoint(speed);
	}

	public String getStatus() throws RemoteException {
		return vehicle.getStatus();
	}

	public String getError() throws RemoteException {
		return vehicle.getError();
	}	
}

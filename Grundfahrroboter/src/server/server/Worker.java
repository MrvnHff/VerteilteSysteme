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

import client.RoboServerInterface;

public class Worker extends Thread implements WorkerInterface{
	private WorkerInterface stub;
	private RoboServerInterface robo;
	private Registry registryW;
	private Registry registryR;
	private Registry returnOfCreateRegistry;
	private Server server;
	
	private String workerIp;
	private int workerPort;
	private String roboIp;
	private int roboPort;
	private String workerName;
	private String roboName;
	
	/**
	 * Legt einen Worker mit den übergebenen Parametern an. Der Thread startet sich dann automatisch selbst.
	 * @param server Referenz auf den dahinterstehenden Server.
	 * @param workerName Name des Workers
	 * @param roboName ID des Roboters
	 * @param roboIp IP des Roboters
	 * @param roboPort Port des Roboters
	 * @param workerPort Port des Workers, an dem der Worker laufen soll
	 */
	public Worker(Server server, String workerName, String roboName, String roboIp, int roboPort, int workerPort) {
		this.workerName = workerName;
		this.workerPort = workerPort;
		this.roboIp = roboIp;
		this.roboName = roboName;
		this.roboPort = roboPort;
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
			registerRobot();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sayHello() throws RemoteException {
		robo.registerWorker(workerName, workerIp, workerPort);		
	}
	
	public String getRoboName() {return roboName;}
	public String getRoboId() {return this.roboIp;}
	public int getRoboPort() {return this.roboPort;}
	public String getWorkerName() {return workerName;}
	public int getWorkerPort() {return this.workerPort;}
	public String getWorkerIp() {return this.workerIp;}
	

	// TODO setWay umbenennen in etwas sinnvolles (Worker + Interface + RoboServer) + Javadoc Kommentar anpassen
	//Javadoc-Kommentar im Interface
	//remoteReachable
	@Override
	public void setWay(String point1, String point2) throws RemoteException {
		System.out.println("Statusabruf: " + robo.getStatus());
	}

	//Javadoc-Kommentar im Interface
	//remoteReachable
	@Override
	public void printStatus(String s) throws RemoteException {
		System.out.println("Statusmeldung: " + s);	
		server.addRobotTextMessage(roboName, s);
	}

	//Javadoc-Kommentar im Interface
	//remoteReachable
	@Override
	public void printError(String s) throws RemoteException {
		System.out.println("Fehlermeldung: " + s);		
		server.addRobotTextMessage(roboName, s);
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
	    registryW.bind(workerName, stub);
	    /*stub = (WorkerInterface) UnicastRemoteObject.exportObject(this, 0); 
        try { 
            registryW = LocateRegistry.createRegistry(workerPort); 
        } catch (ExportException e) {} 
        registryW = LocateRegistry.getRegistry(workerPort); 
        registryW.bind(workerName, stub);*/ 
	    System.out.println("Worker: " + workerName + " bereit!");
	}
	
	
	/**
	 * Sucht nach dem Roboter im Netzwerk und speichert sich diesen ab, um darauf zugreifen zu können.
	 * @throws AccessException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws AlreadyBoundException
	 */
	private void registerRobot() throws AccessException, RemoteException, NotBoundException, AlreadyBoundException {        
	    registryR = LocateRegistry.getRegistry(roboIp, roboPort);
		robo = (RoboServerInterface) registryR.lookup(roboName);		
		System.out.println("Worker: " + workerName + " verbunden mit Roboter " + roboName + "!");		
		sayHello();
	}
	
	
	/**
	 * Ruft die closeConnection() des Roboters auf und und beendet den eigenen Worker-Thread.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void closeConnection() {
		
		try {
			robo.closeConnection();			// Programm auf Roboter beenden
			this.robo = null;				// Dereferenzieren
		} catch (RemoteException e) {
			System.err.println("Worker: " + this.workerName + " Fehler beim Versuch Roboter zu beenden. Roboter: " + this.roboName);
			e.printStackTrace();
		}
		
		try {
			registryW.unbind(workerName);	// Das Binding des Workers zum Remote-Objekt-Stub aufheben
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Worker: " + this.workerName + " Fehler beim unbind.");
			e.printStackTrace();
		}
				
		try { 				// Den Port wieder freigeben 
			UnicastRemoteObject.unexportObject(returnOfCreateRegistry, true);
		} catch (NoSuchObjectException e) {
			System.err.println("Worker: " + this.workerName + " Fehler beim unexport.");
			e.printStackTrace();
		}

		this.robo = null;		//Dereferenzieren, damit der Garbage Collector die Objekte aufsammelt
		this.server = null;
		
		System.out.println("Worker: " + this.workerName + " beendet. (Roboter war: " +  this.roboName + ")");
	}

	/*//FIXME Methoden entfallen 
	public void drive(int speed) throws RemoteException {
		robo.drive(speed);
	}

	public void driveBack(int cm, int speed) throws RemoteException {
		robo.driveBack(cm, speed);
	} */

	public void turnLeft() throws RemoteException {
		robo.turnLeft();
	}

	public void turnRight() throws RemoteException {
		robo.turnRight();
		
	}

	public void stopDrive() throws RemoteException {
		robo.stopDrive();
	}

	public void driveNextPoint(int speed) throws RemoteException {
		robo.driveNextPoint(speed);
	}

	public String getStatus() throws RemoteException {
		return robo.getStatus();
	}

	public String getError() throws RemoteException {
		return robo.getError();
	}	
}

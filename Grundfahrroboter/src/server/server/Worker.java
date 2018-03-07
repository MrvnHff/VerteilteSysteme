package server.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
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
	
	//FIXME Listener aus klasse entfernen
	public Worker(Server server, String workerName, String roboName, String roboIp, int roboPor, int workerPortt) {
		this.workerName = roboName;
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
		//robo.registerWorker(workerName, workerIp, workerPort);		
	}
	
	public String getRoboName() {return roboName;}
	public String getWorkerName() {return workerName;}

	@Override
	public void setWay(String point1, String point2) throws RemoteException {
		//robo.driveCm(10, 20);
		System.out.println("Statusabruf: " + robo.getStatus());
	}

	@Override
	public void printStatus(String s) throws RemoteException {
		System.out.println("Statusmeldung: " + s);	
		server.addRobotTextMessage(roboName, s);
	}

	@Override
	public void printError(String s) throws RemoteException {
		System.out.println("Fehlermeldung: " + s);		
		server.addRobotTextMessage(roboName, s);
	}

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
	
	private void registerRobot() throws AccessException, RemoteException, NotBoundException, AlreadyBoundException {

        
	    registryR = LocateRegistry.getRegistry(roboIp, roboPort);
		robo = (RoboServerInterface) registryR.lookup(roboName);		
		System.out.println("Worker: " + workerName + " verbunden mit Roboter " + roboName + "!");		
		sayHello();
	}
	
	public void closeConnection() throws RemoteException, NotBoundException {
		//TODO Wenn Roboter nicht erreichbar wird Prozess nicht beendet
		robo.closeConnection();
		UnicastRemoteObject.unexportObject(returnOfCreateRegistry, true);
		registryW.unbind(workerName);
		this.interrupt();
	}

	public void drive(int speed) throws RemoteException {
		robo.drive(speed);
	}

	public void driveBack(int cm, int speed) throws RemoteException {
		robo.driveBack(cm, speed);
	}

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

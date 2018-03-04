package server.server;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import client.RoboServerInterface;

public class Worker implements WorkerInterface{
	private WorkerInterface stub;
	private RoboServerInterface robo;
	private Registry registryW;
	private Registry registryR;
	private Listener listener;
	
	private String workerIp;
	private int workerPort;
	private String roboIp;
	private int roboPort;
	private String workerName;
	private String roboName;
	
	public Worker(Listener listener, String workerName, String roboName, String workerIp, String roboIp, int workerPort, int roboPort) {
		this.listener = listener;
		this.workerIp = workerIp;
		this.workerName = workerName;
		this.workerPort = workerPort;
		this.roboIp = roboIp;
		this.roboName = roboName;
		this.roboPort = roboPort;
		
		try {
			registerWorker();
			registerRobot();
			closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sayHello() throws RemoteException {
		robo.registerWorker(workerName, workerIp, workerPort);		
	}

	@Override
	public void setWay(String point1, String point2) throws RemoteException {
		robo.driveCm(10, 20);
		System.out.println("Statusabruf: " + robo.getStatus());
	}

	@Override
	public void printStatus(String s) throws RemoteException {
		System.out.println("Statusmeldung: " + s);		
	}

	@Override
	public void printError(String s) throws RemoteException {
		System.out.println("Fehlermeldung: " + s);		
	}

	private void registerWorker() throws AccessException, RemoteException, AlreadyBoundException {
		stub = (WorkerInterface) UnicastRemoteObject.exportObject(this, 0);
		try {
			registryW = LocateRegistry.createRegistry(workerPort);
		} catch (ExportException e) {}
	    registryW = LocateRegistry.getRegistry(workerPort);
	    registryW.bind(workerName, stub);
	    
	    System.out.println(workerName + " bereit!");
	}
	
	private void registerRobot() throws AccessException, RemoteException, NotBoundException {	    
	    registryR = LocateRegistry.getRegistry(roboIp, roboPort);
		robo = (RoboServerInterface) registryR.lookup(roboName);		
		System.out.println(workerName + " verbunden mit Roboter " + roboName + "!");		
		sayHello();
	}
	
	private void closeConnection() throws RemoteException, NotBoundException {
		robo.closeConnection();
		registryW.unbind(workerName);
		listener.closeWorker(workerName);
	}
	
}

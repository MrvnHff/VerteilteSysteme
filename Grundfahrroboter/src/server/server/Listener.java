package server.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Listener extends Thread implements ListenerInterface{
	//private Worker worker;
	private String ip;
	private int port;
	private Server server;
	private Registry registry;
	private Registry returnOfCreateRegistry;
	
	/**
	 * Startmethode für den Listener
	 * @param server Referenz auf den Server, für den der Listener gestartet wurde
	 * @param port Port, an dem der Listener horchen soll
	 */
	public Listener(Server server, int port) {
		this.server = server;
		InetAddress ipAddr;
		try {
			ipAddr = InetAddress.getLocalHost();
			ip = ipAddr.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
		start();
	}
	
	public void run(){
		registerListener();
	}


	@Override
	public void registerRobot(String roboName, String roboIp, int roboPort) throws RemoteException {
		server.addServerTextMessage("Roboter " + roboName + " hat sich unter der IP " + roboIp + " gemeldet!");
		System.out.println("Listener: Roboter " + roboName + " hat sich unter der IP " + roboIp + " gemeldet!");
		
		try {
			server.addWorker(roboName, roboIp, roboPort);
		} catch (Exception e) {
			server.addServerTextMessage("Fehler bei addWorker()" + e);
			System.out.println("Listener: Fehler bei addWorker()" + e );
		}
		
		/*
		//FIXME ersetzen
		if (server.isAllowedToAddWorker()) {
			int index = server.getNextFreeWorkerNumber();
//			worker = new Worker(this.server, this, "Worker" + (index + 1), roboName, ip, roboIp, (port + index + 1), roboPort);
			if (server != null) {
//				server.addWorker(worker, index);
			}
		}else{
			server.addServerTextMessage("Maximale Anzahl an Workern ist erreicht! Es kann kein Roboter mehr dazukommen!");
			System.out.println("Maximale Anzahl an Workern ist erreicht! Es kann kein Roboter mehr dazukommen!");
		}*/
	}
	
	/**
	 * 
	 */
	private void registerListener() {
		try {
			//Starte lokalen Listener Server am angegebenen Port
			ListenerInterface stub = (ListenerInterface) UnicastRemoteObject.exportObject(this, 0);
			returnOfCreateRegistry = LocateRegistry.createRegistry(port);
	        registry = LocateRegistry.getRegistry(port);
	        registry.bind("Listener", stub);
			System.out.println("Listener: Listener bereit! IP: " + ip + " Port: " + port);
			server.addServerTextMessage("Listener gestartet. IP: " + ip + " Port: " + port);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	/*public void closeWorker(String workerName) {
		server.removeWorker(workerName);
		System.out.println("Worker " + workerName + " beendet!\n");
	}*/
	
	@Override
	public void stopListener() {
		this.interrupt();
		try {
			UnicastRemoteObject.unexportObject(returnOfCreateRegistry, true); //Listener tatsächlich anhalten
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

//Registry registry = LocateRegistry.getRegistry("192.168.178.26", 55555);
//robo = (RoboServerInterface) registry.lookup("Robo2");
//robo.turnLeft();
//robo.turnLeft();
//robo.turnLeft();
//robo.turnLeft();

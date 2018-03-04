package server.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Listener extends Thread implements ListenerInterface{
	private Worker worker;
	private String ip;
	private int port;
	private Server server;
	
	public Listener(Server server, int max) {
		this.server = server;
		InetAddress ipAddr;
		try {
			ipAddr = InetAddress.getLocalHost();
			ip = ipAddr.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		port = 55555;
		start();
	}
	
	public void run(){
		registerListener();
		while (!isInterrupted()) {}
	}

//	public static void main(String args[]) {
//		//RoboServerInterface robo;		
//		ListenerInterface listener = new Listener(4);
//	}

	@Override
	public void registerRobot(String roboName, String roboIp, int roboPort) throws RemoteException {
		System.out.println("Roboter " + roboName + " hat sich unter der IP " + roboIp + " gemeldet!");
		if (server.isAllowedToAddWorker()) {
			int index = server.getNextFreeWorkerNumber();
			worker = new Worker(this, "Worker" + (index + 1), roboName, ip, roboIp, (port + index + 1), roboPort);
			if (server != null) {
				server.addWorker(worker, index);
			}
		}else{
			System.out.println("Maximale Anzahl an Workern ist erreicht! Es kann kein Roboter mehr dazukommen!");
		}
	}
	
	private void registerListener() {
		try {
			//Listener meldet sich im System an unter Port 55555
			ListenerInterface stub = (ListenerInterface) UnicastRemoteObject.exportObject(this, 0);
	        try {
				LocateRegistry.createRegistry(port);
			} catch (Exception e) {}
	        Registry registry = LocateRegistry.getRegistry(port);
	        try {
				registry.bind("Listener", stub);
			} catch (Exception e) {}
			System.out.println("Listener bereit!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void closeWorker(String workerName) {
		worker = null;
		server.removeWorker(workerName);
		System.out.println("Worker " + workerName + " beendet!\n");
	}
	
	@Override
	public void stopListener() {
		interrupted();
	}
}

//Registry registry = LocateRegistry.getRegistry("192.168.178.26", 55555);
//robo = (RoboServerInterface) registry.lookup("Robo2");
//robo.turnLeft();
//robo.turnLeft();
//robo.turnLeft();
//robo.turnLeft();

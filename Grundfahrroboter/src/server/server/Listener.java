package server.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Listener implements ListenerInterface{
	private Worker worker;
	private String ip;
	private int port;
	private int anzahl;
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
		anzahl = 0;
		registerListener();
	}

//	public static void main(String args[]) {
//		//RoboServerInterface robo;		
//		ListenerInterface listener = new Listener(4);
//	}

	@Override
	public void registerRobot(String roboName, String roboIp, int roboPort) throws RemoteException {
		// TODO Auto-generated method stub 
		System.out.println("Roboter " + roboName + " hat sich unter der IP " + roboIp + " gemeldet!");
		if (server.isAllowedToAddWorker()) {
			anzahl++;
			worker = new Worker(this, "Worker" + anzahl, roboName, ip, roboIp, port+anzahl, roboPort);
			if (server != null) {
				server.addWorker(worker);
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
		anzahl--;
	}
}

//Registry registry = LocateRegistry.getRegistry("192.168.178.26", 55555);
//robo = (RoboServerInterface) registry.lookup("Robo2");
//robo.turnLeft();
//robo.turnLeft();
//robo.turnLeft();
//robo.turnLeft();

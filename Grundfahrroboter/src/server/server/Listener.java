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
	
	private Listener() {
		InetAddress ipAddr;
		try {
			ipAddr = InetAddress.getLocalHost();
			ip = ipAddr.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		port = 55555;
		anzahl = 0;
	}

	public static void main(String args[]) {
		//RoboServerInterface robo;		
		
		try {
			//Listener meldet sich im System an unter Port 55555
			ListenerInterface obj = new Listener();
			ListenerInterface stub = (ListenerInterface) UnicastRemoteObject.exportObject(obj, 0);
	        LocateRegistry.createRegistry(55555);
	        Registry registry = LocateRegistry.getRegistry(55555);
	        registry.bind("Listener", stub);
			
			System.out.println("Listener bereit!");
						      
//			Registry registry = LocateRegistry.getRegistry("192.168.178.26", 55555);
//			robo = (RoboServerInterface) registry.lookup("Robo2");
//			robo.turnLeft();
//			robo.turnLeft();
//			robo.turnLeft();
//			robo.turnLeft();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void registerRobot(String roboName, String roboIp, int roboPort) throws RemoteException {
		// TODO Auto-generated method stub 
		System.out.println("Roboter " + roboName + " hat sich unter der IP " + roboIp + " gemeldet!");
		anzahl++;
		worker = new Worker(this, "Worker" + anzahl, roboName, ip, roboIp, port+anzahl, roboPort);
	}	
	
	public void closeWorker(String workerName) {
		worker = null;
		System.out.println("Worker " + workerName + " beendet!\n");
		anzahl--;
	}
}

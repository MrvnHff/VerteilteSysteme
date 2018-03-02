package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Listener implements ListenerInterface{
	private Worker worker;
	private Listener() {}

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
	public void registerRobot(String name, String ip, int port) throws RemoteException {
		// TODO Auto-generated method stub 
		System.out.println("Roboter " + name + " hat sich unter der IP " + ip + " gemeldet!");
		worker = new Worker(name, ip, port);
	}	
}

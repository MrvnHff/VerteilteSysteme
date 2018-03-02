package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Worker implements WorkerInterface{
	private WorkerInterface stub;
	RoboServerInterface robo;
	
	public Worker(String name, String ip, int port) {
		try {
			stub = (WorkerInterface) UnicastRemoteObject.exportObject(this, 0);
			LocateRegistry.createRegistry(55556);
		    Registry registry = LocateRegistry.getRegistry(55556);
		    registry.bind("Worker", stub);
		    
		    System.out.println("Worker bereit!");
		    
		    Registry registryR = LocateRegistry.getRegistry(ip, port);
			robo = (RoboServerInterface) registryR.lookup(name);
			
			System.out.println("Worker verbunden mit Roboter "+ name +"!");
			
			sayHello();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sayHello() throws RemoteException {
		robo.registerWorker("Worker", "192.168.178.24", 55556);		
	}

	@Override
	public void setWay(String point1, String point2) throws RemoteException {
		robo.driveCm(10, 20);
		System.out.println(robo.getStatus());
	}

	
}

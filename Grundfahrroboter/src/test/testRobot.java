package test;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

import Exceptions.RobotException;

import java.net.InetAddress;

import server.server.ListenerInterface;
import server.server.WorkerInterface;
import client.RoboServerInterface;

public class testRobot implements RoboServerInterface{
	private int serverPort;
	private int roboterPort;
	private int waitTime;
	private String robotName;
	
	private WorkerInterface worker;
	private Registry registryW;
	private static boolean shutdown;
	public static final int MAXTRY = 10;
	
	public testRobot(int serverPort, int robotPort, int waitTime, String robotId){
		this.serverPort = serverPort;
		this.roboterPort = robotPort;
		this.robotName = robotId;
		if( waitTime >= 1 && waitTime <= 10){
			this.waitTime = waitTime;
		}else{
			this.waitTime = (int)(Math.random()*10 + 1);
		}
		
		// Initialisieren des Roboters
		shutdown = false;
		
		RoboServerInterface obj;
		RoboServerInterface stub;
		Registry registryR;
		ListenerInterface listener;
		Registry registryL;
		
		//Beim Server registrieren
		try {
			//Roboter registriert sich im System mit seinem Namen und dem Port 55555
			// Roboter stellt sich selbst als Server im Netzwerk bereit
    		obj = this;
    		stub = (RoboServerInterface) UnicastRemoteObject.exportObject(obj, 0); //Behelfs Port
    		LocateRegistry.createRegistry(roboterPort); 
    		registryR = LocateRegistry.getRegistry(roboterPort);
    		registryR.bind(robotName, stub);
    		
    		//Versuch des Verbindungsaufbaus mit dem Listener 
			for (int i = 0; i < MAXTRY; ++i) {
				try {            
            		//Roboter sucht im System nach dem Listener
            		registryL = LocateRegistry.getRegistry("192.168.178.27", serverPort);
            		listener = (ListenerInterface) registryL.lookup("Listener");
            		i = MAXTRY;
					InetAddress ipAddr = InetAddress.getLocalHost();
					System.out.println(ipAddr.getHostAddress());			
					listener.registerRobot(robotName, ipAddr.getHostAddress(), roboterPort);
        		} catch (ConnectException e) {
        			System.out.println("Server/Listener nicht erreichbar! Weitere Versuche:" + (MAXTRY - i - 1));
        			TimeUnit.SECONDS.sleep(2);
        			if (i >= MAXTRY-1) {System.exit(0);}
        		}
			}
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
		
	}
	
	

	public static void main(String[] args){
		try{
			int serverPort = Integer.parseInt(args[0]);
			int robotPort = Integer.parseInt(args[1]);
			int waitTime = Integer.parseInt(args[2]);
			String robotId = args[3];
			testRobot bot = new testRobot(serverPort, robotPort, waitTime, robotId);
		} catch (Exception e){
			System.out.println("Fehlerhafte Eingabe. Keinen Roboter gestartet.");
			System.out.println("Eingabeparameter: Serverport Roboterport WaitTime RobotID");
			System.out.println("1 <= WaitTime <= 10 (Zeit in Sekunden, bis eine Fahrt als ausgeführt gilt");
		}
	}
	
	//########################################
	//## 		Verbindungsmethoden			##
	//########################################
	
	
	@Override
	public void registerWorker(String name, String ip, int port) throws RemoteException {
	    registryW = LocateRegistry.getRegistry(ip, port);
		try {
			//Roboter sucht nach Worker im System. Erst jetzt steht fest kennt der Worker den Roboter und der Roboter den Worker.
			worker = (WorkerInterface) registryW.lookup(name);
			System.out.println("Mit Worker "+ name + " verbunden!");
			worker.printStatus(robotName + " bereit!");
			System.out.println(robotName + " bereit!");
			worker.setWay("", "");
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public String getStatus() throws RemoteException {
		return "getStatus from robot";
	}



	@Override
	public String getError() throws RemoteException {
		return "getError from robot";
	}



	@Override
	public void closeConnection() throws RemoteException {
		worker.printStatus("Aufwiedersehen! Beende mein Programm!");
		shutdown = true;
		System.exit(0);
	}
	
	
	//########################################
	//## 		Fahrmethoden				##
	//########################################
	
		@Override
	public void driveCm(double cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);;
			worker.printStatus("Zeit zu Ende. driveCm abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveCm!");
		}		
	}

	@Override
	public void drive(int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. drive abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei drive!");
		}
	}

	@Override
	public void driveUntilBlack(int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveUntilBlack abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveUntilBlack!");
		}		
	}

	@Override
	public void driveBackCm(double cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveBackCm abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveBackCm!");
		}		
	}

	@Override
	public void driveBack(int cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveBack abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveBack!");
		}
	}

	@Override
	public void turnLeft() throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. turnLeft abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei turnLeft!");
		}
	}

	@Override
	public void turnRight() throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. turnRight abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei turnRight!");
		}		
	}

	@Override
	public void turnAround() throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. turnAround abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei turnAround!");
		}
	}

	@Override
	public void drivePID(int cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. drivePID abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei drivePID!");
		}
	}

	@Override
	public void stopDrive() throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. stopDrive abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei stopDrive!");
		}
	}
	
	@Override
	public void driveNextPoint(int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveNextPoint abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveNextPoint!");
		}		
	}

}


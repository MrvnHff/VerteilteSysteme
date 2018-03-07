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

/**
 * Virtueller Roboter, der das RoboServerInterface implementiert und eine Verbindung zu einem Server 
 * im Netzwerk aufbaut. Dieser Roboter hat den vollen Methodenumfang eines echten Roboters und kann so
 * zum Testen des Servers genutzt werden.
 * @author Janek Dahl
 *
 */
public class VirtualRobot extends Thread implements RoboServerInterface{
	//Attribute des Roboters zum Verhalten / zur Verbindung
	private String serverIp;
	private int serverPort;
	private int roboterPort;
	private int waitTime;
	private String robotName;
	
	public static final int MAXTRY = 10;
	//Attribute für Roboter-Server / Server-Server / Listener / Worker
	private WorkerInterface worker;
	private Registry registryW;
	private Registry returnOfCreateRegistry;
	private RoboServerInterface obj;
	private RoboServerInterface stub;
	private Registry registryR;
	private ListenerInterface listener;
	private Registry registryL;
	
	public VirtualRobot(String serverIp, int serverPort, int robotPort, int waitTime, String robotId){
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.roboterPort = robotPort;
		this.robotName = robotId;
		if( waitTime >= 0 && waitTime <= 10){
			this.waitTime = waitTime;
		}else{
			this.waitTime = (int)(Math.random()*11);
		}		
	}
	
	@Override
	public void run() {
		// Initialisieren des Roboters

				
		//Beim Server registrieren
		try {			
			// Roboter stellt sich selbst als Server im Netzwerk bereit
    		obj = this;
    		stub = (RoboServerInterface) UnicastRemoteObject.exportObject(obj, 0); //Behelfs Port
    		returnOfCreateRegistry = LocateRegistry.createRegistry(roboterPort); 
    		registryR = LocateRegistry.getRegistry(roboterPort);
    		registryR.bind(robotName, stub);
    		
    		//Versuch des Verbindungsaufbaus mit dem Listener des Server-servers
			for (int i = 0; i < MAXTRY; ++i) {
				try {            
            		//Roboter sucht im System nach dem Listener
            		registryL = LocateRegistry.getRegistry(serverIp, serverPort);
            		listener = (ListenerInterface) registryL.lookup("Listener");
            		i = MAXTRY;
					InetAddress ipAddr = InetAddress.getLocalHost();
					System.out.println(ipAddr.getHostAddress());			
					//Registriert sich beim Listener des Server-servers
					listener.registerRobot(robotName, ipAddr.getHostAddress(), roboterPort);
        		} catch (ConnectException e) {
        			System.out.println("VirtualRobot: " + robotName + ": Server/Listener nicht erreichbar! Weitere Versuche:" + (MAXTRY - i - 1));
        			Thread.sleep(2 * 1000);
        			if (i >= MAXTRY-1) { 
        				closeConnection();
		 			}
        		}
			}
        } catch (Exception e) {
            System.err.println("VirtualRobot Exception: " + e.toString());
            e.printStackTrace();
        }
	}
	
	/**
	 * Startet einen Virtuellen Roboter von der Konsole aus mit den angegeben Parameter.
	 * Der Roboter wird als Thread gestartet und die Main geht in sleep() um den Roboter zu erhalten.
	 * @param args ServerIP ServerPort RoboterPort WaitTime RobotID
	 */
	public static void main(String[] args){
		try{
			String serverIp = args[0];
			int serverPort = Integer.parseInt(args[1]);
			int robotPort = Integer.parseInt(args[2]);
			int waitTime = Integer.parseInt(args[3]);
			String robotId = args[4];
			VirtualRobot bot = new VirtualRobot(serverIp, serverPort, robotPort, waitTime, robotId);
			bot.start();
			System.out.println("VirtualRobot: " + robotId + " angelegt." );
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e){
			System.out.println("Fehlerhafte Eingabe. Keinen Roboter gestartet.");
			System.out.println("Eingabeparameter: ServerIp Serverport Roboterport WaitTime RobotID");
			System.out.println("0 <= WaitTime <= 10 (Ausführungsdauer einer Fahroperation in Sekunden. Zufallsdauer falls ungültig.");
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
			System.out.println("VirtualRobot: Mit Worker "+ name + " verbunden!");
			worker.printStatus(robotName + " bereit!");
			System.out.println("VirtualRobot: " + robotName + " bereit!");
			worker.setWay("", "");
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public String getStatus() throws RemoteException {
		return "getStatus from Virtualrobot";
	}



	@Override
	public String getError() throws RemoteException {
		return "getError from Virtualrobot";
	}


	/**
	 * Meldet den Roboter überall ab, schließt den Port und beendet den Thread.
	 */
	@Override
	public void closeConnection() throws RemoteException {
		//FIXME Roboter überall abmelden und Fehler abfangen
		UnicastRemoteObject.unexportObject(returnOfCreateRegistry, true);
		//worker.printStatus("Aufwiedersehen! Beende mein Programm!");
		System.out.println("VirtualRobot: " + robotName + " connection closed.");
		interrupt();
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


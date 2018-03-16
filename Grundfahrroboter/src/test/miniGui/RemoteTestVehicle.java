package test.miniGui;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

import client.client.RemoteVehicleInterface;
import server.server.ListenerInterface;
import server.server.WorkerInterface;

import java.net.InetAddress;


/**
 * Virtueller Roboter, der das RemoteVehicleInterface implementiert und eine Verbindung zu einem Server 
 * im Netzwerk aufbaut. Dieser Roboter hat den vollen Methodenumfang eines echten Roboters und kann so
 * zum Testen des Servers genutzt werden.
 * @author Janek Dahl
 *
 */
public class RemoteTestVehicle extends Thread implements RemoteVehicleInterface{
	//Attribute des Roboters zum Verhalten / zur Verbindung
	private String serverIp;
	private int serverPort;
	private int roboterPort;
	private int waitTime;
	private String robotName;
	
	public static final int MAXTRY = 2;
	//Attribute fuer Roboter-Server / Server-Server / Listener / Worker
	private WorkerInterface worker;
	private Registry registryW;
	private Registry returnOfCreateRegistry;
	private RemoteVehicleInterface obj;
	private RemoteVehicleInterface stub;
	private Registry registryR;
	private ListenerInterface listener;
	private Registry registryL;
	private MiniGui gui;
	
	public RemoteTestVehicle(String serverIp, int serverPort, int robotPort, int waitTime, String robotId){
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
	
	public void setGui(MiniGui gui) {
		this.gui = gui;
	}
	
	@Override
	public void run() {
		// Initialisieren des Roboters

				
		//Beim Server registrieren
		try {			
			// Roboter stellt sich selbst als Server im Netzwerk bereit
    		obj = this;
    		try {
    		stub = (RemoteVehicleInterface)UnicastRemoteObject.exportObject(obj, 0); //Behelfs Port
    			returnOfCreateRegistry = LocateRegistry.createRegistry(roboterPort); 
    			registryR = LocateRegistry.getRegistry(roboterPort);
    			registryR.bind(robotName, stub);
    		} catch(Exception e) 
    		{
    			System.out.println("RemoteTestVehicle:  Port bereits belegt" + e);
    			gui.addMessageToUi("Port bereits belegt" + e);
    		}
    		//Versuch des Verbindungsaufbaus mit dem Listener des Server-servers
			for (int i = 0; i < MAXTRY; ++i) {
				try {            
            		//Roboter sucht im System nach dem Listener
            		registryL = LocateRegistry.getRegistry(null, serverPort);
            		listener = (ListenerInterface) registryL.lookup("Listener");
            		i = MAXTRY;
					InetAddress ipAddr = InetAddress.getLocalHost();
					System.out.println(ipAddr.getHostAddress());			
					//Registriert sich beim Listener des Server-servers
					listener.requestNewWorker(robotName, ipAddr.getHostAddress(), roboterPort);
        		} catch (ConnectException e) {
        			System.out.println("RemoteTestVehicle: " + robotName + ": Server/Listener nicht erreichbar! Weitere Versuche:" + (MAXTRY - i - 1));
        			gui.addMessageToUi("Server/Listener nicht erreichbar! Weitere Versuche:" + (MAXTRY - i - 1));
        			e.printStackTrace();
        			Thread.sleep(2 * 1000);
        			if (i >= MAXTRY-1) { 
        				closeConnection();
		 			}
        		}
			}
        } catch (Exception e) {
            System.err.println("RemoteTestVehicle Exception: " + e.toString());
            gui.addMessageToUi("RemoteTestVehicle Exception: " + e.toString());
            e.printStackTrace();
        }
	}
	

	//########################################
	//## 		Verbindungsmethoden			##
	//########################################
	
	
	@Override
	public void registerWorkerInVehicle(String name, String ip, int port) throws RemoteException {
	    registryW = LocateRegistry.getRegistry(ip, port);
		try {
			//Roboter sucht nach Worker im System. Erst jetzt steht fest kennt der Worker den Roboter und der Roboter den Worker.
			worker = (WorkerInterface) registryW.lookup(name);
			System.out.println("RemoteTestVehicle: Mit Worker "+ name + " verbunden!");
			// Timeout zum unterbinden der Nullpointer Exception. Der Aufruf findet schneller statt, als der Roboter der GUI hinzugefï¿½gt wurde.
			try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {}
			worker.printStatus(robotName + " bereit!");
			System.out.println("RemoteTestVehicle: " + robotName + " bereit!");
			gui.addMessageToUi(robotName + " bereit!");
		} catch (NotBoundException e) {
			System.err.println("RemoteTestVehicle: Fehler innerhalb von registerWorkerInVehicle");
			gui.addMessageToUi("RemoteTestVehicle: Fehler innerhalb von registerWorkerInVehicle");
			e.printStackTrace();
		}
	}



	@Override
	public String getStatus() throws RemoteException {
		return "getStatus from RemoteTestVehicle";
	}



	@Override
	public String getError() throws RemoteException {
		return "getError from RemoteTestVehicle";
	}


	/**
	 * Meldet den Roboter ï¿½berall ab, schlieï¿½t den Port und beendet den Thread.
	 */
	@Override
	public void closeConnection() throws RemoteException {
		try {
			registryR.unbind(this.robotName);
		} catch (NotBoundException e) {
			System.err.println("RemoteTestVehicle: Fehler beim unbind in closeConnection");
			gui.addMessageToUi("RemoteTestVehicle: Fehler beim unbind in closeConnection");
			e.printStackTrace();
		}
		
		UnicastRemoteObject.unexportObject(returnOfCreateRegistry, true);
		System.out.println("RemoteTestVehicle: " + robotName + " connection closed.");
		gui.addMessageToUi("RemoteTestVehicle: " + robotName + " connection closed.");
		interrupt(); 
	}
	
	public void quitWorker() {
		try {
			worker.quitFromVehicle();
		} catch (RemoteException e) {
			System.err.println("VirualRobot: Exception in quitVehicle() ausgelöst.");
			gui.addMessageToUi("VirualRobot: Exception in quitVehicle() ausgelöst.");
		}
	}
	
	
	
	//########################################
	//## 		Fahrmethoden				##
	//########################################
	
	@Override
	public void driveCm(double cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);;
			worker.printStatus("Zeit zu Ende. driveCm abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. driveCm abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveCm!");
			gui.addMessageToUi("Fehler bei driveCm!");
		}		
	}

	@Override
	public void drive(int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. drive abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. drive abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei drive!");
			gui.addMessageToUi("Fehler bei drive!");
		}
	}

	@Override
	public void driveUntilBlack(int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveUntilBlack abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. driveUntilBlack abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveUntilBlack!");
			gui.addMessageToUi("Fehler bei driveUntilBlack!");
		}		
	}

	@Override
	public void driveBackCm(double cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveBackCm abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. driveBackCm abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveBackCm!");
			gui.addMessageToUi("Fehler bei driveBackCm!");
		}		
	}

	@Override
	public void driveBack(int cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveBack abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. driveBack abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveBack!");
			gui.addMessageToUi("Fehler bei driveBack!");
		}
	}

	@Override
	public void turnLeft() throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. turnLeft abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. turnLeft abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei turnLeft!");
			gui.addMessageToUi("Fehler bei turnLeft!");
		}
	}

	@Override
	public void turnRight() throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. turnRight abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. turnRight abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei turnRight!");
			gui.addMessageToUi("Fehler bei turnRight!");
		}		
	}

	@Override
	public void turnAround() throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. turnAround abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. turnAround abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei turnAround!");
			gui.addMessageToUi("Fehler bei turnAround!");
		}
	}

	@Override
	public void drivePID(int cm, int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. drivePID abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. drivePID abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei drivePID!");
			gui.addMessageToUi("Fehler bei drivePID!");
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
			gui.addMessageToUi("Fehler bei stopDrive!");
		}
	}
	
	@Override
	public void driveNextPoint(int speed) throws RemoteException{
		try {
			TimeUnit.SECONDS.sleep(waitTime);
			worker.printStatus("Zeit zu Ende. driveNextPoint abgeschlossen.");
			gui.addMessageToUi("Zeit zu Ende. driveNextPoint abgeschlossen.");
		} catch (Exception e) {
			e.printStackTrace();
			worker.printStatus("Fehler bei driveNextPoint!");
			gui.addMessageToUi("Fehler bei driveNextPoint!");
		}		
	}

}


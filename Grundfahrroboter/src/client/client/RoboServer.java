package client.client;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;

import client.exceptions.RobotException;
import client.fileSystem.FileSystem;
import client.logic.Roboter;
import server.server.ListenerInterface;
import server.server.WorkerInterface;
/**
 * Die Klasse RoboServer enthält die Main, die den Roboter startet und in ihr sind die Methoden implementiert,
 * die von dem Server aufgerufen und somit ausgeführt werden.
 * Die Klasse enthält ein Roboter Objekt, welches für die Grundsteuerung des Roboters zuständig ist.
 * Ebenfalls ist die Klasse dafür zuständig, dass der Roboter auch mit dem Listener und Worker komunizieren kann.
 * @author Lennart Monir
 * @version 0.3
 *
 */
public class RoboServer implements RemoteVehicleInterface{
	private static Roboter robo;
	private static WorkerInterface worker;
	private Registry registryW;
	
	private static String robotName;
	private static boolean shutdown;
	private final static int MAXTRY = 10;

	/**
	 * Die Methode sagt dem Roboter, dass er eine Strecke (cm) fahren soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 * @param cm, die Länge der Strecke
	 * @param speed, die Geschwindigkeit mit der der Roboter fahren soll
	 */
	@Override
	public void driveCm(double cm, int speed) throws RemoteException{
		try {
			robo.driveCm(cm, speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	/**
	 * Die Methode sagt dem Roboter, dass er fahren soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 * @param speed, die Geschwindigkeit mit der der Roboter fahren soll
	 */
	@Override
	public void drive(int speed) throws RemoteException{
		try {
			robo.drive(speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	/**
	 * Die Methode sagt dem Roboter, dass er so lange fahren soll, bis er mit dem Lichsensor Schwarz sieht.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 * @param speed, die Geschwindigkeit mit der der Roboter fahren soll
	 */
	@Override
	public void driveUntilBlack(int speed) throws RemoteException{
		try {
			robo.driveUntilLight(speed, 17, "<=");
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	/**
	 * Die Methode sagt dem Roboter, dass er eine Strecke (cm) rückwärts fahren soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 * @param cm, die Länge der Strecke
	 * @param speed, die Geschwindigkeit mit der der Roboter fahren soll
	 */
	@Override
	public void driveBackCm(double cm, int speed) throws RemoteException{
		try {
			robo.driveCm(cm, -speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	/**
	 * Die Methode sagt dem Roboter, dass er rückwärts fahren soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 * @param speed, die Geschwindigkeit mit der der Roboter fahren soll
	 */
	@Override
	public void driveBack(int speed) throws RemoteException{
		try {
			robo.drive(-speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	/**
	 * Die Methode sagt dem Roboter, dass er sich nach links um 90 Grad drehen soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 */
	@Override
	public void turnLeft() throws RemoteException{
		try {
			robo.turn(90, false);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	/**
	 * Die Methode sagt dem Roboter, dass er sich nach rechst um 90 Grad drehen soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 */
	@Override
	public void turnRight() throws RemoteException{
		try {
			robo.turn(90, true);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	/**
	 * Die Methode sagt dem Roboter, dass er sich um 180 Grad drehen soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 */
	@Override
	public void turnAround() throws RemoteException{
		try {
			robo.turn(180, false);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	/**
	 * Die Methode sagt dem Roboter, dass er eine Strecke (cm) mit dem PID fahren soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 * @param cm, die Länge der Strecke
	 * @param speed, die Geschwindigkeit mit der der Roboter fahren soll
	 */
	@Override
	public void drivePID(int cm, int speed) throws RemoteException{
		try {
			robo.pidLightCm(speed, cm);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	/**
	 * Die Methode sagt dem Roboter, dass er aufhören soll zu fahren.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 */
	@Override
	public void stopDrive() throws RemoteException{
		try {
			robo.stopDrive();
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}
	
	/**
	 * Die Methode sagt dem Roboter, dass er von einem Punkt zum nächsten fahren soll.
	 * Danach wird der Worker über den Stand des Roboters informiert.
	 * @param speed, die Geschwindigkeit mit der der Roboter fahren soll
	 */
	@Override
	public void driveNextPoint(int speed) throws RemoteException{
		try {
			robo.turn(3, false);
			robo.driveCm(3, speed);
			robo.pidLightCm(speed, 68);
			robo.turn(4, true);
			robo.driveCm(6, speed);
			robo.turn(4, false);
			robo.driveUntilLight(speed, 10, "<=");			
			robo.driveCm(10, speed);
			worker.printStatus("Habe den Punkt erreicht!");
		} catch (RobotException e) {
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	/**
	 * Die Methode ruft den aktuellen Status des Roboters ab.
	 */
	@Override
	public String getStatus() throws RemoteException {
		return robo.getStatus();
	}
	
	/**
	 * Die Methode ruft den aktuellen Fehlerstatus des Roboters ab.
	 */
	@Override
	public String getError() throws RemoteException {
		return robo.getError();
	}
	
	/**
	 * Die Methode lässt den Roboter eine Verbindung zum Worker, der ihm zugewiesen wird, aufbauen,
	 * damit er auch Methoden beim Worker aufrufen kann.
	 * 
	 * @param name, der Name des Workers
	 * @param ip, die IP unter der der Worker zu finden ist
	 * @param port, der Port über den der Roboter mit dem Worker kommunizieren kann.
	 */
	@Override
	public void registerWorkerInVehicle(String name, String ip, int port) throws RemoteException {
	    registryW = LocateRegistry.getRegistry(ip, port);
		try {
			//Roboter sucht nach Worker im System. Erst jetzt steht fest kennt der Worker den Roboter und der Roboter den Worker.
			worker = (WorkerInterface) registryW.lookup(name);
			System.out.println("Mit Worker "+ name + " verbunden!");
			worker.printStatus(robotName + " bereit!");
			System.out.println(robotName + " bereit!");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Die Methode schließt die Verbindung und beendet den Roboter.
	 */
	@Override
	public void closeConnection() throws RemoteException{
		worker.printStatus("Aufwiedersehen! Beende mein Programm!");
		shutdown = true;
		System.exit(0);
	}
	
	/**
	 * Die Main baut eine Verbindung zum Listener auf. Dies versucht er eine bestimmte Anzahl oft.
	 * Da jeder Roboter eigene Werte und Konstanten besitzt, habe wir die Werte in verschiedene Proberty Datein
	 * geschrieben. Die werden am Anfang alle ausgelesen.
	 */
public static void main(String args[]) {
	final int ROBO_NUMBER = 3;
	
	double dm = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "Durchmesser"));
	double kp = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_p"));
	double ki = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_i"));
	double kd = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_d"));
	int port = Integer.parseInt(FileSystem.readProperties(ROBO_NUMBER, "RoboPort"));
	robotName = FileSystem.readProperties(ROBO_NUMBER, "Name");
	shutdown = false;
	
	RemoteVehicleInterface obj;
	RemoteVehicleInterface stub;
	Registry registryR;
	ListenerInterface listener;
	Registry registryL;

	try {
		robo = new Roboter(dm,kp,ki,kd);
	} catch (RobotException e1) {
		e1.printStackTrace();
	}     
	
		try {
			//Roboter registriert sich im System mit seinem Namen und dem Port 55555
    		obj = new RoboServer();
    		stub = (RemoteVehicleInterface) UnicastRemoteObject.exportObject(obj, 0);
    		LocateRegistry.createRegistry(port);
    		registryR = LocateRegistry.getRegistry(port);
    		registryR.bind(robotName, stub);
    		
			for (int i = 0; i < MAXTRY; ++i) {
				try {            
            		//Roboter sucht im System nach dem Listener
            		registryL = LocateRegistry.getRegistry("192.168.178.100", 55555);
            		listener = (ListenerInterface) registryL.lookup("Listener");
            		i = MAXTRY;
					InetAddress ipAddr = InetAddress.getLocalHost();
					System.out.println(ipAddr.getHostAddress());			
					listener.requestNewWorker(robotName, ipAddr.getHostAddress(), 55555);
        		} catch (ConnectException e) {
        			System.out.println("Server/Listener nicht erreichbar!");
        			robo.waitMs(2000);
        			if (i >= MAXTRY-1) {System.exit(0);}
        		}
			}
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            try {
				worker.printError(e.toString());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
        }
		if (shutdown){System.exit(0);}
    }	
}
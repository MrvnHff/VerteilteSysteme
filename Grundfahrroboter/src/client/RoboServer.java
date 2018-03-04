package client;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Exceptions.RobotException;

import java.net.InetAddress;
import FileSystem.FileSystem;
import logic.Roboter;
import server.server.ListenerInterface;
import server.server.WorkerInterface;

public class RoboServer implements RoboServerInterface{
	private static Roboter robo;
	private WorkerInterface worker;
	private Registry registryW;
	
	private static String robotName;
	private static boolean shutdown;
	private final static int MAXTRY = 10;

	@Override
	public void driveCm(double cm, int speed) throws RemoteException{
		try {
			robo.driveCm(cm, speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	@Override
	public void drive(int speed) throws RemoteException{
		try {
			robo.drive(speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	@Override
	public void driveUntilBlack(int speed) throws RemoteException{
		try {
			robo.driveUntilLight(speed, 15, "<=");
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	@Override
	public void driveBackCm(double cm, int speed) throws RemoteException{
		try {
			robo.driveCm(cm, -speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	@Override
	public void driveBack(int cm, int speed) throws RemoteException{
		try {
			robo.drive(-speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	@Override
	public void turnLeft() throws RemoteException{
		try {
			robo.turn(90, false);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	@Override
	public void turnRight() throws RemoteException{
		try {
			robo.turn(90, true);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	@Override
	public void turnAround() throws RemoteException{
		try {
			robo.turn(180, false);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	@Override
	public void drivePID(int cm, int speed) throws RemoteException{
		try {
			robo.pidLightCm(speed, cm);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}

	@Override
	public void stopDrive() throws RemoteException{
		try {
			robo.stopDrive();
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}
	}
	
	@Override
	public void driveNextPoint(int speed) throws RemoteException{
		try {
			robo.driveCm(5, speed);
			robo.pidGyroCm(speed, 65);
			robo.driveCm(3, speed);
			robo.driveUntilLight(speed, 5, "<=");
			robo.driveCm(5, speed);
			worker.printStatus(robo.getStatus());
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worker.printStatus(robo.getError());
		}		
	}

	@Override
	public String getStatus() throws RemoteException {
		// TODO Auto-generated method stub
		return robo.getStatus();
	}
	
	@Override
	public String getError() throws RemoteException {
		// TODO Auto-generated method stub
		return robo.getError();
	}
	
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
	public void closeConnection() throws RemoteException{
		worker.printStatus("Aufwiedersehen!\nBeende mein Programm!");
		shutdown = true;
	}
	
public static void main(String args[]) {
	final int ROBO_NUMBER = 2;
	
	double dm = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "Durchmesser"));
	double kp = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_p"));
	double ki = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_i"));
	double kd = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_d"));
	int port = Integer.parseInt(FileSystem.readProperties(ROBO_NUMBER, "RoboPort"));
	robotName = FileSystem.readProperties(ROBO_NUMBER, "Name");
	shutdown = false;
	
	RoboServerInterface obj;
	RoboServerInterface stub;
	Registry registryR;
	ListenerInterface listener;
	Registry registryL;

	try {
		robo = new Roboter(dm,kp,ki,kd);
	} catch (RobotException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}     
	
		try {
			//Roboter registriert sich im System mit seinem Namen und dem Port 55555
    		obj = new RoboServer();
    		stub = (RoboServerInterface) UnicastRemoteObject.exportObject(obj, 0);
    		LocateRegistry.createRegistry(port);
    		registryR = LocateRegistry.getRegistry(port);
    		registryR.bind(robotName, stub);
    		
			for (int i = 0; i < MAXTRY; ++i) {
				try {            
            		//Roboter sucht im System nach dem Listener
            		registryL = LocateRegistry.getRegistry("192.168.178.24", 55555);
            		listener = (ListenerInterface) registryL.lookup("Listener");
            		i = MAXTRY;
					InetAddress ipAddr = InetAddress.getLocalHost();
					System.out.println(ipAddr.getHostAddress());			
					listener.registerRobot(robotName, ipAddr.getHostAddress(), 55555);
        		} catch (ConnectException e) {
        			System.out.println("Server/Listener nicht erreichbar!");
        			robo.waitMs(2000);
        			if (i >= MAXTRY-1) {System.exit(0);}
        		}
			}
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
		if (shutdown){System.exit(0);}
    }	
}
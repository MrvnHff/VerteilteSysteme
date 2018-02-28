package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Exceptions.RobotException;

import java.net.InetAddress;
import FileSystem.FileSystem;
import logic.Roboter;

public class RoboServer implements RoboServerInterface{
	private static Roboter robo;
	private static double dm;
	private static double kp;
	private static double ki;
	private static double kd;

	@Override
	public void driveCm(double cm, int speed) throws RemoteException{
		try {
			robo.driveCm(cm, speed);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void drive(int speed) throws RemoteException{
		try {
			robo.drive(speed);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void driveUntilBlack(int speed) throws RemoteException{
		try {
			robo.driveUntilLight(speed, 15, "<=");
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void driveBackCm(double cm, int speed) throws RemoteException{
		try {
			robo.driveCm(cm, -speed);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void driveBack(int cm, int speed) throws RemoteException{
		try {
			robo.drive(-speed);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void turnLeft() throws RemoteException{
		try {
			robo.turn(90, false);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void turnRight() throws RemoteException{
		try {
			robo.turn(90, true);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void turnAround() throws RemoteException{
		try {
			robo.turn(180, false);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void drivePID(int cm, int speed) throws RemoteException{
		try {
			robo.pidLightCm(speed, cm);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stopDrive() throws RemoteException{
		try {
			robo.stopDrive();
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public String getStatus() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Moin");
		return null;
	}
	
public static void main(String args[]) {
	final int ROBO_NUMBER = 2;
	
	 dm = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "Durchmesser"));
	 kp = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_p"));
	 ki = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_i"));
	 kd = Double.parseDouble(FileSystem.readProperties(ROBO_NUMBER, "PID_d"));
	int port = Integer.parseInt(FileSystem.readProperties(ROBO_NUMBER, "RoboPort"));

	try {
		robo = new Roboter(dm,kp,ki,kd);
	} catch (RobotException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}    
	
        try {
            RoboServerInterface obj = new RoboServer();
            RoboServerInterface stub = (RoboServerInterface) UnicastRemoteObject.exportObject(obj, 0);
            LocateRegistry.createRegistry(port);
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind(FileSystem.readProperties(ROBO_NUMBER, "Name"), stub);
            
            InetAddress ipAddr = InetAddress.getLocalHost();
            System.out.println(ipAddr.getHostAddress());

            System.err.println(FileSystem.readProperties(ROBO_NUMBER, "Name") + " bereit!");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
	
}

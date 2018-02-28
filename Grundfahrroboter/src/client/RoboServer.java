package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import FileSystem.FileSystem;
import logic.Roboter;

public class RoboServer implements RoboServerInterface{
	private static Roboter robo;
	private static double dm;
	private static double kp;
	private static double ki;
	private static double kd;

	@Override
	public void driveCm(double cm, int speed) throws RemoteException {
		robo.setDiameter(dm);
		robo.driveCm(cm, speed);		
	}

	@Override
	public void drive(int speed) throws RemoteException {
		robo.drive(speed);
	}

	@Override
	public void driveUntilBlack(int speed) throws RemoteException {
		robo.driveUntilLight(speed, 5, "<=");
		
	}

	@Override
	public void driveBackCm(double cm, int speed) throws RemoteException {
		robo.setDiameter(dm);
		robo.driveCm(cm, -speed);		
	}

	@Override
	public void driveBack(int cm, int speed) throws RemoteException {
		robo.setDiameter(dm);
		robo.drive(-speed);
	}

	@Override
	public void turnLeft() throws RemoteException {
		robo.turn(90, false);
	}

	@Override
	public void turnRight() throws RemoteException {
		robo.turn(90, true);		
	}

	@Override
	public void turnAround() throws RemoteException {
		robo.turn(180, false);
	}

	@Override
	public void drivePID(int cm, int speed) throws RemoteException {
		robo.setPID(kp, ki, kd);
		robo.setDiameter(dm);
		robo.pidLightCm(speed, cm);
	}

	@Override
	public void stopDrive() throws RemoteException {
		robo.stopDrive();
	}
	
	@Override
	public void driveNextPoint(int speed) throws RemoteException {
		robo.setPID(kp, ki, kd);
		robo.setDiameter(dm);
		robo.driveCm(5, speed);
		robo.pidGyroCm(speed, 65);
		robo.driveCm(3, speed);
		robo.driveUntilLight(speed, 5, "<=");
		robo.driveCm(5, speed);
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

	robo = new Roboter(dm,kp,ki,kd);    
	
        try {
            RoboServerInterface obj = new RoboServer();
            RoboServerInterface stub = (RoboServerInterface) UnicastRemoteObject.exportObject(obj, 0);
            //System.out.println(obj.toString());
            LocateRegistry.createRegistry(port);
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind(FileSystem.readProperties(ROBO_NUMBER, "Name"), stub);

            System.err.println("Roboter bereit");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
	
}

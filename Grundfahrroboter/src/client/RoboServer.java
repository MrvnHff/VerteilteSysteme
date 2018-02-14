package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import logic.Roboter;

public class RoboServer implements RoboServerInterface{
	private Roboter robo = new Roboter(43);

	@Override
	public void driveCm(double cm, int speed) throws RemoteException {
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
		robo.driveCm(cm, -speed);		
	}

	@Override
	public void driveBack(int cm, int speed) throws RemoteException {
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
		robo.pidLightCm(speed, cm);
	}

	@Override
	public void stopDrive() throws RemoteException {
		robo.stopDrive();
	}

	@Override
	public String getStatus() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
public static void main(String args[]) {
        
        try {
            RoboServerInterface obj = new RoboServer();
            RoboServerInterface stub = (RoboServerInterface) UnicastRemoteObject.exportObject(obj, 0);
            //System.out.println(obj.toString());
            LocateRegistry.createRegistry(55555);
            Registry registry = LocateRegistry.getRegistry(55555);
            registry.bind("Robo", stub);

            System.err.println("Roboter bereit");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
	
}

/**
 * @author {garte}
 * @version {10.01.2018}
 */
package client;

/**
 * @author garte
 *
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RoboServerInterface extends Remote {
	
	public void driveCm(double cm, int speed) throws RemoteException;
	public void drive(int speed) throws RemoteException;
	public void driveUntilBlack(int speed) throws RemoteException;
	
	public void driveBackCm(double cm, int speed) throws RemoteException;
	public void driveBack(int cm, int speed) throws RemoteException;
	
	public void turnLeft() throws RemoteException;
	public void turnRight() throws RemoteException;
	public void turnAround() throws RemoteException;
	
	public void drivePID(int cm, int speed) throws RemoteException;
	
	public void stopDrive() throws RemoteException;
	
	public void driveNextPoint(int speed) throws RemoteException;
	
	public String getStatus() throws RemoteException;
}


/**
 * @author {garte}
 * @version {10.01.2018}
 */
package client.client;

/**
 * @author garte
 *
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Interface, welches jedes Fahrzeug, dass sich mit der Server verbinden will, 
 * implementieren muss um die Methoden vom Server ansprechen zu können. 
 * @author garte
 *
 */
public interface RemoteVehicleInterface extends Remote {
	
	public void registerWorkerInVehicle(String name, String ip, int port) throws RemoteException;
	
	public void driveCm(double cm, int speed) throws RemoteException;
	public void drive(int speed) throws RemoteException;
	public void driveUntilBlack(int speed) throws RemoteException;
	
	public void driveBackCm(double cm, int speed) throws RemoteException;
	public void driveBack(int speed) throws RemoteException;
	
	public void turnLeft() throws RemoteException;
	public void turnRight() throws RemoteException;
	public void turnAround() throws RemoteException;
	
	public void drivePID(int cm, int speed) throws RemoteException;
	
	public void stopDrive() throws RemoteException;
	
	public void driveNextPoint(int speed) throws RemoteException;
	
	public String getStatus() throws RemoteException;
	public String getError() throws RemoteException;
	
	public void closeConnection() throws RemoteException;
}


package server.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface das die erreichbaren Methoden des Workers im Netzwerk definiert.
 * 
 */
public interface WorkerInterface extends Remote{
	
	
	/**
	 * Gibt die aktuelle Statusmeldung des Fahrzeugs auf der Konsole aus
	 * @category remoteReachable
	 * @param point1 ohne Sinn
	 * @param point2 ohne Sinn
	 * @throws RemoteException
	 */
	public void setWay(String point1, String point2) throws RemoteException;
	
	
	/**
	 * Gibt die übergebene Meldung auf die Konsole aus und ruft die addVehicleTextMessage() des Servers auf.
	 * @category remoteReachable
	 * @param s Die auszugebende Meldung
	 * @throws RemoteException
	 */
	public void printStatus(String s) throws RemoteException;
	
	
	/**
	 * Gibt die übergebene Meldung auf die Konsole aus und ruft die addVehicleTextMessage() des Servers auf.
	 * @category remoteReachable
	 * @param s Die auszugebende Meldung
	 * @throws RemoteException
	 */
	public void printError(String s) throws RemoteException;
}

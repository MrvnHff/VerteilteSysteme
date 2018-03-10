package server.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface das die erreichbaren Methoden des Workers im Netzwerk definiert.
 * 
 */
public interface WorkerInterface extends Remote{
	
	/**
	 * Beendet die Verbindung vom Fahrzeug aus. Die CloseConnection wird vom Worker aus an das Fahrzeug weitergeben
	 * und der Worker anschließend beendet.
	 * @throws RemoteException
	 */
	public void quitFromVehicle() throws RemoteException;
		
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

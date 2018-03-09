package server.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ListenerInterface extends Remote {
	
	/**
	 * Ermöglicht es einem Fahrzeug sich beim Listener anzumelden, um zum Server hinzugefügt zu werden.
	 * @category remoteReachable
	 * @param vehicleId ID des Fahrzeugs das sich meldet
	 * @param vehicleIp IP-Adresse des Fahrzeugs unter der es im Netzwerk erreichbar ist
	 * @param vehiclePort Port an welchem das Fahrzeug angesprochen werden kann
	 * @throws RemoteException
	 */
	public void requestNewWorker(String vehicleId, String vehicleIp, int vehiclePort) throws RemoteException;
	
}
package server.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ListenerInterface extends Remote {
	
	public void registerRobot(String name, String ip, int port) throws RemoteException;
	public void stopListener() throws RemoteException; //TODO stopListener hier n�tig?? Muss ja nicht im Netzwerk erreichbar sein
}
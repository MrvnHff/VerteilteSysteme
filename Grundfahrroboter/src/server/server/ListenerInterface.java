package server.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ListenerInterface extends Remote {
	
	/**
	 * Erm�glicht es einem Roboter sich beim Listener anzumelden, um zum Server hinzugef�gt zu werden.
	 * @category remoteReachable
	 * @param roboName ID des Roboters
	 * @param roboIp IP des Roboters
	 * @param roboPort Port des Roboters
	 * @throws RemoteException
	 */
	public void registerRobot(String name, String ip, int port) throws RemoteException;
	
}
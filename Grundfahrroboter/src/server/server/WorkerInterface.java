package server.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerInterface extends Remote{
	public void setWay(String point1, String point2) throws RemoteException;
	public void printStatus(String s) throws RemoteException;
	public void printError(String s) throws RemoteException;
}

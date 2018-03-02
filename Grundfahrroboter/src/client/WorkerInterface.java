package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerInterface extends Remote{
	public void sayHello() throws RemoteException;
	public void setWay(String point1, String point2) throws RemoteException;
}

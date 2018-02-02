package de.htwsaar.vs.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WorkerImplement extends UnicastRemoteObject implements Worker{

	protected WorkerImplement() throws RemoteException {
		super();
	}

}

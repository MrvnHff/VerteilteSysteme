package de.htwsaar.vs.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.vs.gui.Gui;
import de.htwsaar.vs.server.graph.RoboGraph;

public interface ServerInterface {
	
	public void turnRobotLeft(String robotId);
	public void turnRobotRight(String robotId);
	public String moveRobotForward(String robotId);
	
	
	public void setGui(Gui gui);
	public RoboGraph getRoboGraph();
	public String getRobotLog();
	public boolean getFlag();
	public void startServer();
	public void stopServer();
	public void printServerLog(String log);
	public void printRobotLog(String log, boolean flag);
}

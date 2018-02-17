package de.htwsaar.vs.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.vs.gui.Gui;
import de.htwsaar.vs.server.graph.RoboGraph;

public interface ServerInterface {
	
	public void turnRobotLeft(String robotId);
	public void turnRobotRight(String robotId);
	public String moveRobotForward(String robotId);
	public void turnRobotLeftGui(String robotId);
	public void moveRobotForwardGui(String robotId);
	public void driveRobotTo(String robotId, String destination);
	
	public void setGui(Gui gui);
	public RoboGraph getRoboGraph();
	public String getRobotLog();
	public boolean getRobotFlag();
	public boolean getServerFlag();
	public String getServerLog();
	public void startServer();
	public void stopServer();
	public void printRobotLog(String log, boolean flag);
}

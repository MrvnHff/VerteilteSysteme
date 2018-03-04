package server.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.gui.Gui;
import server.server.graph.RoboGraph;

public interface ServerInterface {
	
	public void turnRobotLeft(String robotId);
	public void turnRobotRight(String robotId);
	public String moveRobotForward(String robotId);
	public void turnRobotLeftGui(String robotId);
	public void moveRobotForwardGui(String robotId);
	public void driveRobotTo(String robotId, String destination);
	
	public String getRoboErrorLog();
	public String getRoboLog();
	public void printRoboErrorLog(String roboErrorLog);
	public void printRoboLog(String roboLog);
	public String getServerLog();
	
	public void setGui(Gui gui);
	public RoboGraph getRoboGraph();
	public void startServer();
	public void stopServer();
	
	
}

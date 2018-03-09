package server.server;

import server.gui.Gui;
import server.server.graph.RoboGraph;

public interface ServerInterface {
	
	public void turnRobotLeft(String robotId);
	public void turnRobotRight(String robotId);
	public String moveRobotForward(String robotId);
	//public void turnRobotLeftGui(String robotId);
	//public void moveRobotForwardGui(String robotId);
	public void driveRobotTo(String robotId, String destination);
	
	public void setGui(Gui gui);
	public RoboGraph getRoboGraph();
	public void startServer();
	public void stopServer();
	
	public void addRobotTextMessage(String robotId, String message);
	public void addServerTextMessage(String message);
	
	
	public void addWorker(String roboName, String roboIp, int roboPort);
	public void removeWorker(String roboId);
	
	public void activateAutoDst(String roboId);
	public void deactivateAutoDst(String roboId);
	public boolean isRobotInAutoMode(String roboId);
	
}

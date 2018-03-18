package server.server;

import server.gui.Gui;
import server.server.graph.StreetGraph;
/**
 * Das Interface, welches der Server implementieren muss, um nach auﬂen alle notwendigen Methoden bereitszustellen.
 * 
 */
public interface ServerInterface {
	
	public void turnVehicleLeft(String vehicleId);
	public void turnVehicleRight(String vehicleId);
	public String moveVehicleForward(String vehicleId);
	public void driveVehicletTo(String vehicleId, String destination);
	
	public void setGui(Gui gui);
	public StreetGraph getStreetGraph();
	public void startServer();
	public void stopServer();
	
	public void addVehicleTextMessage(String vehicleId, String message);
	public void addServerTextMessage(String message);
	
	
	public void addWorker(String vehicleId, String vehicleIp, int vehiclePort);
	public void removeWorker(String vehicleId);
	
	public void activateAutoDst(String vehicleId);
	public void deactivateAutoDst(String vehicleId);
	public boolean isVehicleInAutoMode(String vehicleId);
	public boolean nodeExists(String nodeId);
	
}

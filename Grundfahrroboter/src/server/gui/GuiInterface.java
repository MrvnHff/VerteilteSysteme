package server.gui;

public interface GuiInterface {

	public void addVehicle(final String vehicleId, final String position);
	public void removeVehicle(String vehicleId);
	public void addServerTextMessage(String msg);
	public void addVehicleTextMessage(String vehicleId, String msg);
	public void setVehiclePosition(String vehicleId, String position);
	public void turnVehicleLeft(String vehicleId);
	public void turnVehicleRight(String vehicleId);
	public void setVehicleDestinationTextField(String vehicleId, String position);
	
}

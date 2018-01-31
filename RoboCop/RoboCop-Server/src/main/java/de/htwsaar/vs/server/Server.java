package de.htwsaar.vs.server;

import de.htwsaar.vs.gui.Gui;
import de.htwsaar.vs.server.graph.RoboGraph;

public class Server {
	
	Gui gui;
	
	RoboGraph roboGraph;
	
	public Server() {
		roboGraph = new RoboGraph(3, 3);
		System.out.println(roboGraph.toString());
	}
	
	private void addRobot(String robotId) {
		gui.addRobot(robotId);
		//roboGraph.addRobot(robotId) // needs to be implemented
		
	}

	public Server(Gui gui) {
		this();
		this.gui = gui;
	}
	
	public RoboGraph getRoboGraph() {
		return this.roboGraph;
	}
	
	public void setGui(Gui gui) {
		this.gui = gui;
		//Wird momentan hier nur zum testen hinzugefügt. die Roboter sollen später natürlich dynamisch hinzugefügt werden sobald sie sich mit dem Server verbinden
		addRobot("George");
	}
	
	
	public static void main(String[] args) {
        Server server = new Server();
   
    }
}

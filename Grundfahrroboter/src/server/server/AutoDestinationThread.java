package server.server;

public class AutoDestinationThread extends Thread {
	private String robotId;
	private Server server;
	
	public AutoDestinationThread(String robotId, Server server) {
		this.robotId = robotId;
		this.server = server;
	}
	@Override public void run() {
		while(!interrupted()) {
			String destination = server.generateRndDestination();
			server.driveRobotTo(robotId, destination);
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			server.addRobotTextMessage(robotId, "Ziel erreicht");
			
		}
	}
}

package server.server;

public class AutoDestinationThread extends Thread {
	private String robotId;
	private Server server;
	
	public AutoDestinationThread(String robotId, Server server) {
		this.robotId = robotId;
	}
	@Override public void run() {
		while(!interrupted()) {
			String destination = server.generateRndDestination();
			server.driveRobotTo(robotId, destination);
		}
	}
}

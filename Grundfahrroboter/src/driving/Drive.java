package driving;

import lejos.robotics.RegulatedMotor;

public class Drive extends Thread{
	private boolean stop;
	Driving drive;
	public Drive(RegulatedMotor b, RegulatedMotor c) {
		drive = new Driving(b, c);
	}
	
	public void run() {
		while(!stop) {}
		drive.stopDriving();
	}
		
	public void drive(int speed) {
		stop = false;
		drive.start(speed);
		start();
	}

	public void stopDriving() {
		stop = true;
	}
}

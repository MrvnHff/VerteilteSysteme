package driving;

import lejos.robotics.RegulatedMotor;

public class Drive {
	//private Driving drive;
	private RegulatedMotor b;
	private RegulatedMotor c;
	public Drive(RegulatedMotor b, RegulatedMotor c) {
		//drive = new Driving(b, c);
		this.b = b;
		this.c = c;
	}
		
	public void drive(int speed) {
		b.forward();
		c.forward();
		b.setSpeed(speed);
		c.setSpeed(speed);
	}

	public void stopDriving() {
		b.setSpeed(0);
		c.setSpeed(0);
	}
}

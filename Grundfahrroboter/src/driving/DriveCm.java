package driving;

import lejos.robotics.RegulatedMotor;
import logic.DegreeCm;

public class DriveCm {

	public static void driveCm(double cm, int speed, RegulatedMotor b, RegulatedMotor c, double diameter) {
		Driving drive = new Driving(b, c);
		System.out.println("Start");
		if (speed >= 0) {
			drive.setDirection(Driving.FORWARD);
			drive.start(speed);
			while (Math.abs(b.getTachoCount()) < Math.abs(DegreeCm.getDegree(cm, diameter))){
				if (b.getTachoCount() < c.getTachoCount()) {
					b.setSpeed(speed + 1);
					c.setSpeed(speed);
				} else if (b.getTachoCount() > c.getTachoCount()) {
					c.setSpeed(speed + 1);
					b.setSpeed(speed);
				} else {
					b.setSpeed(speed);
					c.setSpeed(speed);
			}
		}
		}else {
			drive.setDirection(Driving.BACKWARD);
			drive.start(speed);
			while (Math.abs(b.getTachoCount()) < Math.abs(DegreeCm.getDegree(cm, diameter))){
				if (b.getTachoCount() > c.getTachoCount()) {
					b.setSpeed(speed - 1);
					c.setSpeed(speed);
				} else if (b.getTachoCount() < c.getTachoCount()) {
					c.setSpeed(speed - 1);
					b.setSpeed(speed);
				} else {
					b.setSpeed(speed);
					c.setSpeed(speed);
			}
		}
	}
		
		
		
//		int minspeed = 5;
//		int s = 0;
//		int degree = Math.abs(DegreeCm.getDegree(cm, diameter));
//		if (cm < 0 ^ speed < 0) {
//			drive.setDirection(Driving.BACKWARD);
//		} else {
//			drive.setDirection(Driving.FORWARD);
//		}		
//		drive.start(minspeed);
//		while ((minspeed < Math.abs(speed)) && (Math.abs(b.getTachoCount()) < (degree / 2))) {
//			minspeed += 10;
//			drive.setSpeed(minspeed);
//			Delay.msDelay(5);			
//		}
//		s = Math.abs(b.getTachoCount());
//		while (Math.abs(b.getTachoCount()) < (degree - s)) {}
//		while () {
//			if (minspeed > 40) {
//				minspeed -= 10;
//				drive.setSpeed(minspeed);
//				Delay.msDelay(5);
//			}
//		}
		drive.stopDriving();
		drive = null;
		System.out.println("Ende");
	}
}

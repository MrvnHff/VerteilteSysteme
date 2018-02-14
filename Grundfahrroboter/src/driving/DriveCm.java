package driving;

import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import logic.DegreeCm;

public class DriveCm {

	public static void driveCm(double cm, int speed, RegulatedMotor b, RegulatedMotor c, double diameter) {
		Driving drive = new Driving(b, c);
		
		drive.start(speed);
		while (Math.abs(b.getTachoCount()) < Math.abs(DegreeCm.getDegree(cm, diameter))){
			
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
	}
}

package driving;

import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import logic.GradCm;

public class FahrenCm {

	public static void fahreCm(double cm, int speed, RegulatedMotor b, RegulatedMotor c, GradCm grcm) {
		Fahren drive = new Fahren(b, c);
		int minspeed = 5;
		int s = 0;
		int grad = Math.abs(grcm.getGrad(cm));
		if (cm < 0 ^ speed < 0) {
			drive.setDirection(Fahren.BACKWARD);
		} else {
			drive.setDirection(Fahren.FORWARD);
		}		
		drive.start(minspeed);
		while ((minspeed < Math.abs(speed)) && (Math.abs(b.getTachoCount()) < (grad / 2))) {
			minspeed += 10;
			drive.setSpeed(minspeed);
			Delay.msDelay(5);			
		}
		s = Math.abs(b.getTachoCount());
		while (Math.abs(b.getTachoCount()) < (grad - s)) {}
		while (Math.abs(b.getTachoCount()) < grad) {
			if (minspeed > 40) {
				minspeed -= 10;
				drive.setSpeed(minspeed);
				Delay.msDelay(5);
			}
		}
		drive.stopDrive();
		drive = null;
	}
}

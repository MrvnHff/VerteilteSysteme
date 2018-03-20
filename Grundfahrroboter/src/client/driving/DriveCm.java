package client.driving;

import client.logic.DegreeCm;
import lejos.robotics.RegulatedMotor;

/**
 * Die Klasse steuert das Fahren des Roboters für eine gewisse Strecke (cm).
 * @author Lennart Monir
 * @version 14.03.2018
 * @category Movement
 */

public class DriveCm {
	
	/**
	 * Die Methode benutzt ein Driving Objekt, um das Fahren zu steuern. Die Methode selbst überprüft parallel dazu,
	 * wie weit sich die Motoren schon gedreht haben, damit der Roboter nicht zu weit fährt.
	 * Die Methode kann sowohl das Vorwärts- als auch Rückwärtsfahren steuern.
	 * @param cm, die Strecke in cm
	 * @param speed, die Geschwindigkeit
	 * @param b, Motor B
	 * @param c, Motor C
	 * @param diameter, der Raddurchmesser
	 */
	public static void driveCm(double cm, int speed, RegulatedMotor b, RegulatedMotor c, double diameter) {
		Driving drive = new Driving(b, c);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
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
		drive.stopDriving();
		drive = null;
	}
}

package driving;

import lejos.robotics.RegulatedMotor;
import sensors.Gyrosensor;

/**
 * Die Klasse verwaltet Methoden, die die Steuerung vom Roboter �bernehmen, damit er eine Drehung ausf�hren kann.
 * @author Lennart Monir
 * @version 09.11.2017
 * @category Movement
 *
 */
public class Turn {	

	/**
	 * Die Methode benutzt ein Driving Obejkt, um eine Drehung auszuf�hren. Die Geschwindigkeit der Drehung ist abh�ngig von der bisher zur�ckgelegten Drehung in Grad.
	 * Je gr��er der zur�ckgelegte Winkel, desto langsamer dreht sch der Roboter. Das ist hilfreich, da der Robter so nicht �berdrehen kann.
	 * @param grad, der Winkel, um den sich der Roboter drehen soll.
	 * @param right, true = Rechtsdrehung, false = Linksdrehung
	 * @param b, Motor B
	 * @param c, Motor C
	 * @param gyro, Gyrosensor
	 */
	public static void turn(int grad, boolean right, RegulatedMotor b, RegulatedMotor c, Gyrosensor gyro) {
		Driving drive = new Driving(b, c);
		int speed = grad - Math.abs(gyro.getValue()) + 30;
		gyro.reset();
		if (right) {
			drive.setDirection(Driving.RIGHT);
		} else {
			drive.setDirection(Driving.LEFT);
		}
		drive.start(speed);
		while (grad - Math.abs(gyro.getValue()) > 1) {
			speed = grad - Math.abs(gyro.getValue()) + 50;
			System.out.println(gyro.getValue());
			drive.setSpeed(speed);
		}
		drive.stopDriving();
		System.out.println("Habe mich gedreht! " + gyro.getValue());
	}

}

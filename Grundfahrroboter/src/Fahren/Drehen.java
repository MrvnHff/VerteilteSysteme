package Fahren;

import Sensoren.Gyrosensor;
import lejos.robotics.RegulatedMotor;

/**
 * @author Lennart Monir
 * @version 09.11.2017
 * @category Movement
 *
 */
public class Drehen {	

	//Die Methode benutzt ein Fahren Objekt, um eine Drehung auszuf�hren. Die Geschwindigkeit der Drehung ist abh�ngig von dem bisher zur�ck gelegtem Winkels.
	//Je gr��er der zur�ck gelegte Winkel, desto langsammer die Drehung. Das macht die Drehung genauer.
	public static void drehen(int grad, boolean rechts, RegulatedMotor b, RegulatedMotor c, Gyrosensor gyro) {
		Fahren drive = new Fahren(b, c);
		int speed = grad - Math.abs(gyro.getMessung()) + 30;
		gyro.reset();
		if (rechts) {
			drive.setDirection(Fahren.RIGHT);
		} else {
			drive.setDirection(Fahren.LEFT);
		}
		drive.start(speed);
		while (grad - Math.abs(gyro.getMessung()) > 2) {
			speed = grad - Math.abs(gyro.getMessung()) + 30;
			drive.setSpeed(speed);
		}
		drive.stopDrive();
	}

}

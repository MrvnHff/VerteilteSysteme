package driving;

import lejos.robotics.RegulatedMotor;
import sensors.Gyrosensor;

/**
 * @author Lennart Monir
 * @version 09.11.2017
 * @category Movement
 *
 */
public class Turn {	

	//Die Methode benutzt ein Fahren Objekt, um eine Drehung auszuf�hren. Die Geschwindigkeit der Drehung ist abh�ngig von dem bisher zur�ck gelegtem Winkels.
	//Je gr��er der zur�ck gelegte Winkel, desto langsammer die Drehung. Das macht die Drehung genauer.
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
		while (grad - Math.abs(gyro.getValue()) > 2) {
			speed = grad - Math.abs(gyro.getValue()) + 30;
			drive.setSpeed(speed);
		}
		drive.stopDriving();
	}

}

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

	//Die Methode benutzt ein Fahren Objekt, um eine Drehung auszuführen. Die Geschwindigkeit der Drehung ist abhängig von dem bisher zurück gelegtem Winkels.
	//Je größer der zurück gelegte Winkel, desto langsammer die Drehung. Das macht die Drehung genauer.
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

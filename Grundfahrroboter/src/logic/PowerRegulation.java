package logic;

import lejos.robotics.RegulatedMotor;

/**
 * Die Klasse berechnet zu einem übergebenen Prozentwert eine Geschwindigkeit, die sich an der Akkuleistung orientiert.
 * @author Lennart
 * @version 10.10.2017 
 */
public class PowerRegulation {
	/**
	 * Errechnet zu übergebener Geschwindigkeit in Prozent die Motordrehung in Grad/Sekunde.
	 * @param speed, ein Wert zwischen -100 und 100.
	 * @return m, ein Motor, für den die Geschwindigkeit berechnet werden soll.
	 */
	public static int getSpeed(int speed, RegulatedMotor m) {
		int motorspeed = 0;
		if (Math.abs(speed) > 100) {
			motorspeed = Math.round(m.getMaxSpeed());
		}else {
			motorspeed = Math.round((m.getMaxSpeed()/100)*Math.abs(speed));
		}
		if (speed < 0) {
			motorspeed *= -1;
		}
		return motorspeed;
	}
}

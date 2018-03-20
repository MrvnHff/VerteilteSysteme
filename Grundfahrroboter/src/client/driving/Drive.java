package client.driving;

import lejos.robotics.RegulatedMotor;
/**
 * Die Klasse steuert das Fahren des Roboters für unbestimmte Zeit.
 * @author Lennart Monir
 * @version 14.03.2018
 * @category Movement
 */
public class Drive {
	private RegulatedMotor b;
	private RegulatedMotor c;
	
	/**
	 * Der Konstruktor bekommt die Referenzen auf zwei Motoren.
	 * @param b, Motor B
	 * @param c, Motor C
	 */
	public Drive(RegulatedMotor b, RegulatedMotor c) {
		this.b = b;
		this.c = c;
	}
	
	/**
	 * Die Methode lässt die Motoren bewegen und setzt ihre Geschwindigkeit.
	 * @param speed, die Geschwindigkeit
	 */
	public void drive(int speed) {
		b.forward();
		c.forward();
		b.setSpeed(speed);
		c.setSpeed(speed);
	}

	/**
	 * Die Methode hält die Motoren an.
	 */
	public void stopDriving() {
		b.setSpeed(0);
		c.setSpeed(0);
	}
}

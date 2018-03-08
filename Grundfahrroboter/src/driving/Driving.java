package driving;

import lejos.robotics.RegulatedMotor;

/**
 * Die Klasse erbt von der Threads-Klasse, da das Fahren als Parallel-Prozess angesehen wird.
 * Damit l�sst sich das Fahren abh�ngig machen von verschiedenen Prozessen, die dann von au�en Einfluss auf die Geschwindgkeit und das Fahrverhalten einnehmen k�nnen.
 * @author Lennart Monir
 * @version 09.11.2017
 * @category Movement
 */
public class Driving extends Thread{
	public final static char FORWARD = 'f';
	public final static char BACKWARD = 'b';
	public final static char LEFT = 'l';
	public final static char RIGHT = 'r';
	
	private RegulatedMotor b;
	private RegulatedMotor c;	
	private int speedB;
	private int speedC;
	private char direction;
	

	/**
	 * Der Konstruktor erstellt ein Objekt, das die Steurung der Motoren �berimmt.
	 * @param b
	 * @param c
	 */
	public Driving(RegulatedMotor b, RegulatedMotor c) {
		this.b = b;
		this.c = c;
		speedB = 0;
		speedC = 0;
		direction = FORWARD;
	}

	
	/**
	 * Die Methode run() von der Thread Klasse f�hrt das eigentliche Fahren des Roboters aus.
	 * Zuerst wird die Variable 'direction' ausgewertet, die angibt, ob der Roboter vorw�rts, r�ckw�rts, oder sich auf der Stelle links oder rechts herum drehen soll.
	 * Dann wird eine Endlosschleife (While) gestartet, die von au�en nur �ber die stop-Variable abgebrochen werden kann.
	 * Die Schleife steurt auf Wunsch, dass beide Motoren sich gleichm��ig viel drehen. Dadurch wird garantiert, dass der Roboter, ohne dass er von Sensoren gesteurt wird, relativ gerade gerade f�hrt.
	 * Darum wird dieser Modus auch in der Methode bei der Drehung automatisch ausgeschaltet.
	 */
	//TODO Regulierung auch f�r die Drehung einbauen!
	public void run() {
		b.resetTachoCount();
		c.resetTachoCount();
		b.setSpeed(speedB);
		c.setSpeed(speedC);
		if (direction == BACKWARD) {
			b.backward();
			c.backward();
		}else if (direction == RIGHT) {
			b.forward();
			c.backward();
		}else if (direction == LEFT) {
			b.backward();
			c.forward();
		}else{
			b.forward();
			c.forward();
		}
		//while (!isInterrupted()) {}
	}

	/**
	 * Startet die run() Methode. Beide Motoren bekommen die gleiche Geschwindigkeit.
	 * @param speed, die Geschwindigkeit
	 */
	public void start(int speed) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		this.speedB = speed;
		this.speedC = speed;
		b.resetTachoCount();
		c.resetTachoCount();
		b.setSpeed(speed);
		c.setSpeed(speed);
		start();
	}

	/**
	 * Startet die run() Methode. Beide Motoren bekommen unterschiedliche Geschwindigkeiten.
	 * Die Regulierung wird ausgeschaltet.
	 * @param speedB, Geschwindigkeit f�r Motor B
	 * @param speedC, Geschwindigkeit f�r Motor C
	 */
	public void start(int speedB, int speedC) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		this.speedB = speedB;
		this.speedC = speedC;
		b.resetTachoCount();
		c.resetTachoCount();
		b.setSpeed(speedB);
		c.setSpeed(speedC);
		start();
	}

	public void setSpeed(int speed) {
		this.speedB = speed;
		this.speedC = speed;
		b.setSpeed(speed);
		c.setSpeed(speed);
	}

	public void setSpeedB(int speed) {
		this.speedB = speed;
		b.setSpeed(speed);
	}

	public void setSpeedC(int speed) {
		this.speedC = speed;
		c.setSpeed(speed);
	}

	public int getSpeedB() {
		return speedB;
	}

	public int getSpeedC() {
		return speedC;
	}
	
	public void stopDriving() {		
		b.setSpeed(0);
		c.setSpeed(0);
		interrupt();
	}

	public void setDirection(char d) {
		direction = d;
	}
}

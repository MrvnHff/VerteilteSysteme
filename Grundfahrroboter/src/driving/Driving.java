package driving;

import lejos.robotics.RegulatedMotor;

/**
 * @author Lennart Monir
 * @version 09.11.2017
 * @category Movement
 * 
 * Die Klasse erbt von der Threads-Klasse, da das Fahren als Parallel-Prozess angesehen wird.
 * Damit lässt sich das Fahren abhängig machen von verschiedenen Prozessen, die dann von außen Einfluss auf die Geschwindgkeit und das Fahrverhalten einnehmen können.
 *
 */
public class Driving extends Thread {
	public static final char FORWARD = 'f';
	public static final char BACKWARD = 'b';
	public static final char LEFT = 'l';
	public static final char RIGHT = 'r';
	
	private RegulatedMotor b;
	private RegulatedMotor c;	
	private boolean stop = false;
	private int speedB = 0;
	private int speedC = 0;
	private char direction = FORWARD;
	private boolean regulate;
	

	public Driving(RegulatedMotor b, RegulatedMotor c) {
		this.b = b;
		this.c = c;
		this.regulate = true;
	}

	//
	public void run() {
		stop = false;
		b.resetTachoCount();
		c.resetTachoCount();
		b.setSpeed(speedB);
		c.setSpeed(speedC);
		if (direction == BACKWARD) {
			b.backward();
			c.backward();
		}else if (direction == LEFT) {
			b.forward();
			c.backward();
			regulate = false;
		}else if (direction == RIGHT) {
			b.backward();
			c.forward();
			regulate = false;
		}else{
			b.forward();
			c.forward();
		}
		while (!stop) {
			if (regulate) {
				if (b.getTachoCount() < c.getTachoCount()) {
					b.setSpeed(speedB + 1);
					c.setSpeed(speedC);
				} else if (b.getTachoCount() > c.getTachoCount()) {
					c.setSpeed(speedC + 1);
					b.setSpeed(speedB);
				} else {
					b.setSpeed(speedB);
					c.setSpeed(speedC);
				}
			}
		}
		b.setSpeed(0);
		c.setSpeed(0);		
	}

	public void start(int speed) {
		this.speedB = speed;
		this.speedC = speed;
		b.resetTachoCount();
		c.resetTachoCount();
		start();
	}

	public void start(int speedB, int speedC) {
		this.speedB = speedB;
		this.speedC = speedC;
		b.resetTachoCount();
		c.resetTachoCount();
		start();
	}

	public void setSpeed(int speed) {
		this.speedB = speed;
		this.speedC = speed;
	}

	public void setSpeedB(int speed) {
		this.speedB = speed;
	}

	public void setSpeedC(int speed) {
		this.speedC = speed;
	}

	public int getSpeedB() {
		return speedB;
	}

	public int getSpeedC() {
		return speedC;
	}

	public void setRegulation(boolean regulate) {
		this.regulate = regulate;
	}
	
	public boolean getRegulation() {
		return regulate;
	}
	
	public void stopDriving() {
		stop = true;
	}

	public void setDirection(char d) {
		direction = d;
	}
}

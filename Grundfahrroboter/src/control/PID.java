package control;

import Regler.DRegler;
import Regler.IRegler;
import Regler.PRegler;
import driving.Driving;
import lejos.robotics.RegulatedMotor;
import sensors.DefaultSensor;

public class PID extends Thread {	
	private Driving drive;
	private int average;
	private int speed;
	private DefaultSensor s;
	private PRegler p;
	private IRegler i;
	private DRegler d;
	private boolean stop;

	public PID(int average, DefaultSensor s, double kp, double ki, double kd,RegulatedMotor b, RegulatedMotor c) {		
		drive = new Driving(b, c);
		this.average = average;
		this.s = s;
		p = new PRegler(kp);
		i = new IRegler(ki, average);
		d = new DRegler(kd);
	}

	public void run() {
		stop = false;
		while (!stop) {
			int diff = average - s.getValue();
			double control = Math.round(p.regelP(diff) + i.regelI(diff) + d.regelD(diff));
			drive.setSpeedB((int) (speed + control));
			drive.setSpeedC((int) (speed - control));
			System.out.println(control);
		}
	}

	public void drivePID(int speed) {
		this.speed = speed;
		drive.setDirection(drive.FORWARD);
		drive.start(speed);
		start();			
	}
	
	public void stopPID() {
		drive.stopDriving();
		stop = true;
	}
}

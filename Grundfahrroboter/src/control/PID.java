package control;

import Regler.DRegler;
import Regler.IRegler;
import Regler.PRegler;
import driving.Fahren;
import lejos.robotics.RegulatedMotor;
import sensors.StandartSensor;

public class PID extends Thread {	
	private Fahren drive;
	private int mittelwert;
	private int geschwindigkeit;
	private StandartSensor s;
	private PRegler p;
	private IRegler i;
	private DRegler d;
	private boolean stop;

	public PID(int mittelwert, StandartSensor s, double kp, double ki, double kd,RegulatedMotor b, RegulatedMotor c) {		
		drive = new Fahren(b, c);
		this.mittelwert = mittelwert;
		this.s = s;
		p = new PRegler(kp);
		i = new IRegler(ki, mittelwert);
		d = new DRegler(kd);
	}

	public void run() {
		stop = false;
		while (!stop) {
			int diff = mittelwert - s.getMessung();
			double regelung = Math.round(p.regelP(diff) + i.regelI(diff) + d.regelD(diff));
			drive.setSpeedB((int) (geschwindigkeit + regelung));
			drive.setSpeedC((int) (geschwindigkeit - regelung));
			System.out.println(regelung);
		}
	}

	public void drivePID(int geschwindigkeit) {
		this.geschwindigkeit = geschwindigkeit;
		drive.setDirection('f');
		drive.start(geschwindigkeit);
		start();			
	}
	
	public void stopPID() {
		drive.stopDrive();
		stop = true;
	}
}

package control;

import control.DRegler;
import control.IRegler;
import control.PRegler;
import driving.Driving;
import lejos.robotics.RegulatedMotor;
import sensors.DefaultSensor;

/**
 * Die Klasse PID (Proportional, Integral, Differential) verwaltet einen PID-Regler.
 * Ein PID-Regler ist ein in der Steuerungs- und Regeltechnik weit verbreiteter Regelmechanismus, der einer Steuerungseinheit helfen soll,
 * schnell einen Zielwert zu erreichen. Die Technik soll dem Roboter helfen, dass er mit nur einem Lichtsensor einer Linie folgen kann.
 * Und das möglichst so, dass der Roboter möglichst keine Wellen fährt und die Linie nicht verliert.
 * Mit dem PID-Regler fährt der Roboter auch nicht direkt auf einer Linie, sondern genau auf der Kante der Linie und ihrer benachbarten Ebene.
 * Dabei sollte der Unterschied zwischen den Farbwerten groß genug sein, damit der Roboter klar eine Kante erkennen kann.
 * @author Lennart Monir
 * @version 0.1
 *
 */
public class PID extends Thread {	
	private Driving drive;
	private int average;
	private int speed;
	private DefaultSensor s;
	private PRegler p;
	private IRegler i;
	private DRegler d;
	private boolean stop;

	/**
	 * Der Konstruktor initialisiert einen PID
	 * @param average, der Mittelwert, dem der Roboter folgen soll. Besitzt die Linie einen Farbwert von 0 und die Ebene daneben einen von 100,
	 * so ist der Mittelwert der Kante 50.
	 * @param s, der zu verwendene Sensor {@link sensors.DefaultSensor}
	 * @param kp, der Regelwert, der den P-Anteil steuert.
	 * @param ki, der Regelwert, der den I-Anteil steuert
	 * @param kd, der Regelwert, der den D-Anteil steuert
	 * @param b, Motor B
	 * @param c, Motor C
	 */
	public PID(int average, DefaultSensor s, double kp, double ki, double kd,RegulatedMotor b, RegulatedMotor c) {		
		drive = new Driving(b, c);
		this.average = average;
		this.s = s;
		p = new PRegler(kp);
		i = new IRegler(ki, average);
		d = new DRegler(kd);
	}

	/** 
	 * Die Methode run() startet einen Thread. Dadurch das der PID-Regler in einem separaten Thread läuft, ist es möglich, dass der PID-Regler
	 * durch andere Prozesse von außen beeinflusst werden kann. Zum Beispiel kann ein anderer Sensor so ein Signal geben, dass der PID-Regler stoppt.
	 * Die Methode berechnet die Differenz zwischen dem Sensor und dem Mittelwert und übergibt diesen an die einzelnen Regler und lässt sich den Regelanteil geben.
	 * Die Regelanteile werden aufaddiert und mit der Geschwindigkeit summiert und ergeben für jeden Motor eine neue Geschwindigkeit.
	 */
	public void run() {
		stop = false;
		double px,ix,dx;
		while (!stop) {
			int diff = average - s.getValue();
			px = p.regelP(diff);
			ix = i.regelI(diff);
			dx = d.regelD(diff);
			double control = Math.round(px+ix+dx);
			drive.setSpeedB((int) (speed - control));
			drive.setSpeedC((int) (speed + control));
		}
	}

	/**
	 * Die Methode startete die run() Methode, die einen Thread startet.
	 * Sie setzt die Geschwindigkeit der Motoren des {@link driving.Driving}-Objektes und startet seinen Thread.
	 * Erst dann wird der PID-Regler gestartet.
	 * @param speed
	 */
	public void drivePID(int speed) {
		this.speed = speed;
		drive.setDirection(Driving.FORWARD);
		drive.start(speed);
		start();			
	}
	
	public void stopPID() {
		drive.stopDriving();
		stop = true;
	}
	
	public void setPID(double kp, double ki, double kd) {
		p.setP(kp);
		i.setI(ki);
		d.setD(kd);
	}
}

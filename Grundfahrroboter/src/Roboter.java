import client.Client;
import control.PID;
import driving.Turn;
import driving.DriveCm;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import logic.DegreeCm;
import sensors.Gyrosensor;
import sensors.Lightsensor;
import wait.WaitFor;

/**
 * Die Roboter-Klasse vereint alle Eigenschaften und Klassen des Roboters. Sie soll ein digitales Abbild des Roboters darstellen und alle Funktionen zur Verfügung stellen,
 * die der Roboter besitzt.
 * @author Lennart Monir
 * @version 0.1 *
 */
public class Roboter {
	private double diameter;
	private PID pidLight;
	private PID pidGyro;
	private RegulatedMotor b;
	private RegulatedMotor c;
	private Lightsensor light1;
	private Gyrosensor gyro;
	private Client client;
		
	/**
	 * Der Konstruktor initialisiert einen Client, um mit einem Server zu komunizieren, meldet zwei Motoren an ihren Ports B und C an, meldet einen Lichtsensor am Port
	 * S1 und einen Gyrosensor am Port S3 an.
	 * Ebenso werdem dem Roboter zwei PID-Regler für den Lichtsensor und den Gyrosensor zur Verfügung gestellt.
	 * @param diameter, der Durchmesser der Reifen des Roboters zum Zeitpunkt des Aufrufs des Klassenobjektes.
	 */
	public Roboter (double diameter) {
		client = new Client("192.168.178.24", 6000);
		this.setDiameter(diameter);
		b = new EV3LargeRegulatedMotor(MotorPort.B);
		c = new EV3LargeRegulatedMotor(MotorPort.C);
		light1 = new Lightsensor(1);
		gyro = new Gyrosensor(3);
		pidLight = new PID(50, light1, 0.5, 0.2, 0.8, b, c);
		pidGyro = new PID(0, gyro, 0.5, 0.2, 0.8, b, c);		
	}
	
	public void pidLightCm(int speed, double cm) {
		pidLight.drivePID(speed);
		WaitFor.Degree(b, DegreeCm.getDegree(cm, diameter), ">=");
		pidLight.stopPID();
	}
	
	public void pidGyroCm(int speed, double cm) {
		pidGyro.drivePID(speed);
		WaitFor.Degree(b, DegreeCm.getDegree(cm, diameter), ">=");
		pidGyro.stopPID();
	}
	
	public void driveCm(double cm, int speed) {
		DriveCm.driveCm(cm, speed, b, c, diameter);
	}
		
	public void turn(int degree, boolean right) {
		Turn.turn(degree, right, b, c, gyro);
	}

	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}
	
	public void sendeServer(String anfrage) {
		client.sendRequest(anfrage);
	}
}

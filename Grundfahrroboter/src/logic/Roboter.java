package logic;
import client.Client;
import control.PID;
import driving.Turn;
import driving.Drive;
import driving.DriveCm;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import sensors.Gyrosensor;
import sensors.Lightsensor;
import wait.WaitFor;

/**
 * Die Roboter-Klasse vereint alle Eigenschaften und Klassen des Roboters. Sie soll ein digitales Abbild des Roboters darstellen und alle Funktionen zur Verf�gung stellen,
 * die der Roboter besitzt.
 * @author Lennart Monir
 * @version 0.1 *
 */
public class Roboter {
	private double diameter;
	private PID pidLight;
	private PID pidGyro;
	private Drive drive;
	private RegulatedMotor b;
	private RegulatedMotor c;
	private Lightsensor light1;
	private Gyrosensor gyro;
	//private Client client;
		
	/**
	 * Der Konstruktor initialisiert einen Client, um mit einem Server zu komunizieren, meldet zwei Motoren an ihren Ports B und C an, meldet einen Lichtsensor am Port
	 * S1 und einen Gyrosensor am Port S3 an.
	 * Ebenso werdem dem Roboter zwei PID-Regler f�r den Lichtsensor und den Gyrosensor zur Verf�gung gestellt.
	 * @param diameter, der Durchmesser der Reifen des Roboters zum Zeitpunkt des Aufrufs des Klassenobjektes.
	 */
	public Roboter (double diameter) {
		//client = new Client("192.168.178.24", 6000);
		this.setDiameter(diameter);
		b = new EV3LargeRegulatedMotor(MotorPort.B);
		c = new EV3LargeRegulatedMotor(MotorPort.C);
		light1 = new Lightsensor(1);
		gyro = new Gyrosensor(3);
		pidLight = new PID(50, light1, 2, 25, 20, b, c);
		pidGyro = new PID(0, gyro, 0.5, 0.2, 0.8, b, c);
		drive = new Drive(b, c);
	}
	
	public void pidLightCm(int speed, double cm) {
		pidLight.drivePID(PowerRegulation.getSpeed(speed, b));
		WaitFor.Degree(b, DegreeCm.getDegree(cm, diameter), ">=");
		pidLight.stopPID();
	}
	
	public void pidGyroCm(int speed, double cm) {
		pidGyro.drivePID(PowerRegulation.getSpeed(speed, b));
		WaitFor.Degree(b, DegreeCm.getDegree(cm, diameter), ">=");
		pidGyro.stopPID();
	}
	
	public void driveCm(double cm, int speed) {
		DriveCm.driveCm(cm, PowerRegulation.getSpeed(speed, b), b, c, diameter);
	}
	
	public void drive(int speed) {
		drive.drive(PowerRegulation.getSpeed(speed, b));
	}
	public void stopDrive() {
		drive.stopDriving();
	}
	
	public void driveUntilLight(int speed, int lightlvl, String compare) {
		drive.drive(PowerRegulation.getSpeed(speed, b));
		WaitFor.Sensor(light1, lightlvl, compare);
		drive.stopDriving();
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
	
	public void hardGyroReset() {
		gyro.resetHard();
	}
}

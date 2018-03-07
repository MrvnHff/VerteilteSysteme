package logic;
import Exceptions.RobotException;
import control.PID;
import driving.Turn;
import driving.Drive;
import driving.DriveCm;
import driving.Driving;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import sensors.Gyrosensor;
import sensors.Lightsensor;
import wait.WaitFor;
import lejos.utility.Delay;

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
	private Driving drive;
	private RegulatedMotor b;
	private RegulatedMotor c;
	private Lightsensor light1;
	private Gyrosensor gyro;
	//private Client client;
	
	private String status;
	private String error;
		
	/**
	 * Der Konstruktor initialisiert einen Client, um mit einem Server zu komunizieren, meldet zwei Motoren an ihren Ports B und C an, meldet einen Lichtsensor am Port
	 * S1 und einen Gyrosensor am Port S3 an.
	 * Ebenso werdem dem Roboter zwei PID-Regler für den Lichtsensor und den Gyrosensor zur Verfügung gestellt.
	 * @param diameter, der Durchmesser der Reifen des Roboters zum Zeitpunkt des Aufrufs des Klassenobjektes.
	 * @throws RobotException 
	 */
	public Roboter (double diameter, double p, double i, double d) throws RobotException {
		try {
			this.setDiameter(diameter);
			b = new EV3LargeRegulatedMotor(MotorPort.B);
			c = new EV3LargeRegulatedMotor(MotorPort.C);
			drive = new Driving(b, c);
			light1 = new Lightsensor(1);
			gyro = new Gyrosensor(3);
			pidLight = new PID(50, light1, p, i, d, b, c, drive);
			pidGyro = new PID(0, gyro, 0.5, 0.2, 0.8, b, c, drive);			
			status = "Roboter initialisiert!";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
	}
	
	public void setPID(double kp, double ki, double kd) {
		pidLight.setPID(kp, ki, kd);
	}
	
	public void pidLightCm(int speed, double cm) throws RobotException {
		try {
			pidLight.drivePID(PowerRegulation.getSpeed(speed, b));
			WaitFor.Degree(b, DegreeCm.getDegree(cm, diameter), ">=");
			pidLight.stopPID();
			status = "PID Licht korrekt ausgeführt!";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
	}
	
	public void pidGyroCm(int speed, double cm) throws RobotException {
		try {
			pidGyro.drivePID(PowerRegulation.getSpeed(speed, b));
			WaitFor.Degree(b, DegreeCm.getDegree(cm, diameter), ">=");
			pidGyro.stopPID();
			status = "PID Gyro korrekt ausgeführt!";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
	}
	
	public void driveCm(double cm, int speed) throws RobotException {
		try {
			DriveCm.driveCm(cm, PowerRegulation.getSpeed(speed, b), b, c, diameter);
			status = "Fahre_cm korrekt ausgeführt!";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
	}
	
	public void drive(int speed) throws RobotException {
		try {
			drive.start((PowerRegulation.getSpeed(speed, b)));
			status = "Fahre!";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
	}
	public void stopDrive() throws RobotException {
		try {
			drive.stopDriving();
			status = "Habe angehalten";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
	}
	
	public void driveUntilLight(int speed, int lightlvl, String compare) throws RobotException {
		try {
			Driving drive = new Driving(b, c);
			drive.start(PowerRegulation.getSpeed(speed, b));
			WaitFor.Sensor(light1, lightlvl, compare);
			drive.stopDriving();
			status = "Habe den Lichtwert " + lightlvl + " gefunden!";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
	}
		
	public void turn(int degree, boolean right) throws RobotException {
		try {
			Turn.turn(degree, right, b, c, gyro);
			status = "Habe mich gedreht!";
			error = "Kein Fehler!";
		} catch (Exception e) {
			error = e.toString();
			throw new RobotException(error);
		}
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
	
	public String getStatus() {
		return status;
	}
	
	public String getError() {
		return error;
	}
	
	public void waitMs(long ms) {
		Delay.msDelay(ms);
	}
}

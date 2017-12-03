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

public class Roboter {
	private double diameter;
	private PID pidLight;
	private PID pidGyro;
	private DegreeCm grcm;
	private RegulatedMotor b;
	private RegulatedMotor c;
	private Lightsensor light1;
	private Gyrosensor gyro;
	private Client client;
		
	public Roboter (double diameter) {
		client = new Client("192.168.178.24", 6000);
		this.setDiameter(diameter);
		b = new EV3LargeRegulatedMotor(MotorPort.B);
		c = new EV3LargeRegulatedMotor(MotorPort.C);
		light1 = new Lightsensor(1);
		gyro = new Gyrosensor(3);
		pidLight = new PID(50, light1, 0.5, 0.2, 0.8, b, c);
		pidGyro = new PID(0, gyro, 0.5, 0.2, 0.8, b, c);
		grcm = new DegreeCm(diameter);
	}
	
	public void pidLightCm(int speed, double cm) {
		pidLight.drivePID(speed);
		WaitFor.Degree(b, grcm.getDegree(cm), ">=");
		pidLight.stopPID();
	}
	
	public void pidGyroCm(int speed, double cm) {
		pidGyro.drivePID(speed);
		WaitFor.Degree(b, grcm.getDegree(cm), ">=");
		pidGyro.stopPID();
	}
	
	public void driveCm(double cm, int speed) {
		DriveCm.driveCm(cm, speed, b, c, grcm);
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

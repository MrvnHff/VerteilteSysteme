import Berechnung.GradCm;
import Fahren.Drehen;
import Fahren.FahrenCm;
import Regler.PID;
import Sensoren.Gyrosensor;
import Sensoren.Lichtsensor;
import ServerClient.Client;
import Warten.WartenAuf;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Roboter {
	private double durchmesser;
	private PID pidLicht;
	private PID pidGyro;
	private GradCm grcm;
	private RegulatedMotor b;
	private RegulatedMotor c;
	private Lichtsensor licht1;
	private Gyrosensor gyro;
	private Client client;
		
	public Roboter (double durchmesser) {
		client = new Client("192.168.178.24", 6000);
		this.setDurchmesser(durchmesser);
		b = new EV3LargeRegulatedMotor(MotorPort.B);
		c = new EV3LargeRegulatedMotor(MotorPort.C);
		licht1 = new Lichtsensor(1);
		gyro = new Gyrosensor(3);
		pidLicht = new PID(50, licht1, 0.5, 0.2, 0.8, b, c);
		pidGyro = new PID(0, gyro, 0.5, 0.2, 0.8, b, c);
		grcm = new GradCm(durchmesser);
	}
	
	public void pidLichtCm(int geschwindigkeit, double cm) {
		pidLicht.drivePID(geschwindigkeit);
		WartenAuf.Grad(b, grcm.getGrad(cm), ">=");
		pidLicht.stopPID();
	}
	
	public void pidGyroCm(int geschwindigkeit, double cm) {
		pidGyro.drivePID(geschwindigkeit);
		WartenAuf.Grad(b, grcm.getGrad(cm), ">=");
		pidGyro.stopPID();
	}
	
	public void fahreCm(double cm, int speed) {
		FahrenCm.fahreCm(cm, speed, b, c, grcm);
	}
		
	public void drehen(int grad, boolean rechts) {
		Drehen.drehen(grad, rechts, b, c, gyro);
	}

	public double getDurchmesser() {
		return durchmesser;
	}

	public void setDurchmesser(double durchmesser) {
		this.durchmesser = durchmesser;
	}
	
	public void sendeServer(String anfrage) {
		client.sendeAnfrage(anfrage);
	}
}

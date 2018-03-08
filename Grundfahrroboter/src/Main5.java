import control.PID;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import sensors.Lightsensor;
import wait.WaitFor;

public class Main5 {	
	public static void main(String[] args) {
		RegulatedMotor b;
		RegulatedMotor c;	
		b = new EV3LargeRegulatedMotor(MotorPort.B);
		c = new EV3LargeRegulatedMotor(MotorPort.C);
		Lightsensor licht = new Lightsensor(1);
		PID pid = new PID(50, licht, 0.5, 0.2, 0.8, b, c); 
		pid.drivePID(150);
		WaitFor.Degree(b, 1500, ">=");
		pid.stopPID();
		System.exit(0);
	}

}

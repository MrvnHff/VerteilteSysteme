import control.PID;
import lejos.hardware.Audio;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import sensors.Lightsensor;
import wait.WaitFor;

public class Main {

	public static void main(String[] args) {
		LED led = LocalEV3.get().getLED();
		//Roboter robo = new Roboter(49.4);
		led.setPattern(6);
		RegulatedMotor b;
		RegulatedMotor c;	
		b = new EV3LargeRegulatedMotor(MotorPort.B);
		c = new EV3LargeRegulatedMotor(MotorPort.C);
		Lightsensor licht = new Lightsensor(1);
		PID pid = new PID(50, licht, 1.5, 25, 20, b, c);
		Audio audio = LocalEV3.get().getAudio();
		audio.setVolume(20);
		audio.playTone(1200, 100);
		led.setPattern(7);
		pid.drivePID(200);
		WaitFor.Degree(b, 2000, ">=");
		pid.stopPID();
		System.exit(0);
	}
}

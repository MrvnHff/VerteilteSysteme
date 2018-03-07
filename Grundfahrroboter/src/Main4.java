
import Exceptions.RobotException;
import lejos.hardware.Audio;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.utility.Delay;
import logic.Roboter;

public class Main4 {	
	public static void main(String[] args) throws RobotException {
		LED led = LocalEV3.get().getLED();
		Roboter robo = new Roboter(59, 1.2,15,10);
		led.setPattern(6);
		
		Audio audio = LocalEV3.get().getAudio();
		audio.setVolume(20);
		audio.playTone(1200, 100);
		led.setPattern(7);

		robo.driveUntilLight(20, 10, "<=");
		robo.driveCm(9.5, 20);
		
		for (int i = 0; i < 4; i++) {		
		robo.turn(90, false);
		robo.driveCm(2, 20);
		robo.pidLightCm(20, 68);
		robo.driveCm(6, 20);
		robo.driveUntilLight(20, 10, "<=");
		robo.driveCm(9.5, 20);
		}

		
		System.exit(0);
	}

}

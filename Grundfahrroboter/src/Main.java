import client.Client;
import fileSystem.FileSystem;
import lejos.hardware.Audio;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		LED led = LocalEV3.get().getLED();
		Roboter robo = new Roboter(49.4);
		led.setPattern(7);

		Audio audio = LocalEV3.get().getAudio();
		audio.setVolume(20);
		audio.playTone(1200, 100);
		// robo.sendeServer("Hallo Welt!");
		// Delay.msDelay(10000);
		//robo.fahreCm(30, 700);
		//robo.drehen(60, true);
		//robo.fahreCm(30, 700);
		//robo.drehen(50, false);
		//robo.fahreCm(30, 500);
		//robo.drehen(45, false);
		// robo.folgeCm(25, 2, 200, 45);
		
		FileSystem.createDir("CSV");
		String arr[][] = new String[500][500];
		
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = ""+(i*j);
			}			
		}
		
		FileSystem.saveAsCSV("CSV/test", arr);
		
		Client client = new Client("192.168.178.24", 6000);
		FileSystem.sendFileToServer("CSV/test.csv", client);
	}
}

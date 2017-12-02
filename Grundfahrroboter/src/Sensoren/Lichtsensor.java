package Sensoren;
import lejos.hardware.sensor.EV3ColorSensor;

public class Lichtsensor extends StandartSensor{
	private EV3ColorSensor light;
		
	public Lichtsensor(int port) {
		super(port);
		light = new EV3ColorSensor(p);
		provider = light.getRedMode();
		sample = new float[provider.sampleSize()];
	}
	
	@Override
	public int getMessung() {
		provider.fetchSample(sample, 0);
		return Math.round(sample[0]*100);
	}
		
}

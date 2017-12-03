package sensors;
import sensors.DefaultSensor;
import lejos.hardware.sensor.EV3ColorSensor;

public class Lightsensor extends DefaultSensor{
	private EV3ColorSensor light;
		
	public Lightsensor(int port) {
		super(port);
		light = new EV3ColorSensor(p);
		provider = light.getRedMode();
		sample = new float[provider.sampleSize()];
	}
	
	@Override
	public int getValue() {
		provider.fetchSample(sample, 0);
		return Math.round(sample[0]*100);
	}
		
}

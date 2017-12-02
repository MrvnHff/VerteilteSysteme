package Sensoren;
import lejos.hardware.sensor.EV3GyroSensor;

public class Gyrosensor extends StandartSensor{
	private EV3GyroSensor gyro;
	private float GyroGrad = 0;
	private float grad = 0;
		
	public Gyrosensor(int port) {
		super(port);
		gyro = new EV3GyroSensor(p);
		provider = gyro.getAngleMode();
		sample = new float[provider.sampleSize()];
	}
	
	@Override
	public int getMessung() {
		provider.fetchSample(sample, 0);
		grad = grad + (GyroGrad-sample[0]);
		GyroGrad = sample[0];
		return (int)Math.round(grad) ;
	}
	
	public void reset() {
		grad = 0;
	}
	public void resetHard() {
		grad = 0;
		GyroGrad = 0;
		gyro.reset();
	}
}
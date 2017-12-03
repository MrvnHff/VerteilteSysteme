package sensors;
import sensors.DefaultSensor;
import lejos.hardware.sensor.EV3GyroSensor;

public class Gyrosensor extends DefaultSensor{
	private EV3GyroSensor gyro;
	private float gyroGrad = 0;
	private float grad = 0;
		
	public Gyrosensor(int port) {
		super(port);
		gyro = new EV3GyroSensor(p);
		provider = gyro.getAngleMode();
		sample = new float[provider.sampleSize()];
	}
	
	@Override
	public int getValue() {
		provider.fetchSample(sample, 0);
		grad = grad + (gyroGrad-sample[0]);
		gyroGrad = sample[0];
		return (int)Math.round(grad) ;
	}
	
	public void reset() {
		grad = 0;
	}
	public void resetHard() {
		grad = 0;
		gyroGrad = 0;
		gyro.reset();
	}
}
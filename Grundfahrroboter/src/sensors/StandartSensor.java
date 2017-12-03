package sensors;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

public class StandartSensor {

	protected SampleProvider provider;
	protected float[] sample;
	public Port p;

	public StandartSensor(int port) {
		setP(port);
	}
	
	public int getMessung() {
		return 0;
	}
	
	public SampleProvider getProvider() {
		return provider;
	}

	public void setProvider(SampleProvider provider) {
		this.provider = provider;
	}

	public float[] getSample() {
		return sample;
	}

	public void setSample(float[] sample) {
		this.sample = sample;
	}

	public Port getP() {
		return p;
	}

	public void setP(int port) {
		this.p = LocalEV3.get().getPort("S" + port);
		
	}
}

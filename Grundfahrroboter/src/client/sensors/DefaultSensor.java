package client.sensors;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

/**
 * Die Klasse Default Sensor implementiert alle notwendigen Eigenschaften, die der Roboter benötigt, um die Daten eines Sensors auszulesen.
 * 
 * @author Lennart Monir
 * @version 0.1
 */
public class DefaultSensor {

	protected SampleProvider provider; //Der Sample Provider liefert die Rohdaten eines Sensors.
	protected float[] sample;  //Das sample-Array beinhaltet alle Rohdaten eines Sampels, das der Sample Provider liefert
	public Port p;

	/**
	 * Der Standart Konstruktor DefaultSensor
	 * @param port Int von 1-4 erlaubt
	 */
	public DefaultSensor(int port) {
		setP(port);
	}
		
	public int getValue() {
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

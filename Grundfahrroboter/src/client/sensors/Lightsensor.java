package sensors;
import sensors.DefaultSensor;
import lejos.hardware.sensor.EV3ColorSensor;

/**
 * Die Klasse Lightsensor verwaltet einen Lichtsensor und erbt von der Klasse {@link sensors.DefaultSensor}
 * @author Lennart Monir
 * @version 0.1
 *
 */

public class Lightsensor extends DefaultSensor{
	private EV3ColorSensor light;

	/**
	 * Der Standart Konstruktor meldet einen neuen Lichtsensor an einem Port an und sagt dem Sample Provider,
	 * dass er die Daten vom Lichtsensors mittels des Modus RedMode erfragen soll.
	 * @param port ist der Anschluss, an dem ein Sensor angemeldet werden soll (1-4). ACHTUNG: Es kann auch Softwaretechnisch auch immer nur ein Sensor zur Zeit an den Port angeschlossen sein.
	 */		
	public Lightsensor(int port) {
		super(port);
		light = new EV3ColorSensor(p);
		provider = light.getRedMode();
		sample = new float[provider.sampleSize()];
	}
	
	/**
	 * Die Methode getValue liefert einen Wert >=0 und <=100, da in einem Sample des Lichtsensors nur Werte zwischen 0 und 1 stehen.
	 * Um diese Werte leichter zu verarbeiten, ist es hilfreich diese mit 100 zu multiplizieren. So kann man auch immer von Lichtst�rke in Prozent sprechen.
	 * Beim sample wird der Wert an der Stelle 0 gew�hlt, da das Sample des Lichtsensors drei Felder gro� ist. [R|G|B]!!!
	 * @return Int reflektierte Lichtst�rke
	 */
	@Override
	public int getValue() {
		provider.fetchSample(sample, 0);
		return Math.round(sample[0]*100);
	}
	
	//TODO Lichtsensor auch f�r den Farbmodus und f�r alle drei Ferben (RGB) implementieren. Evtl. noch f�r das Umgebungslicht. 
		
}

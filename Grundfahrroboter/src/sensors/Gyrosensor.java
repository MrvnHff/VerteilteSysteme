package sensors;
import sensors.DefaultSensor;
import lejos.hardware.sensor.EV3GyroSensor;

/**
 * Die Klasse Gyrosensor verwaltet einen Gyrosensor und erbt von der Klasse {@link sensors.DefaultSensor}
 * @author Lennart Monir
 * @version 0.1
 *
 */

public class Gyrosensor extends DefaultSensor{
	private EV3GyroSensor gyro;
	private float gyroGrad = 0;
	private float grad = 0;
		
	
	/**
	 * Der Standart Konstruktor meldet einen neuen Gyrosensor an einem Port an und sagt dem Sample Provider,
	 * dass er die Daten vom Gyrosensor mittels des Modus AngelMode erfragen soll.
	 * @param port ist der Anschluss, an dem ein Sensor angemeldet werden soll (1-4). ACHTUNG: Es kann auch Softwaretechnisch auch immer nur ein Sensor zur Zeit an den Port angeschlossen sein.
	 */
	public Gyrosensor(int port) {
		super(port);
		gyro = new EV3GyroSensor(p);
		provider = gyro.getAngleMode();
		sample = new float[provider.sampleSize()];
	}
	
	/**
	 * Die Methode getValue() liefert einen Int Wert aus einem vom Provider erzeugtem Sampel, das der Sensor generiert hat.
	 * Das Problem des Gyrosensors ist es, dass seine interne Methode {@link lejos.hardware.sensor.EV3GyroSensor#reset()} nicht nur den Zähler der Gradzahl zurücksetzt,
	 * sondern gleich einen ganzen Hardwarereset durchführt. Das hat zur Folge, dass der Roboter jedes mal zwei Sekunden nichts machen kann.
	 * Darum bildet getValue() die Gradzahl in einer neuen Variable ab, die sich über die Differenz eines Samples und der zuvor gemesenen Gradzahl eines Samples bildet.
	 * Und dieser Wert lässt sich dann ganz einfach auf Null zurück setzen, ohne dass der ganze Sensor resetet wird.
	 * @return Int grad, der Winkel, der objektiv gemessen wurde.
	 */ 
	@Override
	public int getValue() {
		provider.fetchSample(sample, 0);		//Der Provider ließt über den Modus ein Sample aus und schreibt es in das sample-Array beginnend ab der Stelle 0.
		grad = grad + (gyroGrad-sample[0]);		//grad bildet den gleichen Wert ab wie der Wert, der im Sample steht. Allerdings wird die Differenz auf grad draufgerechnet.
		gyroGrad = sample[0];					//In gyroGrad wird der aktuelle Wert gespeichert, der dann beim nächsten Aufruf von getValue() zur Differenzbildung herangezogen wird.
		return (int)Math.round(grad) ;			//Rückgabe der grad-Variable nach Rundung und Konvertierung von Float nach Int.
	}
	
	/**
	 * reset() setzt NUR die Variable grad zurück auf 0. Somit wird nicht der ganze Sensor in der Hardware intern zurückgesetzt.
	 * Dadurch kommt es zu keinen Verzögerungen beim Fahren.
	 */
	public void reset() {
		grad = 0;
	}	
	
	/**
	 * resetHard() setzt sowohl die Variablen als auch den ganzen Sensor zurück. Dadurch kommt es zu einem Hardwarereset, der dafür sorgt,
	 * dass der Roboter in der Zeit nichts machen kann (ca. 1-2 Sekunden).
	 */
	public void resetHard() {
		grad = 0;
		gyroGrad = 0;
		gyro.reset();
	}
}
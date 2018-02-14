package wait;

import lejos.robotics.RegulatedMotor;
import sensors.DefaultSensor;

/**
 * Die Klasse WaitFor liefert Methoden, die jeweils While-Schleifen so lange ausführen, bis ein Ereigniss eintritt, das sie beendet.
 * 
 * @author Lennart Monir
 * @version 0.1
 */
public class WaitFor {

	/**
	 * Die Methode Sensor wartet auf ein Ereignss, das für einen übergebenen Sensor eintreffen soll.
	 * @param s Ein Objekt eines Sensors, der von der Klasse {@link sensors.DefaultSensors} erbt.
	 * @param lightlvl Ein Int, dass >=0 und <=100 sein muss.
	 * @param compare Ein String vom Typ "=", "!=", "<", "<=", ">" oder ">=". Andernfalls wird die Methode sofort abgebrochen.
	 */
	public static void Sensor(DefaultSensor s, int lightlvl, String compare){
		boolean check = false;		
		while (!check) {
			switch (compare) {
			case "==":
				if (s.getValue()==lightlvl) {check = true;}
				break;
			case "<":
				if (s.getValue()<lightlvl) {check = true;}
				break;
			case "<=":
				if (s.getValue()<=lightlvl) {check = true;}
				break;
			case ">":
				if (s.getValue()>lightlvl) {check = true;}
				break;
			case ">=":
				if (s.getValue()>=lightlvl) {check = true;}
				break;
			case "!=":
				if (s.getValue()!=lightlvl) {check = true;}
				break;
			default:
				check = true;
			}
		}
	}

	/**
	 * Die Methode Degree wartet auf ein Ereignss, das für einen übergebenen Motor eintreffen soll.
	 * @param s Ein Objekt eines Motors.
	 * @param degreelvl Ein Int, der Int-Werte annehmen darf.
	 * @param compare Ein String vom Typ "=", "!=", "<", "<=", ">" oder ">=". Andernfalls wird die Methode sofort abgebrochen.
	 */
	public static void Degree(RegulatedMotor motor, int degreelvl, String compare){
		boolean check = false;		
		while (!check) {
			switch (compare) {
			case "=":
				if (motor.getTachoCount()==degreelvl) {check = true;}
				break;
			case "<":
				if (motor.getTachoCount()<degreelvl) {check = true;}
				break;
			case "<=":
				if (motor.getTachoCount()<=degreelvl) {check = true;}
				break;
			case ">":
				if (motor.getTachoCount()>degreelvl) {check = true;}
				break;
			case ">=":
				if (motor.getTachoCount()>=degreelvl) {check = true;}
				break;
			case "!=":
				if (motor.getTachoCount()!=degreelvl) {check = true;}
				break;
			default:
				check = true;	
			}
		}
	}	
}

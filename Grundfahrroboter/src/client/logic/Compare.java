package client.logic;

import client.exceptions.RobotException;
import client.sensors.DefaultSensor;
import lejos.robotics.RegulatedMotor;
/**
 * Die Compare-Klasse ermöglicht es, die Messwerte von Sensoren oder Motoren mit einem Wert zu vergleichen,
 * um dann einen boolean-Rückgabewert zu erzeugen.
 * @author Lennart Monir
 * @version 0.2
 */
public class Compare {
	/**
	 * Die Methode vergleicht den Wert eines Sensors mit dem eines anderen Wertes.
	 * @param s, der Sensor der verglichen werden soll.
	 * @param value, der Wert
	 * @param compare, stellt dar, wie der Wert verglichen werden soll.
	 */
	public static boolean Sensor(DefaultSensor s, int value, String compare){
		boolean check = false;		
			switch (compare) {
			case "==":
				if (s.getValue()==value) {check = true;}
				break;
			case "<":
				if (s.getValue()<value) {check = true;}
				break;
			case "<=":
				if (s.getValue()<=value) {check = true;}
				break;
			case ">":
				if (s.getValue()>value) {check = true;}
				break;
			case ">=":
				if (s.getValue()>=value) {check = true;}
				break;
			case "!=":
				if (s.getValue()!=value) {check = true;}
				break;
			default:
				check = true;
			}
			return check;
		}
	
	/**
	 * Die Methode vergleicht den Wert eines Motors mit dem eines anderen Wertes.
	 * @param motor, der Motor der verglichen werden soll.
	 * @param value, der Wert
	 * @param compare, stellt dar, wie der Wert verglichen werden soll.
	 */
	public static boolean compareMotor(RegulatedMotor motor, int value, String compare) {
		boolean check = false;
		switch (compare) {
		case "=":
			if (motor.getTachoCount()==value) {check = true;}
			break;
		case "<":
			if (motor.getTachoCount()<value) {check = true;}
			break;
		case "<=":
			if (motor.getTachoCount()<=value) {check = true;}
			break;
		case ">":
			if (motor.getTachoCount()>value) {check = true;}
			break;
		case ">=":
			if (motor.getTachoCount()>=value) {check = true;}
			break;
		case "!=":
			if (motor.getTachoCount()!=value) {check = true;}
			break;
		default:
			check = true;
	}
		return check;
	}
}


package wait;

import lejos.robotics.RegulatedMotor;
import sensors.DefaultSensor;

public class WaitFor {

	public static void Light(DefaultSensor s, int lightlvl, String compare){
		boolean check = false;		
		while (!check) {
			switch (compare) {
			case "=":
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

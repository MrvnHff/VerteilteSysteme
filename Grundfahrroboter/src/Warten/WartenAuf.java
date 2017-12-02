package Warten;

import Sensoren.StandartSensor;
import lejos.robotics.RegulatedMotor;

public class WartenAuf {

	public static void Licht(StandartSensor s, int lichtlvl, String vergleich){
		boolean check = false;		
		while (!check) {
			switch (vergleich) {
			case "=":
				if (s.getMessung()==lichtlvl) {check = true;}
				break;
			case "<":
				if (s.getMessung()<lichtlvl) {check = true;}
				break;
			case "<=":
				if (s.getMessung()<=lichtlvl) {check = true;}
				break;
			case ">":
				if (s.getMessung()>lichtlvl) {check = true;}
				break;
			case ">=":
				if (s.getMessung()>=lichtlvl) {check = true;}
				break;
			case "!=":
				if (s.getMessung()!=lichtlvl) {check = true;}
				break;
			default:
				check = true;
			}
		}
	}
	
	public static void Grad(RegulatedMotor motor, int gradlvl, String vergleich){
		boolean check = false;		
		while (!check) {
			switch (vergleich) {
			case "=":
				if (motor.getTachoCount()==gradlvl) {check = true;}
				break;
			case "<":
				if (motor.getTachoCount()<gradlvl) {check = true;}
				break;
			case "<=":
				if (motor.getTachoCount()<=gradlvl) {check = true;}
				break;
			case ">":
				if (motor.getTachoCount()>gradlvl) {check = true;}
				break;
			case ">=":
				if (motor.getTachoCount()>=gradlvl) {check = true;}
				break;
			case "!=":
				if (motor.getTachoCount()!=gradlvl) {check = true;}
				break;
			default:
				check = true;	
			}
		}
	}	
}

package client.logic;

import client.sensors.DefaultSensor;
import lejos.robotics.RegulatedMotor;

public class Compare {
	
	public static boolean Sensor(DefaultSensor s, int lightlvl, String compare){
		boolean check = false;		
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
			return check;
		}
	
	public static boolean compareMotor(RegulatedMotor motor, int degreelvl, String compare) {
		boolean check = false;
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
		return check;
	}}


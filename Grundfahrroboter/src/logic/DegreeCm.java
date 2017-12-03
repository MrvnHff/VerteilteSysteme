package logic;

/**
 * @author Lennart
 * @version 10.10.2017
 * 
 * Diese Klasse stellt zu einem übergebenen Durchmesser die Funktionen bereit Gradzahl oder cm zu erhalten.
 */
public class DegreeCm {
	private double diameter = 0;
	private static final double  PI = 3.141;

	public DegreeCm() {}
	public DegreeCm(double diameter) {
		this.diameter = diameter;
	}
	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}
	public double getDiameter() {
		return diameter;
	}
	//Errechnet zu übergebener Strecke in cm die zu fahrende Gradzahl.
	public int getDegree(double cm) {
		int degree = 0;
		degree = (int)(Math.round((cm * 10) / (diameter * PI) * 360));
		return degree;
	}
	//Errechnet zu übergebener Gradzahl die Strecke in cm.
	public double getCm(double degree) {
		double cm = 0;
		cm = (degree * diameter * PI) / (3600);
		return cm;
	}
}

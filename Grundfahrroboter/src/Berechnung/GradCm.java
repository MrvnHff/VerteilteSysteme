package Berechnung;

/**
 * @author Lennart
 * @version 10.10.2017
 * 
 * Diese Klasse stellt zu einem übergebenen Durchmesser die Funktionen bereit Gradzahl oder cm zu erhalten.
 */
public class GradCm {
	private double durchmesser = 0;
	private static final double  PI = 3.141;

	public GradCm() {}
	public GradCm(double durchmesser) {
		this.durchmesser = durchmesser;
	}
	public void setDurchmesser(double durchmesser) {
		this.durchmesser = durchmesser;
	}
	public double getDurchmesser() {
		return durchmesser;
	}
	//Errechnet zu übergebener Strecke in cm die zu fahrende Gradzahl.
	public int getGrad(double cm) {
		int grad = 0;
		grad = (int)(Math.round((cm * 10) / (durchmesser * PI) * 360));
		return grad;
	}
	//Errechnet zu übergebener Gradzahl die Strecke in cm.
	public double getCm(double grad) {
		double cm = 0;
		cm = (grad * durchmesser * PI) / (3600);
		return cm;
	}
}

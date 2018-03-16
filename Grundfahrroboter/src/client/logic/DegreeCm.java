package client.logic;

/**
 * Diese Klasse stellt zu einem übergebenen Durchmesser die Funktionen bereit Gradzahl oder cm zu erhalten.
 * Die Klasse ist deshalb nicht statisch, da sie sich den Durchmesser des Rades merken soll.
 * @author Lennart
 * @version 10.10.2017 
 */
public class DegreeCm {
	private static final double  PI = 3.141;
	
	/**
	 * Errechnet zu übergebener Strecke in cm die zu fahrende Gradzahl.
	 * @param cm, ein double Wert, der in cm die Strecke darstellt.
	 * @return degree, Int Gradzahl
	 */
	public static int getDegree(double cm, double diameter) {
		int degree = 0;
		degree = (int)(Math.round((cm * 10) / (diameter * PI) * 360));
		return degree;
	}
	
	/**
	 * Errechnet zu übergebener Gradzahl in Grad die zu fahrende Strecke.
	 * @param degree, ein double Wert, der in Grad die Motorumdrehung darstellt.
	 * @return cm, Int Strecke in cm
	 */
	public static double getCm(double degree, double diameter) {
		double cm = 0;
		cm = (degree * diameter * PI) / (3600);
		return cm;
	}
}

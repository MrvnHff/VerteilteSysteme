package control;

/**
 * Der D-Regler betrachtet die vergangene und die momentane Regelabweichung und zieht so einen Schluss, wie wohl die Abweichung im n�chsten Schritt sein wird
 * (Differenz der Differenzen, Zukunft). Er steuert �ber seinen kd-Wert der Differenz entgegen.
 * Der D-Regler bildet eine Differenz zwischen den Differenzen. Daf�r wird die Differenz der vorher gemessenen Messung betrachtet und die der jetzigen Messung.
 * @author Lennart Monir
 * @version 0.1
 */
public class DRegler {
	private double kd;
	private int diffAlt;
	
	public DRegler(double kd) {
		this.kd = kd;
	}	
	
	/**
	 * Die Methode bildet den Regelanteil D. Daf�r wird eine Differenz zwischen der jetzigen Differenz und der Dfferenz des voran gegangenen Schittes berechnet.
	 * Diese wird mit dem kd-Wert multipliziert. Anschlie�end wird die jetzige Differenz zur alten Differenz f�r den zuk�nftigen Berechnungsschritt.
	 * @param diff, die momentane Differenz.
	 * @return Regelanteil D
	 */
	public double regelD(int diff) {
		double d = (diff-diffAlt)*kd;
		diffAlt = diff;
		return d;
	}
	
	public void setD(double kd) {
		this.kd = kd;
	}
}

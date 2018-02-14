package control;

/**
 * Der P-Regler betrachtet die momentane Regelabweichung (Differenz, Gegenwart) und steuert über seinen kp-Wert der Differenz gegen.
 * @author Lennart Monir
 * @version 0.1
 */
public class PRegler {
	private double kp;
	
	public PRegler(double kp) {
		this.kp = kp;
	}
	
	/**
	 * Die Methode bekommt die Differenz und multpliziert sie mit dem Regelwert kp und erzeugt so den Regel-Anteil P.
	 * @param diff, die Differenz zwischen Messung und Mittelwert.
	 * @return Regelanteil P.
	 */
	public double regelP(int diff) {
		//System.out.println(diff);
		return diff * kp;
	}
}

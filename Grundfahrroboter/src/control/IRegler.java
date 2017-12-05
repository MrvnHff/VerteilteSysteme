package control;

/**
 * Der I-Regler betrachtet die vergangene Regelabweichung (Differenz, Vergangenheit) und steuert �ber seinen ki-Wert der Differenz entgegen.
 * Der I-Regler summiert alle Differenzen auf und kann so feststellen, ob sich eine Verbesserung der Regelabweichung einstellt.
 * @author Lennart Monir
 * @version 0.1
 */
public class IRegler {
	private double ki;
	private double diffSum;
	private int diffAlt;
	private int mittelwert;

	public IRegler(double ki, int mittelwert) {
		this.ki = ki;
		diffSum = 0;
		diffAlt = 0;
		this.mittelwert = mittelwert;
	}
	/**
	 * Die Methode berechnet den Regelanteil I. Daf�r wird die diffSum mit 0.9975 multipliziert und die momentane Differenz aufaddiert.
	 * Der Wert 0.9975 Sorgt daf�r, dass die Summe ihren Wert �ber die Zeit wieder vergisst. Ebenso kann die Summe auch einen Maximalwert nicht �berschreiten.
	 * Sie konvergiert gegen ca. 20,000. Das ist notwendig, da sonst die Regelsteuerung evtl. zu stark werden kann und der Roboter so �berreagiert.
	 * Ein weiterer Schritt hilft der Summe schneller zu vergessen: Daf�r wird betrachtet, ob der Sensor die Kante �berschritten hat (diff*diffAlt<0?).
	 * Ist dies der Fall, dann kann die Summe sofort auf 0 gesetzt werden. Das wiederum ist notwendig, da es sonst zu lange dauert, bis die Summe wieder klein genug ist.
	 * Der Regelanteil wird zus�tzlich noch stark verkleinert und ist zus�tzlich noch von der Gr��e der Differenz abh�ngig. So wird verhindert, dass der I-Regler an der Kante
	 * zu gro�en Einfluss hat.
	 * @param diff, die momentane Differenz zwischen Sensor und Kante.
	 * @return Regelanteil I
	 */
	public double regelI(int diff) {
		diffSum = diffSum * 0.9975 + diff;
		
		if (diff*diffAlt < 0) {
			diffSum = 0;
		}
		
		diffAlt = diff;
		
		return (diffSum/133.33) * ki * (Math.abs(diff)/mittelwert);
	}
}

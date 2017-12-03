package control;

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
	public double regelI(int diff) {
		diffSum = diffSum * 0.9975 + diff;
		
		if (diff*diffAlt < 0) {
			diffSum = 0;
		}
		
		diffAlt = diff;
		
		return (diffSum/133.33) * ki * (Math.abs(diff)/mittelwert);
	}
}

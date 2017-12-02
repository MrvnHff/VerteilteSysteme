package Regler;

public class PRegler {
	private double kp;
	public PRegler(double kp) {
		this.kp = kp;
	}
	public double regelP(int diff) {
		return diff * kp;
	}
}

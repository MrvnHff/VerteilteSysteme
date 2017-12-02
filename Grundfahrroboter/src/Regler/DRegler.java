package Regler;

public class DRegler {
	private double kd;
	private int diffAlt;
	
	public DRegler(double kd) {
		this.kd = kd;
	}
	
	public double regelD(int diff) {
		double d = (diff-diffAlt)*kd;
		diffAlt = diff;
		return d;
	}
}

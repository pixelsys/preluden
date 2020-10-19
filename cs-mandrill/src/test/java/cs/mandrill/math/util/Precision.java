package cs.mandrill.math.util;

public class Precision {

	public static final double EPSILON = 0.000000000001;
	
	private Precision() {}

	public static boolean equals(double d1, double d2, double epsilon) {
		return Math.abs(d1 - d2) < epsilon;
	}
	
	public static boolean equals(double d1, double d2) {
		return equals(d1, d2, EPSILON);
	}
	
}

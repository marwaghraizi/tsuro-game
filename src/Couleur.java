public enum Couleur {
	R,B,N;
	static Couleur[] opposees = {B,R,N};
	// Retourne la couleur opposée R-> B / B-> R / N -> N
	public Couleur opposee() {
		return opposees[this.ordinal()];
	}
}

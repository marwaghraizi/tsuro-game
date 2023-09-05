
public class Position {
	int i;
	int j;
	public Position(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	// Retourne une nouvelle position en fonction de la direction d'entrée
	public Position move(Dir dir) {
		Position incr = dir.increment();
		return new Position(i + incr.i, j + incr.j);
	}
	
	
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
	
	public String toString() {
		return String.format("Position: (%d, %d)", i, j);
	}
	
	// Retourne true si deux position sont égals
	public boolean equals(Position p) {
		return this.i == p.i && this.j == p.j;
	}
	
	// Retourne true si deux position sont égals
	public boolean equals(Object o){
		if (!(o instanceof Position)) return false;
		Position p =(Position) o;
		return this.i == p.i && this.j == p.j; 
	}
}

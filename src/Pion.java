
public class Pion {

	private Position previousPosition;
	public Position position;
	private Dir previousDir;
	public Dir dir;
	int distance;
	
	// Constructeur
	public Pion(Position position, Dir dir) {
		super();
		this.position = position;
		this.dir = dir;
		this.distance = 0;
	}
	
	// Position suivante en fonction de la direction
	public Position next() {
		return this.position.move(dir);
	}
	
	public void setPosition(Position position) {
		previousPosition = new Position(this.position.i, this.position.j);
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public Position getPreviousPosition() {
		return previousPosition;
	}

	public void setDir(Dir dir) {
		previousDir = this.dir;
		this.dir = dir;
	}

	public Dir getPreviousDir() {
		return previousDir;
	}

	public Dir getDir() {
		return dir;
	}

	public int getDistance() {
		return distance;
	}
	
	public void itDist() {
		this.distance++;
	}

	@Override
	public String toString() {
		return String.format("Position: (%d, %d), Dir: %s", position.i, position.j, dir);
	}
}

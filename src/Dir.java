public enum Dir {
	N,O,S,E;
	// Direction opposée 
	static Dir[] opposees = {S,E,N,O};
	// Incrémentation en focntion de la direction de sortie d'une tuile
	static Position[] position = {new Position(1,0),new Position(0,1),new Position(-1,0),new Position(0,-1)};
	// Retourne la direction opposée 
	public Dir opposee() {
		return opposees[this.ordinal()]; 
	}
	// Incrémentation de la position en fonction de la direction de sortie 
	public Position increment() {
		return position[this.ordinal()];
	}
	
}
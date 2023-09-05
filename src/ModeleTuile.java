public enum ModeleTuile {
	// Direction de sortie avec comme ordre d'entrée NOSE
	CROISEMENT (new Dir[] {Dir.S,Dir.E,Dir.N,Dir.O}),
	VIRAGE_G (new Dir[] {Dir.E,Dir.S,Dir.O,Dir.N}),
	VIRAGE_D (new Dir[] {Dir.O,Dir.N,Dir.E,Dir.S});
	
	public final Dir[] sortie;
	
	ModeleTuile(Dir[] dirs) {
		this.sortie = dirs;
	}
	
	// Retourne une direction de sortie en fonction d'une direction d'entrée
	public Dir sortie(Dir entree) {
		return this.sortie[entree.ordinal()];
	}
}

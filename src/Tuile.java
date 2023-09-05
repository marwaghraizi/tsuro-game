
public class Tuile {
	public ModeleTuile type;
	public Couleur[] bord; 
	public Tuile(ModeleTuile type) {
		this.type = type;
		this.bord = new Couleur[] {Couleur.N,Couleur.N,Couleur.N,Couleur.N};
	}
	
	// Permet d'affecter une couleur à un bord
	public void coloration(Dir dir, Couleur couleur) {
		this.bord[dir.ordinal()] = couleur;
		this.bord[type.sortie(dir).ordinal()] = couleur;
	}
	
	public Couleur couleurBord(Dir dir) {
		return bord[dir.ordinal()];
	}
	
	// Permet d'obtenir le bord
	public Couleur[] getBord() {
		return bord;
	}
}

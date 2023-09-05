
public class Plateau {
	public Tuile[][] plateau;
	public Pion[] joueurs;
	public int largeur;
	public int hauteur;

	public Plateau(Tuile[][] plateau) {
		this.plateau = plateau;
		largeur = plateau[1].length;
		hauteur = plateau[0].length;
		joueurs = new Pion[] {
				new Pion(new Position(largeur/2, -1), Dir.O),
				new Pion(new Position(largeur/2, hauteur), Dir.E), };
	}

	public Pion[] getJoueurs() { // Permet d'obtenir les pions des deux joueurs
		return joueurs;
	}

	public Tuile[][] getPlateau() { // Permet le plateau qui est une matrice de tuiles
		return plateau;
	}

	public Tuile get(int i, int j) { // Permet d'obtenir une tuile précise du plateau en fonction des coordonnées
		return plateau[i][j];
	}

	public Tuile get(Position pos) { // Permet d'obtenir une tuile précise du plateau en fonction d'une position
		return get(pos.i, pos.j);
	}

	public ReponseCoup jouer(Couleur col, Position pos, ModeleTuile t) { // Retourne la réponse du placement d'une tuile

		if (this.interieur(pos) && this.libre(pos) && this.valide(joueurs[col.ordinal()], pos)) {
			this.plateau[pos.i][pos.j] = new Tuile(t);
			ReponseDeplacement resultC = deplacer(col);

			if (resultC == ReponseDeplacement.Collision) { // Si le joueur actuel rencontre le pion adverse, il perd la partie
				return ReponseCoup.WinAdverse;
			}

			ReponseDeplacement result_adverse = deplacer(col.opposee());

			if (resultC == ReponseDeplacement.Vide && result_adverse == ReponseDeplacement.Vide) { // Si les deux joueurs ont une tuile vide en face, égalité
				return ReponseCoup.Draw;
			}
			if (resultC == ReponseDeplacement.Crash && result_adverse == ReponseDeplacement.Vide) { // Si le joueur actuel se crash, le joueur adverse gagne 
				return ReponseCoup.WinAdverse;
			}
			if (resultC == ReponseDeplacement.Vide && result_adverse == ReponseDeplacement.Crash) { // Si le joueur adverse se crash, le joeur actuel l'emporte
				return ReponseCoup.WinC;
			}
			if (resultC == ReponseDeplacement.Crash && result_adverse == ReponseDeplacement.Crash) { //Si les deux joueurs se crash, le joeur ayant la plus grande route l'emporte
				// compare the traversed distances
				int distanceC = joueurs[col.ordinal()].getDistance();
				int distanceAdverse = joueurs[col.opposee().ordinal()].getDistance();

				// if (distanceC == distanceAdverse)
				// return ReponseCoup.Draw;
				if (distanceC >= distanceAdverse)
					return ReponseCoup.WinC;
				return ReponseCoup.WinAdverse;
			}

		}

		System.out.println("Placement non valide"); // si le placement n'est pas valide, affiche une erreur
		return ReponseCoup.Erreur;

	}

	// Vérifie si la tuile est dans le plateau
	public boolean interieur(Position p) {
		return p.i >= 0 && p.i < hauteur && p.j >= 0 && p.j < largeur;
	}

	// Vérifie si la position est libre
	public boolean libre(Position p) {
		return plateau[p.i][p.j] == null;
	}
	
	// Renvoie true si la tuile est bien dans la continuité du chemin de la bonne couleur
	public boolean valide(Pion p, Position pos) { 
		return p.next().equals(pos);
	}

	// Focntion de déplacement du pion
	private ReponseDeplacement deplacer(Couleur col) {
		Pion p = joueurs[col.ordinal()];
		Position next = p.next();
		
		// Si la prochaine position n'est pas à l'interieur = CRASH
		if (!interieur(next))
			return ReponseDeplacement.Crash;
		
		// Si la prochaine position est libre = VIDE
		if (this.libre(next))
			return ReponseDeplacement.Vide; 

		Dir b = p.dir.opposee();
		Tuile tuile = this.get(next);
		Dir sortie = tuile.type.sortie(b);

		if (tuile.couleurBord(sortie) != Couleur.N)
			return ReponseDeplacement.Collision;
		p.setPosition(next);
		p.setDir(sortie);
		p.itDist();
		tuile.coloration(sortie, col);

		return deplacer(col);
	}

}

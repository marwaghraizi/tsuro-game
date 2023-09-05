import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
public class GUI implements ActionListener {

	Affichage affichage; // Affichage principal

	ModeleTuile currentTileType = ModeleTuile.CROISEMENT; 
	boolean isVirageG = true; 
	JFrame frame;
	JPanel panel1; // Panel de l'affichage
	JPanel panel2; // Panel de menu
	JButton boutonRotation; 
	JButton boutonClear;
	JLabel labelCroisement;
	JLabel labelVirage;
	Icon iconeCroisement;
	Icon iconeVirage;
	Icon iconeVirageRotation;
	int n_colonne;
	int n_ligne;
	int largeur;
	int longeur;
	MouseListener listenerCroisement;
	MouseListener listenerVirage;
	KeyListener listenerKeyVirage;

	Plateau plateau;
	Couleur currentTurn;

	public GUI(int n_colonne, int n_ligne) throws IOException {

		this.n_colonne = n_colonne;
		this.n_ligne = n_ligne;
		// Création du plateau
		Tuile[][] tuiles = new Tuile[n_ligne][n_colonne];
		plateau = new Plateau(tuiles);
		currentTurn = Couleur.R;
		
		
		// Permet d'avoir la taille en pixel du plateau
		largeur = (120 * (n_colonne + 2)) + n_colonne + 3;
		longeur = (120 * (n_ligne + 2)) + n_ligne + 3;

		frame = new JFrame();
		panel1 = new JPanel();
		panel2 = new JPanel();
		
		// Creation de l'affichage du plateau
		affichage = new Affichage(largeur, longeur, "sprites_tsuro.png", 3, 2, 120, plateau.getJoueurs());
		
		// Creation des icones pour les JLabel
		iconeCroisement = new ImageIcon(ImageIO.read(new File("croisement.png")));
		labelCroisement = new JLabel(iconeCroisement);

		iconeVirage = new ImageIcon(ImageIO.read(new File("virages.png")));
		labelVirage = new JLabel(iconeVirage);

		iconeVirageRotation = new ImageIcon(ImageIO.read(new File("virage_rotation.png")));
		
		// Creation des evenements pour le Drag and drop des images
		listenerCroisement = new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int i = ((int) Math.floor((e.getY()) / 1.2) / 100) - 1;
				int j = ((int) Math.floor((e.getX() + affichage.width) / 1.2) / 100) - 1;
				currentTileType = ModeleTuile.CROISEMENT;
				placeTile(i, j);
			}
		};

		listenerVirage = new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {

				int i = ((int) Math.floor((e.getY() + (affichage.height / 2)) / 1.2) / 100) - 1;
				int j = ((int) Math.floor((e.getX() + affichage.width) / 1.2) / 100) - 1;

				currentTileType = isVirageG ? ModeleTuile.VIRAGE_G : ModeleTuile.VIRAGE_D;
				placeTile(i, j);
			}
		};
		
		// Feature: rotation grace à la barre espace
		listenerKeyVirage = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (labelVirage.getIcon() == iconeVirage) {
						isVirageG = false;
						labelVirage.setIcon(iconeVirageRotation);
					} else {
						isVirageG = true;
						labelVirage.setIcon(iconeVirage);
					}
				}
			}
		};
		
		// ajout des evenements à nos labels
		labelCroisement.addMouseListener(listenerCroisement);
		labelVirage.addKeyListener(listenerKeyVirage);
		labelVirage.addMouseListener(listenerVirage);
		// Creation des boutons
		boutonRotation = new JButton("Rotation");
		boutonRotation.addActionListener(this);

		boutonClear = new JButton("Clear");
		boutonClear.addActionListener(this);

		panel2.setLayout(new GridLayout(2, 2));
		panel2.add(labelCroisement);
		panel2.add(boutonRotation);
		panel2.add(labelVirage);
		panel2.add(boutonClear);
		panel1.add(affichage);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel1, BorderLayout.WEST);
		frame.add(panel2, BorderLayout.EAST);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		// Evenement du bouton rotation
		if (e.getSource() == boutonRotation) {
			if (labelVirage.getIcon() == iconeVirage) {
				isVirageG = false;
				labelVirage.setIcon(iconeVirageRotation);
			} else {
				isVirageG = true;
				labelVirage.setIcon(iconeVirage);
			}
		} else if (e.getSource() == boutonClear) {
			// Evenement du bouton clear
			Tuile[][] tuiles = new Tuile[n_ligne][n_colonne];
			plateau = new Plateau(tuiles);
			currentTurn = Couleur.R;
			// Efface le panneau affichage et recrée un autre panneau
			panel1.remove(affichage);
			affichage = new Affichage(largeur, longeur, "sprites_tsuro.png", 3, 2, 120, plateau.getJoueurs());
			panel1.add(affichage);
			frame.pack();
		}
	}
	
	// Fonction du contrôleur qui permet de réagir en conséquence de la fonction "Jouer"
	public void placeTile(int i, int j){
		
		ReponseCoup response = plateau.jouer(currentTurn, new Position(i, j), currentTileType);

		if(response == ReponseCoup.Erreur){ // Si la réponse au coup est ERREUR affihe un message dans une fenêtre popup
			JOptionPane.showMessageDialog(null, String.format("Player: %s - %s", currentTurn, response));
			return;
		}

		redrawTiles();
		if(response != ReponseCoup.Draw){ // Si la réponse au coup n'est pas DRAW affiche le message adapté
			JOptionPane.showMessageDialog(null, String.format("Player: %s - %s", currentTurn, response));
			return;
		}
		currentTurn = currentTurn.opposee(); // Inverse la couleur
	}
	
	// Fonction qui redraw tout l'affichage à chaque placement d'une nouvelle tuile
	public void redrawTiles(){

		Tuile[][] tuiles = plateau.getPlateau();
		for (int i = 0; i < tuiles.length; i++) {
			for (int j = 0; j < tuiles[i].length; j++) {
				Tuile tuile = tuiles[i][j];
				
				if (tuile != null) {
					ModeleTuile tileType = tuile.type;
					Couleur[] colors = tuile.getBord();
					
					int[] colorIdxs = new int[4];
					
					for (int k = 0; k < colors.length; k++) {
						colorIdxs[k] = colors[k] == Couleur.R ? 1 : colors[k] == Couleur.B ? 2 : 0;
					}
					// Coordonnées en fonction des résultats du drag and drop
					int x = affichage.size * (j + 1) + 1 + (j + 1);
					int y = affichage.size * (i + 1) + 1 + (i + 1);

					affichage.getImageGraphics().setColor(affichage.tileBackground);
					affichage.getImageGraphics().fillRect(x, y, affichage.size, affichage.size);

					switch(tileType){
						case CROISEMENT:
							affichage.getImageGraphics().drawImage(affichage.sprites[colorIdxs[0]][0][0], x, y, null);
							affichage.getImageGraphics().drawImage(affichage.sprites[colorIdxs[1]][0][1], x, y, null);
							break;
						case VIRAGE_G:
							affichage.getImageGraphics().drawImage(affichage.sprites[colorIdxs[2]][1][0], x, y, null);
							affichage.getImageGraphics().drawImage(affichage.sprites[colorIdxs[0]][1][2], x, y, null);
							break;
						case VIRAGE_D:
							affichage.getImageGraphics().drawImage(affichage.sprites[colorIdxs[1]][1][1], x, y, null);
							affichage.getImageGraphics().drawImage(affichage.sprites[colorIdxs[3]][1][3], x, y, null);
							break;
					}

				}
			}
		}
		// WIP
		
//		Pion[] pions = plateau.getJoueurs();
//		Pion redPion = pions[0];
//		Pion bluePion = pions[1];
		
//		if(redPion.getPreviousPosition() != null){
//			example.movePawn(redPion.getPreviousPosition(), redPion.getPosition(), Couleur.R, redPion.getPreviousDir(), redPion.getDir());
//		}
//
//		if(bluePion.getPreviousPosition() != null){
//			example.movePawn(bluePion.getPreviousPosition(), bluePion.getPosition(), Couleur.B, bluePion.getPreviousDir(), bluePion.getDir());
//		}
		affichage.repaint();

	}
}

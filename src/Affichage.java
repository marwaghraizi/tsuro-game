import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Affichage extends JPanel {
	public final int width;
	public final int height;
	public BufferedImage img;
	public Graphics g_img;

	// couleur de fond d'une tuile
	public final Color tileBackground = new Color(210, 210, 210);

	/* bank of sprites: for each line in the base image, for each color of the image,
	 *  4 versions of the subimages in this line/column: the subimage rotated 0,1,2,3 (fourth) 
	 *  sprintes[color][modeltuile][rotation]
	 */
	public final int size;
	public final BufferedImage[][][] sprites;

	public Affichage(int width, int height, String fileName, int lines, int columns, int size, Pion[] pions) {
		this.width = width;
		this.height = height;
		this.size = size;

		// réglage des dimensions du panneau
		this.setPreferredSize(new Dimension(width, height));
		/*
		 * lecture de l'image de base des sprites. le fichier .png doit être a la racine
		 * du projet (import -> File system).
		 */
		BufferedImage base = null;
		try {
			base = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// répartition des sous-images dans la banque
		sprites = new BufferedImage[lines][columns][4];
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				// subImg : sous-image à recopier en quatre versions
				BufferedImage subImg = base.getSubimage(size * j, size * i, size, size);
				for (int r = 0; r < 4; r++) {
					// création d'une rotation de r quarts de tour autour
					// du centre de la sous-image.
					AffineTransform t = new AffineTransform();
					t.setToQuadrantRotation(r, size / 2, size / 2);
					// création de l'image cible
					sprites[i][j][r] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
					// g2 : point d'accès à ses pixels.
					Graphics2D g2 = (Graphics2D) sprites[i][j][r].getGraphics();
					// copie de la sous-image dans l'image cible, en lui
					// faisant subir la rotation t.
					g2.drawImage(subImg, t, null);
				}
			}
		}
		/*
		 * demande de rafraîchissement du panneau, qui invoquera la paintComponent en
		 * lui passant comme argument un point d'accès aux pixels de la fenêtre.
		 */
		repaint();
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g_img = img.getGraphics();
		
		
		// Création de la grille
		
		g_img.setColor(Color.black);
		for (int ligne = 0; ligne <= height; ligne = ligne + size + 1) {
			g_img.drawLine(0, ligne, width, ligne);
		}
		for (int colonne = 0; colonne <= width; colonne = colonne + size + 1) {
			g_img.drawLine(colonne, 0, colonne, height);
		}
		
		// Création de la bordure
		
		g_img.setColor(Color.gray);
		g_img.fillRect(1, 1, width - 2, size);
		g_img.fillRect(1, 1, size, height - 2);
		g_img.fillRect(width - size - 1, 1, size, height - 2);
		g_img.fillRect(1, height - size - 1, width - 2, size);
		
		// Création des pions 
		
		Pion redPion = pions[0];
		Pion bluePion = pions[1];

		int[] redPionCoords = transformPositionToPixelCoordinates(redPion.getPosition(), Couleur.R, redPion.getDir());
		int[] bluePionCoords = transformPositionToPixelCoordinates(bluePion.getPosition(), Couleur.B, bluePion.getDir());

		g_img.setColor(Color.red);
		g_img.fillOval(redPionCoords[0], redPionCoords[1], 30, 30);
		g_img.setColor(Color.blue);
		g_img.fillOval(bluePionCoords[0], bluePionCoords[1], 30, 30);

	}

	Graphics getImageGraphics() {
		return g_img;
	}
	
	
	// Permet de transformer une position en coordonnées (WIP)
	int[] transformPositionToPixelCoordinates(Position position, Couleur couleur, Dir direction) {

		int[] pixelCoordinates = new int[2];
		pixelCoordinates[0] = (size/2) + (position.i * size);
		pixelCoordinates[1] = (height/2) + (position.j * size);
		if (position.i < 0 || position.i > 2 || position.j < 0 || position.j > 2) {
			pixelCoordinates[0] = (size/2);
			pixelCoordinates[1] = (height/2);
		}
		if (couleur == Couleur.B) {
			pixelCoordinates[0] = width - pixelCoordinates[0];
		}

		int directionMultiplier = couleur == Couleur.R ? 1 : -2;

		switch (direction) {
			case N:
			case S:
				pixelCoordinates[1] = pixelCoordinates[1] + (directionMultiplier * (60 / 2));
				break;
			case E:
			case O:
				pixelCoordinates[0] = pixelCoordinates[0] + (directionMultiplier * (60 / 2));
				break;
			default:
				break;
		}

		pixelCoordinates[1] -= 30/2;
		return pixelCoordinates;
	}
	
	// Fonction pour déplacer le pion (WIP) 
	void movePawn(Position oldPosition, Position newPosition, Couleur couleur, Dir oldDirection, Dir newDirection) {
		g_img.setColor(Color.gray);
		if(couleur == Couleur.R) {
			int[] oldPixelCoordinates = transformPositionToPixelCoordinates(oldPosition, Couleur.R, oldDirection);
			g_img.fillOval(oldPixelCoordinates[0], oldPixelCoordinates[1], 30, 30);

			int[] newPixelCoordinates = transformPositionToPixelCoordinates(newPosition, Couleur.R, newDirection);
			g_img.setColor(Color.RED);
			g_img.fillOval(newPixelCoordinates[0], newPixelCoordinates[1], 30, 30);
		} else {
			g_img.fillOval(width - (size / 2) - 15 + (oldPosition.i * (size + 1)), (height / 2) - 15 + (oldPosition.j * (size + 1)), 30, 30);
			g_img.setColor(Color.BLUE);
			g_img.fillOval(width - (size / 2) - 15 + (newPosition.i * (size + 1)), (height / 2) - 15 + (newPosition.j * (size + 1)), 30, 30);
		}

	}
	
	// Permet l'affichage
	protected void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}
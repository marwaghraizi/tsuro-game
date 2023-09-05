public class Runner {
    public static void main(String[] args) {
    	
        Tuile[][] tiles = new Tuile[3][3];
        Plateau board = new Plateau(tiles);

        // Cas d'égalité
        //moveRed(board,new Position(2, 1), ModeleTuile.VIRAGE_G);
        //moveBlue(board, new Position(1, 0), ModeleTuile.VIRAGE_G);
        //moveRed(board,new Position(2, 0), ModeleTuile.CROISEMENT);
        
        
        // Cas de collision
        //moveRed(board,new Position(1, 2), ModeleTuile.CROISEMENT);
        //moveBlue(board, new Position(1, 0), ModeleTuile.VIRAGE_G);
        //moveRed(board,new Position(1, 1), ModeleTuile.VIRAGE_D);
        //moveBlue(board,new Position(2,0), ModeleTuile.VIRAGE_G);
        //moveRed(board,new Position(2, 1), ModeleTuile.VIRAGE_D);

        
        
        // Example de l'énoncé
        //moveRed(board,new Position(1, 2), ModeleTuile.CROISEMENT);
        //moveBlue(board, new Position(1, 0), ModeleTuile.VIRAGE_G);

        //moveRed(board, new Position(1, 1),  ModeleTuile.CROISEMENT);
        //moveBlue(board,new Position(2,0), ModeleTuile.VIRAGE_G);

        //moveRed(board, new Position(0,0), ModeleTuile.VIRAGE_D);
        //moveBlue(board, new Position(2,1), ModeleTuile.VIRAGE_D);

        //moveRed(board, new Position(0,1), ModeleTuile.CROISEMENT);

    }
    
    
    // Permet de tester
    public static void moveBlue(Plateau board, Position position, ModeleTuile type){
        Pion[] points = board.getJoueurs();
        ReponseCoup result_b = board.jouer(Couleur.B, position, type);

        System.out.println(Couleur.B + " After: " + points[1]);
        if (result_b == ReponseCoup.WinC) {
        	System.out.println("BLUE WINS");
        }
        if (result_b == ReponseCoup.WinAdverse) {
        	System.out.println("RED WINS");
        }
    }

    public static void moveRed(Plateau board, Position position, ModeleTuile type){
        Pion[] points = board.getJoueurs();
        ReponseCoup result = board.jouer(Couleur.R, position, type);
        System.out.println(result); //testing
        System.out.println(Couleur.R + " After: " + points[0]);
        
        if (result == ReponseCoup.WinC) {
        	System.out.println("RED WINS");
        }
        if (result == ReponseCoup.WinAdverse) {
        	System.out.println("BLUE WINS");
        }
    }
}

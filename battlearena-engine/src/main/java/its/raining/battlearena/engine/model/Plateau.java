package its.raining.battlearena.engine.model;

/**
 * Plateau de jeu
 */
public class Plateau {

  /** Joueur 1 */
  private Joueur player1;

  /** Joueur 2 */
  private Joueur player2;

  /** Longueur plateau */
  private int longueur;

  /** Largeur plateau */
  private int largeur;

  public Joueur getPlayer1() {
    return this.player1;
  }

  public void setPlayer1(Joueur player1) {
    this.player1 = player1;
  }

  public Joueur getPlayer2() {
    return this.player2;
  }

  public void setPlayer2(Joueur player2) {
    this.player2 = player2;
  }

  public int getLongueur() {
    return this.longueur;
  }

  public void setLongueur(int longueur) {
    this.longueur = longueur;
  }

  public int getLargeur() {
    return this.largeur;
  }

  public void setLargeur(int largeur) {
    this.largeur = largeur;
  }

}

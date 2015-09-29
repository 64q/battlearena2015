package its.raining.battlearena.engine.model;

/**
 * Board de jeu
 */
public class Board {

  /** Player 1 */
  private Player player1;

  /** Player 2 */
  private Player player2;

  /** Longueur plateau */
  private int longueur;

  /** Largeur plateau */
  private int largeur;

  public Player getPlayer1() {
    return this.player1;
  }

  public void setPlayer1(Player player1) {
    this.player1 = player1;
  }

  public Player getPlayer2() {
    return this.player2;
  }

  public void setPlayer2(Player player2) {
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

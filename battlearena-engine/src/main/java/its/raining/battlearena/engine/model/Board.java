package its.raining.battlearena.engine.model;

/**
 * Board de jeu
 */
public class Board {

  private int nbrActionLeft;

  private Player player1;

  private Player player2;

  public int getNbrActionLeft() {
    return nbrActionLeft;
  }

  public void setNbrActionLeft(int nbrActionLeft) {
    this.nbrActionLeft = nbrActionLeft;
  }

  public Player getPlayer1() {
    return player1;
  }

  public void setPlayer1(Player player1) {
    this.player1 = player1;
  }

  public Player getPlayer2() {
    return player2;
  }

  public void setPlayer2(Player player2) {
    this.player2 = player2;
  }


}

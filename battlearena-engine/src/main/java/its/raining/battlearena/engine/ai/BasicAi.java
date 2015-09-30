package its.raining.battlearena.engine.ai;

import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Move;

import org.springframework.stereotype.Component;

/**
 * IA de base
 */
@Component
public class BasicAi implements Ai {

  private Move lastMove = Move.NA;

  @Override
  public Move play(Board board, Move move) {
    Move ourMove = Move.SHOOT;

    lastMove = ourMove;

    return ourMove;
  }

  public boolean doitSeCouvrir(Board board, Move move) {
    if (move == Move.AIM && board.getPlayer2().getBullet() > 0
        && board.getPlayer1().getShield() > 0) {
      return true;
    }

    return false;
  }

  public boolean doitTirer(Board board, Move move) {

  }

  public boolean doitRecharger(Board board, Move move) {

  }

  public boolean doitViser(Board board, Move move) {

  }
}

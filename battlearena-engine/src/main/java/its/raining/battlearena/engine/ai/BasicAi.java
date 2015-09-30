package its.raining.battlearena.engine.ai;

import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Move;
import its.raining.battlearena.engine.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * IA de base
 */
@Component
public class BasicAi implements Ai {

  private Move ourLastMove = Move.NA;

  private Move themLastMove = Move.NA;

  /**
   * Logger
   */
  private static final Logger LOG = LoggerFactory.getLogger(BasicAi.class);

  @Override
  public Move play(Board board, Move move) {

    Player us = getUs(board);
    Player them = getThem(board);

    Move ourMove;

    if (board.getNbrActionLeft() == us.getShield() && us.getHealth() > them.getHealth()
        && them.getBullet() > 0) {
      ourMove = Move.COVER;
    } else if (doitSeCouvrir(board, move, us, them)) {
      ourMove = Move.COVER;
    } else if (doitRecharger(board, move, us, them)) {
      ourMove = Move.RELOAD;
    } else if (doitViser(board, move, us, them)) {
      ourMove = Move.AIM;
    } else if (doitTirer(board, move, us, them)) {
      ourMove = Move.SHOOT;
    } else {
      ourMove = Move.SHOOT;

    }

    LOG.info("Our move = " + ourMove);
    LOG.info("Their move = " + move);

    ourLastMove = ourMove;
    themLastMove = move;

    return ourMove;
  }

  private Player getThem(Board board) {
    if (board.getPlayer1().getName().equals("It's Raining")) {
      return board.getPlayer2();
    }

    return board.getPlayer1();
  }

  private Player getUs(Board board) {
    if (board.getPlayer1().getName().equals("It's Raining")) {
      return board.getPlayer1();
    }

    return board.getPlayer2();
  }

  public boolean doitSeCouvrir(Board board, Move move, Player us, Player them) {
    if (move == Move.AIM && them.getBullet() > 0 && us.getShield() > 0 && us.getHealth() > 3
        && themLastMove != Move.AIM) {
      return true;
    }

    return false;
  }

  public boolean doitTirer(Board board, Move move, Player us, Player them) {
    if (them.getHealth() == 1 && us.getBullet() > 0 && ourLastMove == Move.AIM) {
      return true;
    }

    return false;
  }

  public boolean doitRecharger(Board board, Move move, Player us, Player them) {
    if (us.getBullet() == 0) {
      return true;
    }
    return false;
  }

  public boolean doitViser(Board board, Move move, Player us, Player them) {
    if ((them.getHealth() > 3 && us.getBullet() > 0 && (Math.random() * 100 > 40)) && (ourLastMove != Move.AIM || them.getShield() > 0) 
        || them.getBullet() == 0 && ourLastMove != Move.AIM) {
      return true;
    }
    return false;
  }
}

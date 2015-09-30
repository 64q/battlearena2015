package its.raining.battlearena.engine.ai;

import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Move;

public class IANiveauFour implements Ai {

  @Override
  public Move play(Board board, Move move) {
    return Move.COVER;
  }

}

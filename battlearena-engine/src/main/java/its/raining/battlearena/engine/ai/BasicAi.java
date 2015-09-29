package its.raining.battlearena.engine.ai;

import org.springframework.stereotype.Component;

import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Coordinates;

/**
 * IA de base
 */
@Component
public class BasicAi implements Ai {

  @Override
  public Coordinates play(Board board, String currentPlayer) {
    return new Coordinates("A2", "C6");
  }

}

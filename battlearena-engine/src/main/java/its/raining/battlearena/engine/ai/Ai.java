package its.raining.battlearena.engine.ai;

import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Move;

import org.springframework.stereotype.Service;

/**
 * Interface permettant d'implémenter une IA
 */
@Service
public interface Ai {
  /**
   * Jouer un coup avec l'IA
   * 
   * @param board plateau de jeu
   * @param move mouvement de l'adversaire
   * @return le mouvement
   */
  Move play(Board board, Move move);
}

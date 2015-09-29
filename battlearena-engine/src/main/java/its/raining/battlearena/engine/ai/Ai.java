package its.raining.battlearena.engine.ai;

import org.springframework.stereotype.Service;

import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Coordinates;

/**
 * Interface permettant d'implémenter une IA
 */
@Service
public interface Ai {
  /**
   * Jouer un coup avec l'IA
   * 
   * @param board plateau de jeu
   * @param idEquipe id de notre équipe
   * @return coordonnées du coup à jouer
   */
  Coordinates play(Board board, String idEquipe);
}

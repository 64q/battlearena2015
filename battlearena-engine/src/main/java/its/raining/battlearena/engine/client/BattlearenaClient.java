package its.raining.battlearena.engine.client;

import its.raining.battlearena.engine.exception.ClientException;
import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Level;
import its.raining.battlearena.engine.model.Move;
import its.raining.battlearena.engine.model.PlayOutcome;
import its.raining.battlearena.engine.model.Status;
import its.raining.battlearena.generated.BattlearenaIo_TestWs;
import its.raining.battlearena.generated.IP521913975_BattleWs;
import its.raining.battlearena.generated.IP521913975_BattleWs.Duel;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Client Rest Battlearena
 * 
 * <p>
 * <b>NB :</b> Penser à changer le client de {@link BattlearenaIo_TestWs} en non test lors de la
 * compétition
 * </p>
 */
@Component
public class BattlearenaClient {

  /** Mapper utilisé pour le {@link Board} de jeu */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /** Client Jersey, a faire pointer vers le vers WS lors de la compétition */
  protected Duel client = IP521913975_BattleWs.duel();

  static {
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * Méthode permettant de pinger le serveur de jeu
   * 
   * @return pong, si le serveur répond
   */
  public String ping() {
    return client.ping().getAsJson(String.class);
  }

  /**
   * Récupère l'id de l'équipe par le nom et le mot de passe
   * 
   * @param nomEquipe
   * @param motDePasse
   * @return l'id de l'équipe
   */
  public String getIdEquipe(String nomEquipe, String motDePasse) {
    return client.playerGetIdEquipeNomEquipeMotDePasse(nomEquipe, motDePasse).getAsJson(
        String.class);
  }

  /**
   * Démarre un match contre l'IA
   * 
   * @param idEquipe
   * @param level
   * @return l'id du match
   */
  public String newPractice(String idEquipe, Level level) {
    return client.practiceNewLevelIdEquipe(level.getCode(), idEquipe).getAsJson(String.class);
  }

  /**
   * Récupère l'état de la partie
   * 
   * @param idEquipe
   * @param idPartie
   * @return l'état de la partie
   */
  public Status getStatus(String idEquipe, String idPartie) {
    return Status.valueOf(client.gameStatusIdPartieIdEquipe(idPartie, idEquipe).getAsJson(
        String.class));
  }

  /**
   * Récupère le plateau de jeu courant, converti le flux JSON en objet Java
   * 
   * @param idPartie
   * @return le {@link Board} de jeu
   */
  public Board getBoard(String idPartie) {
    String result = client.gameBoardIdPartie(idPartie).getAsJson(String.class);

    try {
      return MAPPER.readValue(result, Board.class);
    } catch (IOException e) {
      throw new ClientException("Erreur au parsing du flux JSON", e);
    }
  }

  /**
   * Joue un coup
   * 
   * @param idEquipe
   * @param idPartie
   * @param coords
   * @return l'état du coup
   */
  public PlayOutcome play(String idEquipe, String idPartie, Move move) {
    return PlayOutcome.valueOf(client.gamePlayIdPartieIdEquipeMove(idPartie, idEquipe, move.name())
        .getAsJson(String.class));
  }

  /**
   * Récupère le dernier coup joué de l'adversaire
   * 
   * @param idPartie
   * @return coordonées du coup
   */
  public Move getLastMove(String idPartie, String idEquipe) {
    return Move.valueOf(client.gameGetlastmoveIdPartieIdEquipe(idPartie, idEquipe).getAsJson(
        String.class));
  }

  /**
   * Retourne le prochain match à jouer
   * 
   * @param idEquipe
   * @return l'id du prochain match
   */
  public String nextVersus(String idEquipe) {
    return client.versusNextIdEquipe(idEquipe).getAsJson(String.class);
  }

  /**
   * Retourne le nom de l'adversaire
   * 
   * @param idPartie
   * @param idEquipe
   * @return
   */
  public String getOpponent(String idPartie, String idEquipe) {
    return client.gameOpponentIdPartieIdEquipe(idPartie, idPartie).getAsJson(String.class);
  }
}

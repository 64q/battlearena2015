package its.raining.battlearena.engine.client;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import its.raining.battlearena.engine.exception.ClientException;
import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.Coordinates;
import its.raining.battlearena.engine.model.Status;
import its.raining.battlearena.engine.model.Level;
import its.raining.battlearena.engine.model.PlayOutcome;
import its.raining.battlearena.generated.BattlearenaIo_TestWs;
import its.raining.battlearena.generated.BattlearenaIo_TestWs.Root;

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
  protected Root client = BattlearenaIo_TestWs.root();

  static {
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * Méthode permettant de pinger le serveur de jeu
   * 
   * @return pong, si le serveur répond
   */
  public String ping() {
    return client.ping().getAsTextPlain(String.class);
  }

  /**
   * Récupère l'id de l'équipe par le nom et le mot de passe
   * 
   * @param nomEquipe
   * @param motDePasse
   * @return l'id de l'équipe
   */
  public String getIdEquipe(String nomEquipe, String motDePasse) {
    return client.playerGetIdEquipeNomEquipeMotDePasse(nomEquipe, motDePasse)
        .getAsTextPlain(String.class);
  }

  /**
   * Démarre un match contre l'IA
   * 
   * @param idEquipe
   * @param level
   * @return l'id du match
   */
  public String newPractice(String idEquipe, Level level) {
    return client.practiceNewLevelIdEquipe(level.getCode(), idEquipe).getAsTextPlain(String.class);
  }

  /**
   * Retourne le prochain match contre une IA
   * 
   * @param idEquipe
   * @return l'id du match
   */
  public String nextPractice(String idEquipe) {
    return client.practiceNextIdEquipe(idEquipe).getAsTextPlain(String.class);
  }

  /**
   * Récupère l'état de la partie
   * 
   * @param idEquipe
   * @param idPartie
   * @return l'état de la partie
   */
  public Status getStatus(String idEquipe, String idPartie) {
    return Status.valueOf(
        client.gameStatusIdPartieIdEquipe(idPartie, idEquipe).getAsTextPlain(String.class));
  }

  /**
   * Récupère le plateau de jeu courant, converti le flux JSON en objet Java
   * 
   * @param idPartie
   * @return le {@link Board} de jeu
   */
  public Board getBoard(String idPartie) {
    String result = client.gameBoardIdPartie(idPartie).getAs(String.class);

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
  public PlayOutcome play(String idEquipe, String idPartie, Coordinates coords) {
    return PlayOutcome
        .valueOf(client.gamePlayIdPartieIdEquipeCoordXCoordY(idPartie, idEquipe, coords.x, coords.y)
            .getAsTextPlain(String.class));
  }

  /**
   * Récupère le dernier coup joué de l'adversaire
   * 
   * @param idPartie
   * @return coordonées du coup
   */
  public Coordinates getLastMove(String idPartie) {
    Coordinates coords = new Coordinates();

    String[] result = StringUtils
        .split(client.gameGetlastmoveIdPartie(idPartie).getAsTextPlain(String.class), ",");

    coords.x = result[0];
    coords.y = result[1];

    return coords;
  }

  /**
   * Retourne le prochain match à jouer
   * 
   * @param idEquipe
   * @return l'id du prochain match
   */
  public String nextVersus(String idEquipe) {
    return client.versusNextIdEquipe(idEquipe).getAsTextPlain(String.class);
  }
}

package its.raining.battlearena.client;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import its.raining.battlearena.exception.ClientException;
import its.raining.battlearena.generated.BattlearenaIo_TestWs;
import its.raining.battlearena.generated.BattlearenaIo_TestWs.Root;
import its.raining.battlearena.model.Coords;
import its.raining.battlearena.model.Level;
import its.raining.battlearena.model.Plateau;

/**
 * Client Rest Battlearena
 * 
 * <p>
 * <b>NB :</b> Penser à changer le client de {@link BattlearenaIo_TestWs} en non test lors de la
 * compétition
 * </p>
 * 
 * @author Quentin
 */
@Service
public class BattlearenaClient {

  /** Mapper utilisé pour le {@link Plateau} de jeu */
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
   * Récupère l'état de la partie
   * 
   * @param idEquipe
   * @param idPartie
   * @return l'état de la partie
   */
  public String getStatus(String idEquipe, String idPartie) {
    return client.gameStatusIdPartieIdEquipe(idPartie, idEquipe).getAsTextPlain(String.class);
  }

  /**
   * Récupère le plateau de jeu courant, converti le flux JSON en objet Java
   * 
   * @param idPartie
   * @return le {@link Plateau} de jeu
   */
  public Plateau getBoard(String idPartie) {
    String result = client.gameBoardIdPartie(idPartie).getAs(String.class);

    try {
      return MAPPER.readValue(result, Plateau.class);
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
  public String play(String idEquipe, String idPartie, Coords coords) {
    return client.gamePlayIdPartieIdEquipeCoordXCoordY(idPartie, idEquipe, coords.x, coords.y)
        .getAsTextPlain(String.class);
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

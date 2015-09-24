package its.raining.battlearena.engine;

import its.raining.battlearena.client.BattlearenaClient;
import its.raining.battlearena.exception.EngineException;
import its.raining.battlearena.model.Coords;
import its.raining.battlearena.model.Level;
import its.raining.battlearena.model.Plateau;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class BattlearenaEngine {

  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(BattlearenaEngine.class);

  @Autowired
  private BattlearenaClient client;

  private String nomEquipe;
  private String motDePasse;

  /** Id equipe du serveur */
  private String idEquipe;

  /** Id de la partie */
  private String idPartie;

  public BattlearenaEngine init(String nomEquipe, String motDePasse) {
    this.nomEquipe = nomEquipe;
    this.motDePasse = motDePasse;

    return this;
  }

  public BattlearenaEngine runPractice(Level level) {
    checkConnection();

    // récupération d'un Id d'équipe
    initEquipe(nomEquipe, motDePasse);

    // passage en practice
    newPractice(level);

    // on joue maintenant !
    play();

    return this;
  }

  private void play() {
    String status = client.getStatus(idEquipe, idPartie);

    switch (status) {
      case "GAGNE":
        performWin();
        break;
      case "PERDU":
        performDefeat();
        break;
      case "ANNULE":
        performCancel();
        break;
      case "OUI":
        performTurn();
      case "NON":
      default:
        play();
    }
  }

  private void performCancel() {
    // TODO Auto-generated method stub
    LOG.info("Annulé");
  }

  private void performDefeat() {
    // TODO Auto-generated method stub
    LOG.info("Défaite");
  }

  private void performWin() {
    // TODO Auto-generated method stub
    LOG.info("Victoire");
  }

  /**
   * Joue le tour
   */
  private void performTurn() {
    LOG.info("Tour de jeu");

    // récupérer le plateau
    Plateau plateau = client.getBoard(idEquipe);

    LOG.info("Etat joueur1 " + plateau.getPlayer1().getNom() + " = { nbrDePieces = "
        + plateau.getPlayer1().getNbrDePieces() + " } ");
    LOG.info("Etat joueur2 " + plateau.getPlayer2().getNom() + " = { nbrDePieces = "
        + plateau.getPlayer2().getNbrDePieces() + " } ");

    // jouer le coup
    Coords coords = new Coords(1, 1);

    String result = client.play(idEquipe, idPartie, coords);

    switch (result) {
      case "GAGNE":
        performWin();
      case "KO":
        performDefeat();
      case "OK":
      case "PTT":
      default:
        play();
    }
  }

  public void newPractice(Level level) {
    LOG.info("Initialisation d'une partie en practice de niveau " + level);

    String id = client.newPractice(idEquipe, level);

    if (StringUtils.isEmpty(id) || "NA".equals(id)) {
      throw new EngineException("La partie n'a pas pu être initialisée, arrêt du moteur");
    }

    LOG.info("Identifiant de la partie = " + id);

    idPartie = id;
  }

  public void checkConnection() {
    // test de connectivité avant tout
    if (!"pong".equals(client.ping())) {
      throw new EngineException("Impossible de pinger le serveur de jeu, arrêt du moteur");
    }
  }

  public void initEquipe(String nomEquipe, String motDePasse) {
    Assert.notNull(nomEquipe);
    Assert.notNull(motDePasse);

    String id = client.getIdEquipe(nomEquipe, motDePasse);

    if (StringUtils.isEmpty(id)) {
      throw new EngineException("L'id d'équipe renvoyé est vide");
    }

    LOG.info("Identifiant de l'équipe = " + id);

    // enregistrement dans le moteur
    this.idEquipe = id;
  }
}

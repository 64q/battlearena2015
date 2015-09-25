package its.raining.battlearena.engine;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import its.raining.battlearena.client.BattlearenaClient;
import its.raining.battlearena.exception.EngineException;
import its.raining.battlearena.model.Coords;
import its.raining.battlearena.model.GameType;
import its.raining.battlearena.model.Level;
import its.raining.battlearena.model.Plateau;

/**
 * Moteur de jeu de la Battlearena
 * 
 * <p>
 * <b>Exemple d'utilisation</b>
 * </p>
 * 
 * <code>
 * engine.init("test", "test").run(GameType.VERSUS);
 * </code>
 */
@Component
public class BattlearenaEngine {

  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(BattlearenaEngine.class);

  /** Client Rest */
  @Autowired
  private BattlearenaClient client;

  /** Nom de l'équipe */
  private String nomEquipe;

  /** Mot de passe */
  private String motDePasse;

  /** Id equipe du serveur */
  private String idEquipe;

  /** Id de la partie */
  private String idPartie;

  /**
   * Initialise le moteur avec le nom de l'équipe et le mot de passe
   * 
   * @param nomEquipe
   * @param motDePasse
   * @return le moteur de jeu pour chainage
   */
  public BattlearenaEngine init(String nomEquipe, String motDePasse) {
    this.nomEquipe = nomEquipe;
    this.motDePasse = motDePasse;

    return this;
  }

  /**
   * Démarre une nouvelle partie
   * 
   * @param type
   */
  public void run(GameType type) {
    run(type, null);
  }

  /**
   * Démarre une partie
   * 
   * @param type
   * @param level
   * @return le moteur de jeu
   */
  public void run(GameType type, Level level) {
    Assert.notNull(type);

    // préparation du moteur
    setup(nomEquipe, motDePasse);

    // lancement de la partie
    if (type == GameType.TRAINING) {
      runPractice(level);
    } else {
      runVersus();
    }

    LOG.info("Fin de l'exécution du run");
  }

  /**
   * Démarre un match contre une autre équipe
   * 
   * <p>
   * Récupère le prochain match avec l'appel Rest next match
   * </p>
   */
  public void runVersus() {
    // récupération du prochain match à jouer
    nextVersus();

    // démarrage de la partie
    play();
  }

  /**
   * Initialise le prochain match
   */
  private void nextVersus() {
    String id = client.nextVersus(idEquipe);

    if (StringUtils.isEmpty(id) || "NA".equals(id)) {
      throw new EngineException(
          "L'identifiant du prochain match n'est pas exploitable, arrêt du moteur");
    }

    idPartie = id;

    LOG.info("Prochain match d'id = " + idPartie);
  }

  /**
   * Démarrage un match d'entrainement
   * 
   * @param level niveau du match contre l'IA
   */
  public void runPractice(Level level) {
    // passage en practice
    newPractice(level);

    // démarrage de la partie
    play();
  }

  /**
   * Joue un coup dans la partie, méthode récursive s'exécutant tant que la partie n'est pas
   * terminée
   */
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
        // le cas ANNULE ne peut survenir qu'en match contre une IA
        performCancel();
        break;
      case "OUI":
        performTurn();
      case "NON":
      default:
        play();
    }

    LOG.info("Fin de la partie " + idPartie + " sur l'état = " + status);
  }

  /**
   * Action d'annulation de la partie
   */
  private void performCancel() {
    // TODO Auto-generated method stub
    LOG.info("Annulé");
  }

  /**
   * Action de défaite
   */
  private void performDefeat() {
    // TODO Auto-generated method stub
    LOG.info("Défaite");
  }

  /**
   * Action de victoire
   */
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
    Coords coords = new Coords("1", "1");

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

    idPartie = id;

    LOG.info("Identifiant de la partie = " + idPartie);
  }

  /**
   * Teste la connectivité avec le serveur
   */
  public void checkConnection() {
    if (!"pong".equals(client.ping())) {
      throw new EngineException("Impossible de pinger le serveur de jeu, arrêt du moteur");
    }

    LOG.info("Test de connectivité avec le serveur OK");
  }

  /**
   * Méthode permettant de préparer une partie
   * 
   * @param nomEquipe
   * @param motDePasse
   */
  private void setup(String nomEquipe, String motDePasse) {
    Assert.notNull(nomEquipe);
    Assert.notNull(motDePasse);

    // vérification de la connexion
    checkConnection();

    String id = client.getIdEquipe(nomEquipe, motDePasse);

    if (StringUtils.isEmpty(id)) {
      throw new EngineException("L'id d'équipe renvoyé est vide");
    }

    LOG.info("Identifiant de l'équipe = " + id);

    // enregistrement dans le moteur
    this.idEquipe = id;
  }
}

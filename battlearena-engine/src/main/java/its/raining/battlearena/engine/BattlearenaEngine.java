package its.raining.battlearena.engine;

import its.raining.battlearena.engine.ai.Ai;
import its.raining.battlearena.engine.client.BattlearenaClient;
import its.raining.battlearena.engine.exception.EngineException;
import its.raining.battlearena.engine.model.Board;
import its.raining.battlearena.engine.model.EngineVars;
import its.raining.battlearena.engine.model.Level;
import its.raining.battlearena.engine.model.Mode;
import its.raining.battlearena.engine.model.Move;
import its.raining.battlearena.engine.model.PlayOutcome;
import its.raining.battlearena.engine.model.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Moteur de jeu de la Battlearena
 * 
 * <p>
 * <b>Exemples d'utilisation</b>
 * </p>
 * <p>
 * Pour le versus
 * </p>
 * <code>
 * engine.init("test", "test").runFlowVersus();
 * </code>
 * <p>
 * Pour le practice
 * </p>
 * <code>
 * engine.init("test", "test").runFlowPractice(Level.EASY);
 * </code>
 */
@Component
public class BattlearenaEngine {

  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(BattlearenaEngine.class);

  /** Client Rest */
  @Autowired
  private BattlearenaClient client;

  @Autowired
  @Qualifier(value = "basicAi")
  private Ai ai;

  // -- paramètres du moteur de jeu

  /** Nom de l'équipe */
  private String nomEquipe;

  /** Mot de passe */
  private String motDePasse;

  /** Id equipe du serveur */
  private String idEquipe;

  /** Id de la partie */
  private String idPartie;

  /** Id de l'adversaire */
  private String idAdversaire;

  /** Mode de jeu */
  private Mode mode;

  /** Level de l'IA */
  private Level level;

  // --

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

    // préparation du moteur
    stepGetIdEquipe(this.nomEquipe, this.motDePasse);

    return this;
  }

  /**
   * Démarre un match contre une autre équipe
   * 
   * <p>
   * Récupère le prochain match avec l'appel Rest next match
   * </p>
   */
  public void runFlowVersus() {
    Assert.notNull(idEquipe);

    LOG.info("Début de l'exécution des versus");

    // enregistrement du mode de jeu
    this.mode = Mode.VERSUS;
    // récupération du prochain match à jouer
    nextVersus();
    // démarrage de la partie
    stepGetStatus();

    LOG.info("Fin de l'exécution des versus");
  }

  /**
   * Initialise le prochain match
   */
  protected void nextVersus() {
    String id = client.nextVersus(idEquipe);

    // Cas non géré, on arrête tout
    if (StringUtils.isEmpty(id)) {
      throw new EngineException(
          "L'identifiant du prochain match n'est pas exploitable, arrêt du moteur");
    }

    // On attend d'avoir une partie
    if ("NA".equals(id)) {
      nextVersus();
    }

    idPartie = id;
    // TODO doit être implémenté, comment le récupère-t-on ?
    idAdversaire = "tobeimplemented";

    LOG.info("Prochain match d'id = " + idPartie);
  }

  /**
   * Démarrage un match d'entrainement
   * 
   * @param level niveau du match contre l'IA
   */
  public void runFlowPractice(Level level) {
    Assert.notNull(idEquipe);

    LOG.info("Début de l'exécution du practice");

    // enregistrement paramètres de partie
    this.mode = Mode.PRACTICE;
    this.level = level;
    // passage en practice
    newPractice(level);
    // démarrage de la partie
    stepGetStatus();

    LOG.info("Fin de l'exécution du practice");
  }

  /**
   * Joue un coup dans la partie, méthode récursive s'exécutant tant que la partie n'est pas
   * terminée
   */
  protected void stepGetStatus() {
    Status status = client.getStatus(idEquipe, idPartie);

    switch (status) {
      case VICTORY:
        performVictory();
        break;
      case DEFEAT:
        performDefeat();
        break;
      case CANCELLED:
        performCancelled();
        break;
      case CANPLAY:
        LOG.debug("C'est notre tour");
        stepGetBoard();
        break;
      case CANTPLAY:
      default:
        LOG.debug("Ce n'est pas notre tour");
        stepGetStatus();
    }

    // LOG.info("Fin de la partie " + idPartie + " sur l'état = " + status);
  }

  /**
   * Action d'annulation de la partie
   * 
   * <p>
   * <b>NB :</b> le status Cancelled ne peut survenir qu'en match contre l'IA
   * </p>
   */
  protected void performCancelled() {
    LOG.info("Match contre l'IA annulé, arrêt du moteur");
  }

  /**
   * Action de défaite
   */
  protected void performDefeat() {
    LOG.info("Défaite du match " + idPartie + " contre " + idAdversaire);

    // fin
  }

  /**
   * Action de victoire
   */
  protected void performVictory() {
    LOG.info("Victoire du match " + idPartie + " contre " + idAdversaire);

    // fin
  }

  /**
   * Joue le tour
   */
  protected void stepGetBoard() {
    LOG.info("Tour de jeu");

    // récupérer le plateau
    Board plateau = client.getBoard(idPartie);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Tour de jeu = " + plateau.getNbrActionLeft());
      LOG.debug("Etat joueur1 " + plateau.getPlayer1());
      LOG.debug("Etat joueur2 " + plateau.getPlayer2());
    }

    stepPlay(plateau);
  }

  /**
   * Etape où jouer un coup
   * 
   * @param plateau
   */
  protected void stepPlay(Board plateau) {

    // récupère le dernier coup de l'adversaire
    Move lastMove = client.getLastMove(idPartie, idEquipe);

    // calcul du prochain coup
    Move currentMove = ai.play(plateau, lastMove);

    // joue le coup
    PlayOutcome result = client.play(idEquipe, idPartie, currentMove);

    switch (result) {
      case GAMEOVER:
      case FORBIDDEN:
        performDefeat();
        break;
      case OK:
      case NOT_YET:
      default:
        stepGetStatus();
    }
  }

  /**
   * Initialise un match Practice
   * 
   * @param level
   */
  protected void newPractice(Level level) {
    LOG.info("Initialisation d'une partie en practice de niveau " + level);

    String id = client.newPractice(idEquipe, level);

    // Cas non géré, on arrête tout
    if (StringUtils.isEmpty(id)) {
      throw new EngineException("La partie n'a pas pu être initialisée, arrêt du moteur");
    }

    // On attend d'avoir une partie
    if ("NA".equals(id)) {
      newPractice(level);
    }

    idPartie = id;
    idAdversaire = "IA Battlearena";

    LOG.info("Identifiant de la partie = " + idPartie);
  }

  /**
   * Teste la connectivité avec le serveur
   */
  public void checkConnection() {
    if (!"pong".equals(client.ping())) {
      throw new EngineException("Impossible de pinger le serveur de jeu, arrêt du moteur");
    }

    LOG.info("Test de connectivité avec le serveur ... OK");
  }

  /**
   * Méthode permettant de préparer une partie
   * 
   * @param nomEquipe
   * @param motDePasse
   */
  protected void stepGetIdEquipe(String nomEquipe, String motDePasse) {
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

  /**
   * Récupère les variables intéressantes du moteur pour les redistribuer
   * 
   * @return variables du moteur
   */
  public EngineVars getVars() {
    EngineVars vars = new EngineVars();

    vars.setIdEquipe(idEquipe);
    vars.setIdPartie(idPartie);
    vars.setMode(mode);

    if (null != idEquipe && null != idPartie) {
      // ajout du statut uniquement si on est en partie
      vars.setStatus(client.getStatus(idEquipe, idPartie));
    }

    if (null != idAdversaire) {
      vars.setIdAdversaire(idAdversaire);
    }

    if (null != level) {
      vars.setLevel(level);
    }

    return vars;
  }
}

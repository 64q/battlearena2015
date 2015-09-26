package its.raining.battlearena.engine;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import its.raining.battlearena.engine.client.BattlearenaClient;
import its.raining.battlearena.engine.exception.EngineException;
import its.raining.battlearena.engine.model.Coords;
import its.raining.battlearena.engine.model.EngineVars;
import its.raining.battlearena.engine.model.Level;
import its.raining.battlearena.engine.model.Plateau;

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
 * engine.init("test", "test").run();
 * </code>
 * <p>
 * Pour le training
 * </p>
 * <code>
 * engine.init("test", "test").run(Level.EASY);
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

  /** Id de l'adversaire */
  private String idAdversaire;

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
    setup(this.nomEquipe, this.motDePasse);

    return this;
  }

  /**
   * Démarre une nouvelle partie versus
   * 
   * @param type
   */
  public void run() {
    run(null);
  }

  /**
   * Démarre une partie training si level n'est pas nul
   * 
   * @param level
   */
  public void run(Level level) {

    if (null == level) {
      runVersus();
    } else {
      runPractice(level);
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
    // TODO doit être implémenté
    idAdversaire = "tobeimplemented";

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
  protected void play() {
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
  protected void performCancel() {
    // TODO Auto-generated method stub
    LOG.info("Annulé");
  }

  /**
   * Action de défaite
   */
  protected void performDefeat() {
    // TODO Auto-generated method stub
    LOG.info("Défaite");
  }

  /**
   * Action de victoire
   */
  protected void performWin() {
    // TODO Auto-generated method stub
    LOG.info("Victoire");
  }

  /**
   * Joue le tour
   */
  protected void performTurn() {
    LOG.info("Tour de jeu");

    // récupérer le plateau
    Plateau plateau = client.getBoard(idEquipe);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Etat joueur1 " + plateau.getPlayer1().getNom() + " = { nbrDePieces = "
          + plateau.getPlayer1().getNbrDePieces() + " } ");
      LOG.debug("Etat joueur2 " + plateau.getPlayer2().getNom() + " = { nbrDePieces = "
          + plateau.getPlayer2().getNbrDePieces() + " } ");
    }

    // jouer le coup
    Coords coords = new Coords("1", "1");

    // définition de la prochaine action à effectuer en fonction du résultat du coup
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

    // Cas non géré, on arrête tout
    if (StringUtils.isEmpty(id)) {
      throw new EngineException("La partie n'a pas pu être initialisée, arrêt du moteur");
    }

    // On attend d'avoir une partie
    if ("NA".equals(id)) {
      newPractice(level);
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
  protected void setup(String nomEquipe, String motDePasse) {
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

    if (null != idEquipe && null != idPartie) {
      // ajout du statut uniquement si on est en partie
      vars.setStatus(client.getStatus(idEquipe, idPartie));
    }

    if (null != idAdversaire) {
      vars.setIdAdversaire(idAdversaire);
    }

    return vars;
  }
}

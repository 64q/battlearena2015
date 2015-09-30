package its.raining.battlearena.engine.commandline;

import its.raining.battlearena.engine.BattlearenaEngine;
import its.raining.battlearena.engine.model.Level;
import its.raining.battlearena.engine.model.Mode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Point d'entrée en ligne de commande du moteur de jeu
 */
public class CommandLine {

  // Constantes à configurer

  /** Nom d'équipe */
  public static final String NOM_EQUIPE = "It's Raining";

  /** Mot de passe */
  public static final String MOT_DE_PASSE = "IRhdjks187";

  /** Mode de jeu */
  public static final Mode MODE = Mode.VERSUS;

  /** Level IA */
  public static final Level LEVEL = Level.SIX;

  // -----------------------

  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(CommandLine.class);

  /** Contexte spring */
  private static AbstractApplicationContext context = new ClassPathXmlApplicationContext(
      "spring-context.xml");

  /**
   * Point d'entrée principal en ligne de commande
   * 
   * @param args
   */
  public static void main(String[] args) {
    LOG.info("======================================");
    LOG.info("Lancement du conteneur Battlearena ...");
    LOG.info("======================================");
    LOG.info("Paramètres de la partie :");
    LOG.info(" - Nom équipe = {}", NOM_EQUIPE);
    LOG.info(" - Mode       = {}", MODE);
    LOG.info(" - Level IA  = {}", LEVEL);
    LOG.info("======================================");

    if (MODE == Mode.PRACTICE) {
      context.getBean(BattlearenaEngine.class).init(NOM_EQUIPE, MOT_DE_PASSE)
          .runFlowPractice(LEVEL);
    } else {
      context.getBean(BattlearenaEngine.class).init(NOM_EQUIPE, MOT_DE_PASSE).runFlowVersus();
    }

    context.close();

    LOG.info("===============================================");
    LOG.info("Fin de l'exécution du conteneur Battlearena ...");
    LOG.info("===============================================");
  }
}

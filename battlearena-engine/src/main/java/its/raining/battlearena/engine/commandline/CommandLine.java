package its.raining.battlearena.engine.commandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import its.raining.battlearena.engine.BattlearenaEngine;
import its.raining.battlearena.engine.model.Mode;
import its.raining.battlearena.engine.model.Niveau;

/**
 * Point d'entrée en ligne de commande du moteur de jeu
 */
public class CommandLine {

  // Constantes à configurer

  /** Nom d'équipe */
  public static final String NOM_EQUIPE = "test";

  /** Mot de passe */
  public static final String MOT_DE_PASSE = "test";

  /** Mode de jeu */
  public static final Mode MODE = Mode.PRACTICE;

  /** Niveau IA */
  public static final Niveau NIVEAU = Niveau.MEDIUM;

  // -----------------------

  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(CommandLine.class);

  /** Contexte spring */
  private static AbstractApplicationContext context =
      new ClassPathXmlApplicationContext("spring-context.xml");

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
    LOG.info(" - Niveau IA  = {}", NIVEAU);
    LOG.info("======================================");

    if (MODE == Mode.PRACTICE) {
      context.getBean(BattlearenaEngine.class).init(NOM_EQUIPE, MOT_DE_PASSE).runPractice(NIVEAU);
    } else {
      context.getBean(BattlearenaEngine.class).init(NOM_EQUIPE, MOT_DE_PASSE).runVersus();
    }

    context.close();

    LOG.info("===============================================");
    LOG.info("Fin de l'exécution du conteneur Battlearena ...");
    LOG.info("===============================================");
  }
}

package its.raining.battlearena.engine.commandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import its.raining.battlearena.engine.BattlearenaEngine;
import its.raining.battlearena.engine.model.GameType;
import its.raining.battlearena.engine.model.Level;

public class CommandLine {

  // Constantes à configurer

  /** Nom d'équipe */
  public static final String NOM_EQUIPE = "test";

  /** Mot de passe */
  public static final String MOT_DE_PASSE = "test";

  /** Mode de jeu */
  public static final GameType MODE = GameType.TRAINING;

  /** Niveau IA */
  public static final Level NIVEAU = Level.MEDIUM;

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
    LOG.info("Lancement du conteneur Battlearena ...");

    if (MODE == GameType.TRAINING) {
      context.getBean(BattlearenaEngine.class).init(NOM_EQUIPE, MOT_DE_PASSE).run(NIVEAU);
    } else {
      context.getBean(BattlearenaEngine.class).init(NOM_EQUIPE, MOT_DE_PASSE).run();
    }

    context.close();

    LOG.info("Fin de l'exécution de la Battlearena ...");
  }
}

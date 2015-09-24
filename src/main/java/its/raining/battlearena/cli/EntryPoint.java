package its.raining.battlearena.cli;

import its.raining.battlearena.engine.BattlearenaEngine;
import its.raining.battlearena.model.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EntryPoint {

  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(EntryPoint.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");

    LOG.info("Lancement du conteneur Battlearena ...");

    context.getBean(BattlearenaEngine.class).init("test", "test").runPractice(Level.MEDIUM);

    LOG.info("Fin de l'exécution de la Battlearena ...");

    context.close();
  }
}

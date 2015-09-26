package its.raining.battlearena.server.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import its.raining.battlearena.engine.BattlearenaEngine;
import its.raining.battlearena.engine.model.Niveau;
import its.raining.battlearena.server.websocket.message.TrainingMessage;
import its.raining.battlearena.server.websocket.message.VersusMessage;

@Component
@EnableAsync
public class AsyncRunner {

  @Autowired
  private BattlearenaEngine engine;

  /**
   * Lancement dans un thread du run de l'engine
   * 
   * @param trainingMessage
   */
  @Async
  public void run(TrainingMessage trainingMessage) {
    engine.runPractice(Niveau.valueOf(trainingMessage.getLevel().toUpperCase()));
  }

  /**
   * Lance un versus contre la prochaine équipe
   * 
   * @param versusMessage
   */
  public void run(VersusMessage versusMessage) {
    engine.runVersus();
  }
}

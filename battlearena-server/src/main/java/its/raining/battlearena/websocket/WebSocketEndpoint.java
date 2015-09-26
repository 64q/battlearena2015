package its.raining.battlearena.websocket;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import its.raining.battlearena.engine.BattlearenaEngine;
import its.raining.battlearena.exception.EngineException;
import its.raining.battlearena.model.EngineVars;
import its.raining.battlearena.websocket.message.ConnecterMessage;
import its.raining.battlearena.websocket.message.Message;
import its.raining.battlearena.websocket.message.TrainingMessage;
import its.raining.battlearena.websocket.message.VersusMessage;
import its.raining.battlearena.websocket.response.ConnecterResponse;
import its.raining.battlearena.websocket.response.ErrorResponse;
import its.raining.battlearena.websocket.response.Response;
import its.raining.battlearena.websocket.response.TrainingResponse;
import its.raining.battlearena.websocket.response.VersusResponse;

@Component
@ServerEndpoint(value = "/ws", encoders = {ResponseTextEncoder.class},
    decoders = {MessageTextDecoder.class}, configurator = SpringConfigurator.class)
public class WebSocketEndpoint {

  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(WebSocketEndpoint.class);

  @Autowired
  private AsyncRunner asyncRunner;

  @Autowired
  private BattlearenaEngine engine;

  @OnOpen
  public void open(Session session) {
    LOG.info("Ouverture d'une nouvelle session = {}", session.getId());
  }

  @OnClose
  public void close(Session session) {
    LOG.info("Fermeture d'une session = {}", session.getId());
  }

  @OnError
  public void onError(Session session, Throwable error) {
    LOG.info("Erreur remontée dans le WebSocket pour le client = {}", session.getId(), error);
  }

  @OnMessage
  public void onMessage(Session session, Message message) throws IOException, EncodeException {
    LOG.info("Réception d'un message de type = {} de la session = {}",
        message.getClass().getSimpleName(), session.getId());

    try {
      if (message instanceof ConnecterMessage) {
        handleConnecter(session, message);
      } else if (message instanceof TrainingMessage) {
        handleTraining(session, message);
      } else if (message instanceof VersusMessage) {
        handleVersus(session, message);
      } else {
        session.getBasicRemote().sendObject(new Response("InvalidCommand"));
      }
    } catch (EngineException e) {
      handleError(session, e);
    }

  }

  /**
   * Handle d'un versus
   * 
   * @param session
   * @param message
   * @throws EncodeException
   * @throws IOException
   */
  private void handleVersus(Session session, Message message) throws IOException, EncodeException {
    VersusMessage versusMessage = (VersusMessage) message;

    asyncRunner.run(versusMessage);

    // temps de sommeil nécessaire pour récupérer l'id de partie vu que nous sommes en asynchrone
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      LOG.warn("Impossible d'attendre 3 secondes", e);
    }

    LOG.info("La partie versus a été lancée, en cours de résolution ...");

    EngineVars vars = engine.getVars();

    VersusResponse response = new VersusResponse();
    response.setIdPartie(vars.getIdPartie());
    response.setIdAdversaire(vars.getIdAdversaire());

    session.getBasicRemote().sendObject(response);
  }

  /**
   * Handle le lancement d'un entrainement
   * 
   * @param session
   * @param message
   * @throws IOException
   * @throws EncodeException
   */
  private void handleTraining(Session session, Message message)
      throws IOException, EncodeException {
    TrainingMessage trainingMessage = (TrainingMessage) message;

    asyncRunner.run(trainingMessage);

    // temps de sommeil nécessaire pour récupérer l'id de partie vu que nous sommes en asynchrone
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      LOG.warn("Impossible d'attendre 3 secondes", e);
    }

    LOG.info("La partie d'entrainement a été lancée, en cours de résolution ...");

    TrainingResponse response = new TrainingResponse();
    response.setIdPartie(engine.getVars().getIdPartie());

    session.getBasicRemote().sendObject(response);
  }

  /**
   * Méthode de gestion des exception du moteur
   * 
   * @param session
   * @param e
   * @throws IOException
   * @throws EncodeException
   */
  private void handleError(Session session, EngineException e) throws IOException, EncodeException {
    session.getBasicRemote().sendObject(new ErrorResponse(e.getMessage()));
  }

  /**
   * Handle la connexion
   * 
   * @param session
   * @param message
   * @throws IOException
   * @throws EncodeException
   */
  private void handleConnecter(Session session, Message message)
      throws IOException, EncodeException {
    ConnecterMessage connecterMessage = (ConnecterMessage) message;

    // initialisation du moteur de jeu
    engine.init(connecterMessage.getNomEquipe(), connecterMessage.getMotDePasse());

    ConnecterResponse response = new ConnecterResponse();
    response.setIdEquipe(engine.getVars().getIdEquipe());

    session.getBasicRemote().sendObject(response);
  }
}

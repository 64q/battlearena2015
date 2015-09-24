package its.raining.battlearena.client;

import its.raining.battlearena.exception.ClientException;
import its.raining.battlearena.generated.BattlearenaIo_TestWs;
import its.raining.battlearena.model.Coords;
import its.raining.battlearena.model.Level;
import its.raining.battlearena.model.Plateau;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BattlearenaClient {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * @return pong, si le serveur répond
   */
  public String ping() {
    return BattlearenaIo_TestWs.root().ping().getAsTextPlain(String.class);
  }

  public String getIdEquipe(String nomEquipe, String motDePasse) {
    return BattlearenaIo_TestWs.root().playerGetIdEquipeNomEquipeMotDePasse(nomEquipe, motDePasse)
        .getAsTextPlain(String.class);
  }

  public String newPractice(String idEquipe, Level level) {
    return BattlearenaIo_TestWs.root().practiceNewLevelIdEquipe(level.getCode(), idEquipe)
        .getAsTextPlain(String.class);
  }

  public String getStatus(String idEquipe, String idPartie) {
    return BattlearenaIo_TestWs.root().gameStatusIdPartieIdEquipe(idPartie, idEquipe)
        .getAsTextPlain(String.class);
  }

  public Plateau getBoard(String idPartie) {
    String result = BattlearenaIo_TestWs.root().gameBoardIdPartie(idPartie).getAs(String.class);

    try {
      return MAPPER.readValue(result, Plateau.class);
    } catch (IOException e) {
      throw new ClientException("Erreur au parsing du flux JSON", e);
    }
  }

  public String play(String idEquipe, String idPartie, Coords coords) {
    return BattlearenaIo_TestWs.root()
        .gamePlayIdPartieIdEquipeCoordXCoordY(idPartie, idEquipe, coords.x, coords.y)
        .getAsTextPlain(String.class);
  }
}

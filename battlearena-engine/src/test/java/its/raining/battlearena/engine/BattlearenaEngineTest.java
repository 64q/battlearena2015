package its.raining.battlearena.engine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import its.raining.battlearena.engine.client.BattlearenaClient;
import its.raining.battlearena.engine.exception.EngineException;

/**
 * Tests unitaires du moteur, teste principalement le workflow
 */
@RunWith(value = MockitoJUnitRunner.class)
public class BattlearenaEngineTest {

  @Mock
  private BattlearenaClient client;

  @InjectMocks
  private BattlearenaEngine engine;

  /**
   * Test passant ping
   */
  @Test
  public void testCheckConnectionPassant() {
    Mockito.when(client.ping()).thenReturn("pong");
    engine.checkConnection();
  }

  /**
   * Test non passant ping
   */
  @Test(expected = EngineException.class)
  public void testCheckConnectionNonPassant() {
    Mockito.when(client.ping()).thenReturn("invalide");
    engine.checkConnection();
  }
}

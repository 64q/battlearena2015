package its.raining.battlearena.client;

import org.junit.Assert;
import org.junit.Test;

import its.raining.battlearena.engine.client.BattlearenaClient;

/**
 * Tests d'intégration du client Battlearena
 */
public class BattlearenaClientTest {

  /** Client à tester */
  private final BattlearenaClient client = new BattlearenaClient();

  /**
   * Test passant du ping
   */
  @Test
  public void testPing() {
    Assert.assertEquals("pong", client.ping());
  }
}

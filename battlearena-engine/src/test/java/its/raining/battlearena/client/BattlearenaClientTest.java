package its.raining.battlearena.client;

import org.junit.Assert;
import org.junit.Test;

import its.raining.battlearena.engine.client.BattlearenaClient;

/**
 * Tests d'intégration du client Battlearena
 */
public class BattlearenaClientTest {

  private final BattlearenaClient client = new BattlearenaClient();

  @Test
  public void testPing() {
    Assert.assertEquals("pong", client.ping());
  }
}

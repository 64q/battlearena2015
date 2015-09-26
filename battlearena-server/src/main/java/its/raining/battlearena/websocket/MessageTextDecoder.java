package its.raining.battlearena.websocket;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import its.raining.battlearena.websocket.message.Message;

public class MessageTextDecoder implements Decoder.Text<Message> {
  /** Logger */
  private static final Logger LOG = LoggerFactory.getLogger(MessageTextDecoder.class);

  /** Mapper JSON */
  private final static ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();
  }

  @Override
  public void init(EndpointConfig config) {

  }

  @Override
  public void destroy() {

  }

  @Override
  public Message decode(String s) throws DecodeException {
    try {
      Message message = MAPPER.readValue(s, Message.class);
      return message;
    } catch (IOException e) {
      LOG.error("Erreur de déserialisation", e);
      throw new DecodeException(s, "Erreur lors de la déserialisation", e);
    }
  }

  @Override
  public boolean willDecode(String s) {
    boolean isValid = true;

    try {
      MAPPER.readValue(s, Message.class);
    } catch (IOException e) {
      LOG.error("Erreur lors du décodage, l'objet n'est pas valide", e);

      isValid = false;
    }

    return isValid;
  }

}

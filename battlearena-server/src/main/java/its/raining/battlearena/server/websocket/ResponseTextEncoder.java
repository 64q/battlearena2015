package its.raining.battlearena.server.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import its.raining.battlearena.server.websocket.response.Response;

public class ResponseTextEncoder implements Encoder.Text<Response> {

  /** Mapper JSON */
  private final static ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();
    // N'inclus pas les NULL
    MAPPER.setSerializationInclusion(Include.NON_NULL);
  }

  @Override
  public void init(EndpointConfig config) {

  }

  @Override
  public void destroy() {

  }

  @Override
  public String encode(Response response) throws EncodeException {
    try {
      return MAPPER.writeValueAsString(response);
    } catch (JsonProcessingException e) {
      throw new EncodeException(response, "Erreur à la sérialisation", e);
    }
  }

}

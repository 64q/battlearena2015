package its.raining.battlearena.engine.exception;

public class ClientException extends RuntimeException {

  private static final long serialVersionUID = -4451870095488381171L;

  public ClientException(String message) {
    super(message);
  }

  public ClientException(String message, Exception e) {
    super(message, e);
  }

}

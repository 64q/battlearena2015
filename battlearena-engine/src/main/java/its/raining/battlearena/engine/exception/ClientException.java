package its.raining.battlearena.engine.exception;


public class ClientException extends RuntimeException {

  public ClientException(String message, Exception e) {
    super(message, e);
  }

  private static final long serialVersionUID = -4451870095488381171L;

}

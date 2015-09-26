package its.raining.battlearena.engine.exception;

public class EngineException extends RuntimeException {

  private static final long serialVersionUID = 3239108378018686823L;

  public EngineException(String message) {
    super(message);
  }

  public EngineException(String message, Exception e) {
    super(message, e);
  }
}

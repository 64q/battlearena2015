package its.raining.battlearena.websocket.response;

public class ErrorResponse extends Response {
  private String message;

  public ErrorResponse(String message) {
    this.setType("ErrorResponse");
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


}

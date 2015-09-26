package its.raining.battlearena.server.websocket.response;

public class Response {
  private String type;

  public Response() {

  }

  public Response(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

}

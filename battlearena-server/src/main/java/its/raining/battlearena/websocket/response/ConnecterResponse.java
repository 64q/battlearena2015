package its.raining.battlearena.websocket.response;

public class ConnecterResponse extends Response {
  private String idEquipe;

  public ConnecterResponse() {
    this.setType("ConnecterResponse");
  }

  public String getIdEquipe() {
    return this.idEquipe;
  }

  public void setIdEquipe(String idEquipe) {
    this.idEquipe = idEquipe;
  }

}

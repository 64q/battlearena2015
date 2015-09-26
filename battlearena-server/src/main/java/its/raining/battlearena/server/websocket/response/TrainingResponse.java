package its.raining.battlearena.server.websocket.response;

public class TrainingResponse extends Response {
  private String idPartie;

  public TrainingResponse() {
    this.setType("TrainingResponse");
  }

  public String getIdPartie() {
    return this.idPartie;
  }

  public void setIdPartie(String idPartie) {
    this.idPartie = idPartie;
  }

}

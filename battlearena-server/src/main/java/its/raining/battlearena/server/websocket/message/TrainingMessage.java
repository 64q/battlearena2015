package its.raining.battlearena.server.websocket.message;

public class TrainingMessage extends Message {
  private String level;

  private String idEquipe;

  public String getLevel() {
    return this.level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getIdEquipe() {
    return this.idEquipe;
  }

  public void setIdEquipe(String idEquipe) {
    this.idEquipe = idEquipe;
  }


}

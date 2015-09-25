package its.raining.battlearena.websocket.response;

public class VersusResponse extends Response {
  private String idPartie;

  private String idAdversaire;

  public VersusResponse() {
    this.setType("VersusResponse");
  }

  public String getIdPartie() {
    return this.idPartie;
  }

  public void setIdPartie(String idPartie) {
    this.idPartie = idPartie;
  }

  public String getIdAdversaire() {
    return this.idAdversaire;
  }

  public void setIdAdversaire(String idAdversaire) {
    this.idAdversaire = idAdversaire;
  }


}

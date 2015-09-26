package its.raining.battlearena.server.websocket.message;

public class ConnecterMessage extends Message {
  private String nomEquipe;

  private String motDePasse;

  public String getNomEquipe() {
    return this.nomEquipe;
  }

  public void setNomEquipe(String nomEquipe) {
    this.nomEquipe = nomEquipe;
  }

  public String getMotDePasse() {
    return this.motDePasse;
  }

  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }


}

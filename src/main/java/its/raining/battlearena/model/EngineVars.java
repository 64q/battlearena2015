package its.raining.battlearena.model;

public class EngineVars {

  /** Id équipe */
  private String idEquipe;

  /** Id partie */
  private String idPartie;

  /** Id adversaire */
  private String idAdversaire;

  /** Status */
  private String status;

  public String getIdEquipe() {
    return this.idEquipe;
  }

  public void setIdEquipe(String idEquipe) {
    this.idEquipe = idEquipe;
  }

  public String getIdPartie() {
    return this.idPartie;
  }

  public void setIdPartie(String idPartie) {
    this.idPartie = idPartie;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIdAdversaire() {
    return this.idAdversaire;
  }

  public void setIdAdversaire(String idAdversaire) {
    this.idAdversaire = idAdversaire;
  }

}

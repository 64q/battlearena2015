package its.raining.battlearena.engine.model;

public class EngineVars {

  /** Id équipe */
  private String idEquipe;

  /** Id partie */
  private String idPartie;

  /** Id adversaire */
  private String idAdversaire;

  /** Status */
  private Status status;

  /** Mode */
  private Mode mode;

  /** Level */
  private Level level;

  public Mode getMode() {
    return this.mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public Level getLevel() {
    return this.level;
  }

  public void setLevel(Level niveau) {
    this.level = niveau;
  }

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

  public Status getStatus() {
    return this.status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getIdAdversaire() {
    return this.idAdversaire;
  }

  public void setIdAdversaire(String idAdversaire) {
    this.idAdversaire = idAdversaire;
  }

}

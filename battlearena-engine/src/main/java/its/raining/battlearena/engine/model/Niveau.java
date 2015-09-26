package its.raining.battlearena.engine.model;

public enum Niveau {
  EASY("easy"), MEDIUM("medium"), HARD("hard");

  private String code;

  Niveau(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

}

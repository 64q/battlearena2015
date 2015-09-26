package its.raining.battlearena.engine.model;

public enum Level {
  EASY("easy"), MEDIUM("medium"), HARD("hard");

  private String code;

  Level(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

}

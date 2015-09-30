package its.raining.battlearena.engine.model;

public enum Level {
  ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6");

  private String code;

  Level(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

}

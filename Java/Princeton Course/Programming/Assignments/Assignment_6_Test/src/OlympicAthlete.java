import java.util.LinkedHashMap;

public class OlympicAthlete {
  private String name;
  private int age;
  private String game;

  public OlympicAthlete(String name, int age, String game) {
    this.name = name;
    this.age = age;
    this.game = game;
  }

  LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
  /*@Override
  public boolean equals(Object that) {
    if (this == that) return true;
    OlympicAthlete other = (OlympicAthlete) that;
     return this.age == other.age && this.name == other.name && this.game == other.game;
  }*/

  @Override
  public int hashCode() {
    return name.hashCode() + Integer.hashCode(age) + game.hashCode();
  }

}

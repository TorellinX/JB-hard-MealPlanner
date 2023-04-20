package mealplanner.plan;

public enum Weekday {
  MONDAY, TUESDAY
  , WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
  ;

  @Override
  public String toString() {
    return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
  }

}

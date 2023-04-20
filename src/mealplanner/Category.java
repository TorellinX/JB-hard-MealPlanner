package mealplanner;

public enum Category {
  BREAKFAST, LUNCH, DINNER;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}

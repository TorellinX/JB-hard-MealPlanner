package mealplanner;

import java.util.ArrayList;
import java.util.List;

public class Meals {

  List<Meal> breakfasts = new ArrayList<>();
  List<Meal> lunches = new ArrayList<>();
  List<Meal> dinners = new ArrayList<>();

  List<Meal> getMealsFor(Category category) {
    return switch (category) {
      case BREAKFAST -> breakfasts;
      case DINNER -> dinners;
      case LUNCH -> lunches;
    };
  }

  int getNumberOfMeals() {
    return breakfasts.size() + lunches.size() + dinners.size();
  }

  public Meal getMealById(int id) {
    for (Category category : Category.values()) {
      for (Meal meal : getMealsFor(category)) {
        if (meal.getId() == id) {
          return meal;
        }
      }
    }
    return null;
  }

}

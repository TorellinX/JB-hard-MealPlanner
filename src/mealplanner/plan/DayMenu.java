package mealplanner.plan;

import mealplanner.Category;
import mealplanner.Meal;

public class DayMenu {

  Meal breakfast;
  Meal lunch;
  Meal dinner;

  DayMenu(Meal breakfast, Meal lunch, Meal dinner) {
    this.breakfast = breakfast;
    this.lunch = lunch;
    this.dinner = dinner;
  }

  public Meal getBreakfast() {
    return breakfast;
  }

  public Meal getLunch() {
    return lunch;
  }

  public Meal getDinner() {
    return dinner;
  }

  public Meal getMealFor(Category category) {
    return switch (category) {
      case BREAKFAST -> getBreakfast();
      case LUNCH -> getLunch();
      case DINNER -> getDinner();
    };
  }


  public void setBreakfast(Meal breakfast) {
    this.breakfast = breakfast;
  }

  public void setLunch(Meal lunch) {
    this.lunch = lunch;
  }

  public void setDinner(Meal dinner) {
    this.dinner = dinner;
  }

  public void setMealFor(Category category, Meal meal) {
    switch (category) {
      case BREAKFAST -> setBreakfast(meal);
      case LUNCH -> setLunch(meal);
      case DINNER -> setDinner(meal);
    }
  }


  @Override
  public String toString() {
    return  "Breakfast: " + breakfast.getName() + "\n"
        + "Lunch: " + lunch.getName() + "\n"
        + "Dinner: " + dinner.getName() + "\n";
  }

}

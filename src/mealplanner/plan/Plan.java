package mealplanner.plan;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mealplanner.Category;
import mealplanner.DBManager;
import mealplanner.Meal;

public class Plan {
  Map<Weekday, DayMenu> plan = new HashMap<>();


  public void setMealFor(Weekday day, Category category, Meal meal) {
    DayMenu menu = plan.get(day);
    if (menu == null) {
      menu = new DayMenu(null, null, null);
      plan.put(day, menu);
    }
    menu.setMealFor(category, meal);
  }

  public Meal getMealFor(Weekday day, Category category) {
    return plan.get(day).getMealFor(category);
  }

  public void saveToDB() {
    try {
      DBManager.savePlan(this);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public ArrayList<String> countIngredients() {
    ArrayList<String> ingredientsForPlan = new ArrayList<>();
    for (Weekday day : Weekday.values()) {
      for (Category category : Category.values()) {
        ingredientsForPlan.addAll(plan.get(day).getMealFor(category).getIngredients());
      }
    }
    return ingredientsForPlan;
  }



  public boolean isEmpty() {
    return plan.isEmpty();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Weekday day : Weekday.values()) {
      builder.append(day).append("\n");
      builder.append(plan.get(day).toString());
      builder.append("\n");
    }
    return builder.toString();
  }



}

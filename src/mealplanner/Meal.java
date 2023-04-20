package mealplanner;

import java.util.List;

public class Meal {

  final Category category;
  final String name;
  final List<String> ingredients;
  final int id;

  Meal(Category category, String name, List<String> ingredients, int id) {
    this.category = category;
    this.name = name;
    this.ingredients = ingredients;
    this.id = id;
  }

  public Category getCategory() {
    return category;
  }

  public String getName() {
    return name;
  }

  public List<String> getIngredients() {
    return ingredients;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Name: ").append(name).append("\n");
    builder.append("Ingredients:\n");
    for (String ingredient : ingredients) {
      builder.append(ingredient).append("\n");
    }
    return builder.toString();
  }
}

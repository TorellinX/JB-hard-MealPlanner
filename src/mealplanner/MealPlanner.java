package mealplanner;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mealplanner.plan.Plan;
import mealplanner.plan.ShoppingList;
import mealplanner.plan.Weekday;

public class MealPlanner {

  InputManager inputManager = new InputManager();
  // List<Meal> meals = new ArrayList<>();
//  List<Meal> breakfasts = new ArrayList<>();
//  List<Meal> lunches = new ArrayList<>();
//  List<Meal> dinners = new ArrayList<>();
  Meals meals = new Meals();
  Plan plan = new Plan();
  ShoppingList shoppingList;

  boolean running = true;

  void start() {
    startDB();
    while (running) {
      handleCommand();
    }
  }

  private void startDB() {
    try {
      DBManager.init();
      List <Meal> mealsToAdd = DBManager.getMeals();
      for (Meal meal : mealsToAdd) {
        meals.getMealsFor(meal.getCategory()).add(meal);
      }
      plan = DBManager.getPlan(meals);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      exit();
    }
  }

  private void handleCommand() {
    switch (requestAction()) {
      case ADD -> addMeal();
      case SHOW -> showMeals();
      case PLAN -> planMeals();
      case SAVE -> saveShoppingList();
      case EXIT -> exit();
    }

  }

  private Command requestAction() {
    while (true) {
      System.out.println("What would you like to do (add, show, plan, save, exit)?");
      try {
        return Command.valueOf(inputManager.getInput().toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
  }


  private void addMeal() {
    Category category = requestMealCategory();
    String name = requestNewMealName();
    List<String> ingredients = requestNewMealIngredients();
    Meal newMeal = new Meal(category, name, ingredients, meals.getNumberOfMeals());
    // TODO: check that the mealId is unique
    // meals.add(newMeal);
    meals.getMealsFor(category).add(newMeal);
    try {
      DBManager.addMeal(newMeal);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("The meal has been added!");

  }

  private Category requestMealCategory() {
    System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
    while (true) {
      try {
        return Category.valueOf(inputManager.getInput().toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println(
            "Wrong meal category! Choose from: breakfast, lunch, dinner.");
      }
    }
  }

  private String requestNewMealName() {
    System.out.println("Input the meal's name:");
    while (true) {
      String name = inputManager.getInput();
      if (name == null || name.isBlank() || name.matches(".*[\\d!-@].*")) {
        System.out.println("Wrong format. Use letters only!");
        continue;
      }
      return name;
    }
  }

  private List<String> requestNewMealIngredients() {
    System.out.println("Input the ingredients:");
    while (true) {
      String ingredientsInput = inputManager.getInput();
      if(ingredientsInput.matches(".*((,\\s*,)|[\\d!-+--@]).*") ||
      ingredientsInput.endsWith(",")) {
        System.out.println("Wrong format. Use letters only!");
        continue;
      }
      String[] ingredients = ingredientsInput.split("\\s*,\\s*");
      if (ingredients.length == 0) {
        System.out.println("\"Empty input. Please enter the ingredients separated by commas:\"");
        continue;
      }
      return List.of(ingredients);
    }
  }

  private void showMeals() {
    Category category = requestCategoryToPrint();
    if (meals.getMealsFor(category).isEmpty()) {
      System.out.println("No meals found.");
      return;
    }
    System.out.printf("Category: %s%n", category);
    for (Meal meal : meals.getMealsFor(category)) {
        System.out.println();
        System.out.print(meal);
    }
    System.out.println();
  }

  private Category requestCategoryToPrint() {
    System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
    Category category;
    while (true) {
      try {
        category = Category.valueOf(inputManager.getInput().toUpperCase());
        break;
      } catch (IllegalArgumentException e) {
        System.out.println(
            "Wrong meal category! Choose from: breakfast, lunch, dinner.");
      }
    }
    return category;
  }

  private void planMeals() {
    shoppingList = new ShoppingList();
    for (Weekday day : Weekday.values()) {
      System.out.println(day);
      for (Category category : Category.values()) {
        printMealsFor(category);
        Meal meal = requestToChooseMeal(day, category);
        plan.setMealFor(day, category, meal);
      }
      System.out.printf("Yeah! We planned the meals for %s.%n%n", day);
    }
    countIngredients();
    plan.saveToDB();
    printPlan();
  }

  private Meal requestToChooseMeal(Weekday day, Category category) {
    System.out.printf("Choose the %s for %s from the list above:%n", category, day);
    while (true) {
      String mealNameInput = inputManager.getInput();
      for (Meal meal : meals.getMealsFor(category)) {
        if (meal.getName().equals(mealNameInput)) {
          return meal;
        }
      }
      System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
    }
  }

  private void printMealsFor(Category category) {
    List<Meal> mealsOneCat = new ArrayList<>(meals.getMealsFor(category));
    mealsOneCat.sort(Comparator.comparing(Meal::getName));
    for (Meal meal : mealsOneCat) {
      System.out.println(meal.getName());
    }
  }

//  private List<Meal> getMealsFor(Category category) {
//    return switch (category) {
//      case BREAKFAST -> breakfasts;
//      case DINNER -> dinners;
//      case LUNCH -> lunches;
//    };
//  }

  private void printPlan() {
    System.out.println(plan);
  }

  public void countIngredients() {
    for (String ingredient : plan.countIngredients()) {
      shoppingList.addIngredient(ingredient);
    }
  }

  private void saveShoppingList() {
    if (plan.isEmpty()) {
      System.out.println("Unable to save. Plan your meals first.");
      return;
    }
    if (shoppingList == null || shoppingList.isEmpty()) {
      shoppingList = new ShoppingList();
      countIngredients();
    }
    System.out.println("Input a filename:");
    String fileName = inputManager.getInput();
    boolean saved = shoppingList.saveToFile(fileName);
    if (saved) {
      System.out.println("Saved!");
    }
  }

  private void exit() {
    System.out.println("Bye!");
    running = false;
  }

}

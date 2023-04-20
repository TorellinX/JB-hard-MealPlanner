package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mealplanner.plan.Plan;
import mealplanner.plan.Weekday;

public class DBManager {
  final static String DB_URL = "jdbc:postgresql:meals_db";
  final static String USER = "postgres";
  final static String PASS = "1111";

  public static void init() throws SQLException {
    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    connection.setAutoCommit(true);

    Statement statement = connection.createStatement();
    // statement.executeUpdate("drop table if exists meals");
    // statement.executeUpdate("drop table if exists ingredients");
    statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals (" +
        "category VARCHAR(15) NOT NULL," +
        "meal VARCHAR(50)," +
        "meal_id INT NOT NULL" +
        ")");
    statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients (" +
        "ingredient VARCHAR(15) NOT NULL," +
        "ingredient_id INT NOT NULL," +
        "meal_id INT NOT NULL" +
        ")");
    statement.executeUpdate("CREATE TABLE IF NOT EXISTS plan (" +
        "option VARCHAR(15) NOT NULL," +
        "category VARCHAR(15) NOT NULL," +
        "meal_id INT NOT NULL" +
        ")");
    statement.close();
    connection.close();
  }

  public static void addMeal(Meal meal) throws SQLException {
    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    connection.setAutoCommit(true);

    Statement statement = connection.createStatement();
    statement.executeUpdate(String.format("INSERT INTO meals (category, meal, meal_id) " +
        "VALUES ('%s','%s',%d)", meal.getCategory(), meal.getName(), meal.getId()));
    for (int i = 0; i < meal.getIngredients().size(); i++) {
      statement.executeUpdate(String.format("INSERT INTO ingredients (ingredient, ingredient_id, meal_id) " +
          "values ('%s',%d,%d)", meal.getIngredients().get(i), i, meal.getId()));
    }

    statement.close();
    connection.close();
  }

  public static void savePlan(Plan plan) throws SQLException {
    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    connection.setAutoCommit(true);

    // option (day), category, meal_id
    Statement statement = connection.createStatement();
    statement.executeUpdate("DELETE FROM plan");

    for (Weekday day : Weekday.values()) {
      for (Category category : Category.values()) {
        Meal meal = plan.getMealFor(day, category);
        statement.executeUpdate(String.format("INSERT INTO plan (option, category, meal_id) " +
            "values ('%s','%s',%d)", day, category, meal.getId()));
      }
    }

    statement.close();
    connection.close();
  }

  public static List<Meal> getMeals() throws SQLException {
    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    connection.setAutoCommit(true);

    Statement statement = connection.createStatement();

    ResultSet rs = statement.executeQuery("select * from meals");
    ArrayList<Integer> mealIds = new ArrayList<>();
    HashMap<Integer, Category> categories = new HashMap<>(); // mealId, category
    HashMap<Integer, String> mealNames = new HashMap<>(); // mealId, meal name


    while (rs.next()) {
      Category category = Category.valueOf(rs.getString("category").toUpperCase());
      String name = rs.getString("meal");
      int mealId = rs.getInt("meal_id");
      categories.put(mealId, category);
      mealNames.put(mealId, name);
      mealIds.add(mealId);
    }

    rs = statement.executeQuery("select * from ingredients");
    HashMap<Integer,List<String>> ingredients = new HashMap<>(); // mealId, ingredients
    while (rs.next()) {
      String ingredientName = rs.getString("ingredient");
      int ingredientIndex = rs.getInt("ingredient_id");
      int mealId = rs.getInt("meal_id");
      List<String> ingredientsProMeal;
      ingredientsProMeal = ingredients.get(mealId);
      if (ingredientsProMeal == null) {
        ingredients.put(mealId, new ArrayList<>());
        ingredientsProMeal = ingredients.get(mealId);
      }
      ingredientsProMeal.add(ingredientIndex, ingredientName);
    }

    List<Meal> meals = new ArrayList<>();
    for (int mealId : mealIds) {
      meals.add(new Meal(categories.get(mealId), mealNames.get(mealId), ingredients.get(mealId), mealId));
    }

    statement.close();
    connection.close();
    return meals;
  }

  public static Plan getPlan(Meals meals) throws SQLException {
    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    connection.setAutoCommit(true);

    Statement statement = connection.createStatement();

    ResultSet rs = statement.executeQuery("select * from plan");
    Plan plan = new Plan();
    while (rs.next()) {
      // option, category, meal_id
      Weekday day = Weekday.valueOf(rs.getString("option").toUpperCase());
      Category category = Category.valueOf(rs.getString("category").toUpperCase());
      int mealId = rs.getInt("meal_id");
      plan.setMealFor(day, category, meals.getMealById(mealId));
    }

    statement.close();
    connection.close();
    return plan;
  }
}

package mealplanner.plan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ShoppingList {
  HashMap<String, Integer> list = new HashMap<>();


  public void addIngredient(String ingredient) {
    list.put(ingredient, list.getOrDefault(ingredient, 0) + 1);
  }

  public boolean saveToFile(String fileName) {
    File file = new File(fileName);
    try {
      file.createNewFile();

    } catch (IOException e) {
      System.out.println("Cannot create the file: " + file.getPath());
      return false;
    }

    try (FileWriter writer = new FileWriter(file)){
      writer.write(this.toString());
      return true;
    } catch (IOException e) {
      System.out.println("Cannot save to the file: " + file.getPath());
      return false;
    }
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (String ingredient : list.keySet()) {
      builder.append(ingredient);
      if (list.get(ingredient) > 1) {
        builder.append(" x").append(list.get(ingredient));
      }
      builder.append("\n");
    }
    return builder.toString();
  }

}

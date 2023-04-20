package mealplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputManager {

  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

  String getInput() {
    String line;
    while (true) {
      try {
        line = reader.readLine().strip();
        if (line == null) {
          System.out.println("Error!");
          continue;
        }
      } catch (IOException e) {
        continue;
      }
      return line;
    }
  }

}

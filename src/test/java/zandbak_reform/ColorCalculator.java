package zandbak_reform;

import javafx.scene.paint.Color;

public class ColorCalculator {

  public static Color calculateColor(int colorIndex) {
    int numColors = 8; // Total number of colors in the palette
    int baseHue = 30; // Starting hue value

    // Calculate the hue value based on the index
    int hue = (baseHue + (colorIndex * (360 / numColors))) % 360;

    // Convert the hue value to an RGB color
    Color color = Color.hsb(hue, 1.0, 1.0);

    return color;
  }

  public static void main(String[] args) {
    // int colorIndex = 2; // Replace with the desired color index
    for (int i = 0; i < 11; i++) {
      Color calculatedColor = calculateColor(i);
      System.out.println("Calculated Color: " + calculatedColor.toString());
    }
  }
}

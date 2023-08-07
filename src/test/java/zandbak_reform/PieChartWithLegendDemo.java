package zandbak_reform;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import kwee.gnucashcharts.library.html.ReadHTMLTable;
import kwee.gnucashcharts.library.html.TaartPuntData;

public class PieChartWithLegendDemo extends Application {
  int i = 0;
  double tot_amt = 0.0;

  @Override
  public void start(Stage primaryStage) {
    // String filePath =
    // "G:\\Users\\René\\OneDrive\\Documenten\\Administraties\\Samenvatting.html";
    // // Replace this with
    String filePath = "G:\\Users\\René\\OneDrive\\Documenten\\Administraties\\SamenvattingKH Kwee.html"; // Replace this
                                                                                                         // with
    ReadHTMLTable htmltable = new ReadHTMLTable(filePath);
    ArrayList<String> regels = htmltable.parseHTMLpage();

    TaartPuntData pieData = new TaartPuntData(regels);
//    Set<String> tags = pieData.getTags();
    // String tag = "Inkomen";
    // String tag = "Type";
    String tag = "Kosten";

    // Create a pie chart
    PieChart pieChart = createPieChart(pieData.getPieSlices(tag));

    // Create a GridPane to hold the legend
    GridPane legendGrid = createLegendGrid(pieChart.getData());

    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(pieChart, legendGrid);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 800, 600);

    // Set the title of the window
    tot_amt = calcTotAmount(pieChart.getData());
    double number = Math.round(tot_amt * 100);
    number = number / 100;
    primaryStage.setTitle("Pie Chart " + tag + " (totaal €" + number + ")");

    // Set the scene to the stage
    primaryStage.setScene(scene);

    // Show the stage (display the pie chart with the legend)
    primaryStage.show();
    pieChart = adjustColors(pieChart);
    pieChart.setLegendVisible(false);
  }

  private PieChart createPieChart(Map<String, Double> taartpuntdata) {
    PieChart.Data[] pieChartData = new PieChart.Data[taartpuntdata.size()];

    i = 0;
    Set<String> keys = taartpuntdata.keySet();
    keys.forEach(key -> {
      pieChartData[i] = new PieChart.Data(key, taartpuntdata.get(key));
      i++;
    });

    // Create the pie chart and add the data slices to it
    PieChart pieChart = new PieChart();
    pieChart.getData().addAll(pieChartData);

    return pieChart;
  }

  private PieChart adjustColors(PieChart a_piechart) {
    PieChart l_PieChart = new PieChart();
    l_PieChart = a_piechart;
    ObservableList<Data> observableList = l_PieChart.getData();
    i = 0;
    observableList.forEach(item -> {
      Color Kleur = getDistinctColor(i, observableList.size());
      item.getNode().setStyle("-fx-pie-color: " + toHex(Kleur) + ";");
      i++;
    });
    return l_PieChart;
  }

  private double calcTotAmount(ObservableList<Data> observableList) {
    tot_amt = 0.0;
    observableList.forEach(item -> {
      tot_amt = tot_amt + item.getPieValue();
    });
    return tot_amt;
  }

  private GridPane createLegendGrid(ObservableList<Data> observableList) {
    PieChart.Data[] pieChartData = new PieChart.Data[observableList.size()];
    i = 0;
    tot_amt = 0.0;
    observableList.forEach(item -> {
      pieChartData[i] = item;
      tot_amt = tot_amt + item.getPieValue();
      i++;
    });

    GridPane gridPane = new GridPane();
    gridPane.setCenterShape(true);
    gridPane.setHgap(20);
    gridPane.setVgap(5);

    int row = 0;
    int col = 0;
    i = 0;
    for (PieChart.Data data : pieChartData) {
      double amt = data.getPieValue();
      int perc = (int) ((amt / tot_amt) * 10000.0);
      Color Kleur = getDistinctColor(i, observableList.size());

      Label colorBox = new Label();
      colorBox.setPrefSize(15, 15);
      colorBox.setStyle("-fx-background-color: " + toHex(Kleur) + ";");

      Label label = new Label(data.getName() + " - " + perc / 100.0 + "%");
      label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

      gridPane.add(colorBox, col * 2, row);
      gridPane.add(label, col * 2 + 1, row);

      col++;
      if (col == 3) {
        col = 0;
        row++;
      }
      i++;
    }
    return gridPane;
  }

  private Color getDistinctColor(int index, int lengte) {
    return calculateColor(index, lengte);
  }

  private static Color calculateColor(int colorIndex, int numColors) {
    int baseHue = 30; // Starting hue value
    // Calculate the hue value based on the index
    int hue = (baseHue + (colorIndex * (360 / numColors))) % 360;
    // Convert the hue value to an RGB color
    Color color = Color.hsb(hue, 1.0, 1.0);
    return color;
  }

  private String toHex(Color color) {
    return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

  public static void main(String[] args) {
    launch(args);
  }
}

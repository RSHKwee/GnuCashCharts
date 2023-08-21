package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StackedBarChartWithArrayData extends Application {

  @Override
  public void start(Stage primaryStage) {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

    stackedBarChart.setTitle("Stacked Bar Chart Example");
    xAxis.setLabel("Category");
    yAxis.setLabel("Value");

    // Create an array of data
    String[] categories = { "A", "B", "C" };
    int[][] dataArray = { { 10, 20, 30 }, { 15, 25, 35 } };

    XYChart.Series<String, Number>[] seriesArray = new XYChart.Series[dataArray.length];

    for (int i = 0; i < dataArray.length; i++) {
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName("Series " + (i + 1));

      for (int j = 0; j < categories.length; j++) {
        series.getData().add(new XYChart.Data<>(categories[j], dataArray[i][j]));
      }

      seriesArray[i] = series;
    }

    stackedBarChart.getData().addAll(seriesArray);

    Scene scene = new Scene(stackedBarChart, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Stacked Bar Chart Example");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

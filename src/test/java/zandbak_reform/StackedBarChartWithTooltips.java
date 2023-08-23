package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class StackedBarChartWithTooltips extends Application {

  @Override
  public void start(Stage primaryStage) {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

    stackedBarChart.setTitle("Stacked Bar Chart with Tooltips");
    xAxis.setLabel("Category");
    yAxis.setLabel("Value");

    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.setName("Series 1");
    XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    series2.setName("Series 2");

    String[] categories = { "A", "B", "C" };
    int[][] dataArray = { { 10, 15 }, { 20, 25 }, { 30, 35 } };

    for (int i = 0; i < categories.length; i++) {
      XYChart.Data<String, Number> data1 = new XYChart.Data<>(categories[i], dataArray[i][0]);
      XYChart.Data<String, Number> data2 = new XYChart.Data<>(categories[i], dataArray[i][1]);

      Node nod = data1.getNode();
      series1.getData().add(data1);
      series2.getData().add(data2);

      // Create tooltips for each data point
      Tooltip tooltip1 = new Tooltip("Value: " + dataArray[i][0]);
      Tooltip tooltip2 = new Tooltip("Value: " + dataArray[i][1]);

      Tooltip.install(data1.getNode(), tooltip1);
      Tooltip.install(data2.getNode(), tooltip2);

    }

    stackedBarChart.getData().addAll(series1, series2);

    // Set tooltips to always be visible
    stackedBarChart.setStyle("-fx-tooltip-visible: true;");

    Scene scene = new Scene(stackedBarChart, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Stacked Bar Chart with Tooltips");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

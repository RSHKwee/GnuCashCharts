package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StackedBarChartExample extends Application {

  @Override
  public void start(Stage primaryStage) {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

    stackedBarChart.setTitle("Stacked Bar Chart Example");
    xAxis.setLabel("Category");
    yAxis.setLabel("Value");

    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.setName("Series 1");
    series1.getData().add(new XYChart.Data<>("A", 10));
    series1.getData().add(new XYChart.Data<>("B", 20));
    series1.getData().add(new XYChart.Data<>("C", 30));

    XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    series2.setName("Series 2");
    series2.getData().add(new XYChart.Data<>("A", 15));
    series2.getData().add(new XYChart.Data<>("B", 25));
    series2.getData().add(new XYChart.Data<>("C", 35));
    // series2.

    stackedBarChart.getData().addAll(series1, series2);

    Scene scene = new Scene(stackedBarChart, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Stacked Bar Chart Example");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

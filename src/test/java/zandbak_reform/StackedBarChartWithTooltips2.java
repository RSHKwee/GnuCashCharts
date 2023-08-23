package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class StackedBarChartWithTooltips2 extends Application {
  @Override
  public void start(Stage primaryStage) {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
    stackedBarChart.setTitle("Stacked Bar Chart Example");

    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.setName("Series 1");
    series1.getData().add(new XYChart.Data<>("Category 1", 10));
    series1.getData().add(new XYChart.Data<>("Category 2", 20));
    series1.getData().add(new XYChart.Data<>("Category 3", 30));

    XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    series2.setName("Series 2");
    series2.getData().add(new XYChart.Data<>("Category 1", 15));
    series2.getData().add(new XYChart.Data<>("Category 2", 25));
    series2.getData().add(new XYChart.Data<>("Category 3", 35));

    stackedBarChart.getData().addAll(series1, series2);

    // Create tooltips for the data points
    for (XYChart.Series<String, Number> series : stackedBarChart.getData()) {
      for (XYChart.Data<String, Number> data : series.getData()) {
        Tooltip tooltip = new Tooltip("Value: " + data.getYValue());
        Node nod = data.getNode();
        Tooltip.install(data.getNode(), tooltip);
      }
    }

    Scene scene = new Scene(stackedBarChart, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

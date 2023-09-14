package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CombinedBarChartExample extends Application {

  @Override
  public void start(Stage stage) {
    // Create the X and Y axes
    try {
      CategoryAxis xAxis = new CategoryAxis();
      NumberAxis yAxis = new NumberAxis();

      // Create the BarChart
      BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
      barChart.setTitle("Combined Bar Chart Example");

      // Create a series for the category
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName("Category");

      // Add positive and negative values for the same category
      series.getData().add(new XYChart.Data<>("Category 1", 10));
      series.getData().add(new XYChart.Data<>("Category 2", 20));
      series.getData().add(new XYChart.Data<>("Category 1", -5));
      series.getData().add(new XYChart.Data<>("Category 2", -15));

      // Add the series to the chart
      barChart.getData().add(series);

      // Create a scene and add the chart to it
      Scene scene = new Scene(barChart, 800, 600);
      stage.setScene(scene);

      // Show the stage
      stage.show();

      // Manually create stacked bars
      Pane chartPane = (Pane) barChart.lookup(".chart-plot-background");
      for (int i = 0; i < series.getData().size(); i++) {
        XYChart.Data<String, Number> data = series.getData().get(i);
        double x = xAxis.getDisplayPosition(data.getXValue());
        double y = yAxis.getDisplayPosition(0);
        double height = Math.abs(yAxis.getDisplayPosition(data.getYValue()) - y);
        double width = xAxis.getCategorySpacing() * 0.7; // Adjust this value for spacing
        Rectangle rect = new Rectangle(x - width / 2, y, width, height);
        if (data.getYValue().doubleValue() >= 0) {
          rect.setFill(Color.GREEN);
        } else {
          rect.setFill(Color.RED);
        }
        chartPane.getChildren().add(rect);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

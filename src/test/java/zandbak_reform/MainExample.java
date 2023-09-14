package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class MainExample extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  // Start the javafx application
  @Override
  public void start(Stage stage) {

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();

    XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

    // modify behavior to counter bug
    StackedBarChart<String, Number> barChart = new StackedBarChart<String, Number>(xAxis, yAxis) {
      @Override
      protected void dataItemAdded(XYChart.Series<String, Number> series, int itemIndex,
          XYChart.Data<String, Number> item) {
        super.dataItemAdded(series, itemIndex, item);

        Node bar = item.getNode();
        double barVal = item.getYValue().doubleValue();

        if (barVal < 0) {
          bar.getStyleClass().add("negative");
        }
      }
    };

    // add series
    barChart.getData().addAll(series);

    // THEN add data
    series.getData().add(new XYChart.Data<String, Number>(0 + "", 5));
    series.getData().add(new XYChart.Data<String, Number>(1 + "", -5));
    series.getData().add(new XYChart.Data<String, Number>(0 + "", 3));
    series.getData().add(new XYChart.Data<String, Number>(1 + "", -2));
    series.getData().add(new XYChart.Data<String, Number>(0 + "", 3));
    series.getData().add(new XYChart.Data<String, Number>(1 + "", -2));

    Scene scene = new Scene(barChart, 500, 500);
    stage.setScene(scene);
    stage.show();
  }

}

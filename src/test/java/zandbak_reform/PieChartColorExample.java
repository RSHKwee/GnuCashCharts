package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PieChartColorExample extends Application {

  @Override
  public void start(Stage primaryStage) {
    // Create data slices
    PieChart.Data slice1 = new PieChart.Data("Category 1", 30);
    PieChart.Data slice2 = new PieChart.Data("Category 2", 20);
    PieChart.Data slice3 = new PieChart.Data("Category 3", 50);

    // Create the PieChart
    PieChart pieChart = new PieChart();
    pieChart.getData().addAll(slice1, slice2, slice3);

    // Create the scene and add the PieChart to it
    Scene scene = new Scene(pieChart, 800, 600);

    // Set the scene and show the stage
    primaryStage.setScene(scene);
    primaryStage.setTitle("PieChart Color Example");
    primaryStage.show();

    // Set colors for the data slices after the chart is rendered
    slice1.getNode().setStyle("-fx-pie-color: red;");
    slice2.getNode().setStyle("-fx-pie-color: green;");
    slice3.getNode().setStyle("-fx-pie-color: blue;");
  }

  public static void main(String[] args) {
    launch(args);
  }
}

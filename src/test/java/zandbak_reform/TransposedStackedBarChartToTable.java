package zandbak_reform;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TransposedStackedBarChartToTable extends Application {

  @Override
  public void start(Stage primaryStage) {
    // Create StackedBarChart
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

    // Sample data for StackedBarChart
    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.setName("Category 1");
    series1.getData().add(new XYChart.Data<>("A", 10));
    series1.getData().add(new XYChart.Data<>("B", 20));
    series1.getData().add(new XYChart.Data<>("C", 30));

    XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    series2.setName("Category 2");
    series2.getData().add(new XYChart.Data<>("A", 5));
    series2.getData().add(new XYChart.Data<>("B", 15));
    series2.getData().add(new XYChart.Data<>("C", 25));

    stackedBarChart.getData().addAll(series1, series2);

    // Create TableView
    TableView<XYChart.Data<String, Number>> tableView = new TableView<>();
    ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
    tableView.setItems(data);

    // Create columns in TableView
    for (XYChart.Series<String, Number> series : stackedBarChart.getData()) {
      for (XYChart.Data<String, Number> item : series.getData()) {
        TableColumn<XYChart.Data<String, Number>, Number> column = new TableColumn<>(
            series.getName() + " - " + item.getXValue());
        column.setCellValueFactory(cellData -> item.YValueProperty());
        tableView.getColumns().add(column);
        data.add(item);
      }
    }

    // Create a layout to display both the StackedBarChart and TableView
    HBox root = new HBox(stackedBarChart, tableView);

    Scene scene = new Scene(root, 800, 400);

    primaryStage.setTitle("StackedBarChart to TableView Example");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

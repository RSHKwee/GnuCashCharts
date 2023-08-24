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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StackedBarChartToTableViewExample extends Application {

  @Override
  public void start(Stage primaryStage) {
    // Sample data for StackedBarChart
    ObservableList<Data> chartData = FXCollections.observableArrayList(new Data("Category 1", 10, 5),
        new Data("Category 2", 20, 15), new Data("Category 3", 30, 25));

    // Create StackedBarChart
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.setName("Value 1");
    XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    series2.setName("Value 2");

    // Populate StackedBarChart data
    for (Data data : chartData) {
      series1.getData().add(new XYChart.Data<>(data.getCategory(), data.getValue1()));
      series2.getData().add(new XYChart.Data<>(data.getCategory(), data.getValue2()));
    }

    stackedBarChart.getData().addAll(series1, series2);

    // Create TableView
    TableView<Data> tableView = new TableView<>();
    tableView.setItems(chartData);

    // Create columns in TableView for categories
    TableColumn<Data, String> categoryColumn = new TableColumn<>("Category");
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    tableView.getColumns().add(categoryColumn);

    // Create columns in TableView for values
    TableColumn<Data, Number> value1Column = new TableColumn<>("Value 1");
    value1Column.setCellValueFactory(new PropertyValueFactory<>("value1"));
    tableView.getColumns().add(value1Column);

    TableColumn<Data, Number> value2Column = new TableColumn<>("Value 2");
    value2Column.setCellValueFactory(new PropertyValueFactory<>("value2"));
    tableView.getColumns().add(value2Column);

    // Create a layout to display the StackedBarChart and TableView side by side
    HBox root = new HBox(stackedBarChart, tableView);

    Scene scene = new Scene(root, 800, 400);

    primaryStage.setTitle("StackedBarChart and TableView Example");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  public static class Data {
    private final String category;
    private final Number value1;
    private final Number value2;

    public Data(String category, Number value1, Number value2) {
      this.category = category;
      this.value1 = value1;
      this.value2 = value2;
    }

    public String getCategory() {
      return category;
    }

    public Number getValue1() {
      return value1;
    }

    public Number getValue2() {
      return value2;
    }
  }
}

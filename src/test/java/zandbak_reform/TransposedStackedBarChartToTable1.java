package zandbak_reform;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransposedStackedBarChartToTable1 extends Application {

  @Override
  public void start(Stage stage) {
    // Create a StackedBarChart
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

    // Add data to the chart (Replace this with your data)
    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.setName("Series 1");
    series1.getData().add(new XYChart.Data<>("Category A", 10));
    series1.getData().add(new XYChart.Data<>("Category B", 20));

    XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    series2.setName("Series 2");
    series2.getData().add(new XYChart.Data<>("Category A", 30));
    series2.getData().add(new XYChart.Data<>("Category B", 15));

    stackedBarChart.getData().addAll(series1, series2);

    // Create an ObservableList to hold the transposed data
    ObservableList<TransposedData> transposedData = FXCollections.observableArrayList();

    for (XYChart.Data<String, Number> data : series1.getData()) {
      String category = data.getXValue();
      Number value1 = data.getYValue();
      Number value2 = series2.getData().stream().filter(d -> d.getXValue().equals(category)).findFirst()
          .map(XYChart.Data::getYValue).orElse(0);

      transposedData.add(new TransposedData(category, value1, value2));
    }

    // Create a TableView
    TableView<TransposedData> tableView = new TableView<>(transposedData);

    // Create columns for the TableView
    TableColumn<TransposedData, String> categoryColumn = new TableColumn<>("Category");
    categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

    TableColumn<TransposedData, Number> series1Column = new TableColumn<>("Series 1");
    series1Column.setCellValueFactory(cellData -> cellData.getValue().value1Property());

    TableColumn<TransposedData, Number> series2Column = new TableColumn<>("Series 2");
    series2Column.setCellValueFactory(cellData -> cellData.getValue().value2Property());

    tableView.getColumns().addAll(categoryColumn, series1Column, series2Column);

    // Create a VBox to hold the chart and table
    VBox vbox = new VBox(stackedBarChart, tableView);

    Scene scene = new Scene(vbox, 600, 400);
    stage.setScene(scene);
    stage.show();
  }

  public static class TransposedData {
    private final String category;
    private final Number value1;
    private final Number value2;

    public TransposedData(String category, Number value1, Number value2) {
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

    // Property getters for JavaFX binding
    public StringProperty categoryProperty() {
      return new SimpleStringProperty(category);
    }

    public ObjectProperty<Number> value1Property() {
      return new SimpleObjectProperty<>(value1);
    }

    public ObjectProperty<Number> value2Property() {
      return new SimpleObjectProperty<>(value2);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

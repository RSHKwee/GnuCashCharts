package zandbak_reform;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StackedBarChartToTable extends Application {

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

        // Create a TableView
        TableView<XYChart.Data<String, Number>> tableView = new TableView<>();

        // Create columns for the TableView (one column for each series)
        for (XYChart.Series<String, Number> series : stackedBarChart.getData()) {
            TableColumn<XYChart.Data<String, Number>, Number> column = new TableColumn<>(series.getName());
            column.setCellValueFactory(cellData -> cellData.getValue().YValueProperty());
            tableView.getColumns().add(column);
        }

        // Add the data from the StackedBarChart to the TableView
        tableView.getItems().addAll(series1.getData()); // You can choose the series you want to display

        // Create a VBox to hold the chart and table
        VBox vbox = new VBox(stackedBarChart, tableView);

        Scene scene = new Scene(vbox, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


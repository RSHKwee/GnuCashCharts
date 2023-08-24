package zandbak_reform;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TransposedStackedBarChartToTable extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Sample data for StackedBarChart
        ObservableList<Data> chartData = FXCollections.observableArrayList(
            new Data("Category 1", 10, 5),
            new Data("Category 2", 20, 15),
            new Data("Category 3", 30, 25)
        );

        // Create TableView
        TableView<Data> tableView = new TableView<>();
        tableView.setItems(chartData);

        // Create columns in TableView for categories
        TableColumn<Data, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        tableView.getColumns().add(categoryColumn);

        // Create columns in TableView for numbers
        TableColumn<Data, Number> value1Column = new TableColumn<>("Value 1");
        value1Column.setCellValueFactory(cellData -> cellData.getValue().value1Property());
        tableView.getColumns().add(value1Column);

        TableColumn<Data, Number> value2Column = new TableColumn<>("Value 2");
        value2Column.setCellValueFactory(cellData -> cellData.getValue().value2Property());
        tableView.getColumns().add(value2Column);

        // Create a layout to display the TableView
        VBox root = new VBox(tableView);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("StackedBarChart to TableView Example");
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

        public ReadOnlyStringWrapper categoryProperty() {
            return new ReadOnlyStringWrapper(category);
        }

        public ObjectProperty<Number> value1Property() {
            return new SimpleObjectProperty<>(value1);
        }

        public ObjectProperty<Number> value2Property() {
            return new SimpleObjectProperty<>(value2);
        }
    
    }
}

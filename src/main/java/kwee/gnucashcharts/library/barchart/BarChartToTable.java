package kwee.gnucashcharts.library.barchart;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kwee.gnucashcharts.library.FormatAmount;

public class BarChartToTable {
  StackedBarChart<String, Number> stackedBarChart;
  TableView<TableCell[]> tableView = new TableView<>();

  public BarChartToTable(StackedBarChart<String, Number> a_BarChart) {
    stackedBarChart = a_BarChart;
    toTable();
  }

  private void toTable() {
    ObservableList<Series<String, Number>> series1 = stackedBarChart.getData();
    ObservableList<Data<String, Number>> data1 = series1.get(0).getData();

    double[][] l_table = new double[series1.size()][data1.size()];
    String[] x_Header = new String[series1.size()];
    ArrayList<String> y_Header = new ArrayList<String>();
    // double[][] l_table = new double[data1.size()][series1.size()];
    // String[] y_Header = new String[series1.size()];
    // ArrayList<String> x_Header = new ArrayList<String>();

    int x = 0;
    int y = 0;
    for (XYChart.Series<String, Number> series : stackedBarChart.getData()) {
      x_Header[x] = series.getName();
      y = 0;
      for (XYChart.Data<String, Number> data : series.getData()) {
        String l_xlab = data.getXValue();
        y_Header.add(y, l_xlab);
        double l_amt = (double) data.getYValue();
        l_table[x][y] = l_amt;
        y++;
      }
      x++;
    }

    // Inside your JavaFX application class
    ObservableList<TableCell[]> data = FXCollections.observableArrayList();

    // Populate the ObservableList with your double[][] data
    for (double[] row : l_table) {
      TableCell[] rowData = Arrays.stream(row).mapToObj(TableCell::new).toArray(TableCell[]::new);
      data.add(rowData);
    }

    // Inside your JavaFX application class
    for (int i = 0; i < l_table[0].length; i++) {
      TableColumn<TableCell[], Double> column = new TableColumn<>(y_Header.get(i));

      column.setCellValueFactory(TableCell.getCellValueFactory(i));
      tableView.getColumns().add(column);
    }

    tableView.setItems(data);
  }

  public Scene getScene(VBox SaveDialog) {
    SaveDialog.setAlignment(Pos.CENTER_RIGHT); // Set alignment to right

    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(tableView, SaveDialog);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 800, 600);
    // TODO
    // adjustColors();
//    m_BarChart.setLegendVisible(false);

    return scene;
  }

  public Scene getScene() {
    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(tableView);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 800, 600);
//TODO
    // adjustColors();
    // m_BarChart.setLegendVisible(false);

    return scene;
  }
}

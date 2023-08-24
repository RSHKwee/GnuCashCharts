package kwee.gnucashcharts.library.barchart;

import java.util.Arrays;
import java.util.SortedMap;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.VBox;

import kwee.gnucashcharts.library.FormatAmount;

public class BarChartToTable {
  StackedBarChart<String, Number> stackedBarChart;
  TableView<TableCell[]> tableView = new TableView<>();
  SortedMap<String, Double> m_DateTotAmt;

  public BarChartToTable(StackedBarChart<String, Number> a_BarChart, SortedMap<String, Double> a_DateTotAmt) {
    stackedBarChart = a_BarChart;
    m_DateTotAmt = a_DateTotAmt;
    toTable();
  }

  private void toTable() {
    ObservableList<Series<String, Number>> series1 = stackedBarChart.getData();
    ObservableList<Data<String, Number>> data1 = series1.get(0).getData();

    String[][] l_table = new String[(series1.size() + 1)][(data1.size() + 1)];
    String[] x_Header = new String[series1.size() + 1];
    String[] y_Header = new String[(data1.size() + 1)];

    int x = 0;
    int y = 0;
    for (XYChart.Series<String, Number> series : stackedBarChart.getData()) {
      x_Header[x] = series.getName();
      y = 0;
      for (XYChart.Data<String, Number> data : series.getData()) {
        if (y == 0) {
          l_table[x][y] = series.getName();
          y_Header[y] = "Account";
          y++;
        }
        String l_xlab = data.getXValue();
        y_Header[y] = l_xlab;
        double l_amt = (double) data.getYValue();
        l_table[x][y] = FormatAmount.formatAmount(l_amt);
        y++;
      }
      x++;
    }

    // Add Totals
    for (int i = 0; i < (data1.size() + 1); i++) {
      if (i == 0) {
        l_table[x][i] = "Total:";
      } else {
        double l_amt = m_DateTotAmt.get(y_Header[i]);
        l_table[x][i] = FormatAmount.formatAmount(l_amt);
      }
    }

    // Inside your JavaFX application class
    ObservableList<TableCell[]> data = FXCollections.observableArrayList();

    // Populate the ObservableList with your String[][] data
    for (String[] row : l_table) {
      TableCell[] rowData = Arrays.stream(row).map(TableCell::new).toArray(TableCell[]::new);
      data.add(rowData);
    }

    // Inside your JavaFX application class
    for (int i = 0; i < l_table[0].length; i++) {
      TableColumn<TableCell[], String> column = new TableColumn<>(y_Header[i]);
      final int columnIndex = i;
      column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[columnIndex].getValue()));
      if (i != 0) {
        column.setStyle("-fx-alignment: CENTER-RIGHT;");
      }
      tableView.getColumns().add(column);
    }
    tableView.setItems(data);
  }

  public Scene getScene(VBox SaveDialog) {
    SaveDialog.setAlignment(Pos.CENTER_RIGHT); // Set alignment to right

    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(tableView, SaveDialog);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 1000, 500);
    return scene;
  }

  public Scene getScene() {
    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(tableView);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 1000, 500);
    return scene;
  }
}

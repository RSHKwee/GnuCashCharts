package kwee.gnucashcharts.library.barchart;

import java.util.Arrays;
import java.util.SortedMap;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.SnapshotParameters;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

import kwee.gnucashcharts.library.FormatAmount;

public class BarChartToTableScene {
  private StackedBarChart<String, Number> m_stackedBarChart;
  private TableView<OwnTableCell[]> m_tableView = new TableView<>();
  private SortedMap<String, Double> m_DateTotAmt;
  private int m_NumberColomns;
  private String m_StartPeriod = "";
  private String m_EndPeriod = "";

  public BarChartToTableScene(StackedBarChart<String, Number> a_BarChart, SortedMap<String, Double> a_DateTotAmt) {
    m_stackedBarChart = a_BarChart;
    m_DateTotAmt = a_DateTotAmt;
    toTable();
  }

  public VBox getVBox() {
    return new VBox(m_tableView);
  }

  public TableView<OwnTableCell[]> getTable() {
    return m_tableView;
  }

  public WritableImage getTableViewImage() {
    // Convert the BarChart to a WritableImage
    SnapshotParameters params = new SnapshotParameters();
    params.setDepthBuffer(true);
    WritableImage tableViewImage = m_tableView.snapshot(null, null);
    return tableViewImage;
  }

  public String get_StartPeriod() {
    return m_StartPeriod;
  }

  public String get_EndPeriod() {
    return m_EndPeriod;
  }

  // Private functions
  /**
   * Create TableView with stackedBarChart as source.
   */
  private void toTable() {
    ObservableList<Series<String, Number>> series1 = m_stackedBarChart.getData();
    ObservableList<Data<String, Number>> data1 = series1.get(0).getData();
    m_NumberColomns = data1.size() + 1;

    String[][] l_table = new String[(series1.size() + 1)][m_NumberColomns];
    String[] x_Header = new String[series1.size() + 1];
    String[] y_Header = new String[m_NumberColomns];

    int x = 0;
    int y = 0;
    for (XYChart.Series<String, Number> series : m_stackedBarChart.getData()) {
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
    m_StartPeriod = y_Header[1];
    m_EndPeriod = y_Header[m_NumberColomns - 1];

    // Add Totals
    for (int i = 0; i < m_NumberColomns; i++) {
      if (i == 0) {
        l_table[x][i] = "Total:";
      } else {
        double l_amt = m_DateTotAmt.get(y_Header[i]);
        l_table[x][i] = FormatAmount.formatAmount(l_amt);
      }
    }

    // Populate the ObservableList with your String[][] data
    ObservableList<OwnTableCell[]> data = FXCollections.observableArrayList();
    for (String[] row : l_table) {
      OwnTableCell[] rowData = Arrays.stream(row).map(OwnTableCell::new).toArray(OwnTableCell[]::new);
      data.add(rowData);
    }

    // Inside your JavaFX application class
    for (int i = 0; i < l_table[0].length; i++) {
      TableColumn<OwnTableCell[], String> column = new TableColumn<>(y_Header[i]);
      final int columnIndex = i;
      column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[columnIndex].getValue()));
      if (i != 0) {
        column.setStyle("-fx-alignment: CENTER-RIGHT;-fx-font-size: 9px;");
      }
      m_tableView.getColumns().add(column);
    }
    m_tableView.setItems(data);

  }
}

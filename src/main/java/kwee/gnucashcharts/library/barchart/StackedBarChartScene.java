package kwee.gnucashcharts.library.barchart;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import kwee.gnucashcharts.library.FormatAmount;
import kwee.gnucashcharts.library.LocalDateAndAmount;
import kwee.gnucashcharts.library.gnuCashDb.SamengesteldeStaafData;

//TODO
public class StackedBarChartScene {
  private SamengesteldeStaafData m_barData;
  private GridPane m_legendGrid;
  private StackedBarChart<String, Number> m_BarChart;
  private String m_Tag = "";
  private XYChart.Series<String, Number>[] m_seriesArray;
  private SortedMap<String, Double> m_DateTotAmt = new TreeMap<String, Double>();

  /**
   * Map:(Account, Map:(Enddate, Saldo))
   */
  private SortedMap<String, LocalDateAndAmount> m_SerieData;

  public StackedBarChartScene(SamengesteldeStaafData a_barData, String a_tag) {
    m_barData = a_barData;
    m_SerieData = m_barData.getSeries().get(a_tag);
    m_Tag = a_tag;
    // Create a bar chart & a GridPane to hold the legend
    createBarChart();
    createLegendGrid();
  }

  public StackedBarChart<String, Number> getrBarChart() {
    return m_BarChart;
  }

  public GridPane getLegend() {
    return m_legendGrid;
  }

  public Scene getScene(VBox SaveDialog) {
    SaveDialog.setAlignment(Pos.CENTER_RIGHT); // Set alignment to right

    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(m_BarChart, m_legendGrid, SaveDialog);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 800, 600);
    // TODO
    // adjustColors();
//    m_BarChart.setLegendVisible(false);

    return scene;
  }

  public Scene getScene() {
    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(m_BarChart, m_legendGrid);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 800, 600);
//TODO
    // adjustColors();
    // m_BarChart.setLegendVisible(false);

    return scene;
  }

  public WritableImage getBarChartImage() {
    // Convert the PieChart to a WritableImage
    SnapshotParameters params = new SnapshotParameters();
    params.setDepthBuffer(true);
    WritableImage barChartImage = m_BarChart.snapshot(params, null);
    return barChartImage;
  }

  public WritableImage getLegendImage() {
    // Convert the BarChart to a WritableImage
    WritableImage legendImage = m_legendGrid.snapshot(null, null);
    return legendImage;
  }

  int ik = 0;

  private void createBarChart() {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    m_BarChart = new StackedBarChart<>(xAxis, yAxis);

    m_BarChart.setTitle(m_Tag);
    xAxis.setLabel("Maand");
    yAxis.setLabel(m_Tag);

    Set<String> l_AccountKeys = m_SerieData.keySet();
    SortedSet<String> ls_AccountKeys = new TreeSet<String>(l_AccountKeys);
    @SuppressWarnings("unchecked")
    XYChart.Series<String, Number>[] seriesArray = new XYChart.Series[ls_AccountKeys.size()];
    String[] lsa_AccountKeys = new String[ls_AccountKeys.size()];

    ik = 0;
    ls_AccountKeys.forEach(AccKey -> {
      lsa_AccountKeys[ik] = AccKey;
      ik++;
    });
    for (int i = 0; i < ls_AccountKeys.size(); i++) {
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName(lsa_AccountKeys[i]);
      LocalDateAndAmount DateAmts = m_SerieData.get(lsa_AccountKeys[i]);
      Map<LocalDate, Double> DateAmtMap = DateAmts.getDateAmount();
      Set<LocalDate> l_DateKeys = DateAmtMap.keySet();
      SortedSet<LocalDate> ls_DateKeys = new TreeSet<LocalDate>(l_DateKeys);
      LocalDate[] lsa_DateKeys = new LocalDate[ls_DateKeys.size()];

      ik = 0;
      ls_DateKeys.forEach(datkey -> {
        lsa_DateKeys[ik] = datkey;
        ik++;
      });
      for (int j = 0; j < lsa_DateKeys.length - 1; j++) {
        double ll_amt = DateAmtMap.get(lsa_DateKeys[j]);
        DateTimeFormatter lformatter = DateTimeFormatter.ofPattern("MMMyy");
        String formattedLocalDate = lsa_DateKeys[j].format(lformatter);
        XYChart.Data<String, Number> data1 = new XYChart.Data<>(formattedLocalDate, ll_amt);
        series.getData().add(data1);

        if (m_DateTotAmt.get(formattedLocalDate) == null) {
          m_DateTotAmt.put(formattedLocalDate, ll_amt);
        } else {
          double totAmt = m_DateTotAmt.get(formattedLocalDate) + ll_amt;
          m_DateTotAmt.put(formattedLocalDate, totAmt);
        }
      }
      seriesArray[i] = series;
    }
    m_seriesArray = seriesArray;
    m_BarChart.getData().addAll(seriesArray);

    // Create tool tips for the data points
    for (XYChart.Series<String, Number> series : m_BarChart.getData()) {
      for (XYChart.Data<String, Number> data : series.getData()) {
        double l_amt = (double) data.getYValue();
        Tooltip tooltip = new Tooltip(series.getName() + " " + FormatAmount.formatAmount(l_amt));
        // Node nod = data.getNode();
        Tooltip.install(data.getNode(), tooltip);
      }
    }

    // Create tool tips on X-axis
    xAxis.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) p -> {
      if (p.next()) {
        p.getAddedSubList().forEach(node -> {
          if (node != null && node instanceof Text) {
            final Text textNode = (Text) node;
            final Optional<XYChart.Data<String, Number>> data = m_BarChart.getData().get(0).getData().stream()
                .filter(item -> item.getXValue().equalsIgnoreCase(textNode.getText())).findFirst();
            data.ifPresent(d -> {
              double totAmt = m_DateTotAmt.get(d.getXValue());
              Tooltip.install(textNode, new Tooltip(d.getXValue() + " " + FormatAmount.formatAmount(totAmt)));
            });
          }
        });
      }
    });
  }

  // private functions
  private void adjustColors() {
    // Change the color of a specific data series (Series 1)
    for (int i = 0; i > m_seriesArray.length; i++) {
      Node seriesNode = m_seriesArray[i].getNode();

    }

    XYChart.Series<String, Number> series1 = m_BarChart.getData().get(0); // Assuming it's the first series
    Node seriesNode = series1.getNode();
    seriesNode.setStyle("-fx-bar-fill: #FF5733;"); // Set a custom color for Series 1

    // Change the color of specific bars within the series
    Node barNode = seriesNode.lookup(".data0.chart-bar"); // Adjust for the specific bar you want to change
    barNode.setStyle("-fx-bar-fill: #00FF00;"); // Set a custom color for this bar

  }

  private void createLegendGrid() {
    ObservableList<Series<String, Number>> observableList = m_BarChart.getData();
    m_legendGrid = new GridPane();
    m_legendGrid.setCenterShape(true);
    m_legendGrid.setHgap(20);
    m_legendGrid.setVgap(5);
    m_legendGrid.setAlignment(Pos.CENTER);

    int row = 0;
    int col = 0;
    /*
     * @formatter:off
    m_Teller = 0;
    for (PieChart.Data data : m_pieChartData) {
      Color Kleur = getDistinctColor(m_Teller, observableList.size());
      Label colorBox = new Label();
      colorBox.setPrefSize(15, 15);
      colorBox.setStyle("-fx-background-color: " + toHex(Kleur) + ";");

      double amt = data.getPieValue();
      int perc = (int) ((amt / m_total_amount) * 10000.0);
      Label label = new Label(data.getName() + " - " + perc / 100.0 + "%");
      label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

      m_legendGrid.add(colorBox, col * 2, row);
      m_legendGrid.add(label, col * 2 + 1, row);

      col++;
      if (col == 3) {
        col = 0;
        row++;
      }
    }
      m_Teller++;
      @formatter:on
      */

  }

  private Color getDistinctColor(int colorIndex, int numColors) {
    int baseHue = 30; // Starting hue value
    // Calculate the hue value based on the index
    int hue = (baseHue + (colorIndex * (360 / numColors))) % 360;
    // Convert the hue value to an RGB color
    Color color = Color.hsb(hue, 1.0, 1.0);
    return color;
  }

  private String toHex(Color color) {
    return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

}

package kwee.gnucashcharts.library.piechart;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import kwee.gnucashcharts.library.TaartPuntData;
import kwee.logger.MyLogger;
import javafx.geometry.Pos;

public class PieScene {
  private static final Logger lOGGER = MyLogger.getLogger();
  // private static final long serialVersionUID = -709619748064818482L;
  private int m_Teller = 0;
  private double m_total_amount = 0.0;
  private PieChart m_PieChart;
  private GridPane m_legendGrid;
  private PieChart.Data[] m_pieChartData;

  public PieScene(TaartPuntData pieData, String tag) {
    // Create a pie chart & a GridPane to hold the legend
    createPieChart(pieData.getPieSlices(tag));
    createLegendGrid();
  }

  public PieChart getPieChart() {
    return m_PieChart;
  }

  public GridPane getLegend() {
    return m_legendGrid;
  }

  public Scene getScene(VBox SaveDialog) {
    SaveDialog.setAlignment(Pos.CENTER_RIGHT); // Set alignment to right

    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(m_PieChart, m_legendGrid, SaveDialog);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 800, 600);

    adjustColors();
    m_PieChart.setLegendVisible(false);

    return scene;
  }

  public Scene getScene() {
    // Create a VBox to hold the pie chart and the legend
    VBox vbox = new VBox(m_PieChart, m_legendGrid);

    // Set up the scene and add the VBox to it
    Scene scene = new Scene(vbox, 800, 600);

    adjustColors();
    m_PieChart.setLegendVisible(false);

    return scene;
  }

  public double getTotalAmount() {
    double number = Math.round(m_total_amount * 100);
    number = number / 100;
    return number;
  }

  public WritableImage getPieChartImage() {
    // Convert the PieChart to a WritableImage
    WritableImage pieChartImage = m_PieChart.snapshot(null, null);
    return pieChartImage;
  }

  public WritableImage getLegendImage() {
    // Convert the PieChart to a WritableImage
    WritableImage legendImage = m_legendGrid.snapshot(null, null);
    return legendImage;
  }

  // private functions
  private void adjustColors() {
    ObservableList<Data> observableList = m_PieChart.getData();
    m_Teller = 0;
    observableList.forEach(item -> {
      Color Kleur = getDistinctColor(m_Teller, observableList.size());
      item.getNode().setStyle("-fx-pie-color: " + toHex(Kleur) + ";");
      m_Teller++;
    });
  }

  private void createPieChart(Map<String, Double> taartpuntdata) {
    lOGGER.log(Level.FINE, "#Taartpunt data: " + taartpuntdata.size());
    m_pieChartData = new PieChart.Data[taartpuntdata.size()];
    m_total_amount = 0.0;
    m_Teller = 0;

    Set<String> keys = taartpuntdata.keySet();
    keys.forEach(key -> {
      m_pieChartData[m_Teller] = new PieChart.Data(key, taartpuntdata.get(key));
      m_total_amount = m_total_amount + m_pieChartData[m_Teller].getPieValue();
      lOGGER.log(Level.FINE, "i:" + m_Teller + " Key:" + key + " value:" + m_pieChartData[m_Teller].getPieValue());
      m_Teller++;
    });
    // Create the pie chart and add the data slices to it
    m_PieChart = new PieChart();
    m_PieChart.getData().addAll(m_pieChartData);
  }

  private void createLegendGrid() {
    ObservableList<Data> observableList = m_PieChart.getData();
    m_legendGrid = new GridPane();
    m_legendGrid.setCenterShape(true);
    m_legendGrid.setHgap(20);
    m_legendGrid.setVgap(5);
    m_legendGrid.setAlignment(Pos.CENTER);

    int row = 0;
    int col = 0;
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
      m_Teller++;
    }
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

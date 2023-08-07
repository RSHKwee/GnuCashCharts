package zandbak_reform;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;

public class PieChartDemo extends Frame {
  /**
  * 
  */
  private static final long serialVersionUID = -709619748064818482L;
  /**
  * 
  */
  private int[] data = { 30, 20, 50 }; // Example data for the pie chart

  public static void main(String[] args) {
    PieChartDemo pieChart = new PieChartDemo();
    pieChart.setSize(400, 400);
    pieChart.setVisible(true);
  }

  @Override
  public void paint(Graphics g) {
    drawPieChart(g, data);
  }

  private void drawPieChart(Graphics g, int[] data) {
    int total = 0;
    for (int value : data) {
      total += value;
    }

    int startAngle = 0;
    for (int i = 0; i < data.length; i++) {
      int arcAngle = (int) ((data[i] / (double) total) * 360);
      g.setColor(getColor(i));
      g.fillArc(100, 100, 200, 200, startAngle, arcAngle);
      startAngle += arcAngle;
    }
  }

  private Color getColor(int index) {
    switch (index) {
    case 0:
      return Color.RED;
    case 1:
      return Color.GREEN;
    case 2:
      return Color.BLUE;
    default:
      return Color.BLACK;
    }
  }
}

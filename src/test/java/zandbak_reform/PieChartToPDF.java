package zandbak_reform;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PieChartToPDF {
  private int[] data = { 30, 20, 50 }; // Example data for the pie chart
  private String[] labels = { "Category 1", "Category 2", "Category 3" }; // Labels for the slices

  public static void main(String[] args) {
    PieChartToPDF pieChart = new PieChartToPDF();
    pieChart.createPDF("PieChart.pdf");
  }

  private void createPDF(String filename) {
    Document document = new Document();
    try {
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();

      // Create a new page in the PDF
      document.newPage();

      // Create a PdfContentByte to draw on the PDF
      PdfContentByte contentByte = writer.getDirectContent();

      // Create a PdfTemplate to hold the pie chart image
      PdfTemplate template = contentByte.createTemplate(400, 400);
      Graphics graphics = template.createGraphics(400, 400);
      drawPieChart(graphics, data, labels);
      graphics.dispose();
      contentByte.addTemplate(template, 100, 400);

      // Add legend to the PDF
      addLegend(document);

      document.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void drawPieChart(Graphics g, int[] data, String[] labels) {
    // Same code to draw the pie chart as in the previous example
    // ... (omitted for brevity) ...
  }

  private void addLegend(Document document) throws DocumentException {
    com.itextpdf.text.List legendList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
    legendList.setListSymbol("\u2022"); // Bullet symbol for each item

    for (int i = 0; i < data.length; i++) {
      Phrase phrase = new Phrase(labels[i], new com.itextpdf.text.Font(FontFactory.getFont(FontFactory.HELVETICA, 10)));
      phrase.add(new Chunk("\u00A0\u00A0", new com.itextpdf.text.Font(FontFactory.getFont(FontFactory.HELVETICA, 8))));
      phrase.add(new Chunk("\u25A0", new com.itextpdf.text.Font(FontFactory.getFont(FontFactory.HELVETICA, 8))));

      legendList.add(phrase);
    }

    document.add(legendList);
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

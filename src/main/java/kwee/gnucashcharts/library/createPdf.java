package kwee.gnucashcharts.library;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class createPdf {
  private static float xTitle = (float) 50.0;
  private static float yTitle = (float) 520.0;

  private static float xPosition = (float) -25.0;
  private static float yPosition = (float) 110.0;

  private static float xPositionForLegend = (float) 20.0;
  private static float yPositionForLegend = (float) 10.0;

  static public void CreatePdfFromImage(WritableImage aChartImage, WritableImage aLegendImage, String aTitle,
      String pdfFile) throws IOException {
    // Convert the WritableImage to BufferedImage
    BufferedImage ChartImage = SwingFXUtils.fromFXImage(aChartImage, null);
    BufferedImage LegendImage = SwingFXUtils.fromFXImage(aLegendImage, null);

    // Create a new PDF document
    PDDocument document = new PDDocument();
    PDPage page = new PDPage();

    // Set the page dimensions for landscape orientation
    page.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));

    document.addPage(page);

    // Create a content stream for the page
    PDPageContentStream contentStream = new PDPageContentStream(document, page);

    // Add a title
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18); // Choose your font
    // and size
    contentStream.beginText();
    contentStream.newLineAtOffset(xTitle, yTitle); // Adjust the coordinates
    contentStream.showText(aTitle);
    contentStream.endText();

    // Convert the JavaFX image to PDF image
    PDImageXObject pdfPieChartImage = LosslessFactory.createFromImage(document, ChartImage);
    PDImageXObject pdfLegendImage = LosslessFactory.createFromImage(document, LegendImage);

    // Draw the images on the page
    float imageWidth = ChartImage.getWidth();
    float imageHeight = ChartImage.getHeight();
    contentStream.drawImage(pdfPieChartImage, xPosition, yPosition, imageWidth, imageHeight);

    float legendImageWidth = LegendImage.getWidth();
    float legendImageHeight = LegendImage.getHeight();
    contentStream.drawImage(pdfLegendImage, xPositionForLegend, yPositionForLegend, legendImageWidth,
        legendImageHeight);

    // Close the content stream
    contentStream.close();

    // Save the PDF document to a file
    document.save(pdfFile);

    // Close the PDF document
    document.close();
  }
}

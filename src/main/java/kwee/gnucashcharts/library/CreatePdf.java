package kwee.gnucashcharts.library;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import kwee.library.TimeStamp;
import kwee.logger.MyLogger;

public class CreatePdf {
  private static final Logger lOGGER = MyLogger.getLogger();
  /*
   * @formatter:off
   * For A4 paper size:
   * If you want to work with centimeters instead of points, 
   * you can convert the centimeter values to points using the fact that there 
   * are approximately 28.35 points in a centimeter. 
   * Here's how you can calculate the coordinates of the top-right corner of
   * an A4 paper in centimeters:
   *
   * X-coordinate: A4 width in centimeters = 21.0 cm,
   *    converted to points = 21.0 * 28.35 = 595.35 points.
   * Y-coordinate: A4 height in centimeters = 29.7 cm, 
   *    converted to points = 29.7 * 28.35 = 841.245 points.
   *    
   * So, the top-right corner of an A4 paper in the PDF coordinate system 
   * (assuming portrait orientation) would be approximately (595.35, 841.245) points.
   * 
   * 
   * 
   * For A3 paper size, the dimensions and coordinates of the corners in points (1 inch = 72 points) depend on whether you're working with portrait or landscape orientation.

Portrait Orientation (Width x Height):

A3 dimensions: 297mm x 420mm
Width in points: 841.89 points (297mm x 72 points per inch)
Height in points: 1190.55 points (420mm x 72 points per inch)
The coordinates of the four corners in portrait orientation are as follows:

Top-left corner: (0, 1190.55)
Top-right corner: (841.89, 1190.55)
Bottom-left corner: (0, 0)
Bottom-right corner: (841.89, 0)
Landscape Orientation (Width x Height):

A3 dimensions: 420mm x 297mm
Width in points: 1190.55 points (420mm x 72 points per inch)
Height in points: 841.89 points (297mm x 72 points per inch)
The coordinates of the four corners in landscape orientation are as follows:

Top-left corner: (0, 841.89)
Top-right corner: (1190.55, 841.89)
Bottom-left corner: (0, 0)
Bottom-right corner: (1190.55, 0)
You can use these coordinates to position elements and set the page size when working with A3 paper in your PDF creation code.

   * 
   * 
   * @formatter:on
   */
  private float xTitle = 50;
  private float yTitle = 520;

  private float xFooter = 700; // X-coordinate for the footer text
  private float yFooter = 10; // Y-coordinate for the footer text (adjust as needed)

  private static float xPosition = 0;
  private static float yPosition = 110;

  private static float xPositionForLegend = 20;
  private static float yPositionForLegend = 10;

  private String m_PdfFile = "";
  PDDocument m_document;

  public CreatePdf(String a_pdfFile) {
    m_PdfFile = a_pdfFile;
    // Create a new PDF document
    m_document = new PDDocument();
  }

  public void SaveDocument() {
    try {
      m_document.save(m_PdfFile);
      m_document.close();
    } catch (IOException e) {
      lOGGER.log(Level.INFO, e.getMessage());
    }
  }

  public void addImageTable(WritableImage aChartImage, String aTitle) throws IOException {
    // Convert the WritableImage to BufferedImage
    BufferedImage ChartImage = SwingFXUtils.fromFXImage(aChartImage, null);

    PDPage page = new PDPage();

    float pageHeight = PDRectangle.A2.getHeight();
    float pageWidth = PDRectangle.A2.getWidth();
    calcTitleAndFooterCoord(pageHeight, pageWidth);

    // Set the page dimensions for landscape orientation
    page.setMediaBox(new PDRectangle(pageHeight, pageWidth));
    m_document.addPage(page);

    // Create a content stream for the page

    PDPageContentStream contentStream = new PDPageContentStream(m_document, page);

    addTitle(contentStream, aTitle);

    // Convert the JavaFX image to PDF image
    PDImageXObject pdfPieChartImage = LosslessFactory.createFromImage(m_document, ChartImage);

    // Draw the images on the page
    float scale = pageHeight / ChartImage.getWidth();
    float imageWidth = ChartImage.getWidth() * scale;
    float imageHeight = ChartImage.getHeight() * scale;
    contentStream.drawImage(pdfPieChartImage, xPosition, yPosition, imageWidth, imageHeight);

    addFooter(contentStream, TimeStamp.getTimeStampNow());

    // Close the content stream
    contentStream.close();
  }

  public void addImageAndLegend(WritableImage aChartImage, WritableImage aLegendImage, String aTitle)
      throws IOException {
    // Convert the WritableImage to BufferedImage
    BufferedImage ChartImage = SwingFXUtils.fromFXImage(aChartImage, null);
    BufferedImage LegendImage = SwingFXUtils.fromFXImage(aLegendImage, null);

    PDPage page = new PDPage();

    float pageHeight = PDRectangle.A4.getHeight();
    float pageWidth = PDRectangle.A4.getWidth();
    calcTitleAndFooterCoord(pageHeight, pageWidth);

    // Set the page dimensions for landscape orientation
    page.setMediaBox(new PDRectangle(pageHeight, pageWidth));

    m_document.addPage(page);
    // Create a content stream for the page
    PDPageContentStream contentStream = new PDPageContentStream(m_document, page);

    addTitle(contentStream, aTitle);

    // Convert the JavaFX image to PDF image
    PDImageXObject pdfPieChartImage = LosslessFactory.createFromImage(m_document, ChartImage);
    PDImageXObject pdfLegendImage = LosslessFactory.createFromImage(m_document, LegendImage);

    // Draw the images on the page
    float imageWidth = ChartImage.getWidth();
    float imageHeight = ChartImage.getHeight();
    contentStream.drawImage(pdfPieChartImage, xPosition, yPosition, imageWidth, imageHeight);

    float legendImageWidth = LegendImage.getWidth();
    float legendImageHeight = LegendImage.getHeight();
    contentStream.drawImage(pdfLegendImage, xPositionForLegend, yPositionForLegend, legendImageWidth,
        legendImageHeight);

    addFooter(contentStream, TimeStamp.getTimeStampNow());

    // Close the content stream
    contentStream.close();
  }

  // Private functions
  private void addTitle(PDPageContentStream contentStream, String text) throws IOException {
    contentStream.beginText();
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18); // Choose your font

    contentStream.newLineAtOffset(xTitle, yTitle); // Adjust the coordinates
    contentStream.showText(text);
    contentStream.endText();
  }

  private void addFooter(PDPageContentStream contentStream, String text) throws IOException {
    contentStream.beginText();
    contentStream.setFont(PDType1Font.HELVETICA, 10);

    contentStream.newLineAtOffset(xFooter, yFooter);
    contentStream.showText(text);
    contentStream.endText();
  }

  private void calcTitleAndFooterCoord(float a_PageWidth, float a_PageHeight) {
    xTitle = 50;
    yTitle = a_PageHeight - 75;

    xFooter = a_PageWidth - 140;
    yFooter = 10;
  }

}

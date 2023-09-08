package kwee.gnucashcharts.library;

import java.awt.image.BufferedImage;
import java.io.IOException;
// import java.util.logging.Level;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;

import kwee.library.TimeStamp;
// import kwee.logger.MyLogger;

public class CreatePdf {
  // private static final Logger lOGGER = MyLogger.getLogger();

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
  public enum c_PageSizeEnum {
    A2, A4
  };

  // X,Y coordinate for the title
  private float xTitle = 50; // Fixed
  private float yTitle = 520;
  private float yTitle_Offset = 75;

  // X,Y coordinate for the footer text
  private float xFooter = 700;
  private float yFooter = 10; // Fixed
  private float xFooter_Offset = 140;

  private float xMargin = 20;

  // Position image
  private float xPosition = 20; // Fixed
  private float yPosition = 110;
  private float yPositionDefault = 110;
  private float yPosition_Offset = 600;

  // Position Legend
  private float xPositionForLegend = 20; // Fixed
  private float yPositionForLegend = 10;
  private float yPositionForLegend_Offset = yPosition_Offset + 90;

  private float pageHeight = PDRectangle.A2.getHeight();
  private float pageWidth = PDRectangle.A2.getWidth();

  private String m_PdfFile = "";
  private PDDocument m_document;
  private PDPage m_page;
  private PDPageContentStream contentStream = null;

  /**
   * Constructor
   * 
   * @param a_pdfFile Filename of PDF file to be created.
   */
  public CreatePdf(String a_pdfFile) {
    m_PdfFile = a_pdfFile;
    // Create a new PDF document
    m_document = new PDDocument();
  }

  /**
   * Save document and create file.
   */
  public void SaveDocument() throws IOException {
    // Close the content stream
    addFooter(contentStream, TimeStamp.getTimeStampNow());
    contentStream.close();

    m_document.save(m_PdfFile);
    m_document.close();
  }

  public void CreatePage(c_PageSizeEnum a_PageSize, String aTitle) throws IOException {
    // Close a content stream for the page
    if (contentStream != null) {
      // Close the content stream
      addFooter(contentStream, TimeStamp.getTimeStampNow());
      contentStream.close();
    }

    m_page = new PDPage();
    switch (a_PageSize) {
    case A2:
      pageHeight = PDRectangle.A2.getHeight();
      pageWidth = PDRectangle.A2.getWidth();
      calcTitleAndFooterCoord(pageHeight, pageWidth);
      break;
    case A4:
      pageHeight = PDRectangle.A4.getHeight();
      pageWidth = PDRectangle.A4.getWidth();
      calcTitleAndFooterCoord(pageHeight, pageWidth);
    }

    // Set the page dimensions for landscape orientation
    m_page.setMediaBox(new PDRectangle(pageHeight, pageWidth));
    m_document.addPage(m_page);

    // Create a content stream for the page
    contentStream = new PDPageContentStream(m_document, m_page);
    addTitle(contentStream, aTitle);
  }

  /**
   * Add a Chart image to a page
   * 
   * @param aChartImage
   * @param aTitle
   * @throws IOException
   */
  public void addImageTable(WritableImage aChartImage) throws IOException {
    // Convert the WritableImage to BufferedImage
    BufferedImage ChartImage = SwingFXUtils.fromFXImage(aChartImage, null);

    // Convert the JavaFX image to PDF image
    PDImageXObject pdfPieChartImage = LosslessFactory.createFromImage(m_document, ChartImage);

    // Draw the images on the page
    float scale = pageHeight / ChartImage.getWidth();
    float imageWidth = (ChartImage.getWidth() - (xMargin * 2)) * scale;
    float imageHeight = ChartImage.getHeight() * scale;
    contentStream.drawImage(pdfPieChartImage, xPosition, yPositionDefault, imageWidth, imageHeight);
  }

  /**
   * Add a Chart image and a Legend to a page
   * 
   * @param aChartImage  Image of Chart
   * @param aLegendImage Image of Legend.
   * @param aTitle       Title for Chart
   * @throws IOException may be thrown
   */
  public void addImageAndLegend(WritableImage aChartImage, WritableImage aLegendImage) throws IOException {
    // Convert the WritableImage to BufferedImage
    BufferedImage ChartImage = SwingFXUtils.fromFXImage(aChartImage, null);
    BufferedImage LegendImage = SwingFXUtils.fromFXImage(aLegendImage, null);

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
  }

  public void addTable(TableView<String[]> a_Table) throws IOException {
    // Iterate through TableView rows and columns
    contentStream.beginText();
    for (String[] item : a_Table.getItems()) {
      contentStream.newLineAtOffset(50, 200); // Adjust the coordinates
      for (TableColumn<String[], ?> column : a_Table.getColumns()) {
        Object cellData = column.getCellData(item);

        // Add cellData to PDF
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.showText(cellData.toString());
      }
      contentStream.newLine();
    }
    contentStream.endText();
  }

  // Private functions
  /**
   * Add title to Page
   * 
   * @param contentStream Stream to buildup page
   * @param text          Title text
   * @throws IOException may be thrown
   */
  private void addTitle(PDPageContentStream contentStream, String text) throws IOException {
    contentStream.beginText();
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18); // Choose your font

    contentStream.newLineAtOffset(xTitle, yTitle); // Adjust the coordinates
    contentStream.showText(text);
    contentStream.endText();
  }

  /**
   * Add footer to Page
   * 
   * @param contentStream Stream to buildup page
   * @param text          Footer text
   * @throws IOException may be thrown
   */
  private void addFooter(PDPageContentStream contentStream, String text) throws IOException {
    contentStream.beginText();
    contentStream.setFont(PDType1Font.HELVETICA, 10);

    contentStream.newLineAtOffset(xFooter, yFooter);
    contentStream.showText(text);
    contentStream.endText();
  }

  /**
   * Calculate position Title and Footer, depends on Page size
   * 
   * @param a_PageWidth  Width of Page in points
   * @param a_PageHeight Height of Page in points
   */
  private void calcTitleAndFooterCoord(float a_PageWidth, float a_PageHeight) {
    // xTitle = 50;
    yTitle = a_PageHeight - yTitle_Offset;

    xFooter = a_PageWidth - xFooter_Offset;
    // yFooter = 10;

    // Position image
    // xPosition = 0;
    yPosition = a_PageHeight - yPosition_Offset;

    // Position Legend
    // xPositionForLegend = 20;
    yPositionForLegend = a_PageHeight - yPositionForLegend_Offset;
  }

}

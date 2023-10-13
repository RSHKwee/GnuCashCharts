package zandbak_reform;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TableExample {
  public static void main(String[] args) {
    try {
      PDDocument document = new PDDocument();
      PDPage page = new PDPage(PDRectangle.A4);
      document.addPage(page);

      PDPageContentStream contentStream = new PDPageContentStream(document, page);

      // Define table properties
      float margin = 50;
      float yStart = page.getMediaBox().getHeight() - margin;
      float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
      float yPosition = yStart;
      int rows = 5;
      int cols = 9;
      float rowHeight = 20f;
      float tableHeight = rowHeight * rows;
      float tableYBottom = yStart - tableHeight;

      // Define cell properties
      float tableXStart = margin;
      float cellMargin = 4f;
      float colWidth = tableWidth / (float) cols;
      float cellWidth = colWidth - 1 * cellMargin;

      // Draw table headers
      contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
      for (int i = 0; i < cols; i++) {
        float x = tableXStart + i * colWidth + cellMargin;
        float y = yStart - rowHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Header " + (i + 1));
        contentStream.endText();
      }

      // Draw table rows and values
      contentStream.setFont(PDType1Font.HELVETICA, 12);
      for (int i = 0; i < rows; i++) {
        yPosition -= rowHeight;
        contentStream.drawLine(tableXStart, yPosition, tableXStart + tableWidth, yPosition);
        for (int j = 0; j < cols; j++) {
          float x = tableXStart + j * colWidth + cellMargin;
          contentStream.beginText();
          contentStream.newLineAtOffset(x, yPosition - 12);
          contentStream.showText("Value " + (i + 1) + "," + (j + 1));
          contentStream.endText();
        }
      }

      contentStream.close();
      document.save("TableExample.pdf");
      document.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

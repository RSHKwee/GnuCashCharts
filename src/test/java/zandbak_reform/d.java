package zandbak_reform;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class d {
  void Aap(TableView<String[]> tableView) throws IOException {
    // Your JavaFX TableView setup and data here

    // Create a new PDF document
    PDDocument document = new PDDocument();
    PDPage page = new PDPage();
    document.addPage(page);

    PDPageContentStream contentStream = new PDPageContentStream(document, page);

    // Create a table in the PDF to mimic the TableView
    float margin = 50; // Margin from the page edge
    float yStart = page.getMediaBox().getHeight() - margin;
    float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
    float yPosition = yStart;

    int rows = tableView.getItems().size();
    int cols = tableView.getColumns().size();

//    float tableHeight = 20f * rows; // You may need to adjust this based on your data and font size
    float rowHeight = 20f;
    // float tableYLength = rowHeight * rows;
    float tableXLength = tableWidth;

    // Draw table headers
    float yPositionHeader = yStart;
    for (TableColumn<String[], ?> column : tableView.getColumns()) {
      contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
      contentStream.beginText();
      contentStream.newLineAtOffset(margin, yPositionHeader);
      contentStream.showText(column.getText());
      contentStream.endText();
      yPositionHeader -= 20;
    }

    // Add data to the PDF table
    for (int i = 0; i < rows; i++) {
      yPosition -= rowHeight;
      for (int j = 0; j < cols; j++) {
        TableColumn<?, ?> column = tableView.getColumns().get(j);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + j * (tableXLength / cols), yPosition);
        contentStream.showText(column.getCellData(i).toString());
        contentStream.endText();
      }
    }

    contentStream.close();

    // Save the PDF to a file
    document.save("table_data.pdf");
    document.close();
  }
}

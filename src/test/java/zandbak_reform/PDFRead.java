package zandbak_reform;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFRead {

  public static void main(String[] args) {
    try {
      // Load the PDF document
      PDDocument document = PDDocument.load(new File(
          "D:\\Users\\René\\SynologyDrive\\Documenten\\Afhandeling erfenis\\CTH Zuiderwijk\\Docs\\AFENBIJNL13INGB000075832614-07-25-08-2023.pdf"));

      // Create a PDFTextStripper object
      PDFTextStripper pdfTextStripper = new PDFTextStripper();

      // Extract text from the PDF
      String text = pdfTextStripper.getText(document);

      // Close the document
      document.close();

      // Print the extracted text
      System.out.println(text);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
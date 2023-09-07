package zandbak_reform;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ReadWordDocument {
  public static void main(String[] args) {
    try {
      // Load the Word document
      FileInputStream fis = new FileInputStream(new File(
          "D:\\Users\\René\\SynologyDrive\\Documenten\\Afhandeling erfenis\\CTH Zuiderwijk\\Docs\\AFENBIJC7885384414-07-25-08-2023.docx"));
      XWPFDocument document = new XWPFDocument(fis);

      // Read text from the document
      List<XWPFPictureData> pictures = document.getAllPictures();
      for (XWPFPictureData picture : pictures) {
        byte[] pictureData = picture.getData();
        System.out.println("Picture Content Type: " + pictureData);
        // You can do something with the picture data if needed.
      }

      // Iterate through paragraphs and extract text
      List<XWPFParagraph> paragraphs = document.getParagraphs();
      for (XWPFParagraph paragraph : paragraphs) {
        String text = paragraph.getText();
        System.out.println(text);
      }

      // Close the input stream
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

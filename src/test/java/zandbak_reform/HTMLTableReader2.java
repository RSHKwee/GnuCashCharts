package zandbak_reform;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLTableReader2 {

  public static String readHTMLFromFile(String filePath) throws IOException {
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line);
      }
    }
    return content.toString();
  }

  public static void main(String[] args) {
    String filePath = "G:\\Users\\René\\OneDrive\\Documenten\\Administraties\\Samenvatting.html"; // Replace this with
    String htmlContent = "";
    try {
      htmlContent = readHTMLFromFile(filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Parse the HTML content using Jsoup
    Document doc = Jsoup.parse(htmlContent);

    // Select the table element (if you know its ID or class, use that for more
    // precise selection)
    Elements tables = doc.select("table");

    // Process the table data
    // Rekeningnummer;Rekeningnaam;Column 3;Column 4;Column 5;Column 6;Column
    // 7;Saldo;Toelichting;
    // Colomn 6 of 7 bevat bedrag
    // Toelichting bevat Tag Type:xxxxx
    for (Element table : tables) {
      // Get the header row (first row in the table)
      Elements headerCells = table.select("tr").first().select("th, td");
      String[] headerTexts = new String[headerCells.size()];
      for (int i = 0; i < headerCells.size(); i++) {
        Element header = headerCells.get(i);
        headerTexts[i] = header.text().isEmpty() ? "Column " + (i + 1) : header.text();
      }

      String Header = "";
      for (String headerText : headerTexts) {
        Header = Header + headerText + ";";
        // System.out.print(headerText + ";");
      }
      // System.out.println("headerlength: " + headerCells.size());

      // Process the remaining rows (excluding the header)
      Elements dataRows = table.select("tr").not(":first-child");
      ArrayList<String> dataregels = new ArrayList<String>();

      int maxelems = 0;
      for (Element row : dataRows) {
        int teller = 0;
        Elements dataCells = row.select("td");
        String line = "";
        for (Element cell : dataCells) {
          teller++;
          line = line + cell.text() + ";";
          // System.out.print(cell.text() + ";");
        }
        // System.out.println(" Teller: " + teller);
        if (teller > maxelems) {
          maxelems = teller;
        }
        dataregels.add(line);
      }

      System.out.println(Header);
      dataregels.forEach(line -> {
        System.out.println(line);
      });
    }
  }

}

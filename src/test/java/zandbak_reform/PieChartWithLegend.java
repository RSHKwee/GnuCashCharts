package zandbak_reform;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kwee.gnucashcharts.library.createPdf;
import kwee.gnucashcharts.library.html.PieFromHtmlPage;

public class PieChartWithLegend extends Application {
  int i = 0;
  double tot_amt = 0.0;

  @Override
  public void start(Stage primaryStage) {
    // String filePath =
    // "G:\\Users\\René\\OneDrive\\Documenten\\Administraties\\Samenvatting.html";
    //
    String filePath = "G:\\Users\\René\\OneDrive\\Documenten\\Administraties\\SamenvattingKH Kwee.html"; // Replace this
    // String tag = "Inkomen";
    // String tag = "Type";
    String tag = "Kosten";

    PieFromHtmlPage pie = new PieFromHtmlPage(filePath, tag);

    // Set up the scene and add the VBox to it
    Scene scene = pie.getScene();

    // Set the title of the window
    tot_amt = pie.getTotalAmount();
    String title = tag + " (totaal €" + tot_amt + ")";
    title = title.replace(".", ",");
    primaryStage.setTitle(title);

    // Set the scene to the stage
    primaryStage.setScene(scene);

    // Show the stage (display the pie chart with the legend)
    primaryStage.show();

    try {
      createPdf.CreatePdfFromImage(pie.getPieChartImage(), pie.getLegendImage(), title,
          "F:\\dev\\Tools\\GnuCashCharts\\PieChart.pdf");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

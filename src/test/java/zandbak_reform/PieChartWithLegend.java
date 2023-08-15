package zandbak_reform;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kwee.gnucashcharts.library.CreatePdf;
import kwee.gnucashcharts.library.TaartPuntDataIf;
import kwee.gnucashcharts.library.html.PieFromHtmlPage;
import kwee.gnucashcharts.library.html.ReadHTMLTable;
import kwee.gnucashcharts.library.html.TaartPuntData;
import kwee.gnucashcharts.library.piechart.PieScene;

public class PieChartWithLegend extends Application {
  int i = 0;
  double tot_amt = 0.0;

  @Override
  public void start(Stage primaryStage) {
    String rootdir = "D:\\Users\\René\\OneDrive"; // Laptoprk-2021
    // String rootdir = "G:\\Users\\René\\OneDrive"; // PcPrive-2017

    // String filePath = rootdir + "\\Documenten\\Administraties\\SamenvattingKH
    // Kwee.html"; // Replace this
    // String tag = "Type";
    // String tag = "Kosten";

    String filePath = rootdir + "\\Documenten\\Administraties\\Samenvatting.html"; // Replace this
    // String tag = "Inkomen";
    String tag = "Vermogen";

    ReadHTMLTable htmltable = new ReadHTMLTable(filePath);
    ArrayList<String> regels = htmltable.parseHTMLpage();
    TaartPuntData pieData;
    pieData = new TaartPuntData();
    pieData.putData(regels);

    PieScene pie = new PieScene(pieData, tag);

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
      CreatePdf.CreatePdfFromImage(pie.getPieChartImage(), pie.getLegendImage(), title,
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

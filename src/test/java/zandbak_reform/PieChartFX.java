package zandbak_reform;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import kwee.gnucashcharts.library.html.ReadHTMLTable;
import kwee.gnucashcharts.library.html.TaartPuntData;

public class PieChartFX extends Application {

  @Override
  public void start(Stage primaryStage) {
    String filePath = "G:\\Users\\René\\OneDrive\\Documenten\\Administraties\\Samenvatting.html"; // Replace this with
    ReadHTMLTable htmltable = new ReadHTMLTable(filePath);
    ArrayList<String> regels = htmltable.parseHTMLpage();

    TaartPuntData pieData = new TaartPuntData();
    pieData.putData(regels);
    @SuppressWarnings("unused")
    Set<String> tags = pieData.getTags();
    // String tag = "Inkomen";
    String tag = "Type";

    // Create a pie chart
    PieChart pieChart = createPieChart(pieData.getPieSlices(tag));

    // Set up the scene and add the pie chart to it
    Scene scene = new Scene(pieChart, 800, 600);

    // Set the title of the window
    primaryStage.setTitle("Pie Chart " + tag);

    // Set the scene to the stage
    primaryStage.setScene(scene);

    // Show the stage (display the pie chart)
    primaryStage.show();
    // });

  }

  int i = 0;

  private PieChart createPieChart(Map<String, Double> taartpuntdata) {
    PieChart.Data[] pieChartData = new PieChart.Data[taartpuntdata.size()];

    Set<String> keys = taartpuntdata.keySet();
    keys.forEach(key -> {
      pieChartData[i] = new PieChart.Data(key, taartpuntdata.get(key));
      i++;
    });

    // Create the pie chart and add the data slices to it
    PieChart pieChart = new PieChart();
    pieChart.getData().addAll(pieChartData);

    return pieChart;
  }

  public static void main(String[] args) {
    launch(args);
  }
}

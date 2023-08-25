package kwee.gnucashcharts.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import kwee.gnucashcharts.library.barchart.StackedBarChartScene;
import kwee.gnucashcharts.library.gnuCashDb.SamengesteldeStaafData;
import kwee.gnucashcharts.library.CreatePdf;

import kwee.logger.MyLogger;

public class BarChartWithLegend {
  private static final Logger lOGGER = MyLogger.getLogger();
  private String title = "";

  public void openBarChartWindow(File inpFile, String tag, int a_NrBars) {
    lOGGER.log(Level.INFO, "Tag " + tag);
    ActionGnuCshDbStackedBarChart l_barchart = new ActionGnuCshDbStackedBarChart(inpFile, a_NrBars);
    SamengesteldeStaafData a_barData = l_barchart.getData();

    Stage barchartStage = new Stage();
    StackedBarChartScene l_ScenBar = new StackedBarChartScene(a_barData, tag);

    // Set the title of the window
    title = "Bartchart for " + tag + " period " + l_ScenBar.get_StartPeriod() + " to " + l_ScenBar.get_EndPeriod();
    barchartStage.setTitle(title);

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save File");
    Button saveButton = new Button("Create PDF File");
    saveButton.setOnAction(e -> {
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.pdf)", "*.pdf");
      fileChooser.getExtensionFilters().add(extFilter);
      if (!MainMenu.m_param.get_PdfFile().isBlank()) {
        File intFile = new File(MainMenu.m_param.get_PdfFile());
        String ldir = intFile.getParent();
        fileChooser.setInitialDirectory(new File(ldir));
      }

      // Show save file dialog
      File selectedFile = fileChooser.showSaveDialog(barchartStage);
      if (selectedFile != null) {
        try {
          CreatePdf.CreatePdfFromImage(l_ScenBar.getBarChartImage(), l_ScenBar.getLegendImage(), title,
              selectedFile.getAbsolutePath());
          lOGGER.log(Level.INFO, "PDF file generated: " + selectedFile.getAbsolutePath());

          MainMenu.m_param.set_Pdf_file(selectedFile);
          MainMenu.m_param.save();
        } catch (IOException e1) {
          lOGGER.log(Level.INFO, e1.getMessage());
        }
      }
    });
    VBox saveFileLayout = new VBox(saveButton);
    saveFileLayout.setSpacing(10);
    VBox.setMargin(saveButton, new Insets(10, 10, 10, 10));

    // Set up the scene and add the VBox to it
    Scene scene = l_ScenBar.getScene(saveFileLayout);
    barchartStage.setScene(scene);

    // Show the stage (display the bar chart and the table
    barchartStage.show();
  }
}

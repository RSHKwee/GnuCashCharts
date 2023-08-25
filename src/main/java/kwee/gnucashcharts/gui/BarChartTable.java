package kwee.gnucashcharts.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.geometry.Insets;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kwee.gnucashcharts.library.barchart.BarChartToTableScene;
import kwee.gnucashcharts.library.barchart.StackedBarChartScene;
import kwee.gnucashcharts.library.gnuCashDb.SamengesteldeStaafData;
import kwee.gnucashcharts.library.CreatePdf;

import kwee.logger.MyLogger;

public class BarChartTable {
  private static final Logger lOGGER = MyLogger.getLogger();
  private String title = "";

  public void openBarChartTableWindow(File inpFile, String tag, int a_NrBars) {
    lOGGER.log(Level.INFO, "Tag " + tag);
    ActionGnuCshDbStackedBarChart l_barchart = new ActionGnuCshDbStackedBarChart(inpFile, a_NrBars);
    SamengesteldeStaafData a_barData = l_barchart.getData();
    StackedBarChartScene l_ScenBar = new StackedBarChartScene(a_barData, tag);

    Stage barchartTableStage = new Stage();
    BarChartToTableScene barchartable = new BarChartToTableScene(l_ScenBar.getBarChart(),
        l_ScenBar.getCombinedTotals());
    title = "Table for " + tag + " period " + barchartable.get_StartPeriod() + " to " + barchartable.get_EndPeriod();
    barchartTableStage.setTitle(title);

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
      File selectedFile = fileChooser.showSaveDialog(barchartTableStage);
      if (selectedFile != null) {
        try {
          CreatePdf.CreatePdfFromImage(barchartable.getTableViewImage(), title, selectedFile.getAbsolutePath());
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
    barchartTableStage.setScene(barchartable.getScene(saveFileLayout));
    barchartTableStage.show();
  }
}

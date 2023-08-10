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
import kwee.gnucashcharts.library.FormatAmount;
import kwee.gnucashcharts.library.CreatePdf;
import kwee.gnucashcharts.library.html.PieFromHtmlPage;
import kwee.logger.MyLogger;

public class PieChartWithLegend {
  private static final Logger lOGGER = MyLogger.getLogger();

  private double tot_amt = 0.0;
  private String title = "";

  public void openPieChartWindow(String filePath, String tag) {
    lOGGER.log(Level.INFO, "Filenaam " + filePath + " met tag " + tag);

    Stage piechartStage = new Stage();
    PieFromHtmlPage pie = new PieFromHtmlPage(filePath, tag);

    // Set the title of the window
    tot_amt = pie.getTotalAmount();
    title = tag + " (totaal " + FormatAmount.formatAmount(tot_amt) + ")";
    piechartStage.setTitle(title);

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save File");

    Button saveButton = new Button("Creeer PDF File");
    saveButton.setOnAction(e -> {
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.pdf)", "*.pdf");
      fileChooser.getExtensionFilters().add(extFilter);
      if (!MainMenu.m_param.get_PdfFile().isBlank()) {
        File intFile = new File(MainMenu.m_param.get_PdfFile());
        String ldir = intFile.getParent();
        fileChooser.setInitialDirectory(new File(ldir));
      }
      // Show save file dialog
      File selectedFile = fileChooser.showSaveDialog(piechartStage);
      if (selectedFile != null) {
        try {
          CreatePdf.CreatePdfFromImage(pie.getPieChartImage(), pie.getLegendImage(), title,
              selectedFile.getAbsolutePath());
          lOGGER.log(Level.INFO, "PDF file aangemaakt: " + selectedFile.getAbsolutePath());

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
    Scene scene = pie.getScene(saveFileLayout);
    piechartStage.setScene(scene);

    // Show the stage (display the pie chart with the legend)
    piechartStage.show();
  }
}

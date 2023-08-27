package kwee.gnucashcharts.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kwee.gnucashcharts.library.barchart.BarChartToTableScene;
import kwee.gnucashcharts.library.barchart.StackedBarChartScene;
import kwee.gnucashcharts.library.gnuCashDb.SamengesteldeStaafData;
import kwee.gnucashcharts.library.CreatePdf;

import kwee.logger.MyLogger;

public class BarChartWithLegend {
  private static final Logger lOGGER = MyLogger.getLogger();
  private String title = "";
  private StackedBarChartScene m_BarChartDiagram;
  private BarChartToTableScene m_barchartable;

  public void openTabsWindow(File inpFile, String tag, int a_NrBars) {
    // Initialize
    ActionGnuCshDbStackedBarChart l_barchart = new ActionGnuCshDbStackedBarChart(inpFile, a_NrBars);
    SamengesteldeStaafData a_barData = l_barchart.getData();
    m_BarChartDiagram = new StackedBarChartScene(a_barData, tag);

    m_barchartable = new BarChartToTableScene(m_BarChartDiagram.getBarChart(), m_BarChartDiagram.getCombinedTotals());

    // Layout
    title = "Barchart and Table for " + tag + " period " + m_barchartable.get_StartPeriod() + " to "
        + m_barchartable.get_EndPeriod();

    TabPane tabPane = new TabPane();
    Tab tabDiagram = new Tab("Diagram");
    Tab tabTable = new Tab("Table");

    // Set the main scene on the primaryStage
    Stage tabStage = new Stage();
    tabStage.setTitle(title);

    // Set the panes as the content of the tabs
    VBox tabDiagramSave = new VBox(m_BarChartDiagram.getVBox(), saveDialog(tabStage));
    tabDiagram.setContent(tabDiagramSave);

    VBox tabTableSave = new VBox(m_barchartable.getVBox(), saveDialog(tabStage));
    tabTable.setContent(tabTableSave);
    tabPane.getTabs().addAll(tabDiagram, tabTable);

    // Create a Scene with the TabPane as the root
    Scene mainScene = new Scene(tabPane, 1200, 600);

    tabStage.setScene(mainScene);
    tabStage.show();
  }

  private VBox saveDialog(Stage a_Stage) {
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
      File selectedFile = fileChooser.showSaveDialog(a_Stage);
      if (selectedFile != null) {
        try {
          CreatePdf l_Pdf = new CreatePdf(selectedFile.getAbsolutePath());
          l_Pdf.CreatePage(CreatePdf.c_PageSizeEnum.A2, title);
          l_Pdf.addImageAndLegend(m_BarChartDiagram.getBarChartImage(), m_BarChartDiagram.getLegendImage());

          // l_Pdf.CreatePage(CreatePdf.c_PageSizeEnum.A2, title1);
          // l_Pdf.addTable(m_barchartable.getTable());
          l_Pdf.addImageTable(m_barchartable.getTableViewImage());

          l_Pdf.SaveDocument();
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
    return saveFileLayout;
  }
}

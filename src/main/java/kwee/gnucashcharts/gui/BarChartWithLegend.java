package kwee.gnucashcharts.gui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.Date;
import java.util.logging.Level;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kwee.gnucashcharts.library.barchart.BarChartToTableScene;
import kwee.gnucashcharts.library.barchart.StackedBarChartScene;
import kwee.gnucashcharts.library.gnuCashDb.SamengesteldeStaafData;
import kwee.library.ApplicationMessages;
import kwee.gnucashcharts.library.CreatePdf;
import kwee.gnucashcharts.library.CreatePdf.c_PageSizeEnum;
import kwee.logger.MyLogger;

public class BarChartWithLegend {
  private static final Logger lOGGER = MyLogger.getLogger();
  private String title = "";
  private StackedBarChartScene m_BarChartDiagram;
  private BarChartToTableScene m_barchartable;
  private ApplicationMessages bundle = ApplicationMessages.getInstance();
//  private MessageText m_Messages = new MessageText();

  public void openTabsWindow(File inpFile, String tag, int a_NrBars, LocalDate a_Date) {
    // Initialize
    ActionGnuCshDbStackedBarChart l_barchart = new ActionGnuCshDbStackedBarChart(inpFile, a_NrBars, a_Date);
    SamengesteldeStaafData a_barData = l_barchart.getData();
    m_BarChartDiagram = new StackedBarChartScene(a_barData, tag);
    m_barchartable = new BarChartToTableScene(m_BarChartDiagram.getBarChart(), m_BarChartDiagram.getCombinedTotals());

    // Layout
    title = bundle.getMessage("BarchartTable", tag, m_barchartable.get_StartPeriod(), m_barchartable.get_EndPeriod());

    TabPane tabPane = new TabPane();
    Tab tabDiagram = new Tab(bundle.getMessage("Diagram"));
    Tab tabTable = new Tab(bundle.getMessage("Table"));
    Tab tabTransposedTable = new Tab(bundle.getMessage("TableTransposed"));

    // Set the main scene on the primaryStage
    Stage tabStage = new Stage();
    tabStage.setTitle(title);

    // Set the panes as the content of the tabs
    VBox tabDiagramSave = new VBox(m_BarChartDiagram.getVBox(), saveDialog(tabStage, "BarChart_" + tag));
    tabDiagram.setContent(tabDiagramSave);

    VBox tabTableSave = new VBox(m_barchartable.getVBox(), saveDialog(tabStage, "BarChart_" + tag));
    tabTable.setContent(tabTableSave);

    VBox tabTableTransposed = new VBox(m_barchartable.getVBoxTransposed());
    VBox tabTableTransposedSave = new VBox(saveDialog(tabStage, "BarChart_" + tag), tabTableTransposed);
    tabTableTransposed.prefHeightProperty().bind(tabTableTransposedSave.heightProperty());
    VBox.setVgrow(tabTableTransposed, Priority.ALWAYS);
    tabTableTransposed.resize(1000, 500);
    tabTableTransposedSave.resize(1200, 500);
    tabTransposedTable.setContent(tabTableTransposedSave);

    // Create a Scene with the TabPane as the root
    tabPane.getTabs().addAll(tabDiagram, tabTable, tabTransposedTable);
    Scene mainScene = new Scene(tabPane, 1200, 500);

    tabStage.setScene(mainScene);
    tabStage.show();
  }

  private VBox saveDialog(Stage a_Stage, String a_Filename) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(bundle.getMessage("SaveFile"));

    Button saveButton = new Button(bundle.getMessage("PDFCreate"));
    saveButton.setOnAction(e -> {
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(bundle.getMessage("PDFFiles"), "*.pdf");
      fileChooser.getExtensionFilters().add(extFilter);
      fileChooser.setInitialFileName(a_Filename + CurrentDateStr() + ".pdf");
      if (!MainMenu.m_param.get_PdfFile().isBlank()) {
        File intFile = new File(MainMenu.m_param.get_PdfFile());
        String ldir = intFile.getParent();
        fileChooser.setInitialDirectory(new File(ldir));
      }
      // Show save file dialog
      File selectedFile = fileChooser.showSaveDialog(a_Stage);
      if (selectedFile != null) {
        CreatePdf l_Pdf = new CreatePdf(selectedFile.getAbsolutePath());
        try {
          l_Pdf.CreatePage(CreatePdf.c_PageSizeEnum.A2, title);
          l_Pdf.addImageAndLegend(m_BarChartDiagram.getBarChartImage(), m_BarChartDiagram.getLegendImage());
          l_Pdf.addImageTable(m_barchartable.getTableViewImage());
        } catch (IOException e1) {
          lOGGER.log(Level.INFO, "PDF BarChartImage: " + e1.getMessage());
        }
        try {
          l_Pdf.CreatePage(c_PageSizeEnum.A0, title);
          l_Pdf.addTable(m_barchartable.getTable(), false);
        } catch (IOException e1) {
          lOGGER.log(Level.INFO, "PDF Table: " + e1.getMessage());
        }
        try {
          l_Pdf.CreatePage(c_PageSizeEnum.A3, title);
          l_Pdf.addTable(m_barchartable.getTransposedTable(), true);
        } catch (IOException e1) {
          lOGGER.log(Level.INFO, "PDF Transposed: " + e1.getMessage());
        }
        try {
          l_Pdf.SaveDocument();
        } catch (IOException e1) {
          lOGGER.log(Level.INFO, "PDF Save: " + e1.getMessage());
        }
        lOGGER.log(Level.INFO, bundle.getMessage("PDFGenerated", selectedFile.getAbsolutePath()));
        MainMenu.m_param.set_Pdf_file(selectedFile);
        MainMenu.m_param.save();

      }
    });
    VBox saveFileLayout = new VBox(saveButton);
    saveFileLayout.setSpacing(10);
    VBox.setMargin(saveButton, new Insets(10, 10, 10, 10));
    return saveFileLayout;
  }

  private String CurrentDateStr() {
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("--dd-MM-yyyy");
    String formattedDate = dateFormat.format(currentDate);
    return formattedDate;
  }
}

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
import javafx.scene.layout.Pane;
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
  private StackedBarChartScene l_ScenBar;
  private BarChartToTableScene barchartable;
  private Scene m_scenechart;
  private Scene m_sceneTable;

  public void openTabsWindow(File inpFile, String tag, int a_NrBars) {
    openBarChartWindow(inpFile, tag, a_NrBars);
    openBarChartTableWindow(inpFile, tag, a_NrBars);

    TabPane tabPane = new TabPane();
    Tab tabDiagram = new Tab("Diagram");
    Tab tabTable = new Tab("Table");

    // Create Panes to hold the scenes
    Pane paneDiagram = new Pane(); // You can use other layouts like VBox, HBox, etc. as needed
    Pane paneTable = new Pane();

    // Add the scenes to the panes
    paneDiagram.getChildren().add(m_scenechart.getRoot()); // Assuming scene1 is a Scene instance
    paneTable.getChildren().add(m_sceneTable.getRoot()); // Assuming scene2 is a Scene instance

    // Set the panes as the content of the tabs
    tabDiagram.setContent(paneDiagram);
    tabTable.setContent(paneTable);
    tabPane.getTabs().addAll(tabDiagram, tabTable);

    // Create a Scene with the TabPane as the root
    Scene mainScene = new Scene(tabPane, 800, 600);

    // Set the main scene on the primaryStage
    Stage tabStage = new Stage();
    tabStage.setTitle(tag);

    tabStage.setScene(mainScene);
    tabStage.show();
  }

  public void openBarChartWindow(File inpFile, String tag, int a_NrBars) {
    lOGGER.log(Level.INFO, "Tag " + tag);
    ActionGnuCshDbStackedBarChart l_barchart = new ActionGnuCshDbStackedBarChart(inpFile, a_NrBars);
    SamengesteldeStaafData a_barData = l_barchart.getData();

    Stage barchartStage = new Stage();
    l_ScenBar = new StackedBarChartScene(a_barData, tag);

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
          String title1 = "Table for " + tag + " period " + barchartable.get_StartPeriod() + " to "
              + barchartable.get_EndPeriod();

          CreatePdf l_Pdf = new CreatePdf(selectedFile.getAbsolutePath());
          l_Pdf.CreatePage(CreatePdf.c_PageSizeEnum.A4, title);
          l_Pdf.addImageAndLegend(l_ScenBar.getBarChartImage(), l_ScenBar.getLegendImage());

          l_Pdf.CreatePage(CreatePdf.c_PageSizeEnum.A2, title1);
          l_Pdf.addImageTable(barchartable.getTableViewImage());

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

    // Set up the scene and add the VBox to it
    m_scenechart = l_ScenBar.getScene(saveFileLayout);
  }

  private void openBarChartTableWindow(File inpFile, String tag, int a_NrBars) {
    lOGGER.log(Level.INFO, "Tag " + tag);

    Stage barchartTableStage = new Stage();
    barchartable = new BarChartToTableScene(l_ScenBar.getBarChart(), l_ScenBar.getCombinedTotals());
    title = "Bartchart and Table for " + tag + " period " + barchartable.get_StartPeriod() + " to "
        + barchartable.get_EndPeriod();
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
          CreatePdf l_Pdf = new CreatePdf(selectedFile.getAbsolutePath());
          l_Pdf.CreatePage(CreatePdf.c_PageSizeEnum.A2, title);
          l_Pdf.addImageAndLegend(l_ScenBar.getBarChartImage(), l_ScenBar.getLegendImage());

          // l_Pdf.CreatePage(CreatePdf.c_PageSizeEnum.A2, title1);
          l_Pdf.addImageTable(barchartable.getTableViewImage());

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

    m_sceneTable = barchartable.getScene(saveFileLayout);
  }
}

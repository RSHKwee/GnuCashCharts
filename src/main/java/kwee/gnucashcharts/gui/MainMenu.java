package kwee.gnucashcharts.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import kwee.logger.MyLogger;

import kwee.gnucashcharts.main.Main;
import kwee.gnucashcharts.main.UserSetting;
import kwee.library.JarInfo;
import kwee.gnucashcharts.library.SubjectsColors;
import kwee.gnucashcharts.library.JavaFXLogHandler;
import kwee.gnucashcharts.library.TaartPuntData;

public class MainMenu extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();
  public static UserSetting m_param = new UserSetting();
  static String m_creationtime = Main.m_creationtime;

  private int c_NrBars = 24; // Months
  private int nrBars = c_NrBars;

  private Level m_Level = Level.INFO;
  private String m_Logdir = "c:\\";
  private boolean m_toDisk = false;

  private String m_tag = "";
  private TaartPuntData m_pieData;
  private File m_SelectedFile;
  private SubjectsColors m_SubjColors;

  @Override
  public void start(Stage primaryStage) {
    // Logger setup
    TextArea logTextArea = new TextArea();
    try {
      MyLogger.setup(m_Level, m_Logdir, m_toDisk);

      JavaFXLogHandler fxLogHandler = new JavaFXLogHandler(logTextArea);
      lOGGER.addHandler(fxLogHandler);
    } catch (IOException e1) {
      lOGGER.log(Level.INFO, e1.getMessage());
    }
    // Menubar

    // Main Window
    FileChooser fileChooser = new FileChooser();
    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));

    Label l_file = new Label("Kies een GnuCash bestand");
    Label l_tag = new Label("Kies een tag");

    Button openFileButton = new Button("Open File");
    openFileButton.setOnAction(e -> {
      if (!m_param.get_InputFile().isBlank()) {
        File intFile = new File(m_param.get_InputFile());
        String ldir = intFile.getParent();
        fileChooser.setInitialDirectory(new File(ldir));
      }
      File selectedFile = fileChooser.showOpenDialog(primaryStage);
      if (selectedFile != null) {
        m_SelectedFile = selectedFile;
        if (selectedFile.getAbsolutePath().toLowerCase().contains(".html")) {
          ActionHTMLPieChart pieSelect = new ActionHTMLPieChart(selectedFile);
          m_pieData = pieSelect.getData();
        } else {
          ActionGnuCashDbPieChart pieSelect = new ActionGnuCashDbPieChart(selectedFile);
          m_pieData = pieSelect.getData();
        }

        l_file.setText(selectedFile.getAbsolutePath());
        l_tag.setText("Kies een tag");

        Set<String> tags = m_pieData.getTags();
        // Convert the Set<String> to ObservableList<String>
        ObservableList<String> observableList;
        observableList = FXCollections.observableArrayList(tags);
        comboBox.setItems(observableList);

        m_param.set_InputFile(selectedFile);
        m_param.save();
      }
    });

    comboBox.setOnAction(e -> {
      String selectedOption = comboBox.getValue();
      if (selectedOption != null) {
        lOGGER.log(Level.INFO, "Selected Option: " + selectedOption);
        m_tag = selectedOption;
        l_tag.setText(m_tag);
        m_param.set_Tag(m_tag);
        m_param.save();

        ArrayList<String> l_Subjects = m_pieData.getSubjects(m_tag);
        m_SubjColors = new SubjectsColors(l_Subjects);
      }
    });

    PieChartWithLegend piwindow = new PieChartWithLegend();
    Button buttonPiechart = new Button("Open Piechart");
    buttonPiechart.setOnAction(e -> piwindow.openPieChartWindow(m_pieData, m_tag, m_SubjColors));

    nrBars = m_param.get_NrBars();
    Label titleLabel = new Label(" # bars:");
    TextField integerField = new TextField(Integer.toString(nrBars));
    integerField.setOnAction(e -> {
      try {
        int integerValue = Integer.parseInt(integerField.getText());
        nrBars = integerValue;
        m_param.set_NrBars(nrBars);
        m_param.save();
        lOGGER.log(Level.INFO, "Number of bars: " + nrBars);
      } catch (NumberFormatException ex) {
        lOGGER.log(Level.INFO, "Invalid input. Please enter a valid integer.");
      }
    });

    BarChartWithLegend barwindow = new BarChartWithLegend();
    // BarChartTable barTableWindow = new BarChartTable();
    Button buttonBarchart = new Button("Open Barchart");
    buttonBarchart.setOnAction(e -> {
      ;
      barwindow.openBarChartWindow(m_SelectedFile, m_tag, nrBars);
      // barTableWindow.openBarChartTableWindow(m_SelectedFile, m_tag, nrBars);
    });

    // Do the layout
    HBox openFileLayout = new HBox(openFileButton, l_file);
    HBox selectOptionLayout = new HBox(comboBox, l_tag);
    HBox buttonPiechartLayout = new HBox(buttonPiechart);
    HBox buttonBarchartLayout = new HBox(titleLabel, integerField, buttonBarchart);

    openFileLayout.setSpacing(10);
    selectOptionLayout.setSpacing(10);
    buttonPiechartLayout.setSpacing(10);
    buttonBarchartLayout.setSpacing(10);

    HBox.setMargin(openFileButton, new Insets(10, 10, 10, 10));
    HBox.setMargin(l_file, new Insets(10, 10, 10, 10));

    HBox.setMargin(comboBox, new Insets(10, 10, 10, 10));
    HBox.setMargin(l_tag, new Insets(10, 10, 10, 10));

    HBox.setMargin(buttonPiechart, new Insets(10, 10, 10, 10));

    HBox.setMargin(buttonBarchart, new Insets(10, 10, 10, 10));
    HBox.setMargin(titleLabel, new Insets(10, 10, 10, 10));
    HBox.setMargin(integerField, new Insets(10, 10, 10, 10));

    VBox layout = new VBox(openFileLayout, selectOptionLayout, buttonPiechartLayout, buttonBarchartLayout, logTextArea);
    Scene scene = new Scene(layout, 700, 375);

    primaryStage.setScene(scene);
    primaryStage.setTitle("GnuCash charts (" + m_creationtime + ")");
    primaryStage.show();

    lOGGER.log(Level.INFO, "GnuCash charts (" + m_creationtime + ")");
  }

  public static void main(String[] args) {
    if (m_creationtime == null) {
      m_creationtime = JarInfo.getProjectVersion(Main.class);
    }
    launch(args);
  }
}

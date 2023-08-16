package kwee.gnucashcharts.gui;

import java.io.File;
import java.io.IOException;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import kwee.logger.MyLogger;

import kwee.gnucashcharts.main.Main;
import kwee.gnucashcharts.main.UserSetting;
import kwee.gnucashcharts.library.JavaFXLogHandler;
import kwee.gnucashcharts.library.TaartPuntDataIf;

public class MainMenu extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();
  public static UserSetting m_param = new UserSetting();
  static String m_creationtime = Main.m_creationtime;

  private Level m_Level = Level.INFO;
  private String m_Logdir = "c:\\";
  private boolean m_toDisk = false;

  private String m_tag = "";
  private TaartPuntDataIf m_pieData;

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
      }
    });

    // Do the layout
    PieChartWithLegend piwindow = new PieChartWithLegend();
    Button buttonPiechart = new Button("Open Piechart");
    buttonPiechart.setOnAction(e -> piwindow.openPieChartWindow(m_pieData, m_tag));

    HBox openFileLayout = new HBox(openFileButton, l_file);
    HBox selectOptionLayout = new HBox(comboBox, l_tag);
    HBox buttonPiechartLayout = new HBox(buttonPiechart);

    openFileLayout.setSpacing(10);
    selectOptionLayout.setSpacing(10);
    buttonPiechartLayout.setSpacing(10);

    HBox.setMargin(openFileButton, new Insets(10, 10, 10, 10));
    HBox.setMargin(l_file, new Insets(10, 10, 10, 10));
    HBox.setMargin(comboBox, new Insets(10, 10, 10, 10));
    HBox.setMargin(l_tag, new Insets(10, 10, 10, 10));
    HBox.setMargin(buttonPiechart, new Insets(10, 10, 10, 10));

    VBox layout = new VBox(openFileLayout, selectOptionLayout, buttonPiechartLayout, logTextArea);
    Scene scene = new Scene(layout, 700, 325);

    primaryStage.setScene(scene);
    primaryStage.setTitle("GnuCash charts (" + m_creationtime + ")");
    primaryStage.show();

    lOGGER.log(Level.INFO, "GnuCash charts (" + m_creationtime + ")");
  }

  public static void main(String[] args) {
    launch(args);
  }
}

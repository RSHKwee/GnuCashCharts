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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import kwee.logger.MyLogger;

import kwee.gnucashcharts.main.UserSetting;
import kwee.gnucashcharts.library.JavaFXLogHandler;
import kwee.gnucashcharts.library.html.ReadHTMLTable;
import kwee.gnucashcharts.library.html.TaartPuntData;

public class MainMenu extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();
  public static UserSetting m_param = new UserSetting();

  private Level m_Level = Level.INFO;
  private String m_Logdir = "c:\\";
  private boolean m_toDisk = false;

  private String m_inpfile = "";
  private String m_tag = "";

  @Override
  public void start(Stage primaryStage) {
    TextArea logTextArea = new TextArea();
    try {
      MyLogger.setup(m_Level, m_Logdir, m_toDisk);

      // Create and add the custom handler to the logger
      JavaFXLogHandler fxLogHandler = new JavaFXLogHandler(logTextArea);
      lOGGER.addHandler(fxLogHandler);
    } catch (IOException e1) {
      lOGGER.log(Level.INFO, e1.getMessage());
    }

    FileChooser fileChooser = new FileChooser();
    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));

    Label l_file = new Label("Kies een HTML bestand");
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
        lOGGER.log(Level.INFO, "Selected File: " + selectedFile.getAbsolutePath());
        m_inpfile = selectedFile.getAbsolutePath();
        l_file.setText(selectedFile.getAbsolutePath());
        m_param.set_InputFile(selectedFile.getAbsoluteFile());
        l_tag.setText("Kies een tag");
        m_param.save();

        ReadHTMLTable htmltable = new ReadHTMLTable(selectedFile.getAbsolutePath());
        ArrayList<String> regels = htmltable.parseHTMLpage();
        TaartPuntData pieData = new TaartPuntData(regels);
        Set<String> tags = pieData.getTags();
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
    buttonPiechart.setOnAction(e -> piwindow.openPieChartWindow(m_inpfile, m_tag));

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
    primaryStage.setTitle("GnuCash charts");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

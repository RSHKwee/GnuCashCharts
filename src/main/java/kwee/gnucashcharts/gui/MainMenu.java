package kwee.gnucashcharts.gui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
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
import kwee.gnucashcharts.library.MessageText;
import kwee.gnucashcharts.library.TaartPuntData;

public class MainMenu extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();
  public static UserSetting m_param = new UserSetting();
  private MessageText m_Messages = new MessageText();

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
  private LocalDate m_Date = LocalDate.now();

  @Override
  public void start(Stage primaryStage) {
    // Messages
    String msg_Title = m_Messages.msg_Title;
    String msg_SelectedDate = m_Messages.msg_SelectedDate;
    String msg_InvInpInt = m_Messages.msg_InvInpInt;
    String msg_NrOfMonth = m_Messages.msg_NrOfMonth;
    String msg_MonthLab = m_Messages.msg_MonthLab;
    String msg_DateLabel = m_Messages.msg_DateLabel;
    String msg_SelectedSubject = m_Messages.msg_SelectedSubject;
    String msg_SelectSubject = m_Messages.msg_SelectSubject;
    String msg_ChooseGnuCashFile = m_Messages.msg_ChooseGnuCashFile;
    String msg_OpenFile = m_Messages.msg_OpenFile;
    String msg_OpenPieChart = m_Messages.msg_OpenPieChart;
    String msg_OpenBarChart = m_Messages.msg_OpenBarChart;

    // Logger setup
    TextArea logTextArea = new TextArea();
    try {
      MyLogger.setup(m_Level, m_Logdir, m_toDisk);

      JavaFXLogHandler fxLogHandler = new JavaFXLogHandler(logTextArea);
      lOGGER.addHandler(fxLogHandler);
    } catch (IOException e1) {
      lOGGER.log(Level.INFO, e1.getMessage());
    }
    // Defaults
    nrBars = m_param.get_NrBars();

    // Menubar

    // Main Window
    FileChooser inpFileChooser = new FileChooser();
    ComboBox<String> comboTagBox = new ComboBox<>(
        FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
    Button buttonPiechart = new Button(msg_OpenPieChart);
    TextField integerField = new TextField(Integer.toString(nrBars));
    DatePicker datePicker = new DatePicker();
    Button buttonBarchart = new Button(msg_OpenBarChart);

    comboTagBox.setDisable(true);
    buttonPiechart.setDisable(true);
    integerField.setDisable(true);
    datePicker.setDisable(true);
    buttonBarchart.setDisable(true);

    Label l_file = new Label(msg_ChooseGnuCashFile);
    Label l_tag = new Label(msg_SelectSubject);

    Button openFileButton = new Button(msg_OpenFile);
    openFileButton.setOnAction(e -> {
      if (!m_param.get_InputFile().isBlank()) {
        File intFile = new File(m_param.get_InputFile());
        String ldir = intFile.getParent();
        inpFileChooser.setInitialDirectory(new File(ldir));
      }
      File selectedFile = inpFileChooser.showOpenDialog(primaryStage);
      if (selectedFile != null) {
        m_SelectedFile = selectedFile;
        if (selectedFile.getAbsolutePath().toLowerCase().contains(".html")) {
          ActionHTMLPieChart pieSelect = new ActionHTMLPieChart(selectedFile);
          m_pieData = pieSelect.getData();
        } else {
          ActionGnuCashDbPieChart pieSelect = new ActionGnuCashDbPieChart(selectedFile, m_Date);
          m_pieData = pieSelect.getData();
        }

        l_file.setText(selectedFile.getAbsolutePath());
        l_tag.setText(msg_SelectSubject);

        Set<String> tags = m_pieData.getTags();
        ObservableList<String> observableList;
        observableList = FXCollections.observableArrayList(tags);
        comboTagBox.setItems(observableList);

        comboTagBox.setDisable(false);
        datePicker.setDisable(false);

        m_param.set_InputFile(selectedFile);
        m_param.save();
      }
    });

    comboTagBox.setOnAction(e -> {
      String selectedOption = comboTagBox.getValue();
      if (selectedOption != null) {
        lOGGER.log(Level.INFO, msg_SelectedSubject + ": " + selectedOption);
        m_tag = selectedOption;
        l_tag.setText(m_tag);
        m_param.set_Tag(m_tag);
        m_param.save();

        buttonPiechart.setDisable(false);
        integerField.setDisable(false);
        buttonBarchart.setDisable(false);

        ArrayList<String> l_Subjects = m_pieData.getSubjects(m_tag);
        m_SubjColors = new SubjectsColors(l_Subjects);
      }
    });

    PieChartWithLegend piwindow = new PieChartWithLegend();
    buttonPiechart.setOnAction(e -> {
      ActionGnuCashDbPieChart pieSelect = new ActionGnuCashDbPieChart(m_SelectedFile, m_Date);
      m_pieData = pieSelect.getData();
      piwindow.openPieChartWindow(m_pieData, m_tag, m_SubjColors, m_Date);
    });

    Label nrBarsLabel = new Label(" # " + msg_MonthLab + ": ");
    integerField.setOnAction(e -> {
      try {
        int integerValue = Integer.parseInt(integerField.getText());
        nrBars = integerValue;
        m_param.set_NrBars(nrBars);
        m_param.save();
        lOGGER.log(Level.INFO, msg_NrOfMonth + ": " + nrBars);
      } catch (NumberFormatException ex) {
        lOGGER.log(Level.INFO, msg_InvInpInt);
      }
    });

    Label endDateLabel = new Label(msg_DateLabel + ": ");
    datePicker.setOnAction(event -> {
      LocalDate selectedDate = datePicker.getValue();
      m_Date = selectedDate;
      lOGGER.log(Level.INFO, msg_SelectedDate + ": " + selectedDate);
    });

    BarChartWithLegend barwindow = new BarChartWithLegend();
    buttonBarchart.setOnAction(e -> {
      barwindow.openTabsWindow(m_SelectedFile, m_tag, nrBars, m_Date);
    });

    // Do the layout
    HBox openFileLayout = new HBox(openFileButton, l_file);
    HBox selectOptionLayout = new HBox(endDateLabel, datePicker, comboTagBox, l_tag);
    HBox buttonPiechartLayout = new HBox(buttonPiechart);
    HBox buttonBarchartLayout = new HBox(nrBarsLabel, integerField, buttonBarchart);

    openFileLayout.setSpacing(10);
    selectOptionLayout.setSpacing(10);
    buttonPiechartLayout.setSpacing(10);
    buttonBarchartLayout.setSpacing(10);

    HBox.setMargin(openFileButton, new Insets(10, 10, 10, 10));
    HBox.setMargin(l_file, new Insets(10, 10, 10, 10));

    HBox.setMargin(comboTagBox, new Insets(10, 10, 10, 10));
    HBox.setMargin(l_tag, new Insets(10, 10, 10, 10));

    HBox.setMargin(buttonPiechart, new Insets(10, 10, 10, 10));

    HBox.setMargin(buttonBarchart, new Insets(10, 10, 10, 10));
    HBox.setMargin(nrBarsLabel, new Insets(10, 10, 10, 10));
    HBox.setMargin(integerField, new Insets(10, 10, 10, 10));
    HBox.setMargin(endDateLabel, new Insets(10, 10, 10, 10));
    HBox.setMargin(datePicker, new Insets(10, 10, 10, 10));

    VBox layout = new VBox(openFileLayout, selectOptionLayout, buttonPiechartLayout, buttonBarchartLayout, logTextArea);
    Scene scene = new Scene(layout, 700, 375);

    primaryStage.setScene(scene);
    primaryStage.setTitle(msg_Title);
    primaryStage.show();

    lOGGER.log(Level.INFO, msg_Title);
  }

  /**
   * Main entry point.
   * 
   * @param args Arguments
   */
  public static void main(String[] args) {
    if (m_creationtime == null) {
      m_creationtime = JarInfo.getProjectVersion(Main.class);
    }
    launch(args);
  }
}

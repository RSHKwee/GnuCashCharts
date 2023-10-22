package kwee.gnucashcharts.gui;

import java.awt.Desktop;
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
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import kwee.logger.LoggerPrintErrStream;
import kwee.logger.LoggerPrintOutStream;
import kwee.logger.MyLogger;

import kwee.gnucashcharts.main.Main;
import kwee.gnucashcharts.main.UserSetting;
import kwee.library.ApplicationMessages;
import kwee.library.JarInfo;
import kwee.library.FX.AboutWindow;
import kwee.library.FX.JavaFXLogHandler;
import kwee.gnucashcharts.library.SubjectsColors;
import kwee.gnucashcharts.library.TaartPuntData;

public class MainMenu extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();
  static String m_creationtime = Main.m_creationtime;
  static final String c_CopyrightYear = "2023";
  private static String c_reponame = "GNUCashCharts";

  public static UserSetting m_param = new UserSetting();
  private final String[] c_levels = { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" };

  // Replace "path/to/help/file" with the actual path to your help file
  static final String m_HelpFile = "GNUCharts.chm";

  private String m_Language = "en";

  private ApplicationMessages bundle = ApplicationMessages.getInstance();

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
    // Redirect Out- and Error stream to Logger
    LoggerPrintOutStream.redirectOutStreamToLogger(lOGGER, Level.WARNING);
    LoggerPrintErrStream.redirectErrorStreamToLogger(lOGGER, Level.WARNING);

    // Defaults
    nrBars = m_param.get_NrBars();
    m_Language = m_param.get_Language();
    bundle.changeLanguage(m_param.get_Language());
    m_Logdir = m_param.get_LogDir();
    m_toDisk = m_param.is_toDisk();

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
    MenuBar menubar = doTheMenuBar(primaryStage);

    // Main Window
    FileChooser inpFileChooser = new FileChooser();
    ComboBox<String> comboTagBox = new ComboBox<>(
        FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
    Button buttonPiechart = new Button(bundle.getMessage("OpenPieChart"));
    TextField integerField = new TextField(Integer.toString(nrBars));
    DatePicker datePicker = new DatePicker();
    Button buttonBarchart = new Button(bundle.getMessage("OpenBarChart"));

    comboTagBox.setDisable(true);
    buttonPiechart.setDisable(true);
    integerField.setDisable(true);
    datePicker.setDisable(true);
    buttonBarchart.setDisable(true);

    Label l_file = new Label(bundle.getMessage("ChooseGnuCashFile"));
    Label l_tag = new Label(bundle.getMessage("SelectSubject"));

    Button openFileButton = new Button(bundle.getMessage("OpenFile"));
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
        l_tag.setText(bundle.getMessage("SelectedFile"));

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
        lOGGER.log(Level.INFO, bundle.getMessage("SelectedSubject", selectedOption));
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

    Label nrBarsLabel = new Label(bundle.getMessage("MonthLab"));
    integerField.setOnAction(e -> {
      try {
        int integerValue = Integer.parseInt(integerField.getText());
        nrBars = integerValue;
        m_param.set_NrBars(nrBars);
        m_param.save();
        lOGGER.log(Level.INFO, bundle.getMessage("NrOfMonth", nrBars));
      } catch (NumberFormatException ex) {
        lOGGER.log(Level.INFO, bundle.getMessage("InvInpInt"));
      }
    });

    Label endDateLabel = new Label(bundle.getMessage("DateLabel"));
    datePicker.setOnAction(event -> {
      LocalDate selectedDate = datePicker.getValue();
      m_Date = selectedDate;
      lOGGER.log(Level.INFO, bundle.getMessage("SelectedDate", selectedDate));
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

    BorderPane root = new BorderPane();
    root.setTop(menubar);
    root.setCenter(layout);

    Scene scene = new Scene(root, 700, 375);
    primaryStage.setScene(scene);
    primaryStage.setTitle(bundle.getMessage("Title", m_creationtime));
    primaryStage.show();

    lOGGER.log(Level.INFO, bundle.getMessage("Title", m_creationtime));
  }

  // Menubar
  private int il = 0;

  private MenuBar doTheMenuBar(Stage primaryStage) {
    MenuBar menuBar = new MenuBar();

    // Settings Menu
    Menu mnSettings = new Menu(bundle.getMessage("Settings"));
    mnSettings.setDisable(false);

    // Loglevel:
    Menu mntmLoglevel = new Menu(bundle.getMessage("Loglevel"));
    ToggleGroup toggleGroup = new ToggleGroup();
    RadioMenuItem[] loglevelitems = new RadioMenuItem[c_levels.length];

    ArrayList<String> ca_Levels = new ArrayList<String>();
    for (il = 0; il < c_levels.length; il++) {
      ca_Levels.add(c_levels[il]);
    }

    il = 0;
    ca_Levels.forEach(level -> {
      loglevelitems[il] = new RadioMenuItem(c_levels[il]);
      toggleGroup.getToggles().add(loglevelitems[il]);
      mntmLoglevel.getItems().add(loglevelitems[il]);
      loglevelitems[il].setOnAction(e -> {
        m_Level = Level.parse(level.toUpperCase());
        m_param.set_Level(m_Level);
        MyLogger.changeLogLevel(m_Level);
        lOGGER.log(Level.INFO, level + " Selected");
      });
      if (c_levels[il].toLowerCase().equals(m_Level.toString().toLowerCase())) {
        toggleGroup.selectToggle(loglevelitems[il]);
      }
      il++;
    });
    mnSettings.getItems().add(mntmLoglevel);

    // Language
    ToggleGroup toggleLanguagesGroup = new ToggleGroup();
    Set<String> langs = bundle.getTranslations();
    Menu mntmLanguages = new Menu("Languages");
    il = 0;
    RadioMenuItem[] mnLanguages = new RadioMenuItem[langs.size()];
    langs.forEach(language -> {
      mnLanguages[il] = new RadioMenuItem(language);
      toggleLanguagesGroup.getToggles().add(mnLanguages[il]);
      mntmLanguages.getItems().add(mnLanguages[il]);
      mnLanguages[il].setOnAction(e -> {
        lOGGER.log(Level.INFO, language + " Selected");
        m_Language = language;
        m_param.set_Language(m_Language);
        m_param.save();
        bundle.changeLanguage(language);
        restartGUI(primaryStage);
      });
      if (language.toLowerCase().equals(m_Language.toLowerCase())) {
        toggleLanguagesGroup.selectToggle(mnLanguages[il]);
      }
      il++;
    });
    mnSettings.getItems().add(mntmLanguages);

    // Logfiles
    CheckMenuItem checkMenuItem = new CheckMenuItem("Logfiles");
    checkMenuItem.setOnAction(e -> {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Output Directory");
      File selectedDirectory = directoryChooser.showDialog(primaryStage);

      if (selectedDirectory != null) {
        lOGGER.log(Level.INFO, bundle.getMessage("LogFolder", selectedDirectory.getAbsolutePath()));
        m_Logdir = selectedDirectory.getAbsolutePath() + "/";
        m_param.set_LogDir(m_Logdir);
        m_param.set_toDisk(true);
        m_toDisk = checkMenuItem.isSelected();
        try {
          MyLogger.setup(m_Level, m_Logdir, m_toDisk);
        } catch (IOException es) {
          lOGGER.log(Level.SEVERE, Class.class.getName() + ": " + es.toString());
        }
      } else {
        lOGGER.log(Level.INFO, bundle.getMessage("NoDirectorySelected"));
      }
    });
    mnSettings.getItems().add(checkMenuItem);

    // Stored preferences
    MenuItem menuPreferences = new MenuItem("Preferences");
    menuPreferences.setOnAction(e -> {
      UserSetting showpref = new UserSetting();
      showpref.showAllPreferences(true);
    });
    mnSettings.getItems().add(menuPreferences);

    // Menu "?" and Help
    Menu questMenu = new Menu("?");
    MenuItem menuHelp = new MenuItem("Help");
    menuHelp.setOnAction(e -> {
      File helpFile = new File("help\\" + m_Language + "\\" + m_HelpFile);
      if (helpFile.exists()) {
        try {
          // Open the help file with the default viewer
          Desktop.getDesktop().open(helpFile);
        } catch (IOException e1) {
          lOGGER.log(Level.INFO, e1.getMessage());
          e1.printStackTrace();
        }
      } else {
        lOGGER.log(Level.INFO, bundle.getMessage("HelpFileNotFound", helpFile.getAbsolutePath()));
      }
    });
    questMenu.getItems().add(menuHelp);

    // About
    MenuItem menuAbout = new MenuItem(bundle.getMessage("About"));
    menuAbout.setOnAction(e -> {
      @SuppressWarnings("unused")
      AboutWindow about = new AboutWindow(c_reponame, m_creationtime, c_CopyrightYear);
    });
    questMenu.getItems().add(menuAbout);

    // MenuBar build up
    menuBar.getMenus().addAll(mnSettings, questMenu);
    return menuBar;
  }

  /**
   * Restart GUI for Language change purpose...
   * 
   * @param primaryStage Stage
   */
  private void restartGUI(Stage primaryStage) {
    primaryStage.close();
    start(new Stage());
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

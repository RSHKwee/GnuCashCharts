package zandbak_reform;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import kwee.library.ApplicationMessages;
import kwee.logger.MyLogger;

public class MenuBarMain extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();
  private final String[] c_levels = { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" };

  // Replace "path/to/help/file" with the actual path to your help file
  static final String m_HelpFile = "GNUCharts.chm";

  private Level m_Level = Level.INFO;
  private String m_Language = "en";

  private ApplicationMessages bundle = ApplicationMessages.getInstance();
  private int il = 0;
  /*
   * @formatter:off
   * Menu bar:
   * - Settings:
   *   - Loglevel - "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" 
   *   - LookAndFeel - Nimbus, etc.
   *   - Choose Language - en, nl, etc.
   *   - Checkbox: Create Logfiles -> Output directory
   *   - Stored preferences
   * 
   * - ?:
   *   - Help
   *   - About
   * 
   * @formatter:on
   */

  @Override
  public void start(Stage primaryStage) throws Exception {
    MenuBar menuBar = new MenuBar();

    // Settings Menu
    Menu mnSettings = new Menu("Settings");
    mnSettings.setDisable(false);

    // Loglevel:
    Menu mntmLoglevel = new Menu("Loglevel");
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
        m_Level = Level.parse(level);
        System.out.println(level + " Selected");
      });
      if (c_levels[il].toLowerCase().equals(m_Level.toString().toLowerCase())) {
        toggleGroup.selectToggle(loglevelitems[il]);
      }
      il++;
    });
    mnSettings.getItems().add(mntmLoglevel);

    // Look and Feel
    // Will not be implemented....

    // Language
    ToggleGroup toggleLanguagesGroup = new ToggleGroup();
    Set<String> langs = bundle.getTranslations();
    Menu mntmLanguages = new Menu("Languages");
    il = 0;
    RadioMenuItem[] mnLanguages = new RadioMenuItem[langs.size()];
    langs.forEach(lang -> {
      mnLanguages[il] = new RadioMenuItem(lang);
      toggleLanguagesGroup.getToggles().add(mnLanguages[il]);
      mntmLanguages.getItems().add(mnLanguages[il]);
      mnLanguages[il].setOnAction(e -> {
        m_Language = lang;
        System.out.println(lang + " Selected");
      });
      if (lang.toLowerCase().equals(m_Language.toLowerCase())) {
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
        // The user selected a directory
        String selectedPath = selectedDirectory.getAbsolutePath();
        // Do something with the selected directory path
        System.out.println("Selected Directory: " + selectedPath);
      } else {
        // No directory was selected
        System.out.println("No directory selected.");
      }

    });
    mnSettings.getItems().add(checkMenuItem);

    // Stored preferences

    // Menu "?"
    Menu questMenu = new Menu("?");

    // About
    MenuItem menuAbout = new MenuItem("About");

    // Help
    MenuItem menuHelp = new MenuItem("Help");
    menuHelp.setOnAction(e -> {
      File helpFile = new File("help\\" + m_Language + "\\" + m_HelpFile);

      if (helpFile.exists()) {
        try {
          // Open the help file with the default viewer
          Desktop.getDesktop().open(helpFile);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else {
        lOGGER.log(Level.INFO, bundle.getMessage("HelpFileNotFound", helpFile.getAbsolutePath()));
      }
    });
    questMenu.getItems().add(menuHelp);
    questMenu.getItems().add(menuAbout);

    // MenuBar build up
    menuBar.getMenus().addAll(mnSettings, questMenu);

    // Test purpose
    BorderPane root = new BorderPane();
    root.setTop(menuBar);

    Scene scene = new Scene(root, 800, 600);

    primaryStage.setTitle("JavaFX MenuBar Example");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

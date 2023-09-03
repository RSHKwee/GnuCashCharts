package kwee.gnucashcharts.gui;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kwee.library.ApplicationMessages;
import kwee.logger.MyLogger;

public class MenuBarMain extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();

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

    Menu mnSettings = new Menu("Settings");
    mnSettings.setDisable(false);

    // Loglevel:
    Menu mntmLoglevel = new Menu("Loglevel");
    RadioMenuItem mnItemLogOFF = new RadioMenuItem("OFF");
    RadioMenuItem mnItemLogSEVERE = new RadioMenuItem("SEVERE");
    RadioMenuItem mnItemLogWARNING = new RadioMenuItem("WARNING");
    RadioMenuItem mnItemLogINFO = new RadioMenuItem("INFO");
    RadioMenuItem mnItemLogCONFIG = new RadioMenuItem("CONFIG");
    RadioMenuItem mnItemLogFINE = new RadioMenuItem("FINE");
    RadioMenuItem mnItemLogFINER = new RadioMenuItem("FINER");
    RadioMenuItem mnItemLogFINEST = new RadioMenuItem("FINEST");
    RadioMenuItem mnItemLogALL = new RadioMenuItem("ALL");

    ToggleGroup toggleGroup = new ToggleGroup();
    toggleGroup.getToggles().add(mnItemLogOFF);
    toggleGroup.getToggles().add(mnItemLogSEVERE);
    toggleGroup.getToggles().add(mnItemLogWARNING);
    toggleGroup.getToggles().add(mnItemLogINFO);
    toggleGroup.getToggles().add(mnItemLogCONFIG);
    toggleGroup.getToggles().add(mnItemLogFINE);
    toggleGroup.getToggles().add(mnItemLogFINER);
    toggleGroup.getToggles().add(mnItemLogFINEST);
    toggleGroup.getToggles().add(mnItemLogALL);

    switch (m_Level.toString()) {
    case "OFF":
      toggleGroup.selectToggle(mnItemLogOFF);
      break;
    case "SEVERE":
      toggleGroup.selectToggle(mnItemLogSEVERE);
      break;
    case "WARNING":
      toggleGroup.selectToggle(mnItemLogWARNING);
      break;
    case "INFO":
      toggleGroup.selectToggle(mnItemLogINFO);
      break;
    case "CONFIG":
      toggleGroup.selectToggle(mnItemLogCONFIG);
      break;
    case "FINE":
      toggleGroup.selectToggle(mnItemLogFINE);
      break;
    case "FINER":
      toggleGroup.selectToggle(mnItemLogFINER);
      break;
    case "FINEST":
      toggleGroup.selectToggle(mnItemLogFINEST);
      break;
    case "ALL":
      toggleGroup.selectToggle(mnItemLogALL);
      break;
    default:
      toggleGroup.selectToggle(mnItemLogINFO);
    }

    mntmLoglevel.getItems().add(mnItemLogOFF);
    mntmLoglevel.getItems().add(mnItemLogSEVERE);
    mntmLoglevel.getItems().add(mnItemLogWARNING);
    mntmLoglevel.getItems().add(mnItemLogINFO);
    mntmLoglevel.getItems().add(mnItemLogCONFIG);
    mntmLoglevel.getItems().add(mnItemLogFINE);
    mntmLoglevel.getItems().add(mnItemLogFINER);
    mntmLoglevel.getItems().add(mnItemLogFINEST);
    mntmLoglevel.getItems().add(mnItemLogALL);

    mnSettings.getItems().add(mntmLoglevel);

    mnItemLogOFF.setOnAction(e -> {
      m_Level = Level.OFF;
      System.out.println("OFF Selected");
    });
    mnItemLogSEVERE.setOnAction(e -> {
      m_Level = Level.SEVERE;
      System.out.println("SEVERE Selected");
    });
    mnItemLogWARNING.setOnAction(e -> {
      m_Level = Level.WARNING;
      System.out.println("WARNING Selected");
    });
    mnItemLogINFO.setOnAction(e -> {
      m_Level = Level.INFO;
      System.out.println("INFO Selected");
    });
    mnItemLogCONFIG.setOnAction(e -> {
      m_Level = Level.CONFIG;
      System.out.println("CONFIG Selected");
    });
    mnItemLogFINE.setOnAction(e -> {
      m_Level = Level.FINE;
      System.out.println("FINE Selected");
    });
    mnItemLogFINER.setOnAction(e -> {
      m_Level = Level.FINER;
      System.out.println("FINER Selected");
    });
    mnItemLogFINEST.setOnAction(e -> {
      m_Level = Level.FINEST;
      System.out.println("FINEST Selected");
    });
    mnItemLogALL.setOnAction(e -> {
      m_Level = Level.ALL;
      System.out.println("ALL Selected");
    });

    // Look and Feel

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

    // Stored preferences

    // Menu "?"
    Menu questMenu = new Menu("?");

    // About

    // Help

    menuBar.getMenus().addAll(mnSettings, questMenu);

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

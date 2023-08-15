package zandbak_reform;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import kwee.library.AboutWindow;
import kwee.library.ShowPreferences;
import kwee.logger.MyLogger;

public class MenuBarExample extends Application {
  private static final Logger lOGGER = MyLogger.getLogger();

  @Override
  public void start(Stage primaryStage) {
    MenuBar menuBar = new MenuBar();

    Menu fileMenu = new Menu("File");
    MenuItem openMenuItem = new MenuItem("Open");
    MenuItem saveMenuItem = new MenuItem("Save");
    fileMenu.getItems().addAll(openMenuItem, saveMenuItem);

    Menu editMenu = new Menu("Edit");
    MenuItem cutMenuItem = new MenuItem("Cut");
    MenuItem copyMenuItem = new MenuItem("Copy");
    MenuItem pasteMenuItem = new MenuItem("Paste");
    editMenu.getItems().addAll(cutMenuItem, copyMenuItem, pasteMenuItem);
    /*
 * @formatter:off

    // Define Setting menu in menubalk:
    JMenu mnSettings = new JMenu("Settings");
    mnSettings.setEnabled(true);
    menuBar.add(mnSettings);

    // Option log level
    JCheckBoxMenuItem mntmLoglevel = new JCheckBoxMenuItem("Loglevel");
    mntmLoglevel.setHorizontalAlignment(SwingConstants.LEFT);
    mntmLoglevel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("Loglevel");
        String level = "";
        level = (String) JOptionPane.showInputDialog(frame, "Loglevel?", "INFO", JOptionPane.QUESTION_MESSAGE, null,
            c_levels, m_Level.toString());
        if (level != null) {
          m_Level = Level.parse(level.toUpperCase());
          m_param.set_Level(m_Level);
          MyLogger.changeLogLevel(m_Level);
        }
      }
    });
    mnSettings.add(mntmLoglevel);

    // Add item Look and Feel
    JMenu menu = new JMenu("Look and Feel");
    menu.setName("LookAndFeel");
    menu.setHorizontalAlignment(SwingConstants.LEFT);
    mnSettings.add(menu);

    // Get all the available look and feel that we are going to use for
    // creating the JMenuItem and assign the action listener to handle
    // the selection of menu item to change the look and feel.
    UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
    for (UIManager.LookAndFeelInfo lookAndFeelInfo : lookAndFeels) {
      JMenuItem item = new JMenuItem(lookAndFeelInfo.getName());
      item.addActionListener(event -> {
        try {
          // Set the look and feel for the frame and update the UI
          // to use a new selected look and feel.
          UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
          SwingUtilities.updateComponentTreeUI(this);
          m_param.set_LookAndFeel(lookAndFeelInfo.getClassName());
        } catch (Exception e) {
          LOGGER.log(Level.WARNING, e.getMessage());
        }
      });
      menu.add(item);
    }

    // Option Logging to Disk
    JCheckBoxMenuItem mntmLogToDisk = new JCheckBoxMenuItem("Create logfiles");
    mntmLogToDisk.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = mntmLogToDisk.isSelected();
        if (selected) {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          fileChooser.setSelectedFile(new File(m_LogDir));
          int option = fileChooser.showOpenDialog(GUILayout.this);
          if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            LOGGER.log(Level.INFO, "Log folder: " + file.getAbsolutePath());
            m_LogDir = file.getAbsolutePath() + "/";
            m_param.set_LogDir(m_LogDir);
            m_param.set_toDisk(true);
            m_toDisk = selected;
          }
        } else {
          m_param.set_toDisk(false);
          m_toDisk = selected;
        }
        try {
          MyLogger.setup(m_Level, m_LogDir, m_toDisk);
        } catch (IOException es) {
          LOGGER.log(Level.SEVERE, Class.class.getName() + ": " + es.toString());
          es.printStackTrace();
        }
      }
    });
    mnSettings.add(mntmLogToDisk);

    // Enable for Help
    JCheckBoxMenuItem chckbxmntm4Help = new JCheckBoxMenuItem("Enable all fields");
    chckbxmntm4Help.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxmntm4Help.isSelected();
        m_4Help = selected;
        if (m_4Help) {
          lblCSVFile.setEnabled(true);
          btnReadTransactions.setEnabled(true);
          txtOutputFilename.setEnabled(true);
        } else {
          lblCSVFile.setEnabled(false);
          btnReadTransactions.setEnabled(false);
          txtOutputFilename.setEnabled(false);
        }
        LOGGER.log(Level.INFO, "All fields enabled: " + Boolean.toString(selected));
      }
    });
    mnSettings.add(chckbxmntm4Help);

    // Option Preferences
    JMenuItem mntmPreferences = new JMenuItem("Preferences");
    mntmPreferences.setName("Preferences");
    mntmPreferences.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ShowPreferences showpref = new ShowPreferences(UserSetting.NodePrefName);
        showpref.showAllPreferences();
      }
    });
    mnSettings.add(mntmPreferences);

    // ? item
    JMenu mnHelpAbout = new JMenu("?");
    mnHelpAbout.setHorizontalAlignment(SwingConstants.RIGHT);
    menuBar.add(mnHelpAbout);

    // Help
    JMenuItem mntmHelp = new JMenuItem("Help");
    mntmHelp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File helpFile = new File(m_HelpFile);

        if (helpFile.exists()) {
          try {
            // Open the help file with the default viewer
            Desktop.getDesktop().open(helpFile);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        } else {
          LOGGER.log(Level.INFO, "Help file not found " + helpFile.getAbsolutePath());
        }
      }
    });
    mnHelpAbout.add(mntmHelp);

    // About
    JMenuItem mntmAbout = new JMenuItem("About");
    mntmAbout.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AboutWindow l_window = new AboutWindow(c_repoName, Main.m_creationtime, c_CopyrightYear);
        l_window.setVisible(true);
      }
    });
    mnHelpAbout.add(mntmAbout);
       @formatter:on
    */

    menuBar.getMenus().addAll(fileMenu, editMenu);

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

package kwee.gnucashcharts.main;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import kwee.library.FX.ShowPreferences;

/**
 * User setting persistence.
 * 
 * @author rshkw
 *
 */
public class UserSetting extends ShowPreferences {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  public static String NodePrefName = "kwee.gnucashcharts";

  private String c_Level = "Level";
  private String c_LevelValue = "INFO";

  private String c_ConfirmOnExit = "ConfirmOnExit";
  private String c_toDisk = "ToDisk";
  private String c_LogDir = "LogDir";
  private String c_LookAndFeel = "LookAndFeel";
  private String c_LookAndFeelVal = "Nimbus";
  private String c_InputFile = "InputFile";
  private String c_Pdf_File = "PdfFile";
  private String c_Tag = "Tag";
  private String c_NrBars = "NrBars";
  private String c_Language = "Language";

  private String m_Level = c_LevelValue;
  private String m_LookAndFeel;

  private String m_OutputFolder = "";
  private File[] m_CsvFiles = null;
  private String m_LogDir = "";

  private boolean m_ConfirmOnExit = false;
  private boolean m_toDisk = false;

  private String m_Tag = "";
  private int m_NrBars = 12;
  private String m_InputFile = "";
  private String m_PdfFile = "";
  private String m_Language = "nl";

  private Preferences pref;
  private Preferences userPrefs = Preferences.userRoot();

  /**
   * Constructor Initialize settings
   */
  public UserSetting() {
    super(NodePrefName);
    // Navigate to the preference node that stores the user setting
    pref = userPrefs.node(NodePrefName);

    m_toDisk = pref.getBoolean(c_toDisk, false);
    m_ConfirmOnExit = pref.getBoolean(c_ConfirmOnExit, false);
    m_LookAndFeel = pref.get(c_LookAndFeel, c_LookAndFeelVal);

    m_InputFile = pref.get(c_InputFile, "");
    m_Tag = pref.get(c_Tag, "");
    m_PdfFile = pref.get(c_Pdf_File, "");
    m_NrBars = pref.getInt(c_NrBars, 12);
    m_Language = pref.get(c_Language, "nl");

    m_Level = pref.get(c_Level, c_LevelValue);
    m_LogDir = pref.get(c_LogDir, "");
  }

  public UserSetting(String nodePrefName2) {

  }

  public int get_NrBars() {
    return m_NrBars;
  }

  public void set_NrBars(int m_NrBars) {
    this.m_NrBars = m_NrBars;
  }

  public String get_LogDir() {
    return this.m_LogDir;
  }

  public void set_LogDir(String m_LogDir) {
    this.m_LogDir = m_LogDir;
  }

  public String get_OutputFolder() {
    return m_OutputFolder;
  }

  public File[] get_CsvFiles() {
    return this.m_CsvFiles;
  }

  public String get_Language() {
    return m_Language;
  }

  public Level get_Level() {
    return Level.parse(this.m_Level);
  }

  public String get_LookAndFeel() {
    return this.m_LookAndFeel;
  }

  public String get_PdfFile() {
    return this.m_PdfFile;
  }

  public String get_Tag() {
    return this.m_Tag;
  }

  public String get_InputFile() {
    return this.m_InputFile;
  }

  public boolean is_toDisk() {
    return this.m_toDisk;
  }

  public Preferences getPreferences() {
    return this.pref;
  }

  public boolean is_ConfirmOnExit() {
    return this.m_ConfirmOnExit;
  }

  public void set_Pdf_file(File a_Pdf_file) {
    pref.put(c_Pdf_File, a_Pdf_file.getAbsolutePath());
    this.m_PdfFile = a_Pdf_file.getAbsolutePath();
  }

  public void set_Tag(String a_Tag) {
    pref.put(c_Tag, a_Tag);
    this.m_Tag = a_Tag;
  }

  public void set_toDisk(boolean a_toDisk) {
    pref.putBoolean(c_toDisk, a_toDisk);
    this.m_toDisk = a_toDisk;
  }

  public void set_Language(String m_Language) {
    this.m_Language = m_Language;
  }

  public void set_Level(Level a_Level) {
    pref.put(c_Level, a_Level.toString());
    this.m_Level = a_Level.toString();
  }

  public void set_LookAndFeel(String a_LookAndFeel) {
    pref.put(c_LookAndFeel, a_LookAndFeel);
    this.m_LookAndFeel = a_LookAndFeel;
  }

  public void set_ConfirmOnExit(boolean a_ConfirmOnExit) {
    pref.putBoolean(c_ConfirmOnExit, a_ConfirmOnExit);
    this.m_ConfirmOnExit = a_ConfirmOnExit;
  }

  public void set_InputFile(File a_InputFile) {
    pref.put(c_InputFile, a_InputFile.getAbsolutePath());
    this.m_InputFile = a_InputFile.getAbsolutePath();
  }

  /**
   * Save all settings
   */
  public void save() {
    try {
      pref.putBoolean(c_toDisk, m_toDisk);
      pref.putBoolean(c_ConfirmOnExit, m_ConfirmOnExit);

      pref.put(c_Level, m_Level);
      pref.put(c_LogDir, m_LogDir);

      pref.put(c_LookAndFeel, m_LookAndFeel);
      pref.put(c_Pdf_File, m_PdfFile);
      pref.put(c_Tag, m_Tag);
      pref.putInt(c_NrBars, m_NrBars);
      pref.put(c_InputFile, m_InputFile);

      pref.flush();
    } catch (BackingStoreException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }

  @SuppressWarnings("unused")
  public boolean validateValue(String a_Param, String a_Value) {
    boolean bstat = false;
    if ((a_Param.equals(c_toDisk) || a_Param.equals(c_ConfirmOnExit))) {
      if ((a_Value.equals("true")) || (a_Value.equals("false"))) {
        bstat = true;
      }
    } else if (a_Param.equals(c_Level)) {
      try {
        Level a_Level = Level.parse(a_Value);
        bstat = true;
      } catch (Exception e) {
        bstat = false;
      }
    } else if (a_Param.equals(c_NrBars)) {
      try {
        int bar = Integer.parseInt(a_Value);
        bstat = true;
      } catch (Exception e) {
        bstat = false;
      }
    } else {
      bstat = true;
    }
    return bstat;
  }

  public String print() {
    String l_line = "User setting \n";
    l_line = l_line + "Name: " + pref.name() + "\n";
    l_line = l_line + c_toDisk + ": " + m_toDisk + "\n";
    l_line = l_line + c_ConfirmOnExit + ": " + m_ConfirmOnExit + "\n";

    l_line = l_line + c_LookAndFeel + ": " + m_LookAndFeel + "\n";
    l_line = l_line + c_Pdf_File + ": " + m_PdfFile + "\n";
    l_line = l_line + c_InputFile + ": " + m_InputFile + "\n";
    l_line = l_line + c_Tag + ": " + m_Tag + "\n";
    l_line = l_line + c_NrBars + ": " + m_NrBars + "\n";

    l_line = l_line + c_Level + ": " + m_Level + "\n";
    l_line = l_line + c_LogDir + ": " + m_LogDir + "\n";

    return l_line;
  }
}

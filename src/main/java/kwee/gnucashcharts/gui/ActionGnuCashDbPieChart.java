package kwee.gnucashcharts.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.gnucashcharts.library.TaartPuntDataIf;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;
import kwee.gnucashcharts.library.gnuCashDb.TaartPuntData;

import kwee.logger.MyLogger;

public class ActionGnuCashDbPieChart {
  private static final Logger lOGGER = MyLogger.getLogger();
  private TaartPuntDataIf pieData;

  public ActionGnuCashDbPieChart(File a_SelectedFile) {
    lOGGER.log(Level.INFO, "Selected File: " + a_SelectedFile);
    MainMenu.m_param.set_InputFile(a_SelectedFile.getAbsoluteFile());
    MainMenu.m_param.save();
    // l_file.setText(a_SelectedFile.getAbsolutePath());
    // l_tag.setText("Kies een tag");

    ReadGnuCashDB htmltable = new ReadGnuCashDB(a_SelectedFile);
    ArrayList<String> regels = htmltable.getRegels();

    pieData = new TaartPuntData();
    pieData.putData(regels);
  }

  public TaartPuntDataIf getData() {
    return pieData;
  }
}

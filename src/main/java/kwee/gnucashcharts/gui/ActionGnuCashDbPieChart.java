package kwee.gnucashcharts.gui;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.gnucashcharts.library.GnuCashSingleton;
import kwee.gnucashcharts.library.TaartPuntData;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;
import kwee.gnucashcharts.library.gnuCashDb.TaartPuntDataImpl;

import kwee.logger.MyLogger;

public class ActionGnuCashDbPieChart {
  private static final Logger lOGGER = MyLogger.getLogger();
  private TaartPuntData pieData;
  private GnuCashSingleton bundle = GnuCashSingleton.getInstance();

  public ActionGnuCashDbPieChart(File a_SelectedFile, LocalDate a_Date) {
    lOGGER.log(Level.INFO, bundle.getMessage("SelectedSubject", a_SelectedFile.getAbsolutePath()));
    MainMenu.m_param.set_InputFile(a_SelectedFile.getAbsoluteFile());
    MainMenu.m_param.save();

    ReadGnuCashDB gnucashdbtable = new ReadGnuCashDB(a_SelectedFile, a_Date);
    ArrayList<String> regels = gnucashdbtable.getRegels();

    pieData = new TaartPuntDataImpl();
    pieData.putData(regels);
  }

  public TaartPuntData getData() {
    return pieData;
  }
}

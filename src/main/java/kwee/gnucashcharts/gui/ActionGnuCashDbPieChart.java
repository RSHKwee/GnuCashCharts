package kwee.gnucashcharts.gui;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.gnucashcharts.library.TaartPuntData;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;
import kwee.gnucashcharts.library.gnuCashDb.TaartPuntDataImpl;
import kwee.library.ApplicationMessages;
import kwee.logger.MyLogger;

public class ActionGnuCashDbPieChart {
  private static final Logger lOGGER = MyLogger.getLogger();
  private TaartPuntData pieData;
  private ApplicationMessages bundle = ApplicationMessages.getInstance();
  private ReadGnuCashDB m_gnucashdbtable;

  public ActionGnuCashDbPieChart(File a_SelectedFile) {
    lOGGER.log(Level.INFO, bundle.getMessage("SelectedFile", a_SelectedFile.getAbsolutePath()));
    MainMenu.m_param.set_InputFile(a_SelectedFile.getAbsoluteFile());
    MainMenu.m_param.save();

    m_gnucashdbtable = new ReadGnuCashDB(a_SelectedFile);
  }

  public TaartPuntData getData(LocalDate a_Date) {
    ArrayList<String> regels = m_gnucashdbtable.getRegels(a_Date);

    pieData = new TaartPuntDataImpl();
    pieData.putData(regels);
    return pieData;
  }
}

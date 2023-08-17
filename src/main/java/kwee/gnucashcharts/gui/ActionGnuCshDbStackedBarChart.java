package kwee.gnucashcharts.gui;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.gnucashcharts.library.TaartPuntData;
import kwee.gnucashcharts.library.html.TaartPuntDataImpl;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;
import kwee.gnucashcharts.library.gnuCashDb.SamengesteldeStaafData;
import kwee.logger.MyLogger;

public class ActionGnuCshDbStackedBarChart {
  private static final Logger lOGGER = MyLogger.getLogger();
  private TaartPuntData pieData;
  private SamengesteldeStaafData barData;

  public ActionGnuCshDbStackedBarChart(File a_SelectedFile) {
    lOGGER.log(Level.INFO, "Selected File: " + a_SelectedFile);
    MainMenu.m_param.set_InputFile(a_SelectedFile.getAbsoluteFile());
    MainMenu.m_param.save();

    ReadGnuCashDB gnucashdbtable = new ReadGnuCashDB(a_SelectedFile);
    ArrayList<String> regels = gnucashdbtable.getRegels();

    pieData = new TaartPuntDataImpl();
    pieData.putData(regels);

    LocalDate l_Now = LocalDate.now();

    pieData = new TaartPuntDataImpl();
    pieData.putData(regels);

    int year = l_Now.getYear();
    int month = l_Now.getMonthValue();
    int day = 1;

    LocalDate l_Date = LocalDate.of(year, month, day);

    barData.AddTaartPuntData(l_Date, pieData);

  }

  public TaartPuntData getData() {
    return pieData;
  }

}

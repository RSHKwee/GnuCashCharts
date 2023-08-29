package kwee.gnucashcharts.gui;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.gnucashcharts.library.GnuCashSingleton;
import kwee.gnucashcharts.library.TaartPuntData;
import kwee.gnucashcharts.library.gnuCashDb.TaartPuntDataImpl;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;
import kwee.gnucashcharts.library.gnuCashDb.SamengesteldeStaafData;

import kwee.logger.MyLogger;

public class ActionGnuCshDbStackedBarChart {
  private static final Logger lOGGER = MyLogger.getLogger();
  private int m_NrBars = 6;
  private SamengesteldeStaafData barData = new SamengesteldeStaafData();
  private File m_SelectedFile;
  private GnuCashSingleton bundle = GnuCashSingleton.getInstance();

  /**
   * 
   * @param a_SelectedFile GnuCash file
   * @param a_nrBars       Number of Bars, the bars are a month apart.
   * @param a_Date         Enddate period
   */
  public ActionGnuCshDbStackedBarChart(File a_SelectedFile, int a_nrBars, LocalDate a_Date) {
    lOGGER.log(Level.INFO,
        bundle.getMessage("BarChartSelections", a_SelectedFile.getAbsolutePath(), Integer.toString(a_nrBars)));
    try {
      m_NrBars = a_nrBars;
      m_SelectedFile = a_SelectedFile;
      MainMenu.m_param.set_InputFile(a_SelectedFile.getAbsoluteFile());
      MainMenu.m_param.save();

      addData(a_Date);
      int year = a_Date.getYear();
      int month = a_Date.getMonthValue();
      int day = 1;

      for (int i = 0; i < m_NrBars; i++) {
        LocalDate l_Date;
        int[] l_result1 = decrMonth(year, month);
        year = l_result1[0];
        month = l_result1[1];
        l_Date = LocalDate.of(year, month, day);
        addData(l_Date);
      }
    } catch (Exception e) {
      lOGGER.log(Level.INFO, e.getMessage());
    }
  }

  public SamengesteldeStaafData getData() {
    return barData;
  }

  // Local Functions
  private void addData(LocalDate a_Date) {
    ReadGnuCashDB gnucashdbtable = new ReadGnuCashDB(m_SelectedFile, a_Date);
    ArrayList<String> regels = gnucashdbtable.getRegels();
    TaartPuntData pieData = new TaartPuntDataImpl();
    pieData.putData(regels);

    barData.AddTaartPuntData(a_Date, pieData);
  }

  private int[] decrMonth(int a_year, int a_month) {
    int l_month = a_month - 1;
    int l_year = a_year;
    if (l_month <= 0) {
      l_month = 12;
      l_year = l_year - 1;
    }
    int[] l_result = { l_year, l_month };
    return l_result;
  }
}

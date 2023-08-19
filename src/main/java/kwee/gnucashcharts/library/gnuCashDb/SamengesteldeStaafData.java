package kwee.gnucashcharts.library.gnuCashDb;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import kwee.gnucashcharts.library.TaartPuntData;

public class SamengesteldeStaafData {
  Map<LocalDate, TaartPuntData> m_Kolommen = new TreeMap<LocalDate, TaartPuntData>();

  public SamengesteldeStaafData() {
  }

  public void AddTaartPuntData(LocalDate a_Date, TaartPuntData a_TaartPuntData) {
    if (m_Kolommen.get(a_Date) == null) {
      m_Kolommen.put(a_Date, a_TaartPuntData);
    } else {
      // TODO mergen TaartPuntData....
    }
  }

  public Map<LocalDate, TaartPuntData> getKolommenData() {
    return m_Kolommen;
  }
}

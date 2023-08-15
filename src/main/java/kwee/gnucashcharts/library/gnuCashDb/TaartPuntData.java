package kwee.gnucashcharts.library.gnuCashDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import kwee.gnucashcharts.library.FormatAmount;
import kwee.gnucashcharts.library.TaartPuntDataIf;

public class TaartPuntData implements TaartPuntDataIf {
  /*
   * @formatter:on
   *  0 String m_AccountNr = ""; account.getName()
   *  1 String m_AccountName = ""; account.getDescription()
   *  2 double m_Amount = 0.0; account.getBalanceFormated()
   *  3 double m_Saldo = 0.0;
   *  4 String m_Remark = ""; account.getUserDefinedAttribute("notes")    
   * @formatter:off
   */ 
  // m_Tags : tag, SliceName, SliceAmount
  private Map<String, SortedMap<String, Double>> m_Tags = new TreeMap<String, SortedMap<String, Double>>();

  public TaartPuntData() {
  }

  @Override
  public void putData(ArrayList<String> a_Regels) {
    a_Regels.forEach(regel -> {
      String[] l_Selems = regel.split(";");
      if (l_Selems.length > 4) {
        if (!l_Selems[4].isBlank()) {
          String[] elems = l_Selems[4].split(",");
          for (int i = 0; i < elems.length; i++) {
            String[] tags = elems[i].split(":");
            String tag = tags[0].strip();
            if (tags.length >= 2) {
              String slice = tags[1].strip();
              SortedMap<String, Double> slices = m_Tags.get(tag);
              if (slices == null) {
                slices = new TreeMap<String, Double>();
                double lamt = FormatAmount.convertToDouble(l_Selems[2]);
                slices.put(slice, lamt);
                m_Tags.put(tag, slices);
              } else {
                double amt = 0.0;
                try {
                  amt = slices.get(slice);
                } catch (Exception e) {
                  // Do nothing...
                }
                double lamt = FormatAmount.convertToDouble(l_Selems[2]);             
                amt = amt + lamt;
                slices.put(slice, amt);
                m_Tags.put(tag, slices);
              }
            }
          }
        }
      }
    }); 
    
    // TODO Auto-generated method stub    
  }

  @Override
  public Set<String> getTags() {
    return m_Tags.keySet();
  }

  @Override
  public Map<String, Double> getPieSlices(String a_Tag) {
    Map<String, Double> l_Slices = m_Tags.get(a_Tag);
    if (l_Slices == null) {
      l_Slices = new HashMap<String, Double>();
    }
    return l_Slices;
  }

}
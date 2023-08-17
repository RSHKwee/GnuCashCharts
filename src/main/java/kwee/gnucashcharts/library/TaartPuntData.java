package kwee.gnucashcharts.library;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface TaartPuntData {

  public void putData(ArrayList<String> a_Regels);

  public Set<String> getTags();

  public Map<String, Double> getPieSlices(String a_Tag);

}

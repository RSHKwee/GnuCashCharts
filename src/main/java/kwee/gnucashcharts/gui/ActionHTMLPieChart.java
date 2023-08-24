package kwee.gnucashcharts.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.gnucashcharts.library.TaartPuntData;
import kwee.gnucashcharts.library.html.ReadHTMLTable;
import kwee.gnucashcharts.library.html.TaartPuntDataImpl;
import kwee.logger.MyLogger;

public class ActionHTMLPieChart {
  private static final Logger lOGGER = MyLogger.getLogger();
  private TaartPuntData pieData;

  public ActionHTMLPieChart(File a_SelectedFile) {
    lOGGER.log(Level.INFO, "Selected File: " + a_SelectedFile);
    MainMenu.m_param.set_InputFile(a_SelectedFile.getAbsoluteFile());
    MainMenu.m_param.save();
    // l_file.setText(a_SelectedFile.getAbsolutePath());
    // l_tag.setText("Kies een tag");

    ReadHTMLTable htmltable = new ReadHTMLTable(a_SelectedFile.getAbsolutePath());
    ArrayList<String> regels = htmltable.parseHTMLpage();

    pieData = new TaartPuntDataImpl();
    pieData.putData(regels);
  }

  public TaartPuntData getData() {
    return pieData;
  }

}

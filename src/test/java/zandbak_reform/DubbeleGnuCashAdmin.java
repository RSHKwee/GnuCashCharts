package zandbak_reform;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class DubbeleGnuCashAdmin {

  public static void main(String[] args) {
    File selectedFile = new File("G:\\Users\\René\\SynologyDrive\\Administraties\\Huishouding.gnucash");
    File selectedFile1 = new File("G:\\Users\\René\\SynologyDrive\\Administraties\\Prive_RK.gnucash");
    ReadGnuCashDB gnucashdbtable = new ReadGnuCashDB(selectedFile);
    ReadGnuCashDB gnucashdbtable1 = new ReadGnuCashDB(selectedFile1);
    LocalDate l_date = LocalDate.now();
    ArrayList<String> regels = gnucashdbtable.getRegels(l_date);
    ArrayList<String> regels1 = gnucashdbtable1.getRegels(l_date);
    regels.addAll(regels1);

    System.out.println();
  }

}

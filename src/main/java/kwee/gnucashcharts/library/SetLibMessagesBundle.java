package kwee.gnucashcharts.library;

import org.gnucash.messages.ApplicationMessages;

/**
 * Change Language for GnuCashApi
 */
public class SetLibMessagesBundle {
  public SetLibMessagesBundle() {
  }

  public SetLibMessagesBundle(String a_Code) {
    changeLanguage(a_Code);
  }

  public void changeLanguage(String a_Code) {
    ApplicationMessages bundle = ApplicationMessages.getInstance();
    bundle = ApplicationMessages.getInstance();
    bundle.changeLanguage(a_Code);
  }
}

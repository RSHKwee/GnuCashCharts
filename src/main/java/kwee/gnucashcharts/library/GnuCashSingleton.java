package kwee.gnucashcharts.library;

import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.time.LocalDate;

public class GnuCashSingleton {
  ResourceBundle bundle;

  // Private constructor to prevent instantiation from other classes
  private GnuCashSingleton() {
    Locale locale = new Locale("nl", "NL"); // Dutch locale for Euro format

    // Load the resource bundle from the "translations" subfolder
    bundle = ResourceBundle.getBundle("translations/messages", locale);
  }

  // Private static inner class that holds the Singleton instance
  private static class SingletonHelper {
    private static final GnuCashSingleton INSTANCE = new GnuCashSingleton();
  }

  // Public method to provide access to the Singleton instance
  public static GnuCashSingleton getInstance() {
    return SingletonHelper.INSTANCE;
  }

  // Other methods and fields can be added as needed
  public void changeLanguage(String languageCode) {
    Locale newLocale = new Locale(languageCode);
    setLocale(newLocale);
  }

  public String getMessage(String a_MsgId) {
    String messageTemplate = bundle.getString(a_MsgId);
    return messageTemplate;
  }

  public String getMessage(String a_MsgId, String a_arg1) {
    // Retrieve the message from the bundle
    String messageTemplate = bundle.getString(a_MsgId);
    String formattedMessage = MessageFormat.format(messageTemplate, a_arg1);
    return formattedMessage;
  }

  public String getMessage(String a_MsgId, LocalDate a_arg1) {
    // Retrieve the message from the bundle
    String messageTemplate = bundle.getString(a_MsgId);
    String formattedMessage = MessageFormat.format(messageTemplate, a_arg1);
    return formattedMessage;
  }

  public String getMessage(String a_MsgId, int a_arg1) {
    // Retrieve the message from the bundle
    String messageTemplate = bundle.getString(a_MsgId);
    String formattedMessage = MessageFormat.format(messageTemplate, a_arg1);
    return formattedMessage;
  }

  public String getMessage(String a_MsgId, String a_arg1, String a_arg2) {
    // Retrieve the message from the bundle
    String messageTemplate = bundle.getString(a_MsgId);
    String formattedMessage = MessageFormat.format(messageTemplate, a_arg1, a_arg2);
    return formattedMessage;
  }

  public String getMessage(String a_MsgId, String a_arg1, String a_arg2, String a_arg3) {
    // Retrieve the message from the bundle
    String messageTemplate = bundle.getString(a_MsgId);
    String formattedMessage = MessageFormat.format(messageTemplate, a_arg1, a_arg2, a_arg3);
    return formattedMessage;
  }

  // Privates
  private void setLocale(Locale locale) {
    bundle = ResourceBundle.getBundle("translations/messages", locale);
    Locale.setDefault(locale);
  }

}

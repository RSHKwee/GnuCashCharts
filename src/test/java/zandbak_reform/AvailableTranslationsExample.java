package zandbak_reform;

import java.util.Locale;
import java.util.ResourceBundle;

public class AvailableTranslationsExample {
  public static void main(String[] args) {
    // Define the base name of your resource bundle
    String baseName = "translations/messages";

    // Get an array of available locales
    Locale[] availableLocales = Locale.getAvailableLocales();

    // Iterate through available locales and check for resource bundles
    for (Locale locale : availableLocales) {
      ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
      if (bundle.getLocale().equals(locale)) {
        System.out.println("Translation available for locale: " + locale);
      }
    }
  }
}

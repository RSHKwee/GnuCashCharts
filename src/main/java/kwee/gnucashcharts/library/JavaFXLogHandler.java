package kwee.gnucashcharts.library;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class JavaFXLogHandler extends Handler {
  private TextArea logTextArea;

  public JavaFXLogHandler(TextArea logTextArea) {
    this.logTextArea = logTextArea;
  }

  @Override
  public void publish(LogRecord record) {
    if (record != null) {
      Platform.runLater(() -> {
        StringWriter text = new StringWriter();
        PrintWriter out = new PrintWriter(text);
        if (!record.getMessage().contentEquals(" ") && !record.getMessage().isEmpty()) {
          out.println(logTextArea.getText());
          out.printf(" [%s] %s", record.getLevel(), record.getMessage());
        } else {
          out.println(logTextArea.getText());
          out.printf("  %s", record.getMessage());
        }
        logTextArea.setText(text.toString());
      });
    }
  }

  @Override
  public void flush() {
  }

  @Override
  public void close() throws SecurityException {
  }
}

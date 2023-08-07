package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.logging.*;

public class CombinedWindowExample extends Application {

  private TextArea logTextArea = new TextArea();

  @Override
  public void start(Stage primaryStage) {
    // Set up logger to redirect output to TextArea
    setupLogger();

    // Create the main UI layout
    GridPane layout = new GridPane();
    layout.setHgap(10);
    layout.setVgap(10);

    // Add UI elements to the layout
    Button openLogButton = new Button("Open Log");
    openLogButton.setOnAction(e -> openLogWindow());

    layout.add(openLogButton, 0, 0);

    // Add log TextArea
    layout.add(logTextArea, 0, 1);

    Scene scene = new Scene(layout, 400, 300);

    primaryStage.setTitle("Combined Window Example");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void openLogWindow() {
    // Create a separate log window
    Stage logStage = new Stage();

    // Set up the log window's layout
    GridPane logLayout = new GridPane();
    logLayout.add(logTextArea, 0, 0);

    Scene logScene = new Scene(logLayout, 400, 300);

    logStage.setTitle("Log Window");
    logStage.setScene(logScene);
    logStage.show();
  }

  private void setupLogger() {
    Logger logger = Logger.getLogger("");
    TextAreaHandler textAreaHandler = new TextAreaHandler(logTextArea);

    // Set a formatter for the handler
    SimpleFormatter formatter = new SimpleFormatter();
    textAreaHandler.setFormatter(formatter);

    logger.addHandler(textAreaHandler);
    logger.setLevel(Level.ALL);
  }

  public static void main(String[] args) {
    launch(args);
  }

  // Custom handler to redirect logs to TextArea
  private static class TextAreaHandler extends Handler {
    private final TextArea textArea;

    TextAreaHandler(TextArea textArea) {
      this.textArea = textArea;
    }

    @Override
    public void publish(LogRecord record) {
      String message = getFormatter().format(record);
      textArea.appendText(message + System.lineSeparator());
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
  }
}

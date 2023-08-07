package zandbak_reform;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;

public class JavaFXInAWTExample extends Application {

  @Override
  public void start(Stage primaryStage) {
    JFrame frame = new JFrame("JavaFX in AWT Example");

    JFXPanel fxPanel = new JFXPanel();

    Platform.runLater(() -> {
      Scene scene = createJavaFXScene();
      fxPanel.setScene(scene);
    });

    frame.getContentPane().add(fxPanel);
    frame.setSize(400, 300);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private Scene createJavaFXScene() {
    StackPane root = new StackPane();
    root.getChildren().add(new Label("Hello from JavaFX!"));

    return new Scene(root, 400, 300);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Application.launch(JavaFXInAWTExample.class, args);
    });
  }
}

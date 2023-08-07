package kwee.gnucashcharts.library;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.*;
import javafx.stage.FileChooser;

public class FileKiezer {
  private Scene m_scene;
  private File m_File;
  private VBox m_VBox;

  public FileKiezer(Stage stage, String a_LabelTekst) {
    // create a File chooser
    FileChooser fil_chooser = new FileChooser();

    // create a Label
    Label label = new Label("no files selected");

    // create a Button
    Button button = new Button(a_LabelTekst);

    // create an Event Handler
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // get the file selected
        m_File = fil_chooser.showOpenDialog(stage);
        if (m_File != null) {
          label.setText(m_File.getAbsolutePath() + "  selected");
        }
      }
    };
    button.setOnAction(event);

    // create a VBox
    m_VBox = new VBox(30, label, button);

    // set Alignment
    m_VBox.setAlignment(Pos.CENTER);

    // create a scene
    m_scene = new Scene(m_VBox, 800, 500);
  }

  public Scene getScene() {
    return m_scene;
  }

  public VBox getVbox() {
    return m_VBox;
  }

  public File getChoosenFile() {
    return m_File;
  }
}

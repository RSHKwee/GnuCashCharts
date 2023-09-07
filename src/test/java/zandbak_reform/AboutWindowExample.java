package zandbak_reform;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kwee.library.Githubchecker;

public class AboutWindowExample extends Application {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private String c_Owner = "rshkwee";
  private Label titleLabel;
  private TextArea updateTextArea;
  private Button downloadButton;
  private String repoName = "GnuCashCharts";
  private String creationtime = "12-01-2023";
  private String CopyYear = "2023";

  @Override
  public void start(Stage primaryStage) {
    // Create a new stage for the "About" window
    Stage aboutStage = new Stage();
    aboutStage.setTitle("About My App");

    // Create UI components for displaying information
    Label nameLabel = new Label("My App");
    Label versionLabel = new Label("Version 1.0");
    Label descriptionLabel = new Label("This is a JavaFX application.");

    downloadButton = new Button("Download");
    downloadButton.setDisable(true);
    downloadButton.setVisible(false);
    downloadButton.setOnAction(e -> {
      openDownloadLink(c_Owner, repoName);
    });

    updateTextArea = new TextArea();
    updateTextArea.setEditable(false);
    String latest = Githubchecker.getReleases(c_Owner, repoName);
    String l_message = " \t" + repoName + " version " + creationtime + "\n\n \tCopyright © " + CopyYear;

    if (Githubchecker.isUpdateAvailable(creationtime, latest)) {
      downloadButton.setDisable(false);
      downloadButton.setVisible(true);
      l_message = l_message + "\n\n" + "\t Version available: " + latest;
    } else if (latest.isEmpty()) {
      downloadButton.setDisable(false);
      downloadButton.setVisible(true);
      l_message = l_message + "\n\n" + "\t No versions available. ";
    }
    updateTextArea.setText(l_message);

    Button OKButton = new Button("OK");
    OKButton.setOnAction(e -> {
      aboutStage.close();
    });

    // Create a layout to organize the components
    HBox hbox = new HBox(downloadButton, OKButton);

    VBox vbox = new VBox(10);
    vbox.getChildren().addAll(nameLabel, versionLabel, descriptionLabel, hbox);

    // Create a scene and set it in the stage
    Scene scene = new Scene(vbox, 300, 200);
    aboutStage.setScene(scene);

    // Show the "About" window
    aboutStage.show();
  }

  private void openDownloadLink(String owner, String repoName) {
    try {
      URI uri = new URI("https://github.com/" + owner + "/" + repoName + "/releases");
      Desktop.getDesktop().browse(uri);
    } catch (IOException | URISyntaxException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

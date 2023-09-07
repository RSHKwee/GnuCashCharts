package kwee.gnucashcharts.library;

import java.awt.Desktop;
import java.awt.Label;
import java.awt.TextArea;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AboutWindow extends Application {

  private String c_Owner = "rshkwee";
  private Label titleLabel;
  private TextArea updateTextArea;
  private Button downloadButton;

  @Override
  public void start(Stage primaryStage) {
    // Create the About window stage
    Stage aboutStage = new Stage();
    aboutStage.setTitle("GitHub Update Window");

    // Create layout for the About window
    VBox aboutLayout = new VBox();
    aboutLayout.getStyleClass().add("about-popup"); // Apply CSS class if needed

    // Add content to the layout
    aboutLayout.getChildren().addAll(

        // Add labels, images, and other content here
        new Button("Close"));

    // Set up event handling for the close button
    Button closeButton = (Button) aboutLayout.getChildren().get(aboutLayout.getChildren().size() - 1);
    closeButton.setOnAction(event -> aboutStage.close());

    // Set the layout to the scene
    Scene aboutScene = new Scene(aboutLayout, 300, 200);

    // Link external CSS file if needed
    // aboutScene.getStylesheets().add("path/to/stylesheet.css");

    // Set the scene to the About window stage
    aboutStage.setScene(aboutScene);

    // Show the About window as a popup
    aboutStage.show();
  }

  private void openDownloadLink(String owner, String repoName) {
    try {
      URI uri = new URI("https://github.com/" + owner + "/" + repoName + "/releases");
      Desktop.getDesktop().browse(uri);
    } catch (URISyntaxException | IOException e) {
      // LOGGER.log(Level.INFO, e.getMessage());
      // e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

package zandbak_reform;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class FileChooserWithListViewApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    FileChooser fileChooser = new FileChooser();
    ObservableList<String> itemList = FXCollections.observableArrayList();
    ListView<String> listView = new ListView<>(itemList);

    Button openButton = new Button("Open File");
    openButton.setOnAction(e -> {
      File selectedFile = fileChooser.showOpenDialog(primaryStage);
      if (selectedFile != null) {

        itemList.add(selectedFile.getName());
      }
    });

    VBox layout = new VBox(openButton, listView);

    Scene scene = new Scene(layout, 400, 300);

    primaryStage.setScene(scene);
    primaryStage.setTitle("FileChooser and ItemList Example");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

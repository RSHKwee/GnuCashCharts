package zandbak_reform;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.util.List;

public class FileSelectorWithActions extends Application {

  private ObservableList<File> selectedFiles = FXCollections.observableArrayList();
  private ListView<File> fileListView;

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("File Selector with Actions");

    // Create UI components
    Button selectButton = new Button("Add Files");
    Button removeButton = new Button("Remove Selected");
    Button clearAllButton = new Button("Clear All");

    fileListView = new ListView<>(selectedFiles);
    fileListView.setCellFactory(param -> new FileListCell());

    // Button actions
    selectButton.setOnAction(e -> addFiles(primaryStage));
    removeButton.setOnAction(e -> removeSelectedFile());
    clearAllButton.setOnAction(e -> clearAllFiles());

    // Layout
    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(selectButton, removeButton, clearAllButton);

    VBox layout = new VBox(10);
    layout.setPadding(new Insets(15));
    layout.getChildren().addAll(buttonBox, fileListView);

    Scene scene = new Scene(layout, 600, 400);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void addFiles(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Files");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

    List<File> files = fileChooser.showOpenMultipleDialog(stage);
    if (files != null) {
      selectedFiles.addAll(files);
    }
  }

  private void removeSelectedFile() {
    File selected = fileListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      selectedFiles.remove(selected);
    }
  }

  private void clearAllFiles() {
    selectedFiles.clear();
  }

  // Custom cell to display file information
  private static class FileListCell extends ListCell<File> {
    @Override
    protected void updateItem(File file, boolean empty) {
      super.updateItem(file, empty);
      if (empty || file == null) {
        setText(null);
      } else {
        setText(file.getName() + " - " + (file.length() / 1024) + " KB - " + file.getParent());
      }
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
package zandbak_reform;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.io.File;
import java.util.List;

public class AdvancedFileSelector extends Application {
    
    private ListView<String> fileListView;
    private Label statusLabel;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Advanced File Selector");
        
        // Create UI components
        Button selectButton = new Button("Select Files");
        Button clearButton = new Button("Clear Selection");
        fileListView = new ListView<>();
        statusLabel = new Label("No files selected");
        
        // Set button actions
        selectButton.setOnAction(e -> openFileDialog(primaryStage));
        clearButton.setOnAction(e -> clearSelection());
        
        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(
            selectButton, 
            clearButton, 
            new Label("Selected Files:"),
            fileListView,
            statusLabel
        );
        
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void openFileDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");
        
        // Set initial directory (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        
        // Add file filters
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
            "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter(
            "Text Files", "*.txt", "*.doc", "*.docx", "*.pdf");
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter(
            "All Files", "*.*");
        
        fileChooser.getExtensionFilters().addAll(imageFilter, textFilter, allFilter);
        
        // Open multiple file dialog
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            updateFileList(selectedFiles);
            statusLabel.setText("Selected " + selectedFiles.size() + " file(s)");
        } else {
            statusLabel.setText("No files selected");
        }
    }
    
    private void updateFileList(List<File> files) {
        fileListView.getItems().clear();
        for (File file : files) {
            fileListView.getItems().add(
                file.getName() + " (" + file.getParent() + ")"
            );
        }
    }
    
    private void clearSelection() {
        fileListView.getItems().clear();
        statusLabel.setText("Selection cleared");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

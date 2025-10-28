package zandbak_reform;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;

public class MultipleFileSelectorWithMemory extends Application {
    
    private ListView<String> fileListView;
    private Preferences preferences;
    private static final String LAST_DIRECTORY_KEY = "last_directory";
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Multiple File Selector with Memory");
        
        // Initialize preferences
        preferences = Preferences.userNodeForPackage(MultipleFileSelectorWithMemory.class);
        
        Button selectButton = new Button("Select Files");
        fileListView = new ListView<>();
        
        selectButton.setOnAction(e -> openMultipleFiles(primaryStage));
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(selectButton, fileListView);
        
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void openMultipleFiles(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Multiple Files");
        
        // Set initial directory from preferences
        String lastDirectory = preferences.get(LAST_DIRECTORY_KEY, null);
        if (lastDirectory != null) {
            File dir = new File(lastDirectory);
            if (dir.exists() && dir.isDirectory()) {
                fileChooser.setInitialDirectory(dir);
            }
        }
        
        // Configure for multiple selection
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            // Save the directory of the first selected file
            File firstFile = selectedFiles.get(0);
            File directory = firstFile.getParentFile();
            if (directory != null && directory.exists()) {
                preferences.put(LAST_DIRECTORY_KEY, directory.getAbsolutePath());
            }
            
            // Update the file list
            fileListView.getItems().clear();
            for (File file : selectedFiles) {
                fileListView.getItems().add(file.getAbsolutePath());
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

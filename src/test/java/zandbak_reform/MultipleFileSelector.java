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

public class MultipleFileSelector extends Application {
    
    private ListView<String> fileListView;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Multiple File Selector");
        
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
        
        // Configure for multiple selection
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        
        if (selectedFiles != null) {
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
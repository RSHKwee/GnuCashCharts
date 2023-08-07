package zandbak_reform;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kwee.gnucashcharts.library.html.ReadHTMLTable;
import kwee.gnucashcharts.library.html.TaartPuntData;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class FileChooserWithComboBoxApp extends Application {
  @Override
  public void start(Stage primaryStage) {
    FileChooser fileChooser = new FileChooser();
    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));

    Button openFileButton = new Button("Open File");
    openFileButton.setOnAction(e -> {
      File selectedFile = fileChooser.showOpenDialog(primaryStage);
      if (selectedFile != null) {
        System.out.println("Selected File: " + selectedFile.getAbsolutePath());
        ReadHTMLTable htmltable = new ReadHTMLTable(selectedFile.getAbsolutePath());
        ArrayList<String> regels = htmltable.parseHTMLpage();
        TaartPuntData pieData = new TaartPuntData(regels);
        Set<String> tags = pieData.getTags();

        // Convert the Set<String> to ObservableList<String>
        ObservableList<String> observableList;
        observableList = FXCollections.observableArrayList(tags);
        comboBox.setItems(observableList);
        // Do something with the selected file
      }
    });

    Button selectOptionButton = new Button("Select Tag");
    selectOptionButton.setOnAction(e -> {
      String selectedOption = comboBox.getValue();
      if (selectedOption != null) {
        System.out.println("Selected Option: " + selectedOption);
        // Do something with the selected option
      }
    });

    HBox openFileLayout = new HBox(openFileButton);
    HBox selectOptionLayout = new HBox(comboBox, selectOptionButton);
    VBox layout = new VBox(openFileLayout, selectOptionLayout);

    Scene scene = new Scene(layout, 400, 300);

    primaryStage.setScene(scene);
    primaryStage.setTitle("FileChooser and ComboBox Example");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

package kwee.gnucashcharts.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kwee.gnucashcharts.main.UserSetting;
import kwee.gnucashcharts.library.html.ReadHTMLTable;
import kwee.gnucashcharts.library.html.TaartPuntData;

public class MainMenu extends Application {
  String inpfile = "";
  String tag = "";
  public static UserSetting m_param = new UserSetting();

  @Override
  public void start(Stage primaryStage) {
    FileChooser fileChooser = new FileChooser();
    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));

    Button openFileButton = new Button("Open File");
    Label l_file = new Label("File");
    openFileButton.setOnAction(e -> {
      if (!m_param.get_InputFile().isBlank()) {
        File intFile = new File(m_param.get_InputFile());
        String ldir = intFile.getParent();
        fileChooser.setInitialDirectory(new File(ldir));
      }
      File selectedFile = fileChooser.showOpenDialog(primaryStage);
      if (selectedFile != null) {
        System.out.println("Selected File: " + selectedFile.getAbsolutePath());
        inpfile = selectedFile.getAbsolutePath();
        l_file.setText(selectedFile.getAbsolutePath());
        m_param.set_InputFile(selectedFile.getAbsoluteFile());
        m_param.save();

        ReadHTMLTable htmltable = new ReadHTMLTable(selectedFile.getAbsolutePath());
        ArrayList<String> regels = htmltable.parseHTMLpage();
        TaartPuntData pieData = new TaartPuntData(regels);
        Set<String> tags = pieData.getTags();

        // Convert the Set<String> to ObservableList<String>
        ObservableList<String> observableList;
        observableList = FXCollections.observableArrayList(tags);
        comboBox.setItems(observableList);
      }
    });

    Button selectOptionButton = new Button("Select Tag");
    Label l_tag = new Label("Tag");
    comboBox.setValue(m_param.get_Tag());
    selectOptionButton.setOnAction(e -> {
      String selectedOption = comboBox.getValue();
      if (selectedOption != null) {
        System.out.println("Selected Option: " + selectedOption);
        tag = selectedOption;
        l_tag.setText(tag);
        m_param.set_Tag(tag);
        m_param.save();
      }
    });

    PieChartWithLegend piwindow = new PieChartWithLegend();
    Button buttonPiechart = new Button("Open Piechart");
    buttonPiechart.setOnAction(e -> piwindow.openPieChartWindow(inpfile, tag));

    HBox openFileLayout = new HBox(openFileButton, l_file);
    HBox selectOptionLayout = new HBox(comboBox, selectOptionButton, l_tag);
    HBox buttonPiechartLayout = new HBox(buttonPiechart);
    VBox layout = new VBox(openFileLayout, selectOptionLayout, buttonPiechartLayout);

    Scene scene = new Scene(layout, 400, 300);
    primaryStage.setScene(scene);
    primaryStage.setTitle("GnuCash charts");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

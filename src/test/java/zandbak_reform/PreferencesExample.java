package zandbak_reform;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesExample extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Edit Preferences");

    // Create a Preferences object for your application
    Preferences preferences = Preferences.userRoot().node("kwee.gnucashcharts");

    // Create a TableView to display the preferences
    TableView<PreferenceEntry> tableView = new TableView<>();
    TableColumn<PreferenceEntry, String> keyColumn = new TableColumn<>("Key");
    keyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());

    TableColumn<PreferenceEntry, String> valueColumn = new TableColumn<>("Value");
    valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
    valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    valueColumn.setOnEditCommit(event -> {
      PreferenceEntry entry = event.getRowValue();
      entry.setValue(event.getNewValue());
      preferences.put(entry.getKey(), event.getNewValue());
      try {
        preferences.flush();
      } catch (BackingStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });

    tableView.getColumns().addAll(keyColumn, valueColumn);
    try {
      tableView.setItems(getPreferenceEntries(preferences));
    } catch (BackingStoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // Enable editing in the TableView
    tableView.setEditable(true);

    VBox root = new VBox(10);
    root.getChildren().add(tableView);

    Scene scene = new Scene(root, 400, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  // Helper method to retrieve all preferences and store them in an ObservableList
  private ObservableList<PreferenceEntry> getPreferenceEntries(Preferences preferences) throws BackingStoreException {
    ObservableList<PreferenceEntry> entries = FXCollections.observableArrayList();
    String[] keys = preferences.keys();
    for (String key : keys) {
      String value = preferences.get(key, "");
      entries.add(new PreferenceEntry(key, value));
    }
    return entries;
  }
}

class PreferenceEntry {
  private final SimpleStringProperty key;
  private final SimpleStringProperty value;

  public PreferenceEntry(String key, String value) {
    this.key = new SimpleStringProperty(key);
    this.value = new SimpleStringProperty(value);
  }

  public String getKey() {
    return key.get();
  }

  public SimpleStringProperty keyProperty() {
    return key;
  }

  public String getValue() {
    return value.get();
  }

  public void setValue(String newValue) {
    value.set(newValue);
  }

  public SimpleStringProperty valueProperty() {
    return value;
  }
}

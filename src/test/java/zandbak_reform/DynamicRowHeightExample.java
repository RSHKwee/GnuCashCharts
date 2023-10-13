package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

public class DynamicRowHeightExample extends Application {
  @Override
  public void start(Stage stage) {
    TableView<Person> tableView = new TableView<>();

    TableColumn<Person, String> descriptionColumn = new TableColumn<>("Description");
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    descriptionColumn.setCellFactory(param -> new WrappableCell<>());

    tableView.getColumns().add(descriptionColumn);

    tableView.getItems().addAll(new Person("This is a long description that should wrap within the column width."),
        new Person("Short description."), new Person("Another lengthy description that should wrap."));

    Scene scene = new Scene(tableView, 400, 200);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  public static class Person {
    private final SimpleStringProperty description;

    public Person(String description) {
      this.description = new SimpleStringProperty(description);
    }

    public String getDescription() {
      return description.get();
    }

    public void setDescription(String description) {
      this.description.set(description);
    }
  }

  public static class WrappableCell<T> extends TableCell<Person, String> {
    private final TextFlow textFlow = new TextFlow();

    {
      setGraphic(textFlow);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      if (empty || item == null) {
        textFlow.getChildren().clear();
      } else {
        Text text = new Text(item);
        text.setWrappingWidth(getTableColumn().getWidth());
        textFlow.getChildren().setAll(text);
      }
    }

    @Override
    public void updateIndex(int index) {
      super.updateIndex(index);
      if (isEmpty()) {
        textFlow.setMinHeight(Region.USE_PREF_SIZE);
      } else {
        textFlow.setMinHeight(Region.USE_COMPUTED_SIZE);
      }
    }
  }
}

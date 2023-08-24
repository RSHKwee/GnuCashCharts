package kwee.gnucashcharts.library.barchart;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TableCell {
  private final DoubleProperty value;

  public TableCell(double value) {
    this.value = new SimpleDoubleProperty(value);
  }

  public double getValue() {
    return value.get();
  }

  public DoubleProperty valueProperty() {
    return value;
  }

  public static Callback<TableColumn.CellDataFeatures<TableCell[], Double>, ObservableValue<Double>> getCellValueFactory(
      final int columnIndex) {
    return param -> param.getValue()[columnIndex].valueProperty().asObject();
  }
}

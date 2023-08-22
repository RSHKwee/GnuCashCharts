package zandbak_reform;

//Java program to create label and add Tooltip text to the labels
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.scene.text.Text.*;
import javafx.scene.text.*;

public class ToolTip extends Application {
  // labels
  Label l, l1, l2;

  // tooltip
  Tooltip t, t1, t2;

  // launch the application
  public void start(Stage s) {
    // set title for the stage
    s.setTitle("creating Tooltip");

    // create a tile pane
    TilePane r = new TilePane();

    // create a label
    l = new Label("This is a label 1 ");
    l1 = new Label("This is a label 2 ");
    l2 = new Label("This is a label 3 ");

    // create tooltip for labels
    t = new Tooltip();
    t1 = new Tooltip("tooltip for label 2");
    t2 = new Tooltip("tooltip for label 3");

    // set text for label 1
    t.setText("tooltip for label 1");

    // set font for tooltip
    t.setFont(Font.font("Arial", FontPosture.ITALIC, 15));
    t1.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));

    // set alignment for tooltip text
    t1.setTextAlignment(TextAlignment.LEFT);
    t2.setTextAlignment(TextAlignment.RIGHT);

    // set the tooltip for labels
    l.setTooltip(t);
    l1.setTooltip(t1);
    Tooltip.install(l2, t2);

    // add label
    r.getChildren().add(l);
    r.getChildren().add(l1);
    r.getChildren().add(l2);

    // create a scene
    Scene sc = new Scene(r, 200, 200);

    // set the scene
    s.setScene(sc);

    s.show();
  }

  public static void main(String args[]) {
    // launch the application
    launch(args);
  }
}

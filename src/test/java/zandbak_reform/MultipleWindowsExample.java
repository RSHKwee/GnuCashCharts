package zandbak_reform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MultipleWindowsExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // First Window
        Button button1 = new Button("Open Second Window");
        button1.setOnAction(e -> openSecondWindow());

        StackPane layout1 = new StackPane(button1);
        Scene scene1 = new Scene(layout1, 300, 200);

        primaryStage.setTitle("First Window");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    private void openSecondWindow() {
        // Second Window
        Stage secondStage = new Stage();
        Button button2 = new Button("This is the Second Window");

        StackPane layout2 = new StackPane(button2);
        Scene scene2 = new Scene(layout2, 200, 100);

        secondStage.setTitle("Second Window");
        secondStage.setScene(scene2);
        secondStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


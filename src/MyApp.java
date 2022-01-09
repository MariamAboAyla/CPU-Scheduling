import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class MyApp extends Application {

    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("CPU Scheduler");
        stage.setResizable(false);
        stage.setScene(new PromptScene(new VBox(), 800, 600));
        setStage(stage);
        stage.show();

    }

    public static void setStage(Stage stage) {
        MyApp.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }
}

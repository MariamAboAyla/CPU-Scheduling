import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PromptBox {

    private static Stage stage;
    private static TextField nameField;
    private static ColorPicker colorPicker;
    private static TitledSpinner<Integer> arrivalTimeSpinner;
    private static TitledSpinner<Integer> burstTimeSpinner;
    private static TitledSpinner<Integer> prioritySpinner;
    private static TitledSpinner<Integer> quantumSpinner;
    private static Button addButton;
    private static Process process;


    public static Process display() {
        process = null;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        nameField = new TextField();
        nameField.setPromptText("Enter process name");
        nameField.setStyle("-fx-font-size: 15");
        nameField.setMinWidth(200);

        colorPicker = new ColorPicker();
        colorPicker.setMinWidth(100);

        burstTimeSpinner = new TitledSpinner<>(0, Integer.MAX_VALUE, 1, "Burst Time");
        arrivalTimeSpinner = new TitledSpinner<>(0, Integer.MAX_VALUE, 1, "Arrival Time");
        prioritySpinner = new TitledSpinner<>(0, Integer.MAX_VALUE, 1, "Priority");
        quantumSpinner = new TitledSpinner<>(0, Integer.MAX_VALUE, 1, "Quantum");
        for (TitledSpinner titledSpinner : new TitledSpinner[] {burstTimeSpinner, arrivalTimeSpinner, prioritySpinner, quantumSpinner})
            titledSpinner.getValueFactory().setValue(0);

        addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> addProcess());
        addButton.setStyle("-fx-font-size: 15");
        addButton.setMinWidth(100);

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(nameField, colorPicker, burstTimeSpinner.getAsVBox(), arrivalTimeSpinner.getAsVBox() , prioritySpinner.getAsVBox(), quantumSpinner.getAsVBox());

        hBox.setPadding(new Insets(20, 20, 20, 20));

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hBox, addButton);

        Scene scene = new Scene(vBox, 700, 200);

        stage.setScene(scene);

        stage.showAndWait();

        return process;
    }

    private static void addProcess() {
        if (nameField.getText().isEmpty()) {
            new AlertBox(Alert.AlertType.WARNING, "Error", "Empty name field", "Please enter a process name").showAndWait();
            return;
        }
        process = new Process(nameField.getText(), colorPicker.getValue(), burstTimeSpinner.getValue(), arrivalTimeSpinner.getValue(), prioritySpinner.getValue(), quantumSpinner.getValue());
        stage.close();
    }
}

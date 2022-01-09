import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class ProcessDisplayScene extends Scene {

    private List<Process> processes;
    private List<Process> uniqueProcesses;
    private Button statisticsButton;



    public ProcessDisplayScene(Parent parent, double width, double height, List<Process> processes) {
        super(parent, width, height);
        this.processes = processes;
        initializeComponents();
        build();
    }

    private void initializeComponents() {

    }

    private void build() {
        ScrollPane scrollPane = (ScrollPane) getRoot();

        VBox vBox = new VBox();
        vBox.setSpacing(50);
        vBox.setAlignment(Pos.CENTER);

        scrollPane.setContent(vBox);

        uniqueProcesses = new ArrayList<>();
        processes.forEach(process -> {
            if (!uniqueProcesses.contains(process))
                uniqueProcesses.add(process);
        });

        uniqueProcesses.forEach(process -> {
            HBox hBox = new HBox();

            hBox.setSpacing(20);
            hBox.setPadding(new Insets(20, 20, 20, 20));
            Label nameLabel = new Label("\n" + process.getName());
            nameLabel.setStyle("-fx-font-size: 15;-fx-font-weight: bold");
            nameLabel.setTextFill(process.getColor());
            hBox.getChildren().add(nameLabel);

            int previous = 0;
            for (int i = 0; i < process.getExecutions().size(); ++i) {
                for (int j = previous; j < process.getExecutions().get(i).getStart(); ++j)
                    hBox.getChildren().add(new Label("\t\n--------\t"));
                previous = process.getExecutions().get(i).getEnd();
                for (int j = process.getExecutions().get(i).getStart(); j < process.getExecutions().get(i).getEnd(); ++j)
                    hBox.getChildren().add(new ProcessRectangle(process.getName(), process.getColor()));

            }

            vBox.getChildren().add(hBox);
        });

        statisticsButton = new Button("Statistics");
        statisticsButton.setOnAction(actionEvent -> showStatistics());

        vBox.getChildren().add(statisticsButton);
    }

    private void showStatistics() {
        StatisticsBox.display(uniqueProcesses);
    }
}

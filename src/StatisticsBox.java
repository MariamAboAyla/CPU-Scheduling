import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class StatisticsBox {

    static Stage stage;
    static Label waitingTime;
    static Label turnAroundTime;
    static Label averageWaitingTime;
    static Label averageTurnAroundTime;
    static Label quantumHistoryLabel;
    static Label AGATHistoryLabel;

    public static void display(List<Process> list) {
        stage = new Stage();
        stage.setTitle("Statistics");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);



        StringBuilder waiting = new StringBuilder(), turnAround = new StringBuilder(), averageTurnAround = new StringBuilder(), averageWaiting = new StringBuilder(), quantumHistory = new StringBuilder(), AGATHistory = new StringBuilder();

        double averageTurn = 0, averageWait = 0;
        for (Process process : list) {
            averageTurn += process.getTurnAroundTime();
            averageWait += process.getWaitingTime();
        }

        averageTurn /= list.size();
        averageWait /= list.size();

        for (Process process : list) {
            waiting.append("Waiting Time for ").append(process.getName()).append(" ").append(process.getWaitingTime()).append('\n');
            turnAround.append("Turn around Time for ").append(process.getName()).append(" ").append(process.getTurnAroundTime()).append('\n');
            averageWaiting.append("Average waiting Time for ").append(process.getName()).append(" ").append(averageWait).append('\n');
            averageTurnAround.append("Average turn around Time for ").append(process.getName()).append(" ").append(averageTurn).append('\n');
            quantumHistory.append("Quantum history for ").append(process.getName()).append(" ").append(process.getQuantumTimeHistory()).append('\n');
            AGATHistory.append("AGAT History for ").append(process.getName()).append(" ").append(process.getAGATFactorHistory()).append('\n');
        }

        waitingTime = new Label(waiting.toString());
        turnAroundTime = new Label(turnAround.toString());
        averageTurnAroundTime = new Label(averageTurnAround.toString());
        averageWaitingTime = new Label(averageWaiting.toString());
        quantumHistoryLabel = new Label(quantumHistory.toString());
        AGATHistoryLabel = new Label(AGATHistory.toString());

        for (Label label : new Label[] {waitingTime, turnAroundTime, averageTurnAroundTime, averageWaitingTime, quantumHistoryLabel, AGATHistoryLabel}) {
            label.setStyle("-fx-font-size: 15");
            label.setWrapText(true);
        }

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(waitingTime, turnAroundTime, averageTurnAroundTime, averageWaitingTime, quantumHistoryLabel, AGATHistoryLabel);



        ScrollPane scrollPane = new ScrollPane(vBox);

        Scene scene = new Scene(scrollPane, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

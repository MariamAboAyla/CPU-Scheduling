import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PromptScene extends Scene {

    private TableColumn<Process, String> nameColumn;
    private TableColumn<Process, Color> colorColumn;
    private TableColumn<Process, Integer> arrivalTimeColumn;
    private TableColumn<Process, Integer> burstTimeColumn;
    private TableColumn<Process, Integer> priorityColumn;
    private TableColumn<Process, Integer> quantumColumn;
    private TableView<Process> processTable;

    private Button addProcessButton;
    private Button removeProcessButton;
    private Button startButton;

    private ComboBox<SchedulingAlgorithm> algorithms;
    private TitledSpinner<Integer> contextSwitchSpinner;


    public PromptScene(Parent parent, double width, double height) {
        super(parent, width, height);
        initializeComponents();
        build();
    }

    private void initializeComponents() {
        nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getColor().toString()));

        arrivalTimeColumn = new TableColumn<>("Arrival Time");
        arrivalTimeColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getArrivalTime()));

        burstTimeColumn = new TableColumn<>("Burst Time");
        burstTimeColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getBurstTime()));

        priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPriority()));

        quantumColumn = new TableColumn<>("Quantum");
        quantumColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantum()));

        for (TableColumn column : new TableColumn[] {nameColumn, colorColumn, arrivalTimeColumn, burstTimeColumn, priorityColumn, quantumColumn}) {
            column.setMinWidth(100);
            column.setStyle("-fx-alignment: center");
        }

        processTable = new TableView<>();
        processTable.setMaxWidth(600);
        processTable.getColumns().addAll(nameColumn, colorColumn, burstTimeColumn, arrivalTimeColumn, priorityColumn, quantumColumn);

        algorithms = new ComboBox<>();
        algorithms.setPromptText("Select scheduling algorithm");
        algorithms.setStyle("-fx-font-size: 15");
        algorithms.getItems().addAll(new SJF(), new SRTF(), new PriorityScheduling(), new AGAT());

        contextSwitchSpinner = new TitledSpinner<>(0, Integer.MAX_VALUE, 1, "Context Switch");
        contextSwitchSpinner.getValueFactory().setValue(0);

        removeProcessButton = new Button("Remove process");
        removeProcessButton.setOnAction(actionEvent -> removeProcess());

        addProcessButton = new Button("Add process");
        addProcessButton.setOnAction(actionEvent -> addProcess());

        for (Button button : new Button[] {addProcessButton, removeProcessButton})
            button.setStyle("-fx-font-size: 15");

        startButton = new Button("Start");
        startButton.setOnAction(actionEvent -> start());
        startButton.setStyle("-fx-font-size: 20");
        startButton.setMinWidth(300);
    }



    private void build() {

        HBox comboSpinner = new HBox();
        comboSpinner.setSpacing(100);
        comboSpinner.setAlignment(Pos.CENTER);
        comboSpinner.getChildren().addAll(algorithms, contextSwitchSpinner.getAsVBox());

        HBox buttons = new HBox();
        buttons.setSpacing(50);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(removeProcessButton, addProcessButton);

        VBox vBox = (VBox) getRoot();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(processTable, comboSpinner, buttons, startButton);
    }

    private void removeProcess() {
        Process selectedProcess = processTable.getSelectionModel().getSelectedItem();
        if (selectedProcess == null) {
            new AlertBox(Alert.AlertType.WARNING, "Error", "No selected process", "Please select a process first").showAndWait();
            return;
        }
        processTable.getItems().remove(selectedProcess);
    }

    private void addProcess() {
        Process process = PromptBox.display();
        if (process == null)
            return;
        if (processTable.getItems().stream().anyMatch(tableProcess -> tableProcess.getName().equalsIgnoreCase(process.getName()))) {
            new AlertBox(Alert.AlertType.WARNING, "Error", "Duplicate names found", "Process names should be unique").showAndWait();
            return;
        }
        processTable.getItems().add(process);
    }

    private void start() {
        if (processTable.getItems().isEmpty()) {
            new AlertBox(Alert.AlertType.WARNING, "Error", "Insufficient processes", "Please add at least 1 process").showAndWait();
            return;
        }
        SchedulingAlgorithm algorithm = algorithms.getSelectionModel().getSelectedItem();
        if (algorithm == null) {
            new AlertBox(Alert.AlertType.WARNING, "Error", "No algorithm selected", "Please select a scheduling algorithm").showAndWait();
            return;
        }
        MyApp.getStage().setScene(new ProcessDisplayScene(new ScrollPane(), 800, 600, algorithm.execute(new ArrayList<>(processTable.getItems()), contextSwitchSpinner.getValue())));
    }
}

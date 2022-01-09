import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Process {

    private String name;
    private Color color;
    private int arrivalTime;
    private int quantum;
    private int burstTime;
    private int priority;
    private int AGATFactor;

    private int startTime;
    private int remainingQuantum;
    private int tempArrivalTime;
    private int remainingTime;
    private int stopped;
    private int endTime;
    private int responseTime;
    private  int continueFromHere;
    public int swap = 0;

    private int waitingTime;
    private int turnAroundTime;
    private List<Integer> quantumTimeHistory;
    private List<Integer> AGATFactorHistory;
    private List<Duration> executions;

    public Process(String name, Color color, int burstTime, int arrivalTime, int priority, int quantum) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.quantum = quantum;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingQuantum = this.quantum;
        this.quantumTimeHistory = new ArrayList<>();
        this.AGATFactorHistory = new ArrayList<>();
        this.executions = new ArrayList<>();
        this.tempArrivalTime = arrivalTime;
        this.remainingTime = burstTime;
        this.stopped = -1;
        this.responseTime = -1;
        this.waitingTime = 0;
    }

    public Process(String processName, int arrivalTime, int burstTime) {
        this.name = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime=burstTime;
        stopped=-1;
        responseTime=-1;
        continueFromHere=0;
    }

    public int getAGATFactor() {
        return AGATFactor;
    }

    public void updateAGATFactor(double v1, double v2) {
        AGATFactor = (int) ((10 - priority) + Math.ceil(arrivalTime / v1) + Math.ceil(burstTime / v2));
    }

    public int get40PercentQuantum() {
        return (int) Math.round(quantum * 0.4);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
        this.quantum = Math.max(0, this.quantum);
    }

    public int getTempArrivalTime() {
        return tempArrivalTime;
    }

    public void setTempArrivalTime(int tempArrivalTime) {
        this.tempArrivalTime = tempArrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getRemainingQuantum() {
        return remainingQuantum;
    }

    public void setRemainingQuantum(int remainingQuantum) {
        this.remainingQuantum = remainingQuantum;
        this.remainingQuantum = Math.max(0, this.remainingQuantum);
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public List<Integer> getQuantumTimeHistory() {
        return quantumTimeHistory;
    }

    public void setQuantumTimeHistory(List<Integer> quantumTimeHistory) {
        this.quantumTimeHistory = quantumTimeHistory;
    }

    public List<Integer> getAGATFactorHistory() {
        return AGATFactorHistory;
    }

    public void setAGATFactorHistory(List<Integer> AGATFactorHistory) {
        this.AGATFactorHistory = AGATFactorHistory;
    }

    public List<Duration> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Duration> executions) {
        this.executions = executions;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getStopped() {
        return stopped;
    }

    public void setStopped(int stopped) {
        this.stopped = stopped;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void calculateTurnAroundTime() {
        int start = executions.get(0).getStart();
        int end = 0;
        int i = 0;
        for (Duration duration : executions) {
            if (i == executions.size() - 1)
                end = duration.getEnd();
            i++;
        }
        turnAroundTime = end - start;
    }

    public void calculateWaitingTime() {
        List<Duration> durations = executions.stream().toList();
        for (int i = 1; i < durations.size(); i++)
            waitingTime += (durations.get(i).getStart() - durations.get(i - 1).getEnd());
        waitingTime += (executions.get(0).getStart() - arrivalTime);
    }

    public int getContinueFromHere() {
        return continueFromHere;
    }

    public void setContinueFromHere(int continueFromHere) {
        this.continueFromHere = continueFromHere;
    }
}

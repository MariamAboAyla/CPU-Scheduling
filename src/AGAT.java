import java.util.*;

public class AGAT implements SchedulingAlgorithm {

    private Vector<Process> readyQueue;
    private List<Process> deadList;
    private List<Process> result;
    private Process runningProcess;
    private int currentTime;
    private double v1;
    private double v2;

    @Override
    public List<Process> execute(List<Process> idleProcesses, int contextSwitch) {
        readyQueue = new Vector<>();
        deadList = new ArrayList<>();
        result = new ArrayList<>();
        idleProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));
        currentTime = idleProcesses.get(0).getArrivalTime();
        runningProcess = null;
        v1 = idleProcesses.get(idleProcesses.size() - 1).getArrivalTime() > 10 ? idleProcesses.get(idleProcesses.size() - 1).getArrivalTime() / 10.0 : 1;
        while (deadList.size() != idleProcesses.size()) {
            boolean isSwitched = false;
            idleProcesses.forEach(process -> {
                if (process.getArrivalTime() <= currentTime) {
                    if (!readyQueue.contains(process) && process != runningProcess && process.getBurstTime() != 0)
                        readyQueue.add(process);
                }
            });
            if (runningProcess == null) {
                runningProcess = readyQueue.firstElement();
                updateAllFactors(idleProcesses);
                readyQueue.remove(0);
                runningProcess.setStartTime(currentTime);
            } else if (runningProcess.getBurstTime() == 0) {
                runningProcess.getQuantumTimeHistory().add(runningProcess.getQuantum());
                runningProcess.setQuantum(0);
                runningProcess.getQuantumTimeHistory().add(0);
                runningProcess.getExecutions().add(new Duration(runningProcess.getStartTime(), currentTime));
                deadList.add(idleProcesses.get(idleProcesses.indexOf(runningProcess)));
                result.add(idleProcesses.get(idleProcesses.indexOf(runningProcess)));
                if (deadList.size() == idleProcesses.size())
                    break;
                if (!readyQueue.isEmpty()) {
                    runningProcess = readyQueue.firstElement();
                    currentTime += contextSwitch;
                    runningProcess.setStartTime(currentTime);
                    isSwitched = contextSwitch > 0;
                    updateAllFactors(idleProcesses);
                    readyQueue.remove(0);
                }

            } else if (runningProcess.getRemainingQuantum() == 0 && !readyQueue.isEmpty()) {
                runningProcess.getQuantumTimeHistory().add(runningProcess.getQuantum());
                runningProcess.setQuantum(runningProcess.getQuantum() + 2);
                runningProcess.setRemainingQuantum(runningProcess.getQuantum());
                runningProcess.getExecutions().add(new Duration(runningProcess.getStartTime(), currentTime));
                readyQueue.add(idleProcesses.get(idleProcesses.indexOf(runningProcess)));
                result.add(idleProcesses.get(idleProcesses.indexOf(runningProcess)));
                runningProcess = readyQueue.firstElement();
                currentTime += contextSwitch;
                runningProcess.setStartTime(currentTime);
                isSwitched = contextSwitch > 0;
                updateAllFactors(idleProcesses);
                readyQueue.remove(0);
            } else if (currentTime >= runningProcess.getStartTime() + runningProcess.get40PercentQuantum()) {
                Process bestAGAT = getBestAGATFactor(readyQueue);
                if (bestAGAT != null && runningProcess.getAGATFactor() > bestAGAT.getAGATFactor()) {
                    runningProcess.getQuantumTimeHistory().add(runningProcess.getQuantum());
                    runningProcess.setQuantum(runningProcess.getQuantum() + runningProcess.getRemainingQuantum());
                    runningProcess.setRemainingQuantum(runningProcess.getQuantum());
                    runningProcess.getExecutions().add(new Duration(runningProcess.getStartTime(), currentTime));
                    result.add(idleProcesses.get(idleProcesses.indexOf(runningProcess)));
                    readyQueue.add(idleProcesses.get(idleProcesses.indexOf(runningProcess)));
                    runningProcess = bestAGAT;
                    currentTime += contextSwitch;
                    runningProcess.setStartTime(currentTime);
                    isSwitched = contextSwitch > 0;
                    updateAllFactors(idleProcesses);
                    readyQueue.remove(bestAGAT);
                }
            }
            runningProcess.setBurstTime(runningProcess.getBurstTime() - 1);
            runningProcess.setRemainingQuantum(runningProcess.getRemainingQuantum() - 1);
            currentTime += isSwitched ? 0 : 1;
        }
        deadList.forEach(process -> {
            process.calculateTurnAroundTime();
            process.calculateWaitingTime();
        });
        return result;
    }

    private int getMaxRemainingBurstTime(List<Process> processes) {
        int maxTime = Integer.MIN_VALUE;
        for (Process process : processes)
            maxTime = Math.max(maxTime, process.getBurstTime());
        return maxTime;
    }

    private Process getBestAGATFactor(List<Process> processes) {
        if (processes.isEmpty())
            return null;
        Process bestAGAT = processes.get(0);
        for (int i = 1; i < processes.size(); i++)
            if (processes.get(i).getAGATFactor() < bestAGAT.getAGATFactor())
                bestAGAT = processes.get(i);
        return bestAGAT;
    }

    private void updateAllFactors(List<Process> idleProcesses) {
        int maxRemainingBurstTime = getMaxRemainingBurstTime(idleProcesses);
        v2 = maxRemainingBurstTime > 10 ? maxRemainingBurstTime / 10.0 : 1;
        idleProcesses.forEach(process -> {
            process.updateAGATFactor(v1, v2);
            if (process.getBurstTime() != 0)
                process.getAGATFactorHistory().add(process.getAGATFactor());
        });
    }

    @Override
    public String toString() {
        return "AGAT";
    }
}

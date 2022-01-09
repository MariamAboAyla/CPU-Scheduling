import java.util.*;

public class SJF implements SchedulingAlgorithm {

    List<Process> sorted;

    @Override
    public List<Process> execute(List<Process> processes, int contextSwitch) {
        sorted = sjfNonpreemetive(processes);
        return enhancedSJF(sorted);
    }

    private List<Process> enhancedSJF(List<Process> sorted) {
        List<Process> enhanced = new ArrayList<>(sorted.size());
        int j = sorted.size() - 1;
        int shorterJobs = 0;
        int largerJob = sorted.get(j).getBurstTime();
        while (!sorted.isEmpty()) {
            j = sorted.size() - 1;
            shorterJobs += sorted.get(0).getBurstTime();
            if (shorterJobs > largerJob && sorted.size() > 1 && sorted.get(j).getWaitingTime() >= sorted.get(0).getArrivalTime()) {
                enhanced.add(sorted.get(j));
                sorted.remove(j);
                largerJob = sorted.get(j - 1).getBurstTime();
                shorterJobs = 0;
            } else {
                enhanced.add(sorted.get(0));
                sorted.remove(0);
            }
        }
        avgTurnaroundTime(enhanced);
        avgWaitingTime(enhanced);
        int counter = 0;
        for (Process process : enhanced) {
            process.getExecutions().add(new Duration(counter, process.getBurstTime()));
            counter += process.getBurstTime();
        }
        return enhanced;
    }

    private List<Process> sjfNonpreemetive(List<Process> p) {
        int l = 0;
        List<Process> sorted = new ArrayList<>(p.size());
        p.sort(Comparator.comparing(Process::getBurstTime));
        int index;
        for (int i = 0; i < p.size() - 1; i++) {
            index = i;
            for (int j = i + 1; j < p.size(); j++) {
                if (p.get(j).getTempArrivalTime() < p.get(index).getTempArrivalTime()) {
                    index = j;
                }
            }
            Collections.swap(p, i, index);
        }
        int currTime = p.get(0).getTempArrivalTime();
        PriorityQueue<Process> processPriorityQueue = new PriorityQueue<>(p.size(), new ProcessBurstTimeComparator());
        processPriorityQueue.add(p.get(0));
        p.get(0).setTempArrivalTime(-1);
        while (!processPriorityQueue.isEmpty()) {
            processPriorityQueue.peek().setWaitingTime(currTime - processPriorityQueue.peek().getBurstTime());
            sorted.add(processPriorityQueue.peek() != null ? processPriorityQueue.peek():null);
            currTime += processPriorityQueue.peek() != null ? processPriorityQueue.peek().getBurstTime() : 0;
            processPriorityQueue.remove();

            for (int i = 0; i < p.size(); i++) {
                if (p.get(i).getTempArrivalTime() <= currTime && p.get(i).getTempArrivalTime() != -1) {
                    processPriorityQueue.add(p.get(i));
                    p.get(i).setTempArrivalTime(-1);
                }
            }
        }
        return sorted;
    }

    static class ProcessBurstTimeComparator implements Comparator<Process> {
        @Override
        public int compare(Process o1, Process o2) {
            if (o1.getBurstTime() > o2.getBurstTime())
                return 1;
            else if (o1.getBurstTime() < o2.getBurstTime())
                return -1;
            return 0;
        }
    }

    public void avgTurnaroundTime(List<Process> list){
        int turnaroundTime = 0;
        int time = 0;
        for (Process process : list) {
            turnaroundTime += time + process.getBurstTime() - process.getArrivalTime();
            time += process.getBurstTime();
            process.setTurnAroundTime(time);
        }
    }

    public void avgWaitingTime(List<Process> list){
        int waitingTime = 0;
        int time = list.get(0).getBurstTime();
        list.get(0).setWaitingTime(0);
        for (int i = 1;i<list.size();i++) {
            list.get(i).setWaitingTime(time);
            waitingTime += time- list.get(i).getArrivalTime();
            time += list.get(i).getBurstTime();
        }
    }

    @Override
    public String toString() {
        return "SJF";
    }
}

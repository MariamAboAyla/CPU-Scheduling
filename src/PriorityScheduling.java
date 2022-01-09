import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriorityScheduling implements SchedulingAlgorithm {

    private List<Process> processes;
    private int contextSwitch;
    private int time;

    @Override
    public List<Process> execute(List<Process> processes, int contextSwitch) {
        this.processes = processes;
        this.contextSwitch = contextSwitch;
        this.time = 0;
        sortQueue(0);
        return start();
    }

    public void sortQueue(int index) {
        // to sort them based-on priority
        if (index == 0) {
            processes.sort((o1, o2) -> {
                if (o1.getPriority() != o2.getPriority())
                    return o1.getPriority() - o2.getPriority();
                else
                    return o1.getArrivalTime() - o2.getArrivalTime();
            });
        } else {
            for (int i = index; i < processes.size(); i++) {
                for (int j = i + 1; j < processes.size(); j++) {
                    if (processes.get(i).getPriority() > processes.get(j).getPriority() || (processes.get(i).getPriority() == processes.get(j).getPriority() && processes.get(i).getArrivalTime() > processes.get(j).getArrivalTime())) {
                        Collections.swap(processes, i, j);
                    }
                }
            }
        }

    }

    public List<Process> start() {
//        double check = 0;
        List<Process> result = new ArrayList<>();
        for (int i = 0; i < processes.size(); i++) {
            processes.get(i).setWaitingTime(time);
            processes.get(i).setTurnAroundTime(time + processes.get(i).getBurstTime());
            time += (processes.get(i).getBurstTime() + contextSwitch);

            int ageCnt = (processes.get(i).getBurstTime() + contextSwitch) / processes.get(i).getQuantum();
            aging(i, ageCnt, processes.get(i).getPriority());
            result.add(processes.get(i));
        }
        result.forEach(process -> process.getExecutions().add(new Duration(process.getTurnAroundTime() - process.getBurstTime(), process.getTurnAroundTime())));
        return result;
    }

    private void aging(int index, int cnt, int curPri) {
        // could start from middle item till end
        boolean flag = true;
        for (int now = 0; now < cnt && flag; now++) {
            int mid = (index + (processes.size() - index)) / 2;
            for (int i = processes.size() - 1; i >= mid; i--) {
                int pri = processes.get(i).getPriority();
                if (pri - 1 <= curPri) {
                    flag = false;
                    break;
                }
                processes.get(i).setPriority(pri - 1);
            }
            sortQueue(index);
        }
    }

    @Override
    public String toString() {
        return "Priority Scheduling";
    }
}

import java.util.Comparator;
import java.util.List;

public class SRTF implements SchedulingAlgorithm {

    int overAllTime = 0;

    @Override
    public List<Process> execute(List<Process> processes, int contextSwitch) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        Process working = new Process("temp", 100000, 100000);
        Process temp = working;
        boolean flag = false;

        for (int i = 0; i <= overAllTime + 10000; i++) {
            int idex=-1;
            for (int w = 0; w < processes.size(); w++) {
                if (processes.get(w).getName().equals(working.getName())) {
                    processes.set(w, working);
                    idex = w;
                    break;
                }
            }
            Process temp2 = working;
            for (int p = 0; p < processes.size(); p++) {
                if (processes.get(p).getArrivalTime() <= i &&
                        processes.get(p).getRemainingTime() < working.getRemainingTime() &&
                        processes.get(p).getRemainingTime() > 0) {
                    if (i - working.getArrivalTime() >= 4)
                        continue;

                    for (int j = 0; j < processes.size(); j++) {
                        if (processes.get(j).getName().equals(working.getName())) {
                            processes.set(j, working);

                            break;
                        }
                    }
                    working = processes.get(p);

                }
            }

            if (temp2 != working && flag) {
                if (idex!=-1)
                {
                    if ( processes.get(idex).getContinueFromHere()==0)
                    {
                        processes.get(idex).getExecutions().add(new Duration(processes.get(idex).getResponseTime(),i));
                    }
                    else
                    processes.get(idex).getExecutions().add(new Duration(processes.get(idex).getContinueFromHere(),i));
                    processes.get(idex).swap++;
                }



                i += contextSwitch;
                working.setContinueFromHere(i);

            } else if (temp2 != working && !flag)
                flag = true;



            if (working.getResponseTime() == -1)
                working.setResponseTime(i);


            working.setRemainingTime(working.getRemainingTime() - 1);
            if (working.getRemainingTime() == 0) {
                working.swap++;

                for (int j = 0; j < processes.size(); j++) {
                    if (processes.get(j).getName().equals(working.getName())) {
                        processes.set(j, working);
                        processes.get(j).setEndTime(i + 1);
                        if (processes.get(j).getExecutions().size()==0)
                        {
                            processes.get(j).getExecutions().add(new Duration(processes.get(j).getResponseTime(), processes.get(j).getEndTime()));
                        }
                        else
                            processes.get(j).getExecutions().add(new Duration(processes.get(j).getContinueFromHere(),i+1));
                        working = temp;
                        break;
                    }
                }
            }


        }
        waitingTime(processes);
        turnAroundTime(processes, contextSwitch);
        return processes;
    }

    public void waitingTime(List<Process> processes) {
        for (int j = 0; j < processes.size(); j++) {
            processes.get(j).setWaitingTime(processes.get(j).getEndTime() -
                    processes.get(j).getArrivalTime() - processes.get(j).getBurstTime());
        }
    }

    public void turnAroundTime(List<Process> processes, int contextSwitch) {
        for (int j = 0; j < processes.size(); j++) {
            processes.get(j).setTurnAroundTime(processes.get(j).getWaitingTime() + processes.get(j).getBurstTime() + (contextSwitch * processes.get(j).swap));
        }
    }

    @Override
    public String toString() {
        return "SRTF";
    }
}

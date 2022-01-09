import java.util.List;

public interface SchedulingAlgorithm {
    List<Process> execute(List<Process> processes, int contextSwitch);
}

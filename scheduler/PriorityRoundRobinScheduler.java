package scheduler;

import java.util.*;

public class PriorityRoundRobinScheduler {
    private Map<Integer, Queue<Process>> priorityQueues;
    private int timeQuantum;
    private static final int AGING_THRESHOLD = 7;

    public PriorityRoundRobinScheduler(int timeQuantum) {
        this.priorityQueues = new TreeMap<>();
        this.timeQuantum = timeQuantum;
    }

    public void addProcess(Process process, int currentTime) {
        priorityQueues.putIfAbsent(process.getPriority(), new LinkedList<>());
        process.setLastQueueEntryTime(currentTime);
        priorityQueues.get(process.getPriority()).add(process);
    }

    private void applyAging(int currentTime) {
        Map<Integer, List<Process>> toPromote = new HashMap<>();

        for (Map.Entry<Integer, Queue<Process>> entry : priorityQueues.entrySet()) {
            int priority = entry.getKey();
            Queue<Process> queue = entry.getValue();
            Iterator<Process> it = queue.iterator();

            while (it.hasNext()) {
                Process p = it.next();
                if (currentTime - p.getLastQueueEntryTime() >= AGING_THRESHOLD && priority > 0) {
                    it.remove();
                    toPromote.putIfAbsent(priority - 1, new ArrayList<>());
                    toPromote.get(priority - 1).add(p);
                }
            }
        }

        for (Map.Entry<Integer, List<Process>> entry : toPromote.entrySet()) {
            int newPriority = entry.getKey();
            for (Process p : entry.getValue()) {
                p.setPriority(newPriority);
                addProcess(p, currentTime);
            }
        }
    }

    public void execute() {
        System.out.println("Starting Priority Round Robin Scheduling with Time Quantum: " + timeQuantum + "\n");
        System.out
                .println("Starting Priority Round Robin Scheduling with Aging Threshold at: " + AGING_THRESHOLD + "\n");

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int totalBurstTime = 0;

        List<Process> allProcesses = new ArrayList<>();
        for (Queue<Process> queue : priorityQueues.values()) {
            for (Process p : queue) {
                allProcesses.add(p);
                totalBurstTime += p.getBurstTime();
            }
        }

        List<Process> notArrived = new ArrayList<>(allProcesses);
        notArrived.sort(Comparator.comparingInt(Process::getArrivalTime));

        priorityQueues.clear();

        while (!notArrived.isEmpty() || !allQueuesEmpty()) {

            Iterator<Process> it = notArrived.iterator();
            while (it.hasNext()) {
                Process p = it.next();
                if (p.getArrivalTime() <= currentTime) {
                    addProcess(p, currentTime);
                    it.remove();
                }
            }

            if (allQueuesEmpty()) {
                Process nextToArrive = notArrived.get(0);
                int idleTime = nextToArrive.getArrivalTime() - currentTime;
                System.out.println("CPU is idle for " + idleTime + " units.");
                currentTime = nextToArrive.getArrivalTime();
                applyAging(currentTime);
                continue;
            }

            for (int priority : priorityQueues.keySet()) {
                Queue<Process> queue = priorityQueues.get(priority);
                if (queue == null || queue.isEmpty())
                    continue;

                Process current = queue.poll();

                int execTime = Math.min(timeQuantum, current.getRemainingTime());
                System.out.println("Executing Process ID: " + current.getProcessId() + " (Priority " + priority
                        + ") for " + execTime + " units.");

                currentTime += execTime;
                current.setRemainingTime(current.getRemainingTime() - execTime);
                applyAging(currentTime);

                Iterator<Process> newIt = notArrived.iterator();
                while (newIt.hasNext()) {
                    Process p = newIt.next();
                    if (p.getArrivalTime() <= currentTime) {
                        addProcess(p, currentTime);
                        newIt.remove();
                    }
                }

                if (current.getRemainingTime() > 0) {
                    current.setLastQueueEntryTime(currentTime);
                    queue.add(current);
                } else {
                    current.setCompletionTime(currentTime);
                    completedProcesses.add(current);
                }

                break;
            }
        }

        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\n=== Final Metrics ===");
        System.out.printf("%-10s%-10s%-15s%-15s%-15s\n", "PID", "Prio", "Waiting Time", "Turnaround", "Completion");

        for (Process p : completedProcesses) {
            int waitingTime = p.calculateWaitingTime();
            int turnaroundTime = p.calculateTurnaroundTime();
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            System.out.printf("%-10d%-10d%-15d%-15d%-15d\n",
                    p.getProcessId(), p.getPriority(), waitingTime, turnaroundTime, p.getCompletionTime());
        }

        double averageWaitingTime = totalWaitingTime / (double) completedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / (double) completedProcesses.size();
        double throughput = completedProcesses.size() / (double) currentTime;
        double cpuUtilization = (totalBurstTime / (double) currentTime) * 100;

        System.out.printf("\nCPU Utilization: %.2f%%\n", cpuUtilization);
        System.out.printf("Throughput: %.2f processes/unit time\n", throughput);
        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }

    private boolean allQueuesEmpty() {
        for (Queue<Process> queue : priorityQueues.values()) {
            if (!queue.isEmpty())
                return false;
        }
        return true;
    }
}

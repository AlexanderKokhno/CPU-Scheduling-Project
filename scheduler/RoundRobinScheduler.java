package scheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;

public class RoundRobinScheduler {
    private Queue<Process> readyQueue;
    private int timeQuantum;

    public RoundRobinScheduler(int timeQuantum) {
        this.readyQueue = new LinkedList<>();
        this.timeQuantum = timeQuantum;
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
    }

    public void execute() {
        System.out.println("Starting Round Robin Scheduling with Time Quantum: " + timeQuantum + "\n");

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int totalBurstTime = 0;

        for (Process process : readyQueue) {
            totalBurstTime += process.getBurstTime();
        }
        List<Process> notArrived = new ArrayList<>(readyQueue);
        Queue<Process> queue = new LinkedList<>();

        while (!queue.isEmpty() || !notArrived.isEmpty()) {

            Iterator<Process> it = notArrived.iterator();
            while (it.hasNext()) {
                Process p = it.next();
                if (p.getArrivalTime() <= currentTime) {
                    queue.add(p);
                    it.remove();
                }
            }

            if (queue.isEmpty()) {
                Process nextToArrive = notArrived.get(0);
                int idleTime = nextToArrive.getArrivalTime() - currentTime;
                System.out.println("CPU is idle for " + idleTime + " units.");
                currentTime = nextToArrive.getArrivalTime();
                continue;
            }

            Process currentProcess = queue.poll();
            int execTime = Math.min(timeQuantum, currentProcess.getRemainingTime());

            System.out
                    .println("Executing Process ID: " + currentProcess.getProcessId() + " for " + execTime + " units.");
            currentTime += execTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - execTime);

            if (currentProcess.getRemainingTime() > 0) {
                queue.add(currentProcess);
            } else {
                currentProcess.setCompletionTime(currentTime);
                completedProcesses.add(currentProcess);
            }
        }

        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\n=== Final Metrics ===");
        System.out.printf("%-10s%-18s%-20s\n", "Process", "Waiting Time", "Turnaround Time");
        for (Process p : completedProcesses) {
            int waitingTime = p.calculateWaitingTime();
            int turnaroundTime = p.calculateTurnaroundTime();
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            System.out.printf("%-10d%-18d%-20d\n", p.getProcessId(), waitingTime, turnaroundTime);
        }

        double averageWaitingTime = totalWaitingTime / (double) completedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / (double) completedProcesses.size();
        double throughput = (double) completedProcesses.size() / (double) currentTime;

        double cpuUtilization = (totalBurstTime / (double) currentTime) * 100;

        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
        System.out.printf("Throughput: %.2f processes/unit time\n", throughput);
        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}

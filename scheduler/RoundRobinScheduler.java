package scheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

        for (Process p : readyQueue) {
            totalBurstTime += p.getBurstTime();
        }

        Queue<Process> queue = new LinkedList<>(readyQueue);

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();

            if (currentTime < currentProcess.getArrivalTime()) {
                int idleTime = currentProcess.getArrivalTime() - currentTime;
                System.out.println("CPU is idle for " + idleTime + " units.");
                currentTime += idleTime;
            }

            int execTime = Math.min(timeQuantum, currentProcess.getRemainingTime());

            System.out.println("Executing Process ID: " + currentProcess.getProcessId() + " for " + execTime + " units.");
            currentTime += execTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - execTime);

            if (currentProcess.getRemainingTime() > 0) {
                queue.add(currentProcess);
            } else {
                currentProcess.setCompletionTime(currentTime);
                completedProcesses.add(currentProcess);
            }
        }

        int totalWT = 0;
        int totalTAT = 0;

        System.out.println("\n=== Final Metrics ===");
        System.out.printf("%-10s%-18s%-20s\n", "Process", "Waiting Time", "Turnaround Time");
        for (Process p : completedProcesses) {
            int wt = p.calculateWaitingTime();
            int tat = p.calculateTurnaroundTime();
            totalWT += wt;
            totalTAT += tat;

            System.out.printf("%-10d%-18d%-20d\n", p.getProcessId(), wt, tat);
        }

        double avgWT = totalWT / (double) completedProcesses.size();
        double avgTAT = totalTAT / (double) completedProcesses.size();
        double throughput = (double) completedProcesses.size() / (double) currentTime;

        double cpuUtilization = (totalBurstTime / (double) currentTime) * 100;

        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
        System.out.printf("Throughput: %.2f processes/unit time\n", throughput);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
    }
}

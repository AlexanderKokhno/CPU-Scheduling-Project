// This file implements the Round Robin scheduling algorithm.
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
        int currentTime = 0; // Track the time elapsed from start to finish
        int totalBurstTime = 0; // Total burst time (time the CPU is actively used)

        // Calculate total burst time (CPU busy time)
        for (Process p : readyQueue) {
            totalBurstTime += p.getBurstTime();
        }

        // Clone the queue to avoid modifying the original
        Queue<Process> queue = new LinkedList<>(readyQueue);
        readyQueue.clear(); // clean the original queue

        // Execute processes with Round Robin logic
        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();

            // If the current time is less than the process's arrival time, the CPU is idle
            if (currentTime < currentProcess.getArrivalTime()) {
                int idleTime = currentProcess.getArrivalTime() - currentTime;
                System.out.println("CPU is idle for " + idleTime + " units.");
                currentTime += idleTime; // Add idle time to currentTime
            }

            int execTime = Math.min(timeQuantum, currentProcess.getRemainingTime());

            // Execute the process and update the current time
            System.out
                    .println("Executing Process ID: " + currentProcess.getProcessId() + " for " + execTime + " units.");
            currentTime += execTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - execTime);

            // If the process still has remaining time, re-queue it
            if (currentProcess.getRemainingTime() > 0) {
                queue.add(currentProcess);
            } else {
                // Process is complete, set completion time
                currentProcess.setCompletionTime(currentTime);
                completedProcesses.add(currentProcess);
            }
        }

        // Final Metrics Calculation
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

        // ✅ Calculate CPU utilization correctly
        double cpuUtilization = (totalBurstTime / (double) currentTime) * 100;

        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
}
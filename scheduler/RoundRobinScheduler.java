// This file implements the Round Robin scheduling algorithm.
package scheduler;

import java.util.LinkedList;
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
        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
            if (currentProcess.getRemainingTime() > timeQuantum) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeQuantum);
                System.out.println(
                        "Executing Process ID: " + currentProcess.getProcessId() + " for " + timeQuantum + " units.");
                readyQueue.add(currentProcess);
            } else {
                System.out.println("Executing Process ID: " + currentProcess.getProcessId() + " for "
                        + currentProcess.getRemainingTime() + " units.");
                currentProcess.setCompletionTime(currentProcess.getBurstTime() - currentProcess.getRemainingTime());
                currentProcess.setRemainingTime(0);
                // Calculate waiting and turnaround times here if needed
            }
        }
        // Calculate and print performance metrics here if needed
    }
}
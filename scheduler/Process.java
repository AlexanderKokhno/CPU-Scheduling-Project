package scheduler;

public class Process {
    private int processId;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int completionTime;

    public Process(int processId, int arrivalTime, int burstTime) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.completionTime = 0;
    }

    public int getProcessId() {
        return processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int calculateWaitingTime() {
        return completionTime - arrivalTime - burstTime;
    }

    public int calculateTurnaroundTime() {
        return completionTime - arrivalTime;
    }
}
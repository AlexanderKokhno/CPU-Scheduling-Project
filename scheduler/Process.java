package scheduler;

public class Process {
    private int processId;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int completionTime;
    private int priority;

    public Process(int processId, int arrivalTime, int burstTime, int priority) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.completionTime = 0;
        this.priority = priority;
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

    public int getPriority() {
        return priority;
    }

    private int lastQueueEntryTime;

    public int getLastQueueEntryTime() {
        return lastQueueEntryTime;
    }

    public void setLastQueueEntryTime(int time) {
        this.lastQueueEntryTime = time;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
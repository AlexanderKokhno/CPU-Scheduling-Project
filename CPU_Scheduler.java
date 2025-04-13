import java.io.*;
import java.util.*;

class Process {
  int pid, arrivalTime, burstTime, remainingTime;

  public Process(int pid, int arrivalTime, int burstTime) {
    this.pid = pid;
    this.arrivalTime = arrivalTime;
    this.burstTime = burstTime;
    this.remainingTime = burstTime; // For Round Robin simulation
  }
}

public class CPU_Scheduler {

  public static void main(String[] args) {
    String fileName = args[0]; // First argument: the path to CSV file
    int timeQuantum = Integer.parseInt(args[1]); // Second argument: time quantum

    List<Process> processList = new ArrayList<>();
    readCSV(fileName, processList);

    // Call the Round Robin scheduling method here
    // simulateRoundRobin(processList, timeQuantum);
  }

  public static void readCSV(String fileName, List<Process> processList) {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      br.readLine(); // Skip the header line
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(",");
        int pid = Integer.parseInt(fields[0].trim());
        int arrivalTime = Integer.parseInt(fields[1].trim());
        int burstTime = Integer.parseInt(fields[2].trim());

        processList.add(new Process(pid, arrivalTime, burstTime));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

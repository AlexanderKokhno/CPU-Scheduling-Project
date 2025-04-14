import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main <file-path> <time-quantum>");
            return;
        }

        String filePath = args[0];
        int timeQuantum;

        try {
            timeQuantum = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Time quantum must be an integer.");
            return;
        }

        // Parse the input file to get the list of processes
        List<Process> processes = FileParser.parseFile(filePath);

        // Initialize the scheduler and start the simulation
        RoundRobinScheduler scheduler = new RoundRobinScheduler(timeQuantum);
        for (Process p : processes) {
            scheduler.addProcess(p);
        }
        scheduler.execute();
    }
}
import java.util.List;
import utils.FileParser;
import scheduler.PriorityRoundRobinScheduler;
import scheduler.Process;

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

        // Remember to insert your file path when you run the program, the timeQuantum
        // as well (as an int)
        List<scheduler.Process> processes = FileParser.parseFile(filePath);

        // Initialize the scheduler and start the simulation
        PriorityRoundRobinScheduler scheduler = new PriorityRoundRobinScheduler(timeQuantum);
        for (Process p : processes) {
            scheduler.addProcess(p, 0);
        }
        scheduler.execute();
    }
}

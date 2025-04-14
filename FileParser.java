import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import src.scheduler.Process;

public class FileParser {
    public static List<Process> parseFile(String filePath) {
        List<Process> processList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                int processId = Integer.parseInt(details[0].trim());
                int arrivalTime = Integer.parseInt(details[1].trim());
                int burstTime = Integer.parseInt(details[2].trim());
                
                Process process = new Process(processId, arrivalTime, burstTime);
                processList.add(process);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return processList;
    }
}
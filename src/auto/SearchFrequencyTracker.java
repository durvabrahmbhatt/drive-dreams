package auto;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SearchFrequencyTracker {
    private Map<String, Integer> searchFrequencyMap;
    private final String directoryPath = "C:\\Users\\Dhwani\\Downloads\\car-price";
    private final String csvFilePath = directoryPath + "\\search_frequency.csv";

    public SearchFrequencyTracker() {
        this.searchFrequencyMap = new HashMap<>();
        // Load data from CSV file upon initialization
        loadFromCSV();
    }

    public void incrementSearchFrequency(String word) {
        // If the word is already in the map, increment its frequency, otherwise add it with frequency 1
        searchFrequencyMap.put(word, searchFrequencyMap.getOrDefault(word, 0) + 1);
        // Save data to CSV file after each update
        saveToCSV();
    }

    public int getSearchFrequency(String word) {
        // Return the search frequency of the word, or 0 if it's not found
        return searchFrequencyMap.getOrDefault(word, 0);
    }

    private void loadFromCSV() {
        try {
            // Create directory if it doesn't exist
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Creates parent directories if necessary
            }

            // Create file if it doesn't exist
            File file = new File(csvFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read data from CSV file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    searchFrequencyMap.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
            reader.close();
        } catch (IOException e) {
            // Handle file IO exception
            e.printStackTrace();
        }
    }

    private void saveToCSV() {
        try {
            // Write data to CSV file
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath));
            for (Map.Entry<String, Integer> entry : searchFrequencyMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            // Handle file IO exception
            e.printStackTrace();
        }
    }
}

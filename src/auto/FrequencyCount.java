package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FrequencyCount {

    public void countFrequency(String filePath, String keyword) {
        // The local directory that contains the web pages
        File folder = new File(filePath);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: The specified folder does not exist or is not a directory.");
            return;
        }

        // Get all files from the directory
        File[] listOfFiles = folder.listFiles();

        // Check if the list of files is null or empty
        if (listOfFiles == null || listOfFiles.length == 0) {
            System.out.println("Error: The folder is empty or does not contain any files.");
            return;
        }

        List<String> keywords = new ArrayList<>();
        keywords.add(keyword.toLowerCase());

        int totalFrequency = 0; // Total frequency count for the specified keyword

        // To loop through each web page
        for (File file : listOfFiles) {
            // Checker to check if the file is in .txt format
            if (file.getName().endsWith(".txt")) {
                // to read the contents of the file
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    // To read each line of the file
                    while ((line = reader.readLine()) != null) {
                        // To Tokens the line into words
                        StringTokenizer tokenizer = new StringTokenizer(line);
                        while (tokenizer.hasMoreTokens()) {
                            String word = tokenizer.nextToken().toLowerCase().replaceAll("[^a-z0-9]+", "");

                            // To check if the word is a keyword
                            if (keywords.contains(word)) {
                                totalFrequency++;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Print the total frequency count for the specified keyword
        System.out.println("Total frequency count for the keyword '" + keyword + "': " + totalFrequency);

        // To print the web pages and their frequencies based on keyword matches
        System.out.println("\nWeb pages and their frequencies based on keyword matches: ");
        boolean wordFound = false; // Flag to check if the word is found in any page
        for (File file : listOfFiles) {
            // Checker to check if the file is in .txt format
            if (file.getName().endsWith(".txt")) {
                int frequency = countFrequencyInFile(file, keyword);
                if (frequency > 0) {
                    System.out.println(file.getName() + " - frequency: " + frequency);
                    wordFound = true;
                }
            }
        }

        // If the word is not found in any page, print a message indicating so
        if (!wordFound) {
            System.out.println("Oops! The word '" + keyword + "' is not present in any page.");
        }
    }

    // Helper method to count the frequency of the keyword in a specific file
    private int countFrequencyInFile(File file, String keyword) {
        int frequency = 0;
        // to read the contents of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // To read each line of the file
            while ((line = reader.readLine()) != null) {
                // To Tokens the line into words
                StringTokenizer tokenizer = new StringTokenizer(line);
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken().toLowerCase().replaceAll("[^a-z0-9]+", "");

                    // To check if the word is equal to the keyword
                    if (word.equals(keyword)) {
                        frequency++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frequency;
    }
}

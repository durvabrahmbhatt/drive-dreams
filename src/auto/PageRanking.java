package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class PageRanking {

    // Inner class to represent a web page
    class Page {
        String WebPagename; // to name the web page we are ranking
        int score; // to store the scores of the web page

        // Defining Constructor to store the name and score
        Page(String name, int score) {
            this.WebPagename = name;
            this.score = score;
        }
    }

    public void PageRank(String filePath, String keyword) {
        // The local directory that contains the web pages
        File folder = new File(filePath);
        // To get all files from the directory
        File[] listOfFiles = folder.listFiles();

        // Initialize keyword frequency map
        Map<String, Integer> keywordFrequency = new HashMap<>();

        // Initialize keyword frequency to 0
        keywordFrequency.put(keyword, 0);

        // Initialize a Priority queue to store the web pages as per their respective scores
        PriorityQueue<Page> heap = new PriorityQueue<>(10, (p1, p2) -> Integer.compare(p2.score, p1.score));

        // To loop through each web page
        for (File file : listOfFiles) {
            // Checker to check if the file is in .txt format
            if (file.getName().endsWith(".txt")) {
                int score = 0;
                // to read the contents of the file
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    // To read each line of the file
                    while ((line = reader.readLine()) != null) {
                        // Tokenize the line into words
                        StringTokenizer tokenizer = new StringTokenizer(line);
                        while (tokenizer.hasMoreTokens()) {
                            String word = tokenizer.nextToken().toLowerCase().replaceAll("[^a-z0-9]+", "");

                            // Check if the word is the keyword
                            if (word.equals(keyword)) {
                                // Increase the score and frequency of the keyword
                                score += keywordFrequency.getOrDefault(word, 0) + 1;
                                keywordFrequency.put(word, keywordFrequency.getOrDefault(word, 0) + 1);
                            }
                        }
                    }
                    // Reset the frequency of the keyword to 0
                    keywordFrequency.put(keyword, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Create a page object with the name and score of the file
                Page page = new Page(file.getName(), score);
                // Add the page object to the priority queue
                heap.offer(page);
            }
        }
        // Print the top 10 web pages based on keyword matches
        System.out.println("Top 10 web pages based on keyword matches for \"" + keyword + "\":\n");
        List<Page> topPages = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            if (heap.isEmpty()) {
                break;
            }
            Page page = heap.poll();
            topPages.add(page);
            System.out.println((i + ". " + page.WebPagename + " - score: " + page.score));
        }
    }
}

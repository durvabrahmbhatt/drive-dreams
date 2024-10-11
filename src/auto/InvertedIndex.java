package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {

    // Define a Trie data structure to store the keys (words) of the inverted index
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<Integer> positions = new ArrayList<>();
    }

    private TrieNode root = new TrieNode(); // root of the Trie

    private int[][] occurrences; // 2D array to store the list of occurrences of each word

    public void buildIndex(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + folderPath);
        }

        // Initialize the occurrences array with the number of files in the folder
        occurrences = new int[folder.listFiles().length][];

        int pageIndex = 0; // initializing page index
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                // Read the file and extract the words
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    int position = 0;
                    while ((line = br.readLine()) != null) {
                        String[] words = line.split("\\s+");
                        for (String word : words) {
                            // Add the position of the word in the file to the Trie node
                            addWord(root, word.toLowerCase(), pageIndex, position++);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
                pageIndex++; // increment the page index for the next file
            }
        }
    }

    // Method to add a word to the Trie node
    private void addWord(TrieNode node, String word, int pageIndex, int position) {
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.positions.add(pageIndex * 10000 + position); // encode page index and position as a single integer
    }

    // Method to search for a keyword in the inverted index and display the list of occurrences
    public void searchKeyword(String keyword) {
        TrieNode node = root;
        for (char c : keyword.toLowerCase().toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                System.out.println("Keyword not found: " + keyword);
                return;
            }
        }
        System.out.println("Occurrences of keyword: " + keyword);
        for (int p : node.positions) {
            int pageIndex = p / 10000;
            int position = p % 10000;
            System.out.println("File: " + pageIndex + ", Position: " + position);
        }
    }
}
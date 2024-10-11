package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpellChecker {

    // Creating a trie object to store the dictionary
    private Trie root;

    public SpellChecker() {
        root = new Trie();
    }

    // Add a word to the trie from the text file that we are generating
    private void insertWordInTrie(String word) {
        Trie current = root;
        for (char c : word.toLowerCase().toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new Trie());
        }
        current.isWord = true;
    }

    // Check if a word that we are searching for is in the trie that has been generated
    private boolean searchWordInTrie(String word) {
        Trie current = root;
        for (char c : word.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return false;
            }
            current = current.children.get(c);
        }
        return current.isWord;
    }

    // spellcheck implementation for a given string that we are adding as an input
    public String spellCheck(String input) {
        StringBuilder output = new StringBuilder();
        String[] words = input.split("\\s+");
        for (String word : words) {
            if (searchWordInTrie(word)) {
                output.append(word);
            } else {
                output.append("[").append(word).append("]");
            }
            output.append(" ");
        }
        return output.toString().trim();
    }

    // The trie that has been created from a list of words is built and stored as a Dictionary
    public void storeWordInDictionary(String[] words) {
        for (String word : words) {
            insertWordInTrie(word.toLowerCase());
        }
    }

    // To extract the words from a given input
    public String[] extractWordsFromText(String text) {
        return text.toLowerCase().split("[^a-zA-Z]+");
    }

    // To read the text files and build the dictionary trie
    public void readTextFilesAndBuildDictionary(String folderPath) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                // Using BufferedReader and FileReader to read the files
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                String text = sb.toString();
                // To extract words from Text file that is generated
                String[] words = extractWordsFromText(text);
                storeWordInDictionary(words);
            }
        }
    }

    // To print the whole trie dictionary that we have created
    public void printTrie() {
        printTrie(root, "");
    }

    // Storing the dictionary as a Map
    private void printTrie(Trie node, String prefix) {
        if (node.isWord) {
            System.out.println(prefix);
        }
        for (Map.Entry<Character, Trie> entry : node.children.entrySet()) {
            // Defining Key Value pair for Map DS
            char c = entry.getKey();
            Trie child = entry.getValue();
            printTrie(child, prefix + c);
        }
    }

    // To store the trie dictionary in the form of Hash Map 
    static class Trie {
        Map<Character, Trie> children;
        boolean isWord;

        public Trie() {
            children = new HashMap<>();
            isWord = false;
        }
    }
}

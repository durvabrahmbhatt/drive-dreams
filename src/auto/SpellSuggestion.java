
package auto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SpellSuggestion {

	// Creating a trie object to store the dictionary
	private Trie root;

	public SpellSuggestion(String directoryPath) {
		root = new Trie();
		buildDictionaryFromDirectory(directoryPath);
	}

	// Add a word to the trie
	private void insertWordInTrie(String word) {
		Trie current = root;
		for (char c : word.toCharArray()) {
			current = current.children.computeIfAbsent(c, k -> new Trie());
		}
		current.isWord = true;
	}

	// Build the dictionary from all text files in a given directory
	private void buildDictionaryFromDirectory(String directoryPath) {
		File directory = new File(directoryPath);
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					buildDictionaryFromFile(file.getPath());
				}
			}
		}
	}

	// Build the dictionary from a text file
	private void buildDictionaryFromFile(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split("\\s+");
				for (String word : words) {
					insertWordInTrie(word.toLowerCase());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Check if a word is in the trie
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

	// Calculate the edit distance between two strings
	private int editDistance(String s1, String s2) {
		int m = s1.length();
		int n = s2.length();
		int[][] dp = new int[m + 1][n + 1];
		for (int i = 0; i <= m; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= n; j++) {
			dp[0][j] = j;
		}
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
				dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
			}
		}
		return dp[m][n];
	}

	// Suggest a correction for a misspelled word
	public String suggestWord(String word) {
		Map<String, Integer> suggestions = new HashMap<>();
		List<String> possibleEdits = new ArrayList<>();
		possibleEdits.addAll(edits1(word));
		possibleEdits.addAll(edits2(word));
		for (String edit : possibleEdits) {
			if (searchWordInTrie(edit)) {
				suggestions.put(edit, editDistance(word, edit));
			}
		}
		String suggestion = "";
		int minDistance = Integer.MAX_VALUE;
		for (String key : suggestions.keySet()) {
			int distance = suggestions.get(key);
			if (distance < minDistance) {
				suggestion = key;
				minDistance = distance;
			}
		}
		return suggestion;
	}

	// Generate all possible edits at edit distance 1 from a word
	private List<String> edits1(String word) {
		List<String> edits = new ArrayList<>();
		for (int i = 0; i < word.length(); i++) {
			// deletion
			String deletion = word.substring(0, i) + word.substring(i + 1);
			edits.add(deletion);
			// transposition
			if (i < word.length() - 1) {
				String transposition = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i)
						+ word.substring(i + 2);
				edits.add(transposition);
			}
			// replacement
			for (char c = 'a'; c <= 'z'; c++) {
				String replacement = word.substring(0, i) + c + word.substring(i + 1);
				if (!replacement.equals(word)) {
					edits.add(replacement);
				}
			}
			// insertion
			for (char c = 'a'; c <= 'z'; c++) {
				String insertion = word.substring(0, i) + c + word.substring(i);
				edits.add(insertion);
			}
		}
		return edits;
	}

	// Generate all possible edits at edit distance 2 from a word
	private List<String> edits2(String word) {
		List<String> edits2 = new ArrayList<>();
		List<String> edits1 = edits1(word);
		for (String edit1 : edits1) {
			edits2.addAll(edits1(edit1));
		}
		return edits2;
	}

	// Public method to check the spelling of a word and suggest a correction if necessary
	public String checkSpelling(String word) {
		String lowerCaseWord = word.toLowerCase();
		if (searchWordInTrie(lowerCaseWord)) {
			return lowerCaseWord;
		} else {
			return suggestWord(lowerCaseWord);
		}
	}

	// Class representing a node in the trie
	private static class Trie {
		Map<Character, Trie> children;
		boolean isWord;

		Trie() {
			children = new HashMap<>();
			isWord = false;
		}
	}

	// Public method to check the spelling of a sentence and suggest corrections
	public String checkSpellingWithSpaces(String sentence) {
		String[] words = sentence.split("\\s+"); // Tokenize the input sentence into individual words
		StringBuilder correctedSentence = new StringBuilder();

		for (String word : words) {
			String correctedWord = checkSpelling(word); // Check spelling of each word
			correctedSentence.append(correctedWord).append(" ");
		}

		return correctedSentence.toString().trim();
	}

	public static void main(String[] args) {
		SpellSuggestion spellChecker = new SpellSuggestion("..//pages");
		String sentence = "thiis is a sentennce with mstake";
		String correctedSentence = spellChecker.checkSpellingWithSpaces(sentence);
		System.out.println("Corrected Sentence: " + correctedSentence);
	}
}

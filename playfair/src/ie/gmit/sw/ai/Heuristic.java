package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for providing a probability score for the decrypted
 * text to act as a heuristic.
 */
public class Heuristic {
	/*
	 * The size of the n-grams.
	 */
	private final static int N_GRAM_SIZE = 4;

	/*
	 * A map of n-grams and the total number of their occurrences.
	 */
	private Map<String, Integer> ngrams = new HashMap<String, Integer>();
	
	/*
	 * The total number of n-grams.
	 */
	private double totalNgrams;
	
	/**
	 * This constructor creates a new instance this class and parses a set of
	 * n-grams from the file at the given path.
	 * @param path containing the n-grams.
	 * @throws IOException if the file containing the n-grams does not exist.
	 */
	public Heuristic(String path) throws IOException {
		parseNgrams(path);
	}
	
	/**
	 * This method calculates the the score, h(n), for an encrypted string as a
	 * log probability.
	 * @param encryptedText to score.
	 * @return the score.
	 */
	public double logProbability(String encryptedText) {
		double score = 0;
		
		for (int i = 0; i < encryptedText.length() - N_GRAM_SIZE + 1; i++) {
			Integer count = ngrams.get(encryptedText.substring(i, i + N_GRAM_SIZE));
			
			if (count != null) {
				score += Math.log10(count / totalNgrams);
			}
		}
		
		return score;
	}
	
	/*
	 * This method parses a set of n-grams and their frequencies from a file to a map.
	 */
	private void parseNgrams(String path) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String line = null;
		int occurences;

		// Loop through each line in the file.
		while ((line = br.readLine()) != null) {
			String[] parts = line.split(" ");
			occurences = Integer.parseInt(parts[1]);
			ngrams.put(parts[0], occurences);
			totalNgrams += occurences;
		}
		
		// Tidy up resources when finished.
		br.close();
	}
}

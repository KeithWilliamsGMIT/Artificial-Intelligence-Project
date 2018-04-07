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
	
	/*
	 * This number indicates how far the sliding window moves to the right when calculating the log probability.
	 */
	private int slidingWindowSize;
	
	/**
	 * This constructor creates a new instance this class and parses a set of
	 * n-grams from the file at the given path.
	 * @param path containing the n-grams.
	 * @param slidingWindowSize indicates how many n-grams are sampled.
	 * @throws IOException if the file containing the n-grams does not exist.
	 */
	public Heuristic(String path, int slidingWindowSize) throws IOException {
		parseNgrams(path);
		
		if (slidingWindowSize == 0) {
			this.slidingWindowSize = N_GRAM_SIZE;
		} else {
			this.slidingWindowSize = slidingWindowSize;
		}
	}
	
	/**
	 * This method calculates the the score, h(n), for a decrypted string as a
	 * log probability.
	 * @param decryptedText to score.
	 * @return the score.
	 */
	public double logProbability(String decryptedText) {
		double score = 0;
		
		for (int i = 0; i <= decryptedText.length() - N_GRAM_SIZE; i += slidingWindowSize) {
			Integer count = ngrams.get(decryptedText.substring(i, i + N_GRAM_SIZE));
			
			if (count != null) {
				score += Math.log10(count);
			}
		}
		
		return score / Math.log10(totalNgrams);
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

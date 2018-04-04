package ie.gmit.sw.ai;

import java.util.List;

/**
 * This class is responsible for finding the key used to encrypt given text using
 * the simulated annealing algorithm. This algorithm starts with a random key
 * decrypts the text and scores the resulting plain text. The key will then be
 * changed in some way before the process is repeated. If the score, or heuristic
 * is better than the previous key, the new key is kept. However, to avoid local
 * optima, sometimes we will take a key with a lower score. Logically, this forms
 * a graph. This algorithm is suitable in this case as we do not know the goal
 * node.
 */
public class KeyFinder {
	
	/**
	 * This method finds a key to decrypt the given digraphs using the given
	 * heuristic and the simulated annealing algorithm.
	 * @param heuristic to measure the fitness of decrypted text.
	 * @param digraphs to decrypt.
	 * @return the generated key outputted from the simulated annealing algorithm.
	 */
	public Keyable find(Heuristic heuristic, List<char[]> digraphs) {
		// Generate a random 25 letter key called parent.
		Keyable parent = new PlayfairKey();
		Keyable child;
		
		/*
		 * Create a new decrypter, decrypt the text and score the fitness of the
		 * parent key.
		 */
		PlayfairDecrypter decrypter = new PlayfairDecrypter();
		
		double logProbabilityParent = heuristic.logProbability(decrypter.decrypt(parent, digraphs));
		double logProbabilityChild = 0;
		
		for (int temp = 10; temp > 0; temp--) {
			for (int transitions = 20000; transitions > 0; transitions--) {
				// Set child = shuffleKey(parent)
				child = new PlayfairKey(parent);
				child.shuffleKey();
				
				// Score the fitness of the key as logProbability(child)
				logProbabilityChild = heuristic.logProbability(decrypter.decrypt(child, digraphs));
				
				// Set delta = logProbability(child) - logProbability(parent)
				double delta = logProbabilityChild - logProbabilityParent;
				
				if (delta > 0) {
					// New key is better so set parent = child
					parent = child;
					logProbabilityParent = logProbabilityChild;
				} else if (delta != 0) {
					// New key is worse
					if (1.0 / Math.exp(-delta / temp) > Math.random()) {
						parent = child;
						logProbabilityParent = logProbabilityChild;
					}
				}
			}
		}
		
		// Return the final key.
		return parent;
	}
}

package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for generating and manipulating keys.
 */
public class PlayfairKey implements Keyable {
	/*
	 * The letter 'J' is missing from the Playfair cypher.
	 */
	private final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
	
	/*
	 * This is the number of rows and columns in the matrix.
	 */
	private final int MATRIX_SIZE = 5;
	
	/*
	 * This stores the current state of the key. An ArrayList<> was used as
	 * the order of the elements in the key are important and need to be
	 * accessed using an indices.
	 */
	private List<Character> key = new ArrayList<Character>();
	
	/**
	 * This implicit default constructor generates a random key of unique
	 * letters.
	 */
	public PlayfairKey() {
		for (char c : ALPHABET.toCharArray()) {
			key.add(c);
		}
		
		Collections.shuffle(key);
	}
	
	/**
	 * This constructor generates a new key from another key.
	 */
	public PlayfairKey(Keyable key) {
		this.key = new ArrayList<Character>(key.getKey());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Character> getKey() {
		return key;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void shuffleKey() {
		// Generate a random number between 0 and 100 to determine how to
		// shuffle the key.
		double frequency = Math.random() * 100;
		
		if (frequency <= 90) {
			swapSingleLetter();
		} else if (frequency <= 92) {
			swapRandomRows();
		} else if (frequency <= 94) {
			swapRandomColumns();
		} else if (frequency <= 96) {
			flipAllRows();
		} else if (frequency <= 98) {
			flipAllColumns();
		} else {
			reverseWholeKey();
		}
	}
	
	/*
	 * This method picks two random letters in the key and swaps them.
	 */
	private void swapSingleLetter() {
		// Get two random indices from the list.
		int i1 = (int) (Math.random() * key.size());
		int i2;
		
		do {
			i2 = (int) (Math.random() * key.size());
		} while (i1 == i2);
		
		// Swap the two elements at the given indices.
		swapElements(i1, i2);
	}
	
	/*
	 * This method picks two random rows and swaps them.
	 */
	private void swapRandomRows() {
		// Get two random numbers between 0 and 4.
		// These will be the rows to swap.
		int r1 = (int) (Math.random() * MATRIX_SIZE);
		int r2;
		
		do {
			r2 = (int) (Math.random() * MATRIX_SIZE);
		} while (r1 == r2);
		
		// Swap the two rows.
		for (int i = 0; i < MATRIX_SIZE; i++) {
			int i1 = (r1 * MATRIX_SIZE) + i;
			int i2 = (r2 * MATRIX_SIZE) + i;
			
			swapElements(i1, i2);
		}
	}
	
	/*
	 * This method picks two random columns and swaps them.
	 */
	private void swapRandomColumns() {
		// Get two random numbers between 0 and 4.
		// These will be the columns to swap.
		int c1 = (int) (Math.random() * MATRIX_SIZE);	
		int c2;
		
		do {
			c2 = (int) (Math.random() * MATRIX_SIZE);
		} while (c1 == c2);
		
		// Swap the two columns.
		for (int i = 0; i < MATRIX_SIZE; i++) {
			int i1 = c1 + (MATRIX_SIZE * i);
			int i2 = c2 + (MATRIX_SIZE * i);

			swapElements(i1, i2);
		}
	}
	
	/*
	 * This method flips all rows. Therefore, the first becomes the last,
	 * The second becomes the second last and so on.
	 */
	private void flipAllRows() {
		for (int row = 0; row < (MATRIX_SIZE / 2); row++) {
			for (int i = 0; i < MATRIX_SIZE; i++) {
				int i1 = (row * MATRIX_SIZE) + i;
				int i2 = ((MATRIX_SIZE - 1 - row) * MATRIX_SIZE) + i;
				
				swapElements(i1, i2);
			}
		}
	}
	
	/*
	 * This method flips all columns. Therefore, the first becomes the last,
	 * The second becomes the second last and so on.
	 */
	private void flipAllColumns() {
		for (int col = 0; col < (MATRIX_SIZE / 2); col++) {
			for (int i = 0; i < MATRIX_SIZE; i++) {
				int i1 = col + (MATRIX_SIZE * i);
				int i2 = (MATRIX_SIZE - 1 - col) + (MATRIX_SIZE * i);
				
				swapElements(i1, i2);
			}
		}
	}
	
	/*
	 * This method reverses the whole key.
	 */
	private void reverseWholeKey() {
		Collections.reverse(key);
	}
	
	/*
	 * This method swaps the two elements at the given indices.
	 */
	private void swapElements(int i1, int i2) {
		char c = key.get(i1);
		key.set(i1, key.get(i2));
		key.set(i2, c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Key [key=" + key + "]";
	}
}

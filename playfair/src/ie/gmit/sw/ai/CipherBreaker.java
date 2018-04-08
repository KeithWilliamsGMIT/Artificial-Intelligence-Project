package ie.gmit.sw.ai;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This is a runner class to start the application.
 */
public class CipherBreaker {
	
	/*
	 * Scanner used to accept input from the user.
	 */
	private Scanner scanner;

	/**
	 * This is the entry point to the application.
	 * @param args
	 */
	public static void main(String[] args) {
		CipherBreaker breaker = new CipherBreaker();
		breaker.menu();
	}
	
	public void menu() {
		// Create a scanner to read input.
		scanner = new Scanner(System.in);
		
		// Get a collection of digraphs to decrypt with a key.
		List<char[]> digraphs = getDigraphs();

		// Get a heuristic object that will be used to score decrypted text
		Heuristic heuristic = getHeuristic();

		// Prompt the user to enter temperature and number of transitions.
		System.out.println("Please enter the temperature");
		int temperature = scanner.nextInt();

		System.out.println("Please enter the number of transitions");
		int transitions = scanner.nextInt();
		
		// Ask the user if the want to use multiple threads
		boolean threaded = getYesNoOption("Do you want to run multiple keys in different threads? Y/N");
		
		// Decrypt the text either using multiple threads or in the same thread.
		long begin = 0;
		Keyable key = null;
		
		if (threaded) {
			System.out.println("Please enter the number of keys you wish to start decrypting with");
			int numberOfThreads = scanner.nextInt();
			scanner.nextLine();
			
			begin = System.nanoTime();
			System.out.println("Decrypting text. Please wait...");
			
			try {
				key = ThreadPoolManager.getInstance().start(heuristic, digraphs, temperature, transitions, numberOfThreads);
			} catch (Exception e) {
				System.out.println("An error occurred: " + e.getMessage());
			}
		} else {
			// Prompt the user to enter a number indicating if debugging information should be outputted.
			boolean debug = getYesNoOption("Should additional debugging information be outputted? Y/N");
	
			begin = System.nanoTime();
			System.out.println("Decrypting text. Please wait...");
	
			KeyFinder finder = new KeyFinder();
			key = finder.find(heuristic, digraphs, temperature, transitions, debug);
		}

		// Decrypt the digraphs with the key.
		PlayfairDecrypter decrypter = new PlayfairDecrypter();
		String decryptedText = decrypter.decrypt(key, digraphs);

		System.out.println(key);
		System.out.println("DECRYPTED TEXT: " + decryptedText);

		long end = System.nanoTime();

		System.out.println("DECRYPTED IN " + TimeUnit.NANOSECONDS.toSeconds(end - begin) + " secs");
		
		// Write decrypted text to a file.
		writeTextToFile(decryptedText);

		// Tidy up resources when finished.
		scanner.close();
	}
	
	/*
	 * This method asks the user to choose yes or no and then converts that to a boolean and returns it.
	 */
	private boolean getYesNoOption(String message) {
		char ynOption = 0;

		do {
			System.out.println(message);
			ynOption = scanner.next().toLowerCase().toCharArray()[0];
			scanner.nextLine();
		} while(ynOption != 'y' && ynOption != 'n');
		
		return (ynOption == 'y');
	}
	
	/*
	 * Get a collection of digraphs from a file.
	 */
	private List<char[]> getDigraphs() {
		boolean isParsed = false;
		DigraphParser parser = new DigraphParser();
		List<char[]> digraphs = null;

		do {
			// Prompt the user to enter the path to the file containing the encrypted text.
			System.out.println("Please enter the path for the encrypted file");
			String path = scanner.nextLine();

			// Parse the encrypted file into digraphs.
			try {
				digraphs = parser.parse(path);
				isParsed = true;
				System.out.println(String.format("Parsed %d digraphs", digraphs.size()));
			} catch (IOException e) {
				System.out.println("Unable to parse file! Please try again...");
			}
		} while(!isParsed);
		
		return digraphs;
	}
	
	/*
	 * Initialise and return a Heuristic object.
	 */
	private Heuristic getHeuristic() {
		Heuristic heuristic = null;
		int slidingWindowSize = 0;
		boolean isParsed = false;

		do {
			// Prompt the user to choose how much to move the sliding window when calculating the log probability.
			System.out.println("Please choose how far to move the sliding window when calculating the log probability");
			System.out.println("1) Move it the same number of characters as the n-gram size at a time (Better for larger samples - Quicker)");
			System.out.println("2) Move it one character at a time (Better for smaller samples - More accurate)");
			slidingWindowSize = scanner.nextInt() - 1;
			scanner.nextLine();
		} while(slidingWindowSize != 0 && slidingWindowSize != 1);

		do {
			// Prompt the user to enter the path to the file containing the n-grams.
			System.out.println("Please enter the path for the n-grams file");
			String path = scanner.nextLine();

			try {
				heuristic = new Heuristic(path, slidingWindowSize);
				isParsed = true;
				System.out.println("Successfully parsed file.");
			} catch (IOException e) {
				System.out.println("Unable to parse file! Please try again...");
			}
		} while(!isParsed);
		
		return heuristic;
	}
	
	/*
	 * Write a string of text to a file specified by the user.
	 */
	private void writeTextToFile(String text) {
		boolean isValid = false;
		DecryptedTextWriter writer = new DecryptedTextWriter();

		do {
			// Prompt the user to enter the path to the file to output the decrypted text to.
			System.out.println("Please enter the path of the file to output the decrypted text to.");
			String path = scanner.nextLine();

			// Parse the encrypted file into digraphs.
			try {
				writer.write(text, path);
				isValid = true;
				System.out.println("Successfull outputted decrypted text to file.");
			} catch (IOException e) {
				System.out.println("An error occurred: " + e.getMessage());
			}
		} while(!isValid);
	}
}

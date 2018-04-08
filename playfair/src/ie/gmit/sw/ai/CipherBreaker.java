package ie.gmit.sw.ai;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This is a runner class to start the application.
 */
public class CipherBreaker {
	
	/**
	 * This is the entrypoint to the application.
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a scanner to read input.
		Scanner scanner = new Scanner(System.in);
		
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
		
		int slidingWindowSize = 0;
		
		do {
			// Prompt the user to choose how much to move the sliding window when calculating the log probability.
			System.out.println("Please choose how far to move the sliding window when calculating the log probability");
			System.out.println("1) Move it the same number of characters as the n-gram size at a time (Better for larger samples - Quicker)");
			System.out.println("2) Move it one character at a time (Better for smaller samples - More accurate)");
			slidingWindowSize = scanner.nextInt() - 1;
			scanner.nextLine();
		} while(slidingWindowSize != 0 && slidingWindowSize != 1);
		
		Heuristic heuristic = null;
		isParsed = false;
		
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
		
		// Prompt the user to enter temperature and number of transitions.
		System.out.println("Please enter the temperature");
		int temperature = scanner.nextInt();
		
		System.out.println("Please enter the number of transitions");
		int transitions = scanner.nextInt();
		
		char ynOption = 0;
		boolean debug = true;
		
		do {
			// Prompt the user to enter a number indicating if debugging information should be outputted.
			System.out.println("Should additional debugging information be outputted? Y/N");
			ynOption = scanner.next().toLowerCase().toCharArray()[0];
			scanner.nextLine();
		} while(ynOption != 'y' && ynOption != 'n');
		
		if (ynOption == 'n') {
			debug = false;
		}
		
		long begin = System.nanoTime();
		
		// Find the key to decrypt the text.
		System.out.println("Decrypting text. Please wait...");
		
		KeyFinder finder = new KeyFinder();
		Keyable key = finder.find(heuristic, digraphs, temperature, transitions, debug);
		
		PlayfairDecrypter decrypter = new PlayfairDecrypter();
		String decryptedText = decrypter.decrypt(key, digraphs);
		
		System.out.println(key);
		System.out.println("DECRYPTED TEXT: " + decryptedText);
		
		long end = System.nanoTime();
		
		System.out.println("DECRYPTED IN " + TimeUnit.NANOSECONDS.toSeconds(end - begin) + " secs");
		
		boolean isValid = false;
		DecryptedTextWriter writer = new DecryptedTextWriter();
		
		do {
			// Prompt the user to enter the path to the file to output the decrypted text to.
			System.out.println("Please enter the path of the file to output the decrypted text to.");
			String path = scanner.nextLine();
			
			// Parse the encrypted file into digraphs.
			try {
				writer.write(decryptedText, path);
				isValid = true;
				System.out.println("Successfull outputted decrypted text to file.");
			} catch (IOException e) {
				System.out.println("An error occurred: " + e.getMessage());
			}
		} while(!isValid);
		
		// Tidy up resources when finished.
		scanner.close();
	}
}

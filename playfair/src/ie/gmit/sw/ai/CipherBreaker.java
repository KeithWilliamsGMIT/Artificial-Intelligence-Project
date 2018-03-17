package ie.gmit.sw.ai;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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
		
		// Create a new random key.
		Keyable key = new PlayfairKey();
		
		// Create a new decrypter.
		PlayfairDecrypter decrypter = new PlayfairDecrypter();
		
		Heuristic heuristic = null;
		isParsed = false;
		
		do {
			// Prompt the user to enter the path to the file containing the n-grams.
			System.out.println("Please enter the path for the n-grams file");
			String path = scanner.nextLine();
			
			try {
				heuristic = new Heuristic(path);
				isParsed = true;
				System.out.println("Successfully parsed file.");
			} catch (IOException e) {
				System.out.println("Unable to parse file! Please try again...");
			}
		} while(!isParsed);
				
		// Tidy up resources when finished.
		scanner.close();
	}
}

package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is responsible for parsing a collection of digraphs from a file
 * of encrypted text.
 */
public class DigraphParser {

	/**
	 * This method parses a file at the given path to a collection of digraphs.
	 * @param path to the file.
	 * @return a list of digrpahs.
	 * @throws IOException if the specified file does not exist or cannot be opened.
	 */
	public List<char[]> parse(String path) throws IOException {
		List<char[]> digraphs = new LinkedList<char[]>();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		int character, index = 0;
		char[] digraph = new char[2];

		// Loop through each character in the file.
		while ((character = br.read()) != -1) {
			digraph[index] = Character.toUpperCase((char) character);
			index++;
			
			if (index == 2) {
				digraphs.add(digraph);
				digraph = new char[2];
				index = 0;
			}
		}
		
		// Tidy up resources when finished.
		br.close();
		
		return digraphs;
	}
}

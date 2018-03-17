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
	
	/*
	 * A list of digraphs that have been parsed.
	 */
	private List<char[]> digraphs;
	
	/**
	 * This method returns a list of parsed digraphs.
	 * @return a list of parsed digraphs.
	 */
	public List<char[]> getDigraphs() {
		return digraphs;
	}

	/**
	 * /**
	 * This method parses a file at the given path to a collection of digraphs.
	 * @param path to the file.
	 * @throws IOException if the specified file does not exist or cannot be opened.
	 */
	public void parse(String path) throws IOException {
		digraphs = new LinkedList<char[]>();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		int character, index = 0;
		char[] digraph = new char[2];

		// Loop through each character in the file.
		while ((character = br.read()) != -1) {
			digraph[index] = (char) character;
			index++;
			
			if (index == 2) {
				digraphs.add(digraph);
				index = 0;
			}
		}
		
		// Tidy up resources when finished.
		br.close();
	}
}

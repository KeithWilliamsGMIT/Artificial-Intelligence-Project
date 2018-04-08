package ie.gmit.sw.ai;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is responsible for outputting the decrypted text to a given file.
 */
public class DecryptedTextWriter {
	
	/**
	 * This method writes text out to a file.
	 * @param text to write out.
	 * @param filename of the file to write the text too.
	 * @throws IOException 
	 */
	public void write(String text, String filename) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(text);
	    writer.close();
	}
}

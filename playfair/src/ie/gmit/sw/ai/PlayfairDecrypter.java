package ie.gmit.sw.ai;

import java.util.List;

/**
 * This class is responsible for decrypting playfair entrypted text. Therefore,
 * this class requires a playfair key and a set of digraphs.
 */
public class PlayfairDecrypter {
	
	/**
	 * This method decrypts a collection of digraphs using a given key and returns
	 * the decrytped text.
	 * @param key to decrypt the digraphs with.
	 * @param digraphs to decrypt.
	 * @return the decrypted text.
	 */
	public String decrypt(Keyable key, List<char[]> digraphs) {
		StringBuilder sb = new StringBuilder();
		int i1, i2, row1, row2, col1, col2, r1, r2;
		
		for (char[] digraph : digraphs) {
			i1 = key.getKey().indexOf(digraph[0]);
			i2 = key.getKey().indexOf(digraph[1]);
			
			if ((i1 / 5) == (i2 / 5)) {
				// If the rows are the same...
				col1 = ((i1 % 5) == 0) ? 4 : (i1 % 5) - 1;
				col2 = ((i2 % 5) == 0) ? 4 : (i2 % 5) - 1;
				
				r1 = ((i1 / 5) * 5) + (col1);
				r2 = ((i2 / 5) * 5) + (col2);
			} else if ((i1 % 5) == (i2 % 5)) {	
				// If the columns are the same...
				row1 = ((i1 / 5) == 0) ? 4 * 5 : ((i1 / 5) - 1) * 5;
				row2 = ((i2 / 5) == 0) ? 4 * 5 : ((i2 / 5) - 1) * 5;
				
				r1 = (row1) + (i2 % 5);
				r2 = (row2) + (i1 % 5);
			} else {
				r1 = ((i1 / 5) * 5) + (i2 % 5);
				r2 = ((i2 / 5) * 5) + (i1 % 5);
			}
			
			// Append the decryted digraphs to the decrypted string.
			sb.append(key.getKey().get(r1));
			sb.append(key.getKey().get(r2));
		}
		
		return sb.toString();
	}
}

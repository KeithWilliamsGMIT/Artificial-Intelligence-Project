package ie.gmit.sw.ai;

import java.util.List;

/**
 * This interface should be implemented by any class that plays the role of a
 * key in a cypher.
 */
public interface Keyable {
	
	/**
	 * This method returns the key.
	 * @return the key.
	 */
	public List<Character> getKey();
	
	/**
	 * This method randomly shuffles the key. The key can be potentially
	 * shuffled in several different ways.
	 */
	public void shuffleKey();

}
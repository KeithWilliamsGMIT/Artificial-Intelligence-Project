package ie.gmit.sw.ai;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * This class is responsible for decrypting text on another thread.
 */
public class KeyFinderCallable implements Callable<Keyable> {

	private Heuristic heuristic;
	private List<char[]> digraphs;
	private int initialTemp;
	private int initialTransitions;
	
	/**
	 * Create a new instance of this class that will decrypt text in a separate thread.
	 * @param heuristic to measure the fitness of decrypted text.
	 * @param digraphs to decrypt.
	 * @param initialTemp is the starting temperature.
	 * @param initialTransitions is the number of transitions that occur at each iteration.
	 */
	public KeyFinderCallable(Heuristic heuristic, List<char[]> digraphs, int initialTemp, int initialTransitions) {
		this.heuristic = heuristic;
		this.digraphs = digraphs;
		this.initialTemp = initialTemp;
		this.initialTransitions = initialTransitions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Keyable call() throws Exception {
		KeyFinder finder = new KeyFinder();
		return finder.find(heuristic, digraphs, initialTemp, initialTransitions, false);
	}
	
}

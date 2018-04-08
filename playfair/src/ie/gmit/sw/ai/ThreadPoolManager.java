package ie.gmit.sw.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class is responsible for decrypting text using different keys in different threads and returning the best
 * key that was found. This is a singleton as there should only ever be one thread pool.
 */
public class ThreadPoolManager {
	/*
	 * The max number of threads in the pool.
	 */
	private final static int MAX_THREADS = 10;
	
	/*
	 * Own instance of the ThreadPoolManager class.
	 */
	private static ThreadPoolManager instance;
	
	/*
	 * Executor service that will execute the threads.
	 */
	private ExecutorService executor;
	
	/*
	 * Private constructor to prevent more than one instance of this object from being created.
	 */
	private ThreadPoolManager() { }
	
	/**
	 * Get the single instance of the {@link ie.gmit.sw.ai.ThreadPoolManager} class.
	 * @return the instance of the class.
	 */
	public static ThreadPoolManager getInstance() {
		if (instance == null) {
			instance = new ThreadPoolManager();
		}
		
		return instance;
	}
	
	/**
	 * This method starts a given number of threads to decrypt the same text, or digraphs.
	 * @param heuristic to measure the fitness of decrypted text.
	 * @param digraphs to decrypt.
	 * @param initialTemp is the starting temperature.
	 * @param initialTransitions is the number of transitions that occur at each iteration.
	 * @param numberOfThreads to start.
	 * @return The best key that was found out of all the threads.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Keyable start(Heuristic heuristic, List<char[]> digraphs, int initialTemperature, int initialTransitions, int numberOfThreads) throws InterruptedException, ExecutionException {
		executor = Executors.newFixedThreadPool(MAX_THREADS);
		
		List<Future<Keyable>> list = new LinkedList<Future<Keyable>>();
		Callable<Keyable> callable = new KeyFinderCallable(heuristic, digraphs, initialTemperature, initialTransitions);
		
		PlayfairDecrypter decrypter = new PlayfairDecrypter();
		
		Keyable best = null;
		double bestFitness = 0;
		
		// Start the threads.
		for (int i = 0; i < numberOfThreads; i++) {
			Future<Keyable> future = executor.submit(callable);
			list.add(future);
		}
		
		// Wait for the futures to have a value and save the best key.
		for (Future<Keyable> future : list) {
			double fitness = heuristic.logProbability(decrypter.decrypt(future.get(), digraphs));
			
			if (fitness > bestFitness) {
				bestFitness = fitness;
				best = future.get();
			}
			
			System.out.println("Score of " + fitness + " with " + future.get());
		}
		
		executor.shutdown();
		
		return best;
	}
}

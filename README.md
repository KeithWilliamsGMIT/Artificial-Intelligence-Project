# Artificial Intelligence Project
This is my 4th year project for the artificial intelligence module in college. For this project I was required to develop a Java application which uses the simulated annealing algorithm to break a Playfair cipher.

### Minimum Requirements
1. A menu-driven command line UI that enables a cipher-text source to be specified (a file or URL) and an output destination file for decrypted plain-text.
2. Decrypt cipher-text with a simulated annealing algorithm that uses a log-probability and n-gram statistics as a heuristic evaluation function.

### Running the Application
The following are the steps to run this application:

+ Clone this repository if you have not done so already.

   ```
   git clone https://github.com/KeithWilliamsGMIT/Artificial-Intelligence-Project.git
   ```

+ In a terminal navigate to the root directory of the repository and use the following command to compile the JAR.

   ```
   jar –cf playfair.jar ie/gmit/sw/ai/*.class
   ```

+ Once compiled, run the JAR with the below command.

   ```
   java –cp ./playfair.jar ie.gmit.sw.ai.CipherBreaker
   ```

### Project Breakdown
This application should find a key that can decrypt text which was encrypted using the Playfair cypher. The playfair cypher uses a symmetric key ehich means the same key is used to both encrypt and decrypt the text. The key consists of 25 unique letters, represented as a 5x5 matrix. The simulated annealing algorithm is used as there is no prior knowledge of the state space, or the correct key, and the stated space is too large to use a brute force algorithm as there are 25! possible key combinations.

This application works by first prompting the user for two files. The first is the file to decrypt and the second is a file containing n-grams and the number of occurences in a sample. The encrypted text is split into a collection of digraphs, or groups of two letters. These digraphs are then decrypted using a randomly generated key from a given alphabet (which does not include the letter 'J' in this case). The decrypted text is then scored using a heuristic. This heuristic breaks the decrypted text into a collection of n-grams. Using the file entered by the user at the start, the total number of occurences for each n-gram in the decrypted text are added together and divided by the total sample size.

The simulated annealing algorithm works by generating a child key by making a change to the original parent key. This change is more likely to small, meaning swapping two single letters, and less likely to make a big change, meaning swapping rows or columns, flipping rows or columns or reversing the whole key. Once changed the difference between the fitness of the child and parent keys are calculated. If the child key has a better fitness score, or f(n), than the parent, the child key becomes the new parent. Even if the child fitness score is worse than the parent it still has a chance to become the new parent key. However, the probability of this decreases as the temperature decreases and the worse the fitness score the less likely a child is to become the parent. The algorithm used to calculate this probability is called the acceptance probability function. It is also important to note that every time a change is made to the key it is logically the same as traversing the edge between two adjacent nodes in a graph.

### Additional Features
The following features were added to the project.
+ The most important additional feature in this project is the ability to create a number of threads each with a randomly generated Playfair key. The simulated annealing algorithm is executed in different threads and the best key is choosen. This helps reduce the impact of a bad initial key being generated. The more threads that are created the greater the likelyhood of finding a good key. Of course this has the downside of possibly taking much longer depending on how many threads the user chose to create. This feature uses an ExecutorService, Callables and Futures.
+ The user can input the number of characters the sliding window can move when calculating the log probability of decrypted plain-text. For smaller samples it is preferred to move one character at a time as more n-grams are sampled. For larger samples it is preferred to move n characters where n is the size of the n-grams as it is faster.
+ The user can choose to run the application in debug mode which outputs additional information from the simulated annealing algorithm to the screen. For example, the fitness of the key after each iteration of the temperature and how many keys were generated in total. The goal of this feature is to give the user a better understanding of how the simulated annealing algorithm works. When the debugging option is not set the application simply outputs the decrypted text along with the key used to decrypt it. This option is only available when the user chooses not to run multiple threads. The total time taken to execute the simulated annealing algorithm is also given.

### Conclusion
This was my first attempt at using a heuristic informed search. I found it to be an interesting problem with several aspect. Finding the good values for the temperature and transitions seemed to be trial and error. This lead the simulated annealing algorithm getting stuck at a local optima early on, resulting in the algorithm behaving like a standard hill climbing algorithm. Despite looking I could not find a good guideline for consistantly chosing good values for these variables. However, I was able to decrypt a number of encrypted text files to readable text. The below table describes the parameters that worked for me to decrypt given files.

| File Description               | Digraphs |Temperature | Transitions | Window Distance |
|--------------------------------|----------|------------|-------------|-----------------|
| Hobbit (First 2000 characters) | 1000     | 10         | 50000       | 4               |
| Hobbit                         | 18371    | 15         | 30000       | 4               |
| Hints                          | 371      | 10         | 50000       | 1               |

I realised the following from running these tests:
1. The temperature and number of transitions can have a big impact on the final result.
2. The heuristic is crucial for converging on the correct key.
3. The initial key can influence the result.

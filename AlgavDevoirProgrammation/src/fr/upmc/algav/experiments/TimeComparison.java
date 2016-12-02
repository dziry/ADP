package fr.upmc.algav.experiments;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.stream.Stream;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.GraphReader;

public class TimeComparison {

    // Eclipse IDE
	private final static String DIRECTORY_PATH = "files/Shakespeare";
	// IntelliJ IDE
    //private final static String DIRECTORY_PATH = "AlgavDevoirProgrammation/files/Shakespeare";

    //private final static String RESULTS_PATH = "results/results.txt";
    // Alternate path for IntelliJ
    private final static String RESULTS_PATH = "AlgavDevoirProgrammation/results/results.txt";
    private final static String RESULTS_ENCODING = "utf-8";

    private static final String SEPARATOR_LINE = "------------------------------------------------------------------";

    private static Writer resultsWriter;
    private static int numberOfFiles;
	private static HashSet<String> shakespeareWords;

	private static ITrie hybridTrie;
	private static ITrie balancedHybridTrie;
    private static ITrie patriciaTrie;
	
	private static double insertionTimeInHybridTrie = 0;
	private static double insertionTimeInBalancedHybridTrie = 0;
	private static double insertionTimeInPatriciaTrie = 0;
	private static double searchWordsTimeInHybridTrie = 0;
	private static double searchWordsTimeInBalancedHybridTrie = 0;
	private static double searchWordsTimeInPatriciaTrie = 0;
	private static double prefixCountTimeInHybridTrie = 0;
	private static double prefixCountTimeInBalancedHybridTrie = 0;
	private static double prefixCountTimeInPatriciaTrie = 0;
	private static double removeWordsTimeInHybridTrie = 0;
	private static double removeWordsTimeInBalancedHybridTrie = 0;
	private static double removeWordsTimeInPatriciaTrie = 0;

	public static void main(String[] args) {
        initWriter();
        initWordsSet();

        printTestBegin();

        constructTries();

        printTestEnd();

        /*for (int attempt = 0; attempt < NUMBER_OF_ATTEMPTS; attempt++) {
			runTests(attempt);
		}

		printAverageResults();	*/
    }

    private static void initWriter() {
        resultsWriter = null;

        try {
            resultsWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(RESULTS_PATH), RESULTS_ENCODING));
        } catch (IOException ex) {
            System.err.println("Error while initializing results writer!");
            ex.printStackTrace();
        }
    }

    private static void printTestBegin() {
        String begin =  "BEGIN OF TIME COMPARISON\n" +
                        SEPARATOR_LINE + "\n" +
                        SEPARATOR_LINE + "\n" +
                        SEPARATOR_LINE + "\n\n";

        System.out.println(begin);
        writeToResultsFile(begin);


        String totalOfWords = "Total of distinct words in Shakespeare's works: " + shakespeareWords.size() + "\n\n";

        System.out.println(totalOfWords);
        writeToResultsFile(totalOfWords);
    }

    private static void printTestEnd() {
        String end =    "\n" + SEPARATOR_LINE + "\n" +
                        SEPARATOR_LINE + "\n" +
                        SEPARATOR_LINE + "\n" +
                        "END OF TIME COMPARISON";

        System.out.println(end);
        writeToResultsFile(end);

        try {
            resultsWriter.close();
        } catch (Exception ex) {
            System.err.println("Error while closing results file writer!");
            ex.printStackTrace();
        }
    }

    private static void writeToResultsFile(String stringToWrite) {
        try {
            resultsWriter.write(stringToWrite);
        } catch (Exception e) {
            System.err.println("Error while writing to results file!");
            e.printStackTrace();
        }
    }

    private static void initWordsSet() {
        shakespeareWords = new HashSet<>();
        numberOfFiles = 0;

		try(Stream<Path> paths = Files.walk(Paths.get(DIRECTORY_PATH))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					numberOfFiles++;

                    GraphReader graphReader = new GraphReader(filePath.toString());
                    shakespeareWords.addAll(graphReader.read());
				}
			});
		} catch (Exception e) {
			System.err.println("Error while reading the files for all works of Shakespeare: ");
			e.printStackTrace();
		}
	}

    private static void constructTries() {
        hybridTrie = new HybridTrie();
        balancedHybridTrie = new HybridTrie();
        patriciaTrie = new PatriciaTrie(new Alphabet());

        double hybridTrieDuration = getDurationForTrieConstruction(hybridTrie);
        double balancedHybridTrieDuration = getDurationForTrieConstruction(balancedHybridTrie);
        double patriciaTrieDuration = getDurationForTrieConstruction(patriciaTrie);

        String times =  "Construction of tries:\n" +
                        SEPARATOR_LINE + "\n" +
                        "Total time for Hybrid Trie: " + hybridTrieDuration + " ms\n" +
                        "Total time for Balanced Hybrid Trie: " + balancedHybridTrieDuration + " ms\n" +
                        "Total time for Patricia Trie: " + patriciaTrieDuration + " ms\n\n";

        System.out.println(times);
        writeToResultsFile(times);
    }

    private static double getDurationForTrieConstruction(ITrie trie) {
        Instant start = null;
        Instant end = null;

        start = Instant.now();
        trie.insert(shakespeareWords);
        end = Instant.now();

        return (double) Duration.between(start, end).toMillis();
    }
	
	/*private static void runTests(int attempt) {
		insertExperiments();

		// getting ready for the next three experiments
		final int indexOfFile = 6;		

		if (attempt == 0) {
			hybridTrie.removeAll();
			balancedHybridTrie.removeAll();
			patriciaTrie.removeAll();
			insertPartiallyOrientedListsInTrie(indexOfFile);
			printDataStatistics();			
		}

		searchExperiments(indexOfFile);
		prefixExperiments(indexOfFile);
		removeExperiments(indexOfFile);
	}

	private static void insertExperiments() {
		for (int index = 0; index < numberOfFiles; index++) {			
			insertionTimeInHybridTrie += getInsertionTime(hybridTrie, index);
			insertionTimeInBalancedHybridTrie += getBalancedInsertionTime((HybridTrie) balancedHybridTrie, index);
			insertionTimeInPatriciaTrie += getInsertionTime(patriciaTrie, index);
		}
	}
	
	private static void searchExperiments(int indexOfFile) {
		int numberOfWordsToSearch = 500;
		searchWordsTimeInHybridTrie += getSearchWordsTime(hybridTrie, indexOfFile, numberOfWordsToSearch);
		searchWordsTimeInBalancedHybridTrie += getSearchWordsTime(balancedHybridTrie, indexOfFile, numberOfWordsToSearch);
		searchWordsTimeInPatriciaTrie += getSearchWordsTime(patriciaTrie, indexOfFile, numberOfWordsToSearch);
	}

	private static void prefixExperiments(int indexOfFile) {
		int numberOfWordsToCountPrefix = 500;
		prefixCountTimeInHybridTrie += getCountPrefixTime(hybridTrie, indexOfFile, numberOfWordsToCountPrefix);
		prefixCountTimeInBalancedHybridTrie += getCountPrefixTime(balancedHybridTrie, indexOfFile, numberOfWordsToCountPrefix);
		prefixCountTimeInPatriciaTrie += getCountPrefixTime(patriciaTrie, indexOfFile, numberOfWordsToCountPrefix);
	}

	private static void removeExperiments(int indexOfFile) {
		int numberOfWordsToRemove = 500;
		removeWordsTimeInHybridTrie += getRemoveWordsTime(hybridTrie, indexOfFile, numberOfWordsToRemove);
		removeWordsTimeInBalancedHybridTrie += getRemoveWordsTime(balancedHybridTrie, indexOfFile, numberOfWordsToRemove);
		removeWordsTimeInPatriciaTrie += getRemoveWordsTime(patriciaTrie, indexOfFile, numberOfWordsToRemove);
	}

	private static double getInsertionTime(ITrie trie, int fileIndex) {
		Instant start = null;
		Instant end = null;

		trie.removeAll();
		start = Instant.now();
		trie.insert(shakespeareWords.get(fileIndex));
		end = Instant.now();

    	return (double) Duration.between(start, end).toNanos();
	}
	
	private static double getBalancedInsertionTime(HybridTrie trie, int fileIndex) {
		Instant start = null;
		Instant end = null;

		trie.removeAll();
		start = Instant.now();
		trie.insertBalanced(shakespeareWords.get(fileIndex));
		end = Instant.now();

    	return (double) Duration.between(start, end).toNanos();
	}

	private static void printAverageResults() {		
		double averageInsertionTimeInHybridTrie = insertionTimeInHybridTrie / NUMBER_OF_ATTEMPTS;
		double averageInsertionTimeInBalancedHybridTrie = insertionTimeInBalancedHybridTrie / NUMBER_OF_ATTEMPTS;
		double averageInsertionTimeInPatriciaTrie = insertionTimeInPatriciaTrie / NUMBER_OF_ATTEMPTS;
		double averageSearchWordsTimeInHybridTrie = searchWordsTimeInHybridTrie / NUMBER_OF_ATTEMPTS;
		double averageSearchWordsTimeInBalancedHybridTrie = searchWordsTimeInBalancedHybridTrie / NUMBER_OF_ATTEMPTS;
		double averageSearchWordsTimeInPatriciaTrie = searchWordsTimeInPatriciaTrie / NUMBER_OF_ATTEMPTS;
		double averagePrefixCountTimeInHybridTrie = prefixCountTimeInHybridTrie / NUMBER_OF_ATTEMPTS;
		double averagePrefixCountTimeInBalancedHybridTrie = prefixCountTimeInBalancedHybridTrie / NUMBER_OF_ATTEMPTS;
		double averagePrefixCountTimeInPatriciaTrie = prefixCountTimeInPatriciaTrie / NUMBER_OF_ATTEMPTS;
		double averageRemoveTimeInHybridTrie = removeWordsTimeInHybridTrie / NUMBER_OF_ATTEMPTS;
		double averageRemoveTimeInBalancedHybridTrie = removeWordsTimeInBalancedHybridTrie / NUMBER_OF_ATTEMPTS;
		double averageRemoveTimeInPatriciaTrie = removeWordsTimeInPatriciaTrie / NUMBER_OF_ATTEMPTS;

		System.out.println("\nLes résultats obtenues:");
		System.out.println("-------------------------");
		System.out.println("- Insertion: " + numberOfFiles + " fichiers (entre 16465 et 32861 mots)");
		System.out.println("Trie Hybride: " + averageInsertionTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averageInsertionTimeInBalancedHybridTrie + " ns");
		System.out.println("Patricia Trie: " + averageInsertionTimeInPatriciaTrie + " ns");
		
		System.out.println("\n- Recherche: 500 mots");
		System.out.println("Trie Hybride: " + averageSearchWordsTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averageSearchWordsTimeInBalancedHybridTrie + " ns");
		System.out.println("Patricia Trie: " + averageSearchWordsTimeInPatriciaTrie + " ns");
		
		System.out.println("\n- Préfixe:");
		System.out.println("Trie Hybride: " + averagePrefixCountTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averagePrefixCountTimeInBalancedHybridTrie + " ns");
		System.out.println("Patricia Trie: " + averagePrefixCountTimeInPatriciaTrie + " ns");
		
		System.out.println("\n- Suppression:");
		System.out.println("Trie Hybride: " + averageRemoveTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averageRemoveTimeInBalancedHybridTrie + " ns");
		System.out.println("Patricia Trie: " + averageRemoveTimeInPatriciaTrie + " ns");
	}
	
	private static double getSearchWordsTime(ITrie trie, int fileIndex, int numberOfWords) {
		Instant start = null;
		Instant end = null;
		double duration = 0;

		for (int wordsCount = 0; wordsCount < numberOfWords; wordsCount++) {
			int randomWordIndex = getRandomWordIndex(numberOfWords);

			start = Instant.now();
			trie.search(shakespeareWords.get(fileIndex).get(randomWordIndex));
			end = Instant.now();

			duration += (double) Duration.between(start, end).toNanos();
		}

    	return duration / (double) numberOfWords;
	}
	
	private static double getCountPrefixTime(ITrie trie, int fileIndex, int numberOfWords) {
		Instant start = null;
		Instant end = null;
		double duration = 0;

		for (int wordsCount = 0; wordsCount < numberOfWords; wordsCount++) {
			int randomWordPrefix = getRandomWordIndex(numberOfWords);
			String randomWord = shakespeareWords.get(fileIndex).get(randomWordPrefix);
			String randomPrefix = getRandomPrefix(randomWord);

			start = Instant.now();
			trie.getPrefixCount(randomPrefix);
			end = Instant.now();

			duration += (double) Duration.between(start, end).toNanos();
		}

    	return duration / (double) numberOfWords;
	}
	
	private static String getRandomPrefix(String randomWord) {
		int beginIndex = ThreadLocalRandom.current().nextInt(0, randomWord.length());
		int endIndex = ThreadLocalRandom.current().nextInt(beginIndex, randomWord.length());

		if (beginIndex == endIndex) {
			return randomWord;
		} else {
			return randomWord.substring(beginIndex, endIndex);
		}
	}

	private static double getRemoveWordsTime(ITrie trie, int fileIndex, int numberOfWords) {
		Instant start = null;
		Instant end = null;
		double duration = 0;

		for (int wordsCount = 0; wordsCount < numberOfWords; wordsCount++) {
			int randomWordIndex = getRandomWordIndex(numberOfWords);

			start = Instant.now();
			trie.remove(shakespeareWords.get(fileIndex).get(randomWordIndex));
			end = Instant.now();

			duration += (double) Duration.between(start, end).toNanos();
		}
    	return duration / (double) numberOfWords;
	}
	
	private static int getRandomWordIndex(int max) {
		OfInt iterator = ThreadLocalRandom.current().ints(0, max).distinct().iterator();

		return iterator.next();
	}
	
	private static void insertPartiallyOrientedListsInTrie(int fileIndex) {
		ArrayList<String> wordsListFromFile = new ArrayList<String>(shakespeareWords.get(fileIndex));
		ArrayList<String> listToSort = new ArrayList<String>();
		ArrayList<String> ordinaryList = new ArrayList<String>();

		for (int i = 0; i < wordsListFromFile.size(); i++) {
			if (i % 500 == 0) {
				listToSort.add(wordsListFromFile.get(i));
			} else {
				ordinaryList.add(wordsListFromFile.get(i));

			}
		}

		System.out.println("listToSort.size(): " + listToSort.size());
		Collections.sort(listToSort);
		hybridTrie.insert(listToSort);
		((HybridTrie) balancedHybridTrie).insertBalanced(listToSort);
		patriciaTrie.insert(listToSort);
		hybridTrie.insert(ordinaryList);
		((HybridTrie) balancedHybridTrie).insertBalanced(ordinaryList);
		patriciaTrie.insert(ordinaryList);
	}
	
	private static void printDataStatistics() {
		System.out.println("\nLes statistiques des données utilisés pour les expérimentations (Recherche, Préfixe, Suppression): ");
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println("- Oeuvre: hamlet.txt");
		System.out.println("- Taille : 164.04 KB");
		System.out.println("- Nombre de mots: 32861");
		System.out.println("- Au moins 66 mots sont ordonnées");
		System.out.println("- Longeur mini de mots: 1");
		System.out.println("- Longeur maxi de mots: 14");
		System.out.println("- Profendeur moyen des arbres:");
		System.out.println("    Trie Hybride: " + hybridTrie.getAverageDepthOfLeaves());
		System.out.println("    Trie Hybride équilibré: " + balancedHybridTrie.getAverageDepthOfLeaves());
		System.out.println("    Patricia Trie: " + patriciaTrie.getAverageDepthOfLeaves());
		System.out.println("- Hauteur de l'arbres:");
		System.out.println("    Trie Hybride: " + hybridTrie.getHeight());
		System.out.println("    Trie Hybride équilibré: " + balancedHybridTrie.getHeight());
		System.out.println("    Patricia Trie: " + patriciaTrie.getHeight());
	}*/
}


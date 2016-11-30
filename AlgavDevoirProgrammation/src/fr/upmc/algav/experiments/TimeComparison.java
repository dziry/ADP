package fr.upmc.algav.experiments;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.ThreadLocalRandom;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.GraphReader;

public class TimeComparison {

	private final static int NUMBER_OF_ATTEMPTS = 10;
	//private final static String DIRECTORY_PATH = "files/Shakespeare";
	// Alternate path for IntelliJ
	private final static String DIRECTORY_PATH = "AlgavDevoirProgrammation/files/Shakespeare";
	private static int numberOfFiles = 0;

	private static ArrayList<ArrayList<String>> shakespeareWordsList = new ArrayList<>();

	private static ITrie hybridTrie = new HybridTrie();
	private static ITrie balancedHybridTrie = new HybridTrie();
    private static ITrie patriciaTrie = new PatriciaTrie(new Alphabet());
	
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
		System.out.println("Les résultats obtenu sont la moyenne de " + NUMBER_OF_ATTEMPTS + " essais");
		System.out.println("------------------------------------------------");

		numberOfFiles = new File(DIRECTORY_PATH).listFiles().length;

		initWordsList();

		for (int attempt = 0; attempt < NUMBER_OF_ATTEMPTS; attempt++) {
			runTests(attempt);
		}

		printAverageResults();					
    }
    
	private static void initWordsList() {
		final File folder = new File(DIRECTORY_PATH);

		for (final File fileEntry : folder.listFiles()) {
			final String filePath = folder + "/" + fileEntry.getName();			
			GraphReader graphReader = new GraphReader(filePath);

			shakespeareWordsList.add(graphReader.read());
		}
	}
	
	private static void runTests(int attempt) {
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
		trie.insert(shakespeareWordsList.get(fileIndex));
		end = Instant.now();

    	return (double) Duration.between(start, end).toNanos();
	}
	
	private static double getBalancedInsertionTime(HybridTrie trie, int fileIndex) {
		Instant start = null;
		Instant end = null;

		trie.removeAll();
		start = Instant.now();
		trie.insertBalanced(shakespeareWordsList.get(fileIndex));
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
			trie.search(shakespeareWordsList.get(fileIndex).get(randomWordIndex));
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
			String randomWord = shakespeareWordsList.get(fileIndex).get(randomWordPrefix);
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
			trie.remove(shakespeareWordsList.get(fileIndex).get(randomWordIndex));
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
		ArrayList<String> wordsListFromFile = new ArrayList<String>(shakespeareWordsList.get(fileIndex));
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
	}
}


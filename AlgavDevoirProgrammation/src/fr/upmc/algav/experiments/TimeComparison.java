package fr.upmc.algav.experiments;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.ThreadLocalRandom;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.tools.Reader;

/*
 * TODO
 */

public abstract class TimeComparison {

	private final static int numberOfAttempts = 50;
	private final static String directoryPath = "files/Shakespeare";
	private final static int numberOfFiles = new File(directoryPath).listFiles().length;	
	private final static HashMap<Integer, ArrayList<String>> shakespeareWordsList = new HashMap<Integer, ArrayList<String>>(numberOfFiles);
	
	private static ITrie hybridTrie = new HybridTrie();
	private static ITrie balancedHybridTrie = new HybridTrie();
//	private static ITrie patriciaTrie = new PatriciaTrie(new Alphabet());
	
	private static double insertionTimeInHybridTrie = 0;
	private static double insertionTimeInBalancedHybridTrie = 0;
//	private static double insertionTimeInPatriciaTrie = 0;
	private static double searchWordsTimeInHybridTrie = 0;
	private static double searchWordsTimeInBalancedHybridTrie = 0;
//	private static double searchWordsTimeInPatriciaTrie = 0;
	private static double prefixCountTimeInHybridTrie = 0;
	private static double prefixCountTimeInBalancedHybridTrie = 0;
//	private static double prefixCountTimeInPatriciaTrie = 0;
	private static double removeWordsTimeInHybridTrie = 0;
	private static double removeWordsTimeInBalancedHybridTrie = 0;
//	private static double removeWordsTimeInPatriciaTrie = 0;

	public static void main(String[] args) {
		System.out.println("Les résultats obtenu sont la moyenne de " + numberOfAttempts + " essais");
		System.out.println("------------------------------------------------");
		putWordsInHashMapOfLists();
		for (int attempt = 0; attempt < numberOfAttempts; attempt++) {
			runTests(attempt);			
		}
		printAverageResults();					
    }
    
	public static void putWordsInHashMapOfLists() {
		final File folder = new File(directoryPath);
		int index = 0;
		for (final File fileEntry : folder.listFiles()) {
			final String filePath = folder + "/" + fileEntry.getName();			
			Reader reader = new Reader(filePath);
			shakespeareWordsList.put(index++, reader.read());
		}
	}
	
	private static void runTests(int attempt) {
		insertExperiments();		
		// getting ready for the next three experiments
		final int indexOfFile = 6;		
		if (attempt == 0) {
			hybridTrie.removeAll();
			balancedHybridTrie.removeAll();
//			patriciaTrie.removeAll();
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
//			insertionTimeInPatriciaTrie += getInsertionTime(patriciaTrie, index);
		}
	}
	
	private static void searchExperiments(int indexOfFile) {
		int numberOfWordsToSearch = 500;
		searchWordsTimeInHybridTrie += getSearchWordsTime(hybridTrie, indexOfFile, numberOfWordsToSearch);
		searchWordsTimeInBalancedHybridTrie += getSearchWordsTime(balancedHybridTrie, indexOfFile, numberOfWordsToSearch);
//		searchWordsTimeInPatriciaTrie += getSearchWordsTime(patriciaTrie, indexOfFile, numberOfWordsToSearch);
	}

	private static void prefixExperiments(int indexOfFile) {		
		int numberOfWordsToCountPrefix = 500;
		prefixCountTimeInHybridTrie += getCountPrefixTime(hybridTrie, indexOfFile, numberOfWordsToCountPrefix);
		prefixCountTimeInBalancedHybridTrie += getCountPrefixTime(balancedHybridTrie, indexOfFile, numberOfWordsToCountPrefix);
//		prefixCountTimeInPatriciaTrie += getCountPrefixTime(patriciaTrie, indexOfFile, numberOfWordsToRemove);
	}

	private static void removeExperiments(int indexOfFile) {
		int numberOfWordsToRemove = 500;
		removeWordsTimeInHybridTrie += getRemoveWordsTime(hybridTrie, indexOfFile, numberOfWordsToRemove);
		removeWordsTimeInBalancedHybridTrie += getRemoveWordsTime(balancedHybridTrie, indexOfFile, numberOfWordsToRemove);
//		removeWordsTimeInPatriciaTrie += getRemoveWordsTime(patriciaTrie, indexOfFile, numberOfWordsToRemove);
	}

	private static double getInsertionTime(ITrie trie, int FileIndex) {
		Instant start = null;
		Instant end = null;
		trie.removeAll();
		start = Instant.now();
		trie.insert(shakespeareWordsList.get(FileIndex));
		end = Instant.now();
    	return (double)Duration.between(start, end).toNanos();
	}
	
	private static double getBalancedInsertionTime(HybridTrie trie, int FileIndex) {
		Instant start = null;
		Instant end = null;
		trie.removeAll();
		start = Instant.now();
		trie.insertBalanced(shakespeareWordsList.get(FileIndex));
		end = Instant.now();
    	return (double)Duration.between(start, end).toNanos();
	}

	private static void printAverageResults() {		
		double averageInsertionTimeInHybridTrie = insertionTimeInHybridTrie / numberOfAttempts;
		double averageInsertionTimeInBalancedHybridTrie = insertionTimeInBalancedHybridTrie / numberOfAttempts;
//		double averageInsertionTimeInPatriciaTrie = insertionTimeInPatriciaTrie / numberOfAttempts;
		double averageSearchWordsTimeInHybridTrie = searchWordsTimeInHybridTrie / numberOfAttempts;
		double averageSearchWordsTimeInBalancedHybridTrie = searchWordsTimeInBalancedHybridTrie / numberOfAttempts;
//		double averageSearchWordsTimeInPatriciaTrie = searchWordsTimeInPatriciaTrie / numberOfAttempts;
		double averagePrefixCountTimeInHybridTrie = prefixCountTimeInHybridTrie / numberOfAttempts;
		double averagePrefixCountTimeInBalancedHybridTrie = prefixCountTimeInBalancedHybridTrie / numberOfAttempts;
//		double averagePrefixCountTimeInPatriciaTrie = prefixCountTimeInPatriciaTrie / numberOfAttempts;
		double averageRemoveTimeInHybridTrie = removeWordsTimeInHybridTrie / numberOfAttempts;
		double averageRemoveTimeInBalancedHybridTrie = removeWordsTimeInBalancedHybridTrie / numberOfAttempts;
//		double averageRemoveTimeInPatriciaTrie = removeWordsTimeInPatriciaTrie / numberOfAttempts;

		System.out.println("\nLes résultats obtenues:");
		System.out.println("-------------------------");
		System.out.println("- Insertion: " + numberOfFiles + " fichiers (entre 16465 et 32861 mots)");
		System.out.println("Trie Hybride: " + averageInsertionTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averageInsertionTimeInBalancedHybridTrie + " ns");
//		System.out.println("Patricia Trie: " + averageInsertionTimeInPatriciaTrie + " ns");
		
		System.out.println("\n- Recherche: 500 mots");
		System.out.println("Trie Hybride: " + averageSearchWordsTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averageSearchWordsTimeInBalancedHybridTrie + " ns");
//		System.out.println("Patricia Trie: " + averageSearchWordsTimeInPatriciaTrie + " ns");
		
		System.out.println("\n- Préfixe:");
		System.out.println("Trie Hybride: " + averagePrefixCountTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averagePrefixCountTimeInBalancedHybridTrie + " ns");
//		System.out.println("Patricia Trie: " + averagePrefixCountTimeInPatriciaTrie + " ns");
		
		System.out.println("\n- Suppression:");
		System.out.println("Trie Hybride: " + averageRemoveTimeInHybridTrie + " ns");
		System.out.println("Trie Hybride équilibré: " + averageRemoveTimeInBalancedHybridTrie + " ns");
//		System.out.println("Patricia Trie: " + averageRemoveTimeInPatriciaTrie + " ns");
	}
	
	private static double getSearchWordsTime(ITrie trie, int FileIndex, int numberOfWords) {
		Instant start = null;
		Instant end = null;
		double duration = 0;
		for (int wordsCount = 0; wordsCount < numberOfWords; wordsCount++) {
			int randomWordIndex = getRandomWordIndex(numberOfWords);
			start = Instant.now();
			trie.search(shakespeareWordsList.get(FileIndex).get(randomWordIndex));
			end = Instant.now();
			duration += (double)Duration.between(start, end).toNanos();
		}
    	return duration / (double)numberOfWords;
	}
	
	private static double getCountPrefixTime(ITrie trie, int FileIndex, int numberOfWords) {
		Instant start = null;
		Instant end = null;
		double duration = 0;
		for (int wordsCount = 0; wordsCount < numberOfWords; wordsCount++) {
			int randomWordPrefix = getRandomWordIndex(numberOfWords);
			String randomWord = shakespeareWordsList.get(FileIndex).get(randomWordPrefix);
			String randomPrefix = getRandomPrefix(randomWord);
			start = Instant.now();
			trie.getPrefixCount(randomPrefix);
			end = Instant.now();
			duration += (double)Duration.between(start, end).toNanos();
		}
    	return duration / (double)numberOfWords;
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

	private static double getRemoveWordsTime(ITrie trie, int FileIndex, int numberOfWords) {
		Instant start = null;
		Instant end = null;
		double duration = 0;
		for (int wordsCount = 0; wordsCount < numberOfWords; wordsCount++) {
			int randomWordIndex = getRandomWordIndex(numberOfWords);
			start = Instant.now();
			trie.remove(shakespeareWordsList.get(FileIndex).get(randomWordIndex));
			end = Instant.now();
			duration += (double)Duration.between(start, end).toNanos();
		}
    	return duration / (double)numberOfWords;
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
//		System.out.println("listToSort.size(): " + listToSort.size());
		Collections.sort(listToSort);
		hybridTrie.insert(listToSort);
		((HybridTrie) balancedHybridTrie).insertBalanced(listToSort);
//		patriciaTrie.insert(listToSort);
		hybridTrie.insert(ordinaryList);
		((HybridTrie) balancedHybridTrie).insertBalanced(ordinaryList);
//		patriciaTrie.insert(ordinaryList);
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
//		System.out.println("    Patricia Trie: " + patriciaTrie.getAverageDepthOfLeaves());
		System.out.println("- Hauteur de l'arbres:");
		System.out.println("    Trie Hybride: " + hybridTrie.getHeight());
		System.out.println("    Trie Hybride équilibré: " + balancedHybridTrie.getHeight());
//		System.out.println("    Patricia Trie: " + patriciaTrie.getHeight());		
	}
}


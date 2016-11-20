package fr.upmc.algav.tests;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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

	private final static int numberOfAttempts = 10;
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

	public static void main(String[] args) {
		putWordsInHashMapOfLists();		
		for (int attempt = 0; attempt < numberOfAttempts; attempt++) {
			runTests();			
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
	
	private static void runTests() {
		for (int index = 0; index < numberOfFiles; index++) {
			/* Insert */
			insertionTimeInHybridTrie += getInsertionTime(hybridTrie, index);
			insertionTimeInBalancedHybridTrie += getBalancedInsertionTime((HybridTrie) balancedHybridTrie, index);
//			insertionTimeInPatriciaTrie += getInsertionTime(patriciaTrie, index);
		}			
			// getting ready for the next three experiments
			final int indexOfFile = 6;
			hybridTrie.removeAll();
			balancedHybridTrie.removeAll();
//			patriciaTrie.removeAll();
			hybridTrie.insert(shakespeareWordsList.get(indexOfFile));
			balancedHybridTrie.insert(shakespeareWordsList.get(indexOfFile));
//			patriciaTrie.insert(shakespeareWordsList.get(indexOfFile));
			
			/* Search */
			searchWordsTimeInHybridTrie += getSearchWordsTime(hybridTrie, indexOfFile, 500);
			searchWordsTimeInBalancedHybridTrie += getSearchWordsTime(balancedHybridTrie, indexOfFile, 500);
//			searchWordsTimeInPatriciaTrie += getSearchWordsTime(patriciaTrie, indexOfFile, 500);
			
			/* Prefix */
			
			/* Remove */
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
		
		System.out.println("averageInsertionTimeInHybridTrie: " + averageInsertionTimeInHybridTrie + " ns");
		System.out.println("averageInsertionTimeInBalancedHybridTrie: " + averageInsertionTimeInBalancedHybridTrie + " ns");
//		System.out.println("averageInsertionTimeInPatriciaTrie: " + averageInsertionTimeInPatriciaTrie + " ns");
		System.out.println("averageSearchWordsTimeInHybridTrie: " + averageSearchWordsTimeInHybridTrie + " ns");
		System.out.println("averageSearchWordsTimeInBalancedHybridTrie: " + averageSearchWordsTimeInBalancedHybridTrie + " ns");
//		System.out.println("averageSearchWordsTimeInPatriciaTrie: " + averageSearchWordsTimeInPatriciaTrie + " ns");
	}
	
	private static double getSearchWordsTime(ITrie trie, int FileIndex, int numberOfWords) {
		Instant start = null;
		Instant end = null;
		start = Instant.now();
		for (int wordsCount = 0; wordsCount < numberOfWords; wordsCount++) {
			int randomWord = getRandomWordIndex(numberOfWords);
			trie.search(shakespeareWordsList.get(FileIndex).get(randomWord));
		}
		end = Instant.now();
    	return (double)Duration.between(start, end).toNanos() / numberOfWords;
	}
	
	private static int getRandomWordIndex(int max) {
		OfInt iterator = ThreadLocalRandom.current().ints(0, max).distinct().iterator();
		return iterator.next();
	}
}


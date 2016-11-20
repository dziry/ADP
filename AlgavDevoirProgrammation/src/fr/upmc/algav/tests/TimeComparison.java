package fr.upmc.algav.tests;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.tools.Reader;

public class TimeComparison {

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

	public static void main(String[] args) {
		putWordsInHashMapOfLists();		
		for (int attempt = 0; attempt < numberOfAttempts; attempt++) {
			runTests();			
		}
		printAverageResults();					
    }
    
	private static void putWordsInHashMapOfLists() {
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
			insertionTimeInHybridTrie += getInsertionTime(hybridTrie, index);
			insertionTimeInBalancedHybridTrie += getBalancedInsertionTime((HybridTrie) balancedHybridTrie, index);
//			insertionTimeInPatriciaTrie += getInsertionTime(patriciaTrie, index);
		}
	}

	private static double getInsertionTime(ITrie trie, int index) {
		Instant start = null;
		Instant end = null;
		start = Instant.now();
		trie.insert(shakespeareWordsList.get(index));
		end = Instant.now();
    	return (double)Duration.between(start, end).toNanos();
	}
	
	private static double getBalancedInsertionTime(HybridTrie trie, int index) {
		Instant start = null;
		Instant end = null;
		start = Instant.now();
		trie.insertBalanced(shakespeareWordsList.get(index));
		end = Instant.now();
    	return (double)Duration.between(start, end).toNanos();
	}

	private static void printAverageResults() {
		double averageInsertionTimeInHybridTrie = insertionTimeInHybridTrie / numberOfAttempts;
		double averageInsertionTimeInBalancedHybridTrie = insertionTimeInBalancedHybridTrie / numberOfAttempts;
//		double averageInsertionTimeInPatriciaTrie = insertionTimeInPatriciaTrie / numberOfAttempts;
		
		System.out.println("averageInsertionTimeInHybridTrie: " + averageInsertionTimeInHybridTrie);
		System.out.println("averageInsertionTimeInBalancedHybridTrie: " + averageInsertionTimeInBalancedHybridTrie);
//		System.out.println("averageInsertionTimeInPatriciaTrie: " + averageInsertionTimeInPatriciaTrie);
		
	}
}


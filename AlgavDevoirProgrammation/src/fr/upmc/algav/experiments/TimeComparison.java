package fr.upmc.algav.experiments;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.GraphReader;

public class TimeComparison {

    // Eclipse IDE
	private final static String DIRECTORY_PATH = "files/Shakespeare";
	// IntelliJ IDE
    //private final static String DIRECTORY_PATH = "AlgavDevoirProgrammation/files/Shakespeare";

    // Eclipse IDE
    private final static String RESULTS_PATH = "results/results.txt";
    // IntelliJ IDE
    //private final static String RESULTS_PATH = "AlgavDevoirProgrammation/results/results.txt";
    private final static String RESULTS_ENCODING = "utf-8";

    private static final String SEPARATOR_LINE = "------------------------------------------------------------------";

    private static Writer resultsWriter;
    private static int numberOfFiles;    
	private static Collection<String> shakespeareWords;	

    private static final int REPETITIONS_PER_TEST = 1;

    private static final String UNKNOWN_INSERTED_WORD = "osahezrnthxshekdzsdfjdgefjhtnsh";

    private static final int NUMBER_OF_WORDS_TO_REMOVE_SEARCH_PREFIX = 20;


	public static void main(String[] args) {
        initWriter();
        // true to accept duplicated words, false else
        initWordsSet(false);

        printTestBegin();

        doTrieConstructionTest();
//        doSetOfWordsSearchTest();
//        doCountAllWordsInTrieTest();
//        doCountPrefixesTest();
        doConversionTest();
//        doUnknownWordInsertionTest();
//        doSetOfWordsRemovalTest();
//        doPatriciaConstructionsTest();

        printTestEnd();
    }

    private static PatriciaTrie createNewAlreadyConstructedPatriciaTrie() {
        PatriciaTrie res = new PatriciaTrie(new Alphabet());

        res.insert(shakespeareWords);

        return res;
    }

    private static HybridTrie createNewAlreadyConstructedHybridTrie() {
        HybridTrie res = new HybridTrie();

        res.insert(shakespeareWords);

        return res;
    }

    private static HybridTrie createNewAlreadyConstructedBalancedHybridTrie() {
        HybridTrie res = new HybridTrie();

        res.insertBalanced(shakespeareWords);

        return res;
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

    private static void initWordsSet(boolean isDuplicatedWordsAccepted) {
    	if (isDuplicatedWordsAccepted) {
    		// Duplicated words
    		shakespeareWords = new ArrayList<>();
    	} else {
    		// Distinct words
    		shakespeareWords = new HashSet<>();
    	}
        numberOfFiles = 0;

		try (Stream<Path> paths = Files.walk(Paths.get(DIRECTORY_PATH))) {
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

    private static void doTrieConstructionTest() {
        ArrayList<Double> hybridTrieConstructionTimes = new ArrayList<>();
        ArrayList<Double> balancedHybridTrieConstructionTimes = new ArrayList<>();
        ArrayList<Double> patriciaTrieConstructionTimes = new ArrayList<>();

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double hybridTrieDuration = getDurationForTrieConstruction(TrieType.HYBRID);
            double balancedHybridTrieDuration = getDurationForTrieConstruction(TrieType.BALANCED_HYBRID);
            double patriciaTrieDuration = getDurationForTrieConstruction(TrieType.PATRICIA);

            hybridTrieConstructionTimes.add(hybridTrieDuration);
            balancedHybridTrieConstructionTimes.add(balancedHybridTrieDuration);
            patriciaTrieConstructionTimes.add(patriciaTrieDuration);
        }

        String heading = "Construction of tries for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                          SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Total time for Hybrid Trie: " + hybridTrieConstructionTimes.get(i) + " ms\n" +
                            "Total time for Balanced Hybrid Trie: " + balancedHybridTrieConstructionTimes.get(i) + " ms\n" +
                            "Total time for Patricia Trie: " + patriciaTrieConstructionTimes.get(i) + " ms\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average times:\n" +
                "Average time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieConstructionTimes) + " ms\n" +
                "Average time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieConstructionTimes) + " ms\n" +
                "Average time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieConstructionTimes) + " ms\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }

    private static double calculateAverageForSeveralResults(ArrayList<Double> results) {
        double res = -1;

        if (results != null) {
            double total = 0;

            for (Double r : results) {
                total += r;
            }

            BigDecimal bd = new BigDecimal(total / results.size());
            bd = bd.setScale(2, RoundingMode.HALF_UP);

            res = bd.doubleValue();
        }

        return res;
    }

    private static double getDurationForTrieConstruction(TrieType type) {
        Instant start = null;
        Instant end = null;

        if (type == TrieType.BALANCED_HYBRID) {
            final HybridTrie balancedHTrie = new HybridTrie();

            start = Instant.now();
            balancedHTrie.insertBalanced(shakespeareWords);
            end = Instant.now();
        } else if (type == TrieType.HYBRID) {
            final HybridTrie hTrie = new HybridTrie();

            start = Instant.now();
            hTrie.insert(shakespeareWords);
            end = Instant.now();
        } else {
            final PatriciaTrie pTrie = new PatriciaTrie(new Alphabet());

            start = Instant.now();
            pTrie.insert(shakespeareWords);
            end = Instant.now();
        }
        return (double) Duration.between(start, end).toMillis();
    }

    private static void doUnknownWordInsertionTest() {
        ArrayList<Double> hybridTrieInsertionTimes = new ArrayList<>();
        ArrayList<Double> balancedHybridTrieInsertionTimes = new ArrayList<>();
        ArrayList<Double> patriciaTrieInsertionTimes = new ArrayList<>();

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double hybridTrieDuration = getDurationForTrieInsertion(TrieType.HYBRID, UNKNOWN_INSERTED_WORD);
            double balancedHybridTrieDuration = getDurationForTrieInsertion(TrieType.BALANCED_HYBRID, UNKNOWN_INSERTED_WORD);
            double patriciaTrieDuration = getDurationForTrieInsertion(TrieType.PATRICIA, UNKNOWN_INSERTED_WORD);

            hybridTrieInsertionTimes.add(hybridTrieDuration);
            balancedHybridTrieInsertionTimes.add(balancedHybridTrieDuration);
            patriciaTrieInsertionTimes.add(patriciaTrieDuration);
        }

        String heading = "\nInsertion of unknown word \"" + UNKNOWN_INSERTED_WORD + "\" for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                         SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Insertion time for Hybrid Trie: " + hybridTrieInsertionTimes.get(i) + " ns\n" +
                            "Insertion time for Balanced Hybrid Trie: " + balancedHybridTrieInsertionTimes.get(i) + " ns\n" +
                            "Insertion time for Patricia Trie: " + patriciaTrieInsertionTimes.get(i) + " ns\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average insertion times:\n" +
                "Average insertion time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieInsertionTimes) + " ns\n" +
                "Average insertion time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieInsertionTimes) + " ns\n" +
                "Average insertion time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieInsertionTimes) + " ns\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }

    private static double getDurationForTrieInsertion(TrieType type, String wordToInsert) {
        Instant start = null;
        Instant end = null;

        if (type == TrieType.BALANCED_HYBRID) {
            final HybridTrie balancedHTrie = createNewAlreadyConstructedBalancedHybridTrie();

            start = Instant.now();
            balancedHTrie.insertBalanced(wordToInsert);
            end = Instant.now();
        } else if (type == TrieType.HYBRID) {
            final HybridTrie hTrie = createNewAlreadyConstructedHybridTrie();

            start = Instant.now();
            hTrie.insert(wordToInsert);
            end = Instant.now();
        } else {
            final PatriciaTrie pTrie = createNewAlreadyConstructedPatriciaTrie();

            start = Instant.now();
            pTrie.insert(wordToInsert);
            end = Instant.now();
        }

        return (double) Duration.between(start, end).toNanos();
    }

    private static void doSetOfWordsRemovalTest() {
        ArrayList<Double> hybridTrieRemovalTimes = new ArrayList<>();
        ArrayList<Double> balancedHybridTrieRemovalTimes = new ArrayList<>();
        ArrayList<Double> patriciaTrieRemovalTimes = new ArrayList<>();

        HashSet<String> wordsToRemove = generateRandomSetOfExistingWords();

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double hybridTrieDuration = getDurationForSetOfWordsTrieRemoval(TrieType.HYBRID, wordsToRemove);
            double balancedHybridTrieDuration = getDurationForSetOfWordsTrieRemoval(TrieType.BALANCED_HYBRID, wordsToRemove);
            double patriciaTrieDuration = getDurationForSetOfWordsTrieRemoval(TrieType.PATRICIA, wordsToRemove);

            hybridTrieRemovalTimes.add(hybridTrieDuration);
            balancedHybridTrieRemovalTimes.add(balancedHybridTrieDuration);
            patriciaTrieRemovalTimes.add(patriciaTrieDuration);
        }

        String heading = "\nRemoval of set of existing words for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                         SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);


        String removedWordsCaption = "Removed words:\n[\n";

        for (String wToRemove : wordsToRemove) {
            removedWordsCaption += "\"" + wToRemove + "\"\n";
        }

        removedWordsCaption += "]\n\n";

//        System.out.println(removedWordsCaption);
//        writeToResultsFile(removedWordsCaption);


        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Removal time for Hybrid Trie: " + hybridTrieRemovalTimes.get(i) + " ns\n" +
                            "Removal time for Balanced Hybrid Trie: " + balancedHybridTrieRemovalTimes.get(i) + " ns\n" +
                            "Removal time for Patricia Trie: " + patriciaTrieRemovalTimes.get(i) + " ns\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average removal times:\n" +
                "Average removal time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieRemovalTimes) + " ns\n" +
                "Average removal time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieRemovalTimes) + " ns\n" +
                "Average removal time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieRemovalTimes) + " ns\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }

    private static HashSet<String> generateRandomSetOfExistingWords() {
        HashSet<String> res = new HashSet<>();

        ArrayList<String> existingWords = new ArrayList<>(shakespeareWords);

        while (res.size() < NUMBER_OF_WORDS_TO_REMOVE_SEARCH_PREFIX) {
            int randomWordIndex = ThreadLocalRandom.current().nextInt(0, existingWords.size());
            res.add(existingWords.get(randomWordIndex));
        }

        return res;
    }

    private static double getDurationForSetOfWordsTrieRemoval(TrieType type, HashSet<String> wordsToRemove) {
        Instant start = null;
        Instant end = null;

        if (type == TrieType.BALANCED_HYBRID) {
            final HybridTrie balancedHTrie = createNewAlreadyConstructedBalancedHybridTrie();

            start = Instant.now();

            for (String w : wordsToRemove) {
                balancedHTrie.remove(w);
            }

            end = Instant.now();
        } else if (type == TrieType.HYBRID) {
            final HybridTrie hTrie = createNewAlreadyConstructedHybridTrie();

            start = Instant.now();

            for (String w : wordsToRemove) {
                hTrie.remove(w);
            }

            end = Instant.now();
        } else {
            final PatriciaTrie pTrie = createNewAlreadyConstructedPatriciaTrie();

            start = Instant.now();

            for (String w : wordsToRemove) {
                pTrie.remove(w);
            }

            end = Instant.now();
        }

        return (double) Duration.between(start, end).toNanos();
    }
    
    private static void doSetOfWordsSearchTest() {
        ArrayList<Double> hybridTrieSearchTimes = new ArrayList<>();
        ArrayList<Double> balancedHybridTrieSearchTimes = new ArrayList<>();
        ArrayList<Double> patriciaTrieSearchTimes = new ArrayList<>();

        HashSet<String> wordsToSearch = generateRandomSetOfExistingWords();

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double hybridTrieDuration = getDurationForSetOfWordsTrieSearch(TrieType.HYBRID, wordsToSearch);
            double balancedHybridTrieDuration = getDurationForSetOfWordsTrieSearch(TrieType.BALANCED_HYBRID, wordsToSearch);
            double patriciaTrieDuration = getDurationForSetOfWordsTrieSearch(TrieType.PATRICIA, wordsToSearch);

            hybridTrieSearchTimes.add(hybridTrieDuration);
            balancedHybridTrieSearchTimes.add(balancedHybridTrieDuration);
            patriciaTrieSearchTimes.add(patriciaTrieDuration);
        }

        String heading = "\nLooking for a set of existing words for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                         SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);


        String searchedWordsCaption = "Words to find:\n[\n";

        for (String wToSearch : wordsToSearch) {
            searchedWordsCaption += "\"" + wToSearch + "\"\n";
        }

        searchedWordsCaption += "]\n\n";

//        System.out.println(searchedWordsCaption);
//        writeToResultsFile(searchedWordsCaption);


        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Searching time for Hybrid Trie: " + hybridTrieSearchTimes.get(i) + " ns\n" +
                            "Searching time for Balanced Hybrid Trie: " + balancedHybridTrieSearchTimes.get(i) + " ns\n" +
                            "Searching time for Patricia Trie: " + patriciaTrieSearchTimes.get(i) + " ns\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average searching times:\n" +
                "Average searching time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieSearchTimes) + " ns\n" +
                "Average searching time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieSearchTimes) + " ns\n" +
                "Average searching time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieSearchTimes) + " ns\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }
    
    private static double getDurationForSetOfWordsTrieSearch(TrieType type, HashSet<String> wordsToSearch) {
        Instant start = null;
        Instant end = null;

        if (type == TrieType.BALANCED_HYBRID) {
            final HybridTrie balancedHTrie = createNewAlreadyConstructedBalancedHybridTrie();

            start = Instant.now();

            for (String word : wordsToSearch) {
                balancedHTrie.search(word);
            }

            end = Instant.now();
        } else if (type == TrieType.HYBRID) {
            final HybridTrie hTrie = createNewAlreadyConstructedHybridTrie();

            start = Instant.now();

            for (String word : wordsToSearch) {
                hTrie.search(word);
            }

            end = Instant.now();
        } else {
            final PatriciaTrie pTrie = createNewAlreadyConstructedPatriciaTrie();

            start = Instant.now();

            for (String word : wordsToSearch) {
                pTrie.search(word);
            }

            end = Instant.now();
        }

        return (double) Duration.between(start, end).toNanos();
    }

    private static void doCountAllWordsInTrieTest() {
        ArrayList<Double> hybridTrieCountWordsTimes = new ArrayList<>();
        ArrayList<Double> balancedHybridTrieCountWordsTimes = new ArrayList<>();
        ArrayList<Double> patriciaTrieCountWordsTimes = new ArrayList<>();


        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double hybridTrieDuration = getDurationForCountAllWordsInTrie(TrieType.HYBRID);
            double balancedHybridTrieDuration = getDurationForCountAllWordsInTrie(TrieType.BALANCED_HYBRID);
            double patriciaTrieDuration = getDurationForCountAllWordsInTrie(TrieType.PATRICIA);

            hybridTrieCountWordsTimes.add(hybridTrieDuration);
            balancedHybridTrieCountWordsTimes.add(balancedHybridTrieDuration);
            patriciaTrieCountWordsTimes.add(patriciaTrieDuration);
        }

        String heading = "\nCounting all existing words in the Trie for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                         SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Counting operation time for Hybrid Trie: " + hybridTrieCountWordsTimes.get(i) + " ns\n" +
                            "Counting operation time for Balanced Hybrid Trie: " + balancedHybridTrieCountWordsTimes.get(i) + " ns\n" +
                            "Counting operation time for Patricia Trie: " + patriciaTrieCountWordsTimes.get(i) + " ns\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average Counting operation times:\n" +
                "Average counting operation time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieCountWordsTimes) + " ns\n" +
                "Average counting operation time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieCountWordsTimes) + " ns\n" +
                "Average counting operation time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieCountWordsTimes) + " ns\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }
    
    private static double getDurationForCountAllWordsInTrie(TrieType type) {
        Instant start = null;
        Instant end = null;

        if (type == TrieType.BALANCED_HYBRID) {
            final HybridTrie balancedHTrie = createNewAlreadyConstructedBalancedHybridTrie();

            start = Instant.now();
            balancedHTrie.getWordCount();
            end = Instant.now();
            
        } else if (type == TrieType.HYBRID) {
            final HybridTrie hTrie = createNewAlreadyConstructedHybridTrie();

            start = Instant.now();
            hTrie.getWordCount();
            end = Instant.now();
            
        } else {
            final PatriciaTrie pTrie = createNewAlreadyConstructedPatriciaTrie();

            start = Instant.now();
            pTrie.getWordCount();
            end = Instant.now();
        }

        return (double) Duration.between(start, end).toNanos();
    }
    
    private static void doCountPrefixesTest() {
        ArrayList<Double> hybridTrieCountPrefixTimes = new ArrayList<>();
        ArrayList<Double> balancedHybridTrieCountPrefixTimes = new ArrayList<>();
        ArrayList<Double> patriciaTrieCountPrefixTimes = new ArrayList<>();

        HashSet<String> prefixesToCount = generateRandomSetOfPrefixes();

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double hybridTrieDuration = getDurationForCountPrefix(TrieType.HYBRID, prefixesToCount);
            double balancedHybridTrieDuration = getDurationForCountPrefix(TrieType.BALANCED_HYBRID, prefixesToCount);
            double patriciaTrieDuration = getDurationForCountPrefix(TrieType.PATRICIA, prefixesToCount);

            hybridTrieCountPrefixTimes.add(hybridTrieDuration);
            balancedHybridTrieCountPrefixTimes.add(balancedHybridTrieDuration);
            patriciaTrieCountPrefixTimes.add(patriciaTrieDuration);
        }

        String heading = "\nCounting prefixes for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                         SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);

        String PrefixesCaption = "Prefixes to count:\n";
        PrefixesCaption += prefixesToCount.toString();
        PrefixesCaption += "\n\n";

        System.out.println(PrefixesCaption);
//        writeToResultsFile(PrefixesCaption);


        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Counting prefix time for Hybrid Trie: " + hybridTrieCountPrefixTimes.get(i) + " ns\n" +
                            "Counting prefix time for Balanced Hybrid Trie: " + balancedHybridTrieCountPrefixTimes.get(i) + " ns\n" +
                            "Counting prefix time for Patricia Trie: " + patriciaTrieCountPrefixTimes.get(i) + " ns\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average counting prefix times:\n" +
                "Average counting prefix time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieCountPrefixTimes) + " ns\n" +
                "Average counting prefix time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieCountPrefixTimes) + " ns\n" +
                "Average counting prefix time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieCountPrefixTimes) + " ns\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }
    
    private static HashSet<String> generateRandomSetOfPrefixes() {			
		ArrayList<String> existingWords = new ArrayList<>(shakespeareWords);
		HashSet<String> result = new HashSet<>();
		
		while (result.size() < NUMBER_OF_WORDS_TO_REMOVE_SEARCH_PREFIX) {
			int randomWordIndex = ThreadLocalRandom.current().nextInt(0, existingWords.size());
			String randomWord = existingWords.get(randomWordIndex);
			
			int beginIndex = ThreadLocalRandom.current().nextInt(0, randomWord.length());
			int endIndex = ThreadLocalRandom.current().nextInt(beginIndex, randomWord.length());

			if (beginIndex == endIndex) {
				result.add(randomWord);
			} else {
				result.add(randomWord.substring(beginIndex, endIndex));
			}            
        }	
		
		return result;
    }
    
    private static double getDurationForCountPrefix(TrieType type, HashSet<String> prefixesToCount) {
        Instant start = null;
        Instant end = null;

        if (type == TrieType.BALANCED_HYBRID) {
            final HybridTrie balancedHTrie = createNewAlreadyConstructedBalancedHybridTrie();

            start = Instant.now();

            for (String prefix : prefixesToCount) {
                balancedHTrie.getPrefixCount(prefix);
            }

            end = Instant.now();
        } else if (type == TrieType.HYBRID) {
            final HybridTrie hTrie = createNewAlreadyConstructedHybridTrie();

            start = Instant.now();

            for (String prefix : prefixesToCount) {
                hTrie.getPrefixCount(prefix);
            }

            end = Instant.now();
        } else {
            final PatriciaTrie pTrie = createNewAlreadyConstructedPatriciaTrie();

            start = Instant.now();

            for (String prefix : prefixesToCount) {
                pTrie.getPrefixCount(prefix);
            }

            end = Instant.now();
        }

        return (double) Duration.between(start, end).toNanos();
    }
    
    private static void doConversionTest() {
        ArrayList<Double> hybridTrieConversionTimes = new ArrayList<>();
        ArrayList<Double> balancedHybridTrieConversionTimes = new ArrayList<>();
        ArrayList<Double> patriciaTrieConversionTimes = new ArrayList<>();

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double hybridTrieDuration = getDurationForConversion(TrieType.HYBRID);
            double balancedHybridTrieDuration = getDurationForConversion(TrieType.BALANCED_HYBRID);
            double patriciaTrieDuration = getDurationForConversion(TrieType.PATRICIA);

            hybridTrieConversionTimes.add(hybridTrieDuration);
            balancedHybridTrieConversionTimes.add(balancedHybridTrieDuration);
            patriciaTrieConversionTimes.add(patriciaTrieDuration);
        }

        String heading = "\nConversion for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                         SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Conversion time for Hybrid Trie: " + hybridTrieConversionTimes.get(i) + " ns\n" +
                            "Conversion time for Balanced Hybrid Trie: " + balancedHybridTrieConversionTimes.get(i) + " ns\n" +
                            "Conversion time for Patricia Trie: " + patriciaTrieConversionTimes.get(i) + " ns\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average conversion times:\n" +
                "Average conversion time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieConversionTimes) + " ns\n" +
                "Average conversion time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieConversionTimes) + " ns\n" +
                "Average conversion time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieConversionTimes) + " ns\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }
    
    private static double getDurationForConversion(TrieType type) {
        Instant start = null;
        Instant end = null;

        if (type == TrieType.BALANCED_HYBRID) {
            final HybridTrie balancedHTrie = createNewAlreadyConstructedBalancedHybridTrie();

            start = Instant.now();
            balancedHTrie.toPatriciaTrie();
            end = Instant.now();
            
        } else if (type == TrieType.HYBRID) {
            final HybridTrie hTrie = createNewAlreadyConstructedHybridTrie();

            start = Instant.now();
            hTrie.toPatriciaTrie();
            end = Instant.now();
            
        } else {
            final PatriciaTrie pTrie = createNewAlreadyConstructedPatriciaTrie();

            start = Instant.now();
            pTrie.toHybridTrie();
            end = Instant.now();
        }

        return (double) Duration.between(start, end).toNanos();
    }
    
    private static void doPatriciaConstructionsTest() {
        ArrayList<Double> constructionByAllWordsInsertionTimes = new ArrayList<>();
        ArrayList<Double> constructionByMergeTimes = new ArrayList<>();

        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            double allWordsConstructionDuration = getDurationForPatriciaConstruction(false);
            double mergeConstructionDuration = getDurationForPatriciaConstruction(true);

            constructionByAllWordsInsertionTimes.add(allWordsConstructionDuration);
            constructionByMergeTimes.add(mergeConstructionDuration);
        }

        String heading = "\nInsertions vs. Merge: Comparing the construction of a Patricia trie for " + REPETITIONS_PER_TEST + " repetitions:\n" +
                        SEPARATOR_LINE + "\n";
        System.out.println(heading);
        writeToResultsFile(heading);


        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                    "Construction time for all words insertion: " + constructionByAllWordsInsertionTimes.get(i) + " ms\n" +
                    "Construction time for tries merge: " + constructionByMergeTimes.get(i) + " ms\n";

//            System.out.println(times);
//            writeToResultsFile(times);
        }

        String aTimes =  "Average construction times:\n" +
                "Average construction time for all words insertion: " + calculateAverageForSeveralResults(constructionByAllWordsInsertionTimes) + " ms\n" +
                "Average construction time for tries merge: " + calculateAverageForSeveralResults(constructionByMergeTimes) + " ms\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }

    private static double getDurationForPatriciaConstruction(boolean isMerge) {
        Instant start = null;
        Instant end = null;

        if (isMerge) {
            ArrayList<PatriciaTrie> subTries = new ArrayList<>();

            try(Stream<Path> paths = Files.walk(Paths.get(DIRECTORY_PATH))) {
                paths.forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        GraphReader graphReader = new GraphReader(filePath.toString());

                        PatriciaTrie pt = new PatriciaTrie(new Alphabet());
                        pt.insert(graphReader.read());
                        subTries.add(pt);
                    }
                });
            } catch (Exception e) {
                System.err.println("Error while reading the files for Patricia trie merging: ");
                e.printStackTrace();
            }

            IPatriciaTrie tempTrie = new PatriciaTrie(new Alphabet());
            start = Instant.now();

            for (PatriciaTrie subTrie : subTries) {
                tempTrie = tempTrie.merge(subTrie);
            }

            end = Instant.now();
        } else {
            PatriciaTrie patriciaTrie = new PatriciaTrie(new Alphabet());

            start = Instant.now();
            patriciaTrie.insert(shakespeareWords);
            end = Instant.now();
        }

        return (double) Duration.between(start, end).toMillis();
    }
}

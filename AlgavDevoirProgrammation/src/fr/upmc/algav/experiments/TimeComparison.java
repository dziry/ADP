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
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.GraphReader;

/**
 * TODO:
 * - Test for conversion UHT/BHT vs. PT
 * - Test for getWordCount
 * - Test for prefix count
 */
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
	private static HashSet<String> shakespeareWords;

    private static final int REPETITIONS_PER_TEST = 10;

    private static final String UNKNOWN_INSERTED_WORD = "osahezrnthxshekdzsdfjdgefjhtnsh";

    private static final int NUMBER_OF_WORDS_TO_REMOVE = 20;


	public static void main(String[] args) {
        initWriter();
        initWordsSet();

        printTestBegin();

        doTrieConstructionTest();
        doUnknownWordInsertionTest();
        doSetOfWordsRemovalTest();
        doPatriciaConstructionsTest();

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

            System.out.println(times);
            writeToResultsFile(times);
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

            System.out.println(times);
            writeToResultsFile(times);
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

        HashSet<String> wordsToRemove = createRandomSetOfExistingWordsToRemove();

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

        System.out.println(removedWordsCaption);
        writeToResultsFile(removedWordsCaption);


        for (int i = 0; i < REPETITIONS_PER_TEST; i++) {
            String times =  "Repetition " + (i + 1) + ":\n" +
                            "Removal time for Hybrid Trie: " + hybridTrieRemovalTimes.get(i) + " ns\n" +
                            "Removal time for Balanced Hybrid Trie: " + balancedHybridTrieRemovalTimes.get(i) + " ns\n" +
                            "Removal time for Patricia Trie: " + patriciaTrieRemovalTimes.get(i) + " ns\n";

            System.out.println(times);
            writeToResultsFile(times);
        }

        String aTimes =  "Average removal times:\n" +
                "Average removal time for Hybrid Trie: " + calculateAverageForSeveralResults(hybridTrieRemovalTimes) + " ns\n" +
                "Average removal time for Balanced Hybrid Trie: " + calculateAverageForSeveralResults(balancedHybridTrieRemovalTimes) + " ns\n" +
                "Average removal time for Patricia Trie: " + calculateAverageForSeveralResults(patriciaTrieRemovalTimes) + " ns\n";

        System.out.println(aTimes);
        writeToResultsFile(aTimes);
    }

    private static HashSet<String> createRandomSetOfExistingWordsToRemove() {
        HashSet<String> res = new HashSet<>();

        ArrayList<String> existingWords = new ArrayList<>(shakespeareWords);

        while (res.size() < NUMBER_OF_WORDS_TO_REMOVE) {
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

            System.out.println(times);
            writeToResultsFile(times);
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


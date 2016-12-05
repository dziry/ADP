package fr.upmc.algav.experiments;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.TextFileReader;

public class StructureComparison {
	private final static String CONFIG = "%-25s%-15s%-15s%-15s%-15s%-20s%s\n";

	// Eclipse IDE
	private final static String DIRECTORY_PATH = "files/Shakespeare";
    // IntelliJ IDE
    //private final static String DIRECTORY_PATH = "AlgavDevoirProgrammation/files/Shakespeare";

    // Eclipse IDE
    private final static String RESULTS_PATH = "results/structure_results.txt";
    // IntelliJ IDE
    //private final static String RESULTS_PATH = "AlgavDevoirProgrammation/results/structure_results.txt";
    private final static String RESULTS_ENCODING = "utf-8";

    private static Writer resultsWriter;

    private static final String SEPARATOR_LINE = "------------------------------------------------------------------";

    public static void main(String[] args) {
		initWriter();
        printTestBegin();


		ITrie originalHT = new HybridTrie();
		ITrie balancedHT = new HybridTrie();
		ITrie patricia = new PatriciaTrie(new Alphabet());

		String arg0 = "File";
        String arg1 = "Nodes";
		String arg2 = "Words";
		String arg3 = "Null Pointers";
		String arg4 = "Height";
		String arg5 = "Average Leaf Depth";
		String arg6 = "Structure";

		try (Stream<Path> paths = Files.walk(Paths.get(DIRECTORY_PATH))) {
			System.out.format(CONFIG, arg0, arg1, arg2, arg3, arg4, arg5, arg6);
            writeToResultsFileFormatted(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
			System.out.println("------------------------------------------------------------------------------------------------------\n");
		    writeToResultsFile("------------------------------------------------------------------------------------------------------\n\n");

			paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		        	run(originalHT, filePath, TrieType.HYBRID);
		        	run(balancedHT, filePath, TrieType.BALANCED_HYBRID);
                    run(patricia, filePath, TrieType.PATRICIA);
		        	System.out.println("------------------------------------------------------------------------------------------------------\n");
                    writeToResultsFile("------------------------------------------------------------------------------------------------------\n\n");
		        }
		    });
		} catch (IOException e) {
			e.printStackTrace();
		}

        printTestEnd();
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

    private static void writeToResultsFile(String stringToWrite) {
        try {
            resultsWriter.write(stringToWrite);
        } catch (Exception e) {
            System.err.println("Error while writing to results file!");
            e.printStackTrace();
        }
    }

    private static void writeToResultsFileFormatted(String arg0, String arg1, String arg2,
                                                    String arg3, String arg4, String arg5, String arg6) {
        try {
            resultsWriter.write(String.format(CONFIG, arg0, arg1, arg2, arg3, arg4, arg5, arg6));
        } catch (Exception e) {
            System.err.println("Error while writing to results file!");
            e.printStackTrace();
        }
    }

    private static void printTestBegin() {
        String begin =  "BEGIN OF STRUCTURE COMPARISON\n" +
                SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n\n";

        //System.out.println(begin);
        writeToResultsFile(begin);
    }

    private static void printTestEnd() {
        String end =    "\n" + SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n" +
                SEPARATOR_LINE + "\n" +
                "END OF STRUCTURE COMPARISON";

        //System.out.println(end);
        writeToResultsFile(end);

        try {
            resultsWriter.close();
        } catch (Exception ex) {
            System.err.println("Error while closing results file writer!");
            ex.printStackTrace();
        }
    }
	
	private static void run(ITrie trie, Path filePath, TrieType type) {
		TextFileReader textFileReader = new TextFileReader(filePath.toString());
		ArrayList<String> wordsList = textFileReader.read();
		
		if (type == TrieType.HYBRID || type == TrieType.PATRICIA) {
			trie.insert(wordsList);			
		} else if (type == TrieType.BALANCED_HYBRID) {
			((HybridTrie) trie).insertBalanced(wordsList);
		}
			
		String fileName = filePath.getFileName().toString();
        int nodeCount = trie.getNodeCount();
		int words = trie.getWordCount();
		int nil = trie.getNullPointerCount();
		int height = trie.getHeight();
        BigDecimal aDepth = new BigDecimal(trie.getAverageDepthOfLeaves());
        aDepth = aDepth.setScale(2, RoundingMode.HALF_UP);
		double averageDepth = aDepth.doubleValue();
		String structMsg;

		if (type == TrieType.HYBRID) structMsg = "Original-HT";
		else if (type == TrieType.BALANCED_HYBRID) structMsg = "Balanced-HT";
		else structMsg = "Patricia";
			
		System.out.format(CONFIG, fileName, nodeCount, words, nil, height, averageDepth, structMsg);
        writeToResultsFileFormatted(fileName, Integer.toString(nodeCount), Integer.toString(words), Integer.toString(nil),
                Integer.toString(height), Double.toString(averageDepth), structMsg);
	}
}

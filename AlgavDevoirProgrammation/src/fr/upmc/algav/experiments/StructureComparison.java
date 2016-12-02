package fr.upmc.algav.experiments;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.GraphReader;


public class StructureComparison {
	private final static String CONFIG = "%-25s%-15s%-15s%-15s%-20s%s\n";

	// Eclipse IDE
	private final static String DIRECTORY_PATH = "files/Shakespeare";
    // IntelliJ IDE
    //private final static String DIRECTORY_PATH = "AlgavDevoirProgrammation/files/Shakespeare";
	
	public static void main(String[] args) {
		ITrie originalHT = new HybridTrie();
		ITrie balancedHT = new HybridTrie();
		ITrie patricia = new PatriciaTrie(new Alphabet());

		String arg1 = "File";
		String arg2 = "Words";
		String arg3 = "Null Pointers";
		String arg4 = "Height";
		String arg5 = "Average Leaf Depth";
		String arg6 = "Structure";

		try (Stream<Path> paths = Files.walk(Paths.get(DIRECTORY_PATH))) {
			System.out.format(CONFIG, arg1, arg2, arg3, arg4, arg5, arg6);
			System.out.println("------------------------------------------------------------------------------------------------------\n");
		    
			paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		        	run(originalHT, filePath, TrieType.HYBRID);
		        	run(balancedHT, filePath, TrieType.BALANCED_HYBRID);
                    run(patricia, filePath, TrieType.PATRICIA);
		        	System.out.println("------------------------------------------------------------------------------------------------------\n");
		        }
		    });
		} catch (IOException e) {
			e.printStackTrace();
		} 	
	}
	
	private static void run(ITrie trie, Path filePath, TrieType type) {
		GraphReader graphReader = new GraphReader(filePath.toString());
		ArrayList<String> wordsList = graphReader.read();
		
		if (type == TrieType.HYBRID || type == TrieType.PATRICIA) {
			trie.insert(wordsList);			
		} else if (type == TrieType.BALANCED_HYBRID) {
			((HybridTrie) trie).insertBalanced(wordsList);
		}
			
		String fileName = filePath.getFileName().toString();
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
			
		System.out.format(CONFIG, fileName, words, nil, height, averageDepth, structMsg);
	}
}

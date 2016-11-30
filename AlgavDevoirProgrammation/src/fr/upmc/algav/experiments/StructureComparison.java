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
    //private final static String DIRECTORY_PATH = "files/Shakespeare";
    // Alternate path for IntelliJ
    private final static String DIRECTORY_PATH = "AlgavDevoirProgrammation/files/Shakespeare";

	private enum Struct {
        Hybrid, BalancedHybrid, SortedHybrid, SortedBalancedHybrid, Patricia
	}
	
	public static void main(String[] args) {
		ITrie originalHT = new HybridTrie();
		ITrie balancedHT = new HybridTrie();
		ITrie patricia = new PatriciaTrie(new Alphabet());
		ITrie originalSortedHT = new HybridTrie();
		ITrie balancedSortedHT = new HybridTrie();		
				
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
		        	run(originalHT, filePath, Struct.Hybrid); 
		        	run(balancedHT, filePath, Struct.BalancedHybrid);
		        	run(originalSortedHT, filePath, Struct.SortedHybrid); 
		        	run(balancedSortedHT, filePath, Struct.SortedBalancedHybrid);
                    run(patricia, filePath, Struct.Patricia);
		        	System.out.println("------------------------------------------------------------------------------------------------------\n");
		        }
		    });
		} catch (IOException e) {
			e.printStackTrace();
		} 	
	}
	
	private static void run(ITrie trie, Path filePath, Struct struct) {
		GraphReader graphReader = new GraphReader(filePath.toString());
		ArrayList<String> wordsList = graphReader.read();
		
		if (struct == Struct.Hybrid || struct == Struct.Patricia) {
			trie.insert(wordsList);			
		} else if (struct == Struct.BalancedHybrid) {
			((HybridTrie) trie).insertBalanced(wordsList);
		} else if (struct == Struct.SortedHybrid) {
			Collections.sort(wordsList);
			trie.insert(wordsList);
		} else if (struct == Struct.SortedBalancedHybrid) {
			Collections.sort(wordsList);
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

		if (struct == Struct.Hybrid) structMsg = "Original-HT";			
		else if (struct == Struct.BalancedHybrid) structMsg = "Balanced-HT";
		else if (struct == Struct.SortedHybrid) structMsg = "O-HT sorted";
		else if (struct == Struct.SortedBalancedHybrid) structMsg = "B-HT sorted";
		else structMsg = "Patricia";
			
		System.out.format(CONFIG, fileName, words, nil, height, averageDepth, structMsg);
	}
}

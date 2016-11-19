package fr.upmc.algav.tests;

import java.io.IOException;
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
import fr.upmc.algav.tools.Reader;

public class ComparativeStudy { 

	private final static String config = "%-25s%-15s%-15s%-15s%-20s%s\n";
	private static enum Struct { Hybrid, BalancedHybrid, SortedHybrid, SortedBalancedHybrid, Patricia };
	
	public static void main(String[] args) {
		
		ITrie originalHT = new HybridTrie();
		ITrie balancedHT = new HybridTrie();
		ITrie patricia = new PatriciaTrie(new Alphabet());
		ITrie originalSortedHT = new HybridTrie();
		ITrie balancedSortedHT = new HybridTrie();		
				
		String arg1 = "File";
		String arg2 = "Words";
		String arg3 = "Null";
		String arg4 = "Height";
		String arg5 = "Average depth";
		String arg6 = "Structure";

		try (Stream<Path> paths = Files.walk(Paths.get("files/Shakespeare"))) {
						
			System.out.format(config, arg1, arg2, arg3, arg4, arg5, arg6);
			System.out.println("------------------------------------------------------------------------------------------------------\n");
		    
			paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		        	run(originalHT, filePath, Struct.Hybrid); 
		        	run(balancedHT, filePath, Struct.BalancedHybrid);
//		        	run(patricia, filePath, Struct.Patricia);
		        	run(originalSortedHT, filePath, Struct.SortedHybrid); 
		        	run(balancedSortedHT, filePath, Struct.SortedBalancedHybrid);
		        	System.out.println("------------------------------------------------------------------------------------------------------\n");
		        }
		    });
		} catch (IOException e) {
			e.printStackTrace();
		} 	
	}
	
	private static void run(ITrie trie, Path filePath, Struct struct) {
		Reader reader = new Reader(filePath.toString());
		ArrayList<String> wordsList = new ArrayList<String>();		
		
		wordsList = reader.read();
		
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
			
		String fileName = filePath.subpath(2, 3).toString();
		int words = trie.countWords();
		int nil = trie.countNull();
		int height = trie.height();
		double aDepth = trie.averageDepth();
		String structMsg;

		if (struct == Struct.Hybrid) structMsg = "Original-HT";			
		else if (struct == Struct.BalancedHybrid) structMsg = "Balanced-HT";
		else if (struct == Struct.SortedHybrid) structMsg = "O-HT sorted";
		else if (struct == Struct.SortedBalancedHybrid) structMsg = "B-HT sorted";
		else structMsg = "Patricia";
			
		System.out.format(config, fileName, words, nil, height, aDepth, structMsg);
				
	}
}

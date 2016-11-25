package fr.upmc.algav.experiments;

import java.util.ArrayList;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.tools.GraphReader;

/*
 * TODO
 */

public class VisualComparaison {

	public static void main(String[] args) {
		
		ArrayList<String> list = new ArrayList<String>();

		System.out.println("L'exemple du cours | One word by line :\n");		
		GraphReader graphReaderOne = new GraphReader("files/exerciseExample.txt");
		list = graphReaderOne.read();
		for (String word : list) {
			System.out.println(word);
		}
		System.out.println();
		ITrie hybridTrieExerciseExample = new HybridTrie();
		hybridTrieExerciseExample.insert(list);
		hybridTrieExerciseExample.print("hybridTrieExerciseExample");		
			
		System.out.println("\nL'exemple de base | Full Text :\n");
		GraphReader graphReaderFull = new GraphReader("files/basicExample.txt");
		list = graphReaderFull.read();
		for (String word : list) {
			System.out.println(word);
		}
		System.out.println();
		ITrie hybridTrieBasicExample = new HybridTrie();
		hybridTrieBasicExample.insert(list);
		hybridTrieBasicExample.print("hybridTrieBasicExample");
	}
}

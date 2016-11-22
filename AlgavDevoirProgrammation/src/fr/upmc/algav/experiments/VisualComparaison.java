package fr.upmc.algav.experiments;

import java.util.ArrayList;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.tools.Reader;

/*
 * TODO
 */

public class VisualComparaison {

	public static void main(String[] args) {
		
		ArrayList<String> list = new ArrayList<String>();

		System.out.println("L'exemple du cours | One word by line :\n");		
		Reader readerOne = new Reader("files/exerciseExample.txt");
		list = readerOne.read();
		for (String word : list) {
			System.out.println(word);
		}
		System.out.println();
		ITrie hybridTrieExerciseExample = new HybridTrie();
		hybridTrieExerciseExample.insert(list);
		hybridTrieExerciseExample.print("hybridTrieExerciseExample");		
			
		System.out.println("\nL'exemple de base | Full Text :\n");
		Reader readerFull = new Reader("files/basicExample.txt");
		list = readerFull.read();
		for (String word : list) {
			System.out.println(word);
		}
		System.out.println();
		ITrie hybridTrieBasicExample = new HybridTrie();
		hybridTrieBasicExample.insert(list);
		hybridTrieBasicExample.print("hybridTrieBasicExample");
	}
}

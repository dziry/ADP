package fr.upmc.algav.experiments;

import java.util.ArrayList;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.GraphReader;

public class VisualComparison {

	public static void main(String[] args) {
		ArrayList<String> list ;

		System.out.println("Course example | One word by line :\n");
		GraphReader graphReaderOne = new GraphReader("AlgavDevoirProgrammation/files/exerciseExample.txt");
		list = graphReaderOne.read();

		ITrie hybridTrieExerciseExample = new HybridTrie();
		hybridTrieExerciseExample.insert(list);
		hybridTrieExerciseExample.print("visual_comparison_example_course_HT.dot");

        ITrie patriciaTrieExerciseExample = new PatriciaTrie(new Alphabet());
        patriciaTrieExerciseExample.insert(list);
        patriciaTrieExerciseExample.print("visual_comparison_example_course_PT.dot");

        System.out.println("\nBasic example | Full text :\n");
		GraphReader graphReaderFull = new GraphReader("AlgavDevoirProgrammation/files/basicExample.txt");
		list = graphReaderFull.read();

		ITrie hybridTrieBasicExample = new HybridTrie();
		hybridTrieBasicExample.insert(list);
		hybridTrieBasicExample.print("visual_comparison_example_basic_HT.dot");

        ITrie patriciaTrieBasicExample = new PatriciaTrie(new Alphabet());
        patriciaTrieBasicExample.insert(list);
        patriciaTrieBasicExample.print("visual_comparison_example_basic_PT.dot");
	}
}

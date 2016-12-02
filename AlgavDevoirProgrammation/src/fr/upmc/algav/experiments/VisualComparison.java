package fr.upmc.algav.experiments;

import java.util.ArrayList;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.GraphReader;

public class VisualComparison {

	// Eclipse IDE
	private final static String EXERCISE_EXAMPLE_FILE_PATH = "files/exerciseExample.txt";
	// IntelliJ IDE
	//private final static String EXERCISE_EXAMPLE_FILE_PATH = "AlgavDevoirProgrammation/files/exerciseExample.txt";

	// Eclipse IDE
	private final static String BASIC_EXAMPLE_FILE_PATH = "files/basicExample.txt";
	// IntelliJ IDE
	//private final static String BASIC_EXAMPLE_FILE_PATH = "AlgavDevoirProgrammation/files/basicExample.txt";

	public static void main(String[] args) {
		ArrayList<String> list ;

		System.out.println("Course example | One word by line :\n");
		GraphReader graphReaderOne = new GraphReader(EXERCISE_EXAMPLE_FILE_PATH);
		list = graphReaderOne.read();

		ITrie hybridTrieExerciseExample = new HybridTrie();
		hybridTrieExerciseExample.insert(list);
		hybridTrieExerciseExample.print("visual_comparison_example_course_HT.dot");

        ITrie patriciaTrieExerciseExample = new PatriciaTrie(new Alphabet());
        patriciaTrieExerciseExample.insert(list);
        patriciaTrieExerciseExample.print("visual_comparison_example_course_PT.dot");

        System.out.println("\nBasic example | Full text :\n");
		GraphReader graphReaderFull = new GraphReader(BASIC_EXAMPLE_FILE_PATH);
		list = graphReaderFull.read();

		ITrie hybridTrieBasicExample = new HybridTrie();
		hybridTrieBasicExample.insert(list);
		hybridTrieBasicExample.print("visual_comparison_example_basic_HT.dot");

        ITrie patriciaTrieBasicExample = new PatriciaTrie(new Alphabet());
        patriciaTrieBasicExample.insert(list);
        patriciaTrieBasicExample.print("visual_comparison_example_basic_PT.dot");
	}
}

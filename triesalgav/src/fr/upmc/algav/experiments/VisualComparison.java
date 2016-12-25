package fr.upmc.algav.experiments;

import java.util.ArrayList;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import fr.upmc.algav.tools.TextFileReader;

public class VisualComparison {

	private final static String EXERCISE_EXAMPLE_FILE_PATH = "triesalgav/files/exerciseExample.txt";
	private final static String BASIC_EXAMPLE_FILE_PATH = "triesalgav/files/basicExample.txt";

	public static void main(String[] args) {
		ArrayList<String> list ;

		System.out.println("Course example | One word by line :\n");
		TextFileReader textFileReaderOne = new TextFileReader(EXERCISE_EXAMPLE_FILE_PATH);
		list = textFileReaderOne.read();

		ITrie hybridTrieExerciseExample = new HybridTrie();
		hybridTrieExerciseExample.insert(list);
		hybridTrieExerciseExample.print("visual_comparison_example_course_HT.dot");

        ITrie patriciaTrieExerciseExample = new PatriciaTrie(new Alphabet());
        patriciaTrieExerciseExample.insert(list);
        patriciaTrieExerciseExample.print("visual_comparison_example_course_PT.dot");

        System.out.println("\nBasic example | Full text :\n");
		TextFileReader textFileReaderFull = new TextFileReader(BASIC_EXAMPLE_FILE_PATH);
		list = textFileReaderFull.read();

		ITrie hybridTrieBasicExample = new HybridTrie();
		hybridTrieBasicExample.insert(list);
		hybridTrieBasicExample.print("visual_comparison_example_basic_HT.dot");

        ITrie patriciaTrieBasicExample = new PatriciaTrie(new Alphabet());
        patriciaTrieBasicExample.insert(list);
        patriciaTrieBasicExample.print("visual_comparison_example_basic_PT.dot");
	}
}

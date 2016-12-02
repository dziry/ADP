package fr.upmc.algav.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.upmc.algav.errors.HybridTrieError;
import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.tools.GraphReader;

public class HybridTrieTest extends AbstractTrieTest {
	// Eclipse IDE
	private static final String EXAMPLE_PATH = "files/exerciseExample.txt";
	// IntelliJ IDE
	//private static final String EXAMPLE_PATH = "AlgavDevoirProgrammation/files/exerciseExample.txt";
	private static ArrayList<String> wordsList = null;
    private static ITrie hybridTrie = null;
		
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {
        GraphReader graphReader = new GraphReader(EXAMPLE_PATH);
		wordsList = new ArrayList<>();
		wordsList = graphReader.read();

		hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}
    
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void testHybridTrie() {
		assertNotNull("Hybrid trie is not null: ", new HybridTrie());
	}

	@Test
	public final void testIsEmpty_beforeAddingWords() {
		assertTrue("Hybrid trie is not empty: ", new HybridTrie().isEmpty());
	}
	
	@Test
	public final void testIsEmpty_afterAddingWords() {
		assertFalse("Hybrid trie is not empty: ", hybridTrie.isEmpty());
	}
	
	@Test
	public final void testRemoveAll() {
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert("word1");
		hybridTrie.insert("word2");
		hybridTrie.insert("word3");
		hybridTrie.removeAll();

		assertTrue("Hybrid trie is empty after remove all: ", hybridTrie.isEmpty());
	}
	
	@Test
	public final void testInsertWordsOneByOne() {
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert("lou");
		hybridTrie.insert("leve");
		hybridTrie.insert("les");
		hybridTrie.insert("loups");
		hybridTrie.insert("dans");
		hybridTrie.insert("le");
		hybridTrie.insert("lourds");

        final int expectedHybridTrieSize = 7;
		final int calculatedHybridTrieSize = hybridTrie.getWordCount();

        assertEquals(expectedHybridTrieSize + " words added successfully: ", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testInsertWordsFromList() {
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);

		final int expectedHybridTrieSize = 12;
		final int calculatedHybridTrieSize = hybridTrie.getWordCount();

        assertEquals("List of words added successfully: ", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testInsertWordExist() {
		final int oldHybridTrieSize = hybridTrie.getWordCount();
		hybridTrie.insert("dans");
		final int newHybridTrieSize = hybridTrie.getWordCount();

		assertEquals("Inserting an existing word which should be ignored: ", oldHybridTrieSize, newHybridTrieSize);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertNullWord() {
		final String word = null;
		hybridTrie.insert(word);
	}

	@Test (expected = HybridTrieError.class)
	public final void testInsertEmptyWord() {
		hybridTrie.insert("");
	}

	@Test (expected = HybridTrieError.class)
	public final void testInsertNullWords() {
		final ArrayList<String> words = null;
		hybridTrie.insert(words);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertEmptyWords() {
		final ArrayList<String> words = new ArrayList<String>();
		hybridTrie.insert(words);
	}
	
	@Test
	public final void runNominalTestSearch_beforeAddingWords() {				
		testSearch(new HybridTrie(), "lourds", false);
		testSearch(new HybridTrie(), "dans", false);
	}
	
	@Test
	public final void runNominalTestSearch_afterAddingWords() {				
		testSearch(hybridTrie, "lourds", true);
		testSearch(hybridTrie, "dans", true);
		testSearch(hybridTrie, "le", true);
		testSearch(hybridTrie, "lourd", false);
		testSearch(hybridTrie, "lour", false);
		testSearch(hybridTrie, "d", false);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestSearch_CaseForWordIsNull() {
		testSearch(hybridTrie, null, false);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestSearch_CaseForWordIsEmpty() {
		testSearch(hybridTrie, "", false);
	}
	
	@Test
	public final void runNominalTestCountWords_beforeAddingWords() {				
		testCountWords(new HybridTrie(), 0);
	}
	
	@Test
	public final void runNominalTestCountWords_afterAddingWords() {
		testCountWords(hybridTrie, 12);
	}
	
	@Test
	public final void runNominalTestListWords_beforeAddingWords() {				
		testListWords(new HybridTrie(), null);
	}
	
	@Test
	public final void runNominalTestListWords_afterAddingWords() {
		ArrayList<String> wordsListClone = new ArrayList<String>(wordsList);		
		Collections.sort(wordsListClone);
		testListWords(hybridTrie, wordsListClone);
	}
		
	@Test
	public final void runNominalTestCountNull_beforeAddingWords() {
		testCountNull(new HybridTrie(), 0);
	}
	
	@Test
	public final void runNominalTestCountNull_afterAddingWords() {
		testCountNull(hybridTrie, 69);
	}
	
	@Test
	public final void runNominalTestHeight_beforeAddingWords() {
		testHeight(new HybridTrie(), 0);
	}
	
	@Test
	public final void runNominalTestHeight_afterAddingWords() {
		testHeight(hybridTrie, 6);
	}

	@Test
	public final void runNominalTestAverageDepth_beforeAddingWords() {
		testAverageDepth(new HybridTrie(), 0);
	}
	
	@Test
	public final void runNominalTestAverageDepth_afterAddingWords() {
		final double totalDepthForLeaves = 45.0;
		final double leavesCount = 10.0;
		testAverageDepth(hybridTrie, totalDepthForLeaves / leavesCount);
	}

	@Test
	public final void runNominalTestPrefix_beforeAddingWords() {
		testPrefix(new HybridTrie(), "lou", 0);
		testPrefix(new HybridTrie(), "ta", 0);
		testPrefix(new HybridTrie(), "l", 0);
	}
	
	@Test
	public final void runNominalTestPrefix_afterAddingWords() {
		testPrefix(hybridTrie, "lou", 3);
		testPrefix(hybridTrie, "ta", 1);
		testPrefix(hybridTrie, "lox", 0);
		testPrefix(hybridTrie, "lourds", 1);
		testPrefix(hybridTrie, "lourd.", 0);
		testPrefix(hybridTrie, "le", 3);
		testPrefix(hybridTrie, "t", 1);
		testPrefix(hybridTrie, "l", 7);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestPrefix_caseForWordIsNull() {
		testPrefix(hybridTrie, null, -1);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestPrefix_caseForWordIsEmpty() {
		testPrefix(hybridTrie, "", -1);
	}
	
	@Test
	public final void runNominalTestRemove_beforeAddingWords() {
		assertFalse("Removing from an empty hybrid trie: ", new HybridTrie().remove("hello"));
	}
	
	@Test
	public final void runNominalTestRemove_afterAddingWords() {
		// Set up before starting removing words
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);
		hybridTrie.insert("l");
		hybridTrie.print("original");
			
		// Run tests after adding words
		assertFalse("Removing a word that does not exist in the hybrid trie: ", hybridTrie.remove("lourd"));
		assertFalse("Removing a word that does not exist in the hybrid trie: ", hybridTrie.remove("lourdss"));
		testRemove(hybridTrie, "luxe", hybridTrie.getNullPointerCount() - 6, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "leve", hybridTrie.getNullPointerCount() - 4, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "les", hybridTrie.getNullPointerCount() - 2, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "lou", hybridTrie.getNullPointerCount() - 0, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "lourds", hybridTrie.getNullPointerCount() - 6, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "loups", hybridTrie.getNullPointerCount() - 8, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "le", hybridTrie.getNullPointerCount() - 2, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "l", hybridTrie.getNullPointerCount() - 2, hybridTrie.getHeight() - 1);
		testRemove(hybridTrie, "olive", hybridTrie.getNullPointerCount() - 10, hybridTrie.getHeight() - 1);
		testRemove(hybridTrie, "tapis", hybridTrie.getNullPointerCount() - 10, hybridTrie.getHeight() - 0);
		testRemove(hybridTrie, "vert", hybridTrie.getNullPointerCount() - 8, hybridTrie.getHeight() - 1);
		testRemove(hybridTrie, "dans", hybridTrie.getNullPointerCount() - 6, hybridTrie.getHeight() - 2);
		testRemove(hybridTrie, "de", hybridTrie.getNullPointerCount() - 5, hybridTrie.getHeight() - 1);
	}

	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestRemove_caseForWordIsNull() {
		new HybridTrie().remove(null);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestRemove_caseForWordIsEmpty() {
		new HybridTrie().remove("");
	}
	
	@Test
	public final void testPrint() {
		hybridTrie.print("HT_printTest");
		final String expectedPrintResult = "\n"
				 + "In order to know if the print test goes well, you should make a visual check.\n"
				 + "First, go to '/drawables' and open the file HT_printTest.txt with a DOT Viewer.\n"
				 + "Then, make sure that this properties are correct :\n"
				 + "- The number of nodes is 34,\n"
				 + "- The number of final nodes is 12,\n"
				 + "- The number of leaves is 10,\n"
				 + "- The number of red arcs is 24,\n"
				 + "- The number of blue arcs is 4,\n"
				 + "- The number of green arcs is 5,\n"
				 + "- The number of nil pointers is 69,\n"
				 + "- The average depth is 4.5,\n"
				 + "- The height is 6.\n";
		System.out.println(expectedPrintResult);		   
	}
	
	@Test
	public final void testInsertBalanced() {
		/* The purpose of this manipulation is to make an unbalanced tries by inserting a few words in order. */
		// Eclipse IDE
		GraphReader graphReader = new GraphReader("files/Shakespeare/john.txt");
		// IntelliJ IDE
		//GraphReader graphReader = new GraphReader("AlgavDevoirProgrammation/files/Shakespeare/john.txt");
		ArrayList<String> wordsListFromFile = new ArrayList<>();
		ArrayList<String> sortedList = new ArrayList<>();
		ArrayList<String> ordinaryList = new ArrayList<>();

		wordsListFromFile = graphReader.read();
		
		// Split the original list to two other lists. 
		for (int wordIndex = 0; wordIndex < wordsListFromFile.size(); wordIndex++) {
			if (wordIndex % 500 == 0) {
				sortedList.add(wordsListFromFile.get(wordIndex));
			} else {
				ordinaryList.add(wordsListFromFile.get(wordIndex));
			}
		}
		
		// Sort the first list in an ascending order and keep the second one as it is.
		Collections.sort(sortedList);

		// Insert both lists in each trie
		HybridTrie hybridTrie = new HybridTrie();
		hybridTrie.insert(sortedList);
		hybridTrie.insert(ordinaryList);
		
		HybridTrie balancedHybridTrie = new HybridTrie();		
		balancedHybridTrie.insertBalanced(sortedList);
		balancedHybridTrie.insertBalanced(ordinaryList);
		
		// insertBalanced() method should guarantee a better average depth i.e. a balanced trie.
		assertTrue("Insert balanced: ", balancedHybridTrie.getAverageDepthOfLeaves() < hybridTrie.getAverageDepthOfLeaves());
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertBalanced_caseForWordIsNull() {
		final String word = null;
		((HybridTrie) hybridTrie).insertBalanced(word);
	}

	@Test (expected = HybridTrieError.class)
	public final void testInsertBalanced_caseForWordIsEmpty() {
		((HybridTrie) hybridTrie).insertBalanced("");
	}

	@Test (expected = HybridTrieError.class)
	public final void testInsertBalanced_caseForWordsListIsNull() {
		final ArrayList<String> words = null;
		((HybridTrie) hybridTrie).insertBalanced(words);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertBalanced_caseForWordsListIsEmpty() {
		final ArrayList<String> words = new ArrayList<String>();
		((HybridTrie) hybridTrie).insertBalanced(words);
	}
	
	@Test
	public final void testToPatriciaTrie() {
		// TODO
//		IPatriciaTrie patriciaTrie = new PatriciaTrie(new Alphabet());
//		patriciaTrie.insert(wordsList);
//		IPatriciaTrie patriciaTrieFromHT = ((HybridTrie) hybridTrie).toPatriciaTrie();
//		assertTrue("insert balanced", patriciaTrie.getStoredWords() == patriciaTrieFromHT.getStoredWords());
	}
}

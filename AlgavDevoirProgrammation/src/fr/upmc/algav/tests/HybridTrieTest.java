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
import fr.upmc.algav.hybridtries.IHybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.tools.GraphReader;

public class HybridTrieTest extends AbstractTrieTest {

	private static final String EXAMPLE_PATH = "files/exerciseExample.txt";
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
	public final void testIsEmpty() {
		// Before adding words
		assertTrue("Hybrid trie is not empty: ", new HybridTrie().isEmpty());

		// After adding words
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
	public final void runNominalTestSearch() {				
		// Before adding words
		testSearch(new HybridTrie(), "lourds", false);
		testSearch(new HybridTrie(), "dans", false);
		
		// After adding words
		testSearch(hybridTrie, "lourds", true);
		testSearch(hybridTrie, "dans", true);
		testSearch(hybridTrie, "le", true);
		testSearch(hybridTrie, "lourd", false);
		testSearch(hybridTrie, "lour", false);
		testSearch(hybridTrie, "d", false);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestSearch_1() {
		// Case for word is null
		testSearch(hybridTrie, null, false);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestSearch_2() {
		// Case for word is empty
		testSearch(hybridTrie, "", false);
	}
	
	@Test
	public final void runNominalTestCountWords() {				
		// Before adding words
		testCountWords(new HybridTrie(), 0);
		
		// After adding words
		testCountWords(hybridTrie, 12);
	}
	
	@Test
	public final void runNominalTestListWords() {				
		// Before adding words
		testListWords(new HybridTrie(), null);
		
		// After adding words
		ArrayList<String> wordsListClone = new ArrayList<String>(wordsList);		
		Collections.sort(wordsListClone);
		testListWords(hybridTrie, wordsListClone);
	}
		
	@Test
	public final void runNominalTestCountNull() {
		// Before adding words
		testCountNull(new HybridTrie(), 0);
		
		// After adding words
		testCountNull(hybridTrie, 69);
	}
	
	@Test
	public final void runNominalTestHeight() {
		// Before adding words
		testHeight(new HybridTrie(), 0);
		
		// After adding words
		testHeight(hybridTrie, 6);
	}

	@Test
	public final void runNominalTestAverageDepth() {
		// Before adding words
		testAverageDepth(new HybridTrie(), 0);

		// After adding words
		final double someDepths = 49.0;
		final double someNodes = 34.0;
		testAverageDepth(hybridTrie, someDepths/someNodes);
	}

	@Test
	public final void runNominalTestPrefix() {
		// Before adding words
		testPrefix(new HybridTrie(), "lou", 0);
		testPrefix(new HybridTrie(), "ta", 0);
		testPrefix(new HybridTrie(), "l", 0);
		
		// After adding words
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
	public final void runExceptionalTestPrefix_1() {
		// Case for word is null
		testPrefix(hybridTrie, null, -1);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestPrefix_2() {
		// Case for word is empty
		testPrefix(hybridTrie, "", -1);
	}
	
	@Test
	public final void runNominalTestRemove() {
		// Before adding words
		assertFalse("Removing from an empty hybrid trie: ", new HybridTrie().remove("hello"));
		
		// Set up before starting removing words
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);
		hybridTrie.insert("l");
		hybridTrie.print("original");
			
		// After adding words
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
	public final void runExceptionalTestRemove_1() {
		// Case for word is null
		new HybridTrie().remove(null);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestRemove_2() {
		// Case for word is empty
		new HybridTrie().remove("");
	}
	
	@Test
	public final void testInsertBalanced() {
		GraphReader graphReader = new GraphReader("files/Shakespeare/john.txt");
		ArrayList<String> wordsList = new ArrayList<>();
		wordsList = graphReader.read();
		HybridTrie hybridTrie = new HybridTrie();
		IHybridTrie balancedHybridTrie = new HybridTrie();
		
		hybridTrie.insert(wordsList);
		balancedHybridTrie.insertBalanced(wordsList);
		
		assertTrue("Insert balanced: ", balancedHybridTrie.getAverageDepthOfLeaves() < hybridTrie.getAverageDepthOfLeaves());
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertBalancedNullWord() {
		final String word = null;
		((HybridTrie) hybridTrie).insertBalanced(word);
	}

	@Test (expected = HybridTrieError.class)
	public final void testInsertBalancedEmptyWord() {
		((HybridTrie) hybridTrie).insertBalanced("");
	}

	@Test (expected = HybridTrieError.class)
	public final void testInsertBalancedNullWords() {
		final ArrayList<String> words = null;
		((HybridTrie) hybridTrie).insertBalanced(words);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertBalancedEmptyWords() {
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

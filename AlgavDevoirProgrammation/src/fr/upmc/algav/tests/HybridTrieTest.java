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
import fr.upmc.algav.tools.Reader;

public class HybridTrieTest extends AbstractTrieTest {

	private static final String EXAMPLE_PATH = "files/exerciseExample.txt";
	private static ArrayList<String> wordsList = null;
	private static Reader reader = null;
	private static ITrie hybridTrie = null;
		
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {		
		reader = new Reader(EXAMPLE_PATH);
		wordsList = new ArrayList<String>();
		wordsList = reader.read();
		hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);		
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}
    
	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void testHybridTrie() {
		assertNotNull("hybridTrie not null", new HybridTrie());
	}

	@Test
	public final void testIsEmpty() {
		// before adding words
		assertTrue("hybridTrie is not empty", new HybridTrie().isEmpty());

		// after adding words
		assertFalse("hybridTrie is not empty", hybridTrie.isEmpty());
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
		final int calculatedHybridTrieSize = hybridTrie.countWords();
		assertEquals(expectedHybridTrieSize + " words added successfully", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testInsertWordsFromList() {
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);
		final int expectedHybridTrieSize = 12;
		final int calculatedHybridTrieSize = hybridTrie.countWords();
		assertEquals("a list of words added successfully", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testInsertWordExist() {
		final int oldHybridTrieSize = hybridTrie.countWords();
		hybridTrie.insert("dans");
		final int newHybridTrieSize = hybridTrie.countWords();
		assertEquals("insert existing word? -> ignore", oldHybridTrieSize, newHybridTrieSize);
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
//		// before adding words
//		testSearch(new HybridTrie(), "lourds", false);
//		testSearch(new HybridTrie(), "dans", false);
//		
//		// after adding words
//		testSearch(hybridTrie, "lourds", true);
//		testSearch(hybridTrie, "dans", true);
//		testSearch(hybridTrie, "le", true);
//		testSearch(hybridTrie, "lourd", false);
//		testSearch(hybridTrie, "lour", false);
//		testSearch(hybridTrie, "d", false);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestSearch_1() {
		// case word is null
		testSearch(hybridTrie, null, false);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestSearch_2() {
		// case word is empty
		testSearch(hybridTrie, "", false);
	}
	
	@Test
	public final void runNominalTestCountWords() {				
		// before adding words
		testCountWords(new HybridTrie(), 0);
		
		// after adding words
		testCountWords(hybridTrie, 12);
	}
	
	@Test
	public final void runNominalTestListWords() {				
		// before adding words
		testListWords(new HybridTrie(), null);
		
		// after adding words
		Collections.sort(wordsList);
		testListWords(hybridTrie, wordsList);
	}
		
	@Test
	public final void runNominalTestCountNull() {
		// before adding words
		testCountNull(new HybridTrie(), 0);
		
		// after adding words
		testCountNull(hybridTrie, 69);
	}
	
	@Test
	public final void runNominalTestHeight() {
		// before adding words
		testHeight(new HybridTrie(), 0);
		
		// after adding words
		testHeight(hybridTrie, 6);
	}

	@Test
	public final void runNominalTestAverageDepth() {
		// before adding words
		testAverageDepth(new HybridTrie(), 0);

		// after adding words
		final double someDepths = 49.0;
		final double someNodes = 34.0;
		testAverageDepth(hybridTrie, someDepths/someNodes);
	}

	@Test
	public final void runNominalTestPrefix() {
		// before adding words
		testPrefix(new HybridTrie(), "lou", 0);
		testPrefix(new HybridTrie(), "ta", 0);
		testPrefix(new HybridTrie(), "l", 0);
		
		// after adding words
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
		// case word is null
		testPrefix(hybridTrie, null, -1);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestPrefix_2() {
		// case word is empty
		testPrefix(hybridTrie, "", -1);
	}
	
	@Test
	public final void testRemove() {
		// TODO
	}

	@Test
	public final void testPrint() {
		// TODO
	}

	@Test
	public final void testIsBalanced() {
		// TODO
	}
	
	@Test
	public final void testBalance() {
		// TODO
	}
	
	@Test
	public final void testToPatriciaTrie() {
		// TODO
	}
}

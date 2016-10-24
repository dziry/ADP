package fr.upmc.algav.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.upmc.algav.errors.HybridTrieError;
import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.tools.Reader;

public class HybridTrieTest {

	private static final String EXAMPLE_PATH = "files/exerciseExample.txt";
	private static ArrayList<String> wordsList = null;
	private static Reader reader = null;
	private static ITrie hybridTrie;
	
	@SuppressWarnings("null")
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
		ITrie hybridTrie = new HybridTrie();
		assertNotNull("hybridTrie not null", hybridTrie);
	}

	@Test
	public final void testIsEmpty() {
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
		assertEquals("7 words added successfully", hybridTrie.countWords(), 7);
	}
	
	@SuppressWarnings("null")
	@Test
	public final void testInsertWordsFromList() {
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);
		final int expectedHybridTrieSize = 12;
		int calculatedHybridTrieSize = hybridTrie.countWords();
		assertEquals("a list of words added successfully", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testInsertWordExist() {
		int oldHybridTrieSize = hybridTrie.countWords();
		hybridTrie.insert("dans");
		int newHybridTrieSize = hybridTrie.countWords();
		assertEquals("insert existing word? -> ignore", oldHybridTrieSize, newHybridTrieSize);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertEmpty() {
		hybridTrie.insert("");
	}

	@Test
	public final void testSearch() {
		assertTrue("words found", hybridTrie.search("lourds")
		                       && hybridTrie.search("dans")
		                       && hybridTrie.search("le"));
		
		assertFalse("words not found", hybridTrie.search("lourd")
					                && hybridTrie.search("lour")
					                && hybridTrie.search(""));
	}

	@Test
	public final void testCountWordsAtBeginning() {
		ITrie hybridTrie = new HybridTrie();
		final int expectedHybridTrieSize = 0;
		int calculatedHybridTrieSize = hybridTrie.countWords();
		assertEquals("there is "+ expectedHybridTrieSize + " word", calculatedHybridTrieSize, expectedHybridTrieSize);	
	}
	
	@Test
	public final void testCountWordsAtEnd() {
		final int expectedHybridTrieSize = 12;
		int calculatedHybridTrieSize = hybridTrie.countWords();
		assertEquals("there are "+ expectedHybridTrieSize + " words", calculatedHybridTrieSize, expectedHybridTrieSize);
		ITrie hybridTrie = new HybridTrie();
		assertEquals("there is 0 word", hybridTrie.countWords(), 0);
	}
	
	@Test
	public final void testListWords() {
		// TODO
	}

	@Test
	public final void testCountNull() {
		// TODO
	}

	@Test
	public final void testHeight() {
		// TODO
	}

	@Test
	public final void testAverageDepth() {
		// TODO
	}

	@Test
	public final void testPrefix() {
		// TODO
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
	public final void testToPatriciaTrie() {
		// TODO
	}

	@Test
	public final void testBalance() {
		// TODO
	}
}

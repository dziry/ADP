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
		ITrie hybridTrie = new HybridTrie();
		assertTrue("hybridTrie is not empty", hybridTrie.isEmpty());
	}
	
	@Test
	public final void testIsNotEmpty() {
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
	
	@SuppressWarnings("null")
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
	public final void testInsertEmpty() {
		hybridTrie.insert("");
	}

	@Test
	public final void testSearchPositive() {
		assertTrue("words found", hybridTrie.search("lourds"));
		assertTrue("words found", hybridTrie.search("dans"));
		assertTrue("words found", hybridTrie.search("le"));		                       
	}
	
	@Test
	public final void testSearchNegtive() {	
		assertFalse("words not found", hybridTrie.search("lourd"));
		assertFalse("words not found", hybridTrie.search("lour"));
		assertFalse("words not found", hybridTrie.search("d"));
	}

	@Test
	public final void testCountWordsAtBeginning() {
		ITrie hybridTrie = new HybridTrie();
		final int expectedHybridTrieSize = 0;
		final int calculatedHybridTrieSize = hybridTrie.countWords();
		assertEquals("there is "+ expectedHybridTrieSize + " word", calculatedHybridTrieSize, expectedHybridTrieSize);	
	}
	
	@Test
	public final void testCountWordsAtEnd() {
		final int expectedHybridTrieSize = 12;
		final int calculatedHybridTrieSize = hybridTrie.countWords();
		assertEquals("there are "+ expectedHybridTrieSize + " words", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testListWords() {				
		Collections.sort(wordsList);
		final ArrayList<String> expectedHybridTrieList = wordsList;
		final ArrayList<String> calculatedHybridTrieList = hybridTrie.listWords();
		assertEquals("listing words by ascending order", calculatedHybridTrieList, expectedHybridTrieList);
	}	

	@Test
	public final void testCountNull() {
		final int expectedHybridTrieList = 69;
		final int calculatedHybridTrieList = hybridTrie.countNull();		
		assertEquals(expectedHybridTrieList + " nil pointers found", calculatedHybridTrieList, expectedHybridTrieList);
	}
	
	@Test
	public final void testHeightAtBeginning() {
		ITrie hybridTrie = new HybridTrie();
		final int expectedHybridTrieHeight = 0;
		final int calculatedHybridTrieHeight = hybridTrie.height();
		assertEquals("the height is " + expectedHybridTrieHeight, calculatedHybridTrieHeight, expectedHybridTrieHeight);	
	}

	@Test
	public final void testHeightAtEnd() {
		final int expectedHybridTrieHeight = 6;
		final int calculatedHybridTrieHeight = hybridTrie.height();
		assertEquals("the height is " + expectedHybridTrieHeight, calculatedHybridTrieHeight, expectedHybridTrieHeight);	
	}
	
	@Test
	public final void testAverageDepth() {
		final int someDepths = 49;
		final int someNodes = 34;
		final double delta = 0.1; //FIXME
		final double expectedHybridTrieAverageDepth = (double)someDepths / (double)someNodes;
		final double calculatedHybridTrieAverageDepth = hybridTrie.averageDepth();
		assertEquals("the average depth is " + expectedHybridTrieAverageDepth, calculatedHybridTrieAverageDepth, expectedHybridTrieAverageDepth, delta);
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

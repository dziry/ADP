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
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.patriciatries.PatriciaTrie;
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
	public void setUp() throws Exception { }

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
		final int calculatedHybridTrieSize = hybridTrie.getWordCount();
		assertEquals(expectedHybridTrieSize + " words added successfully", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testInsertWordsFromList() {
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);
		final int expectedHybridTrieSize = 12;
		final int calculatedHybridTrieSize = hybridTrie.getWordCount();
		assertEquals("a list of words added successfully", calculatedHybridTrieSize, expectedHybridTrieSize);
	}
	
	@Test
	public final void testInsertWordExist() {
		final int oldHybridTrieSize = hybridTrie.getWordCount();
		hybridTrie.insert("dans");
		final int newHybridTrieSize = hybridTrie.getWordCount();
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
		// before adding words
		testSearch(new HybridTrie(), "lourds", false);
		testSearch(new HybridTrie(), "dans", false);
		
		// after adding words
		testSearch(hybridTrie, "lourds", true);
		testSearch(hybridTrie, "dans", true);
		testSearch(hybridTrie, "le", true);
		testSearch(hybridTrie, "lourd", false);
		testSearch(hybridTrie, "lour", false);
		testSearch(hybridTrie, "d", false);
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
		ArrayList<String> wordsListClone = new ArrayList<String>(wordsList);		
		Collections.sort(wordsListClone);
		testListWords(hybridTrie, wordsListClone);
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
	public final void runNominalTestRemove() {
		// before adding words
		assertFalse("remove from an empty trie", new HybridTrie().remove("hello"));
		
		// setUp before start removing words
		ITrie hybridTrie = new HybridTrie();
		hybridTrie.insert(wordsList);
		hybridTrie.insert("l");
		hybridTrie.print("original");
			
		// after adding words
		assertFalse("remove a word that does not exist in the trie", hybridTrie.remove("lourd"));
		assertFalse("remove a word that does not exist in the trie", hybridTrie.remove("lourdss"));
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
		// case word is null
		new HybridTrie().remove(null);
	}
	
	@Test (expected = HybridTrieError.class)
	public final void runExceptionalTestRemove_2() {
		// case word is empty
		new HybridTrie().remove("");
	}
	
	@Test
	public final void testPrint() {
		// TODO		
	}
	
	@Test
	public final void testInsertBalanced() {
		Reader reader = new Reader("files/Shakespeare/john.txt");
		ArrayList<String> wordsList = new ArrayList<String>();
		wordsList = reader.read();		
		HybridTrie hybridTrie = new HybridTrie();
		IHybridTrie balancedHybridTrie = new HybridTrie();
		
		hybridTrie.insert(wordsList);
		balancedHybridTrie.insertBalanced(wordsList);
		
		assertTrue("insert balanced", balancedHybridTrie.getAverageDepth() < hybridTrie.getAverageDepth());
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
		IPatriciaTrie patriciaTrie = new PatriciaTrie(new Alphabet());
		patriciaTrie.insert(wordsList);
		IPatriciaTrie patriciaTrieFromHT = ((HybridTrie) hybridTrie).toPatriciaTrie();
		assertTrue("insert balanced", patriciaTrie.getStoredWords() == patriciaTrieFromHT.getStoredWords());
	}
}

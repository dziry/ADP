package fr.upmc.algav.tests;

import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatriciaTrieTest extends AbstractTrieTest {
	private PatriciaTrie patriciaTrie;

	private static final String[] TEST_DATA = {
			"ROMANE", "ROMANUS", "ROMULUS", "RUBENS", "RUBER",
			"RUBICON", "RUBICUNDUS", "RUB", "hello", "ROMULUSBBB",
			"ROMANE", "ROMANE", "RUBER", "ROMANE", "RUB"
	};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {
		patriciaTrie = new PatriciaTrie(new Alphabet());
		insertTestData();
	}

	private void insertTestData() {
		for (int i = 0; i < TEST_DATA.length; i++) {
			patriciaTrie.insert(TEST_DATA[i]);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void search_existingWord_overSeveralEdges() {
		boolean wordInTrie = patriciaTrie.search("ROMANE");
		assertTrue(wordInTrie);
	}

	@Test
	public void search_existingWord_atOnlyOneEdge() {
		boolean wordInTrie = patriciaTrie.search("hello");
		assertTrue(wordInTrie);
	}

	@Test
	public void search_existingWord_withResultOnlyEdge() {
		boolean wordInTrie = patriciaTrie.search("RUB");
		assertTrue(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_completelyNotInTrie() {
		boolean wordInTrie = patriciaTrie.search("NOTINTREE");
		assertFalse(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_prefixPartlyInTreeOverEdges() {
		boolean wordInTrie = patriciaTrie.search("ROM");
		assertFalse(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_prefixPartlyInTreeAtOneEdge() {
		boolean wordInTrie = patriciaTrie.search("ULUS");
		assertFalse(wordInTrie);
	}

	@Test
	public void insert_newWord() {
		String newWord = "BLABLA";

		assertFalse(patriciaTrie.search(newWord));
		patriciaTrie.insert(newWord);
		assertTrue(patriciaTrie.search(newWord));
	}

	@Test
	public final void runNominalTestIsEmpty() {
		// TODO
	}

	@Test
	public final void runNominalTestInsert() {
		// TODO
	}

	@Test
	public final void runNominalTestSearch() {
		// TODO
	}

	@Test
	public final void runNominalTestCountWords() {
		// TODO
	}

	@Test
	public final void runNominalTestListWords() {				
		// TODO
	}

	@Test
	public final void runNominalTestCountNull() {
		// TODO
	}

	@Test
	public final void runNominalTestHeight() {
		// TODO
	}

	@Test
	public final void runNominalTestAverageDepth() {
		// TODO
	}

	@Test
	public final void runNominalTestPrefix() {
		// TODO
	}

	@Test
	public final void runNominalTestRemove() {
		// TODO
	}
	
	@Test
	public final void runNominalTestPrint() {
		// TODO
	}

	@Test
	public final void runNominalTestMerge() {
		// TODO
	}

	@Test
	public final void runNominalTestToHybridTrie() {
		// TODO
	}
}

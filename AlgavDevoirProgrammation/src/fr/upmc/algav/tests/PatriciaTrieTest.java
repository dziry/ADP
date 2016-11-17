package fr.upmc.algav.tests;

import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class PatriciaTrieTest extends AbstractTrieTest {
	private PatriciaTrie patriciaTrie;

	private static final long EXPECTED_NULL_POINTER_COUNT = 2287;
	private static final String WORD_OVER_SEVERAL_EDGES = "ROMANE";
	private static final String WORD_AT_ONLY_ON_EDGE = "hello";
	private static final String WORD_WITH_RESULT_ONLY_EDGE = "RUB";
	private static final String NOT_EXISTING_WORD = "NOTINTRIE";
	private static final String NEW_INSERTED_WORD = "IAmNew";
	private static final String PREFIX_PARTLY_IN_TREE_OVER_EDGES = "ROM";
	private static final String PREFIX_PARTLY_IN_TREE_ONE_EDGE = "ULUS";

	private static final String[] TEST_DATA = {
			WORD_OVER_SEVERAL_EDGES, "ROMANUS", "ROMULUS", "RUBENS", "RUBER",
			"RUBICON", "RUBICUNDUS", WORD_WITH_RESULT_ONLY_EDGE, WORD_AT_ONLY_ON_EDGE,
			"ROMULUSBBB", WORD_OVER_SEVERAL_EDGES, WORD_OVER_SEVERAL_EDGES, "RUBER",
			WORD_OVER_SEVERAL_EDGES, WORD_WITH_RESULT_ONLY_EDGE
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
		boolean wordInTrie = patriciaTrie.search(WORD_OVER_SEVERAL_EDGES);
		assertTrue(wordInTrie);
	}

	@Test
	public void search_existingWord_atOnlyOneEdge() {
		boolean wordInTrie = patriciaTrie.search(WORD_AT_ONLY_ON_EDGE);
		assertTrue(wordInTrie);
	}

	@Test
	public void search_existingWord_withResultOnlyEdge() {
		boolean wordInTrie = patriciaTrie.search(WORD_WITH_RESULT_ONLY_EDGE);
		assertTrue(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_completelyNotInTrie() {
		boolean wordInTrie = patriciaTrie.search(NOT_EXISTING_WORD);
		assertFalse(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_prefixPartlyInTrieOverEdges() {
		boolean wordInTrie = patriciaTrie.search(PREFIX_PARTLY_IN_TREE_OVER_EDGES);
		assertFalse(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_prefixPartlyInTrieAtOneEdge() {
		boolean wordInTrie = patriciaTrie.search(PREFIX_PARTLY_IN_TREE_ONE_EDGE);
		assertFalse(wordInTrie);
	}

	@Test
	public void insert_newWord() {
		assertFalse(patriciaTrie.search(NEW_INSERTED_WORD));
		patriciaTrie.insert(NEW_INSERTED_WORD);
		assertTrue(patriciaTrie.search(NEW_INSERTED_WORD));
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
		HashSet<String> wordsSet = new HashSet<>();

		for (String word : TEST_DATA) {
			wordsSet.add(word);
		}

		assertEquals(wordsSet.size(), patriciaTrie.getWordCount());
	}

	@Test
	public final void runNominalTestListWords() {
		ArrayList<String> storedWords = patriciaTrie.getStoredWords();

		for (String word : TEST_DATA) {
			assertTrue(storedWords.contains(word));
		}
	}

	@Test
	public final void runNominalTestCountNull() {
		assertEquals(EXPECTED_NULL_POINTER_COUNT, patriciaTrie.getNullPointerCount());
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

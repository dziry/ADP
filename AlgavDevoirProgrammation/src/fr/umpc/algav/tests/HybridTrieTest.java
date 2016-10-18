package fr.umpc.algav.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.umpc.algav.errors.HybridTrieError;
import fr.umpc.algav.hybridtries.HybridTrie;
import fr.umpc.algav.interfaces.ITrie;

public class HybridTrieTest {

	private static ITrie hybridTrie;
	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {
		hybridTrie = new HybridTrie();
		hybridTrie.insert("lou");
		hybridTrie.insert("leve");
		hybridTrie.insert("les");
		hybridTrie.insert("loups");
		hybridTrie.insert("dans");
		hybridTrie.insert("le");
		hybridTrie.insert("lourds");
		hybridTrie.insert("tapis");
		hybridTrie.insert("de");
		hybridTrie.insert("luxe");
		hybridTrie.insert("vert");
		hybridTrie.insert("olive");
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
	public final void testInsertNormalValues() {
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
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertWordExist() {
		hybridTrie.insert("dans");
	}
	
	@Test (expected = HybridTrieError.class)
	public final void testInsertNull() {
		hybridTrie.insert(null);
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
	public final void testCountWords() {
		assertEquals("there are 12 words", hybridTrie.countWords(), 12);
		ITrie hybridTrie = new HybridTrie();
		assertEquals("there is 0 word", hybridTrie.countWords(), 0);
	}

	@Test
	public final void testListWords() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testCountNull() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testHeight() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAverageDepth() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testPrefix() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRemove() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testPrint() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToPatriciaTrie() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testBalance() {
		fail("Not yet implemented"); // TODO
	}
}

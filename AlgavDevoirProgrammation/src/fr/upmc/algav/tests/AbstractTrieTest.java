package fr.upmc.algav.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import fr.upmc.algav.interfaces.ITrie;

public abstract class AbstractTrieTest {

	protected final void testSearch(ITrie trie, String word, boolean expectedTrieSerchResult) {						
		final boolean calculatedTrieSerch = trie.search(word);
		if (expectedTrieSerchResult == true) {
			assertTrue("Searching the word", calculatedTrieSerch);
		} else {			
			assertFalse("Searching the word", calculatedTrieSerch);
		}
	}
	
	protected final void testCountWords(ITrie trie, int expectedTrieCountWords) {						
		final int calculatedHybridTrieCountWords = trie.countWords();
		assertEquals("counting words", calculatedHybridTrieCountWords, expectedTrieCountWords);
	}
	
	protected final void testListWords(ITrie trie, ArrayList<String> expectedTrieListWords) {						
		final ArrayList<String> calculatedHybridTrieList = trie.listWords();
		assertEquals("listing words by ascending order", calculatedHybridTrieList, expectedTrieListWords);
	}

	protected final void testCountNull(ITrie trie, int expectedTrieCountNull) {
		final int calculatedTrieCountNull = trie.countNull();		
		assertEquals("counting nil pointer", calculatedTrieCountNull, expectedTrieCountNull);
	}
	
	protected final void testHeight(ITrie trie, int expectedTrieHeight) {		
		final int calculatedTrieHeight = trie.height();
		assertEquals("calulating the height", calculatedTrieHeight, expectedTrieHeight);	
	}
	
	protected final void testAverageDepth(ITrie trie, double expectedTrieAverageDepth) {
		final double precision = 0.00001;
		final double calculatedTrieAverageDepth = trie.averageDepth();
		assertEquals("calulating the average depth", calculatedTrieAverageDepth, expectedTrieAverageDepth, precision);
	}
	
	protected final void testPrefix(ITrie trie, String word, int expectedPrefixCount) {
		final int calculatedPrefixCount = trie.prefix(word);
		assertEquals("counting common prefixe", calculatedPrefixCount, expectedPrefixCount);
	}
}

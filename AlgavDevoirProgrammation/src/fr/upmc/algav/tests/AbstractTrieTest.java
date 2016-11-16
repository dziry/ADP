package fr.upmc.algav.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import fr.upmc.algav.interfaces.ITrie;

public abstract class AbstractTrieTest {

	protected final void testSearch(ITrie trie, String word, boolean expectedTrieSerchResult) {						
		final boolean calculatedTrieSerch = trie.search(word);
		if (expectedTrieSerchResult) {
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
	
	protected final void testRemove(ITrie trie, String word, int expectedCountNullResult, int expectedHeightResult) {		
		if (!trie.isEmpty()) {
			final boolean previewsSearchResult = trie.search(word);
			final int previewsCountWordsResult = trie.countWords();
			final ArrayList<String> previewsListWordsResult = trie.listWords();
			final int previewsPrefixResult = trie.prefix(word);		
			
			trie.remove(word);			
					
			final boolean newSearchResult;
			final int newCountWordsResult;
			final ArrayList<String> newListWordsResult;
			final int newCountNullResult; 
			final int newHeightResult;
			final int newPrefixResult;		
														
			newSearchResult = trie.search(word);
			newCountWordsResult = trie.countWords();
			newListWordsResult = trie.listWords();
			newCountNullResult = trie.countNull(); 
			newHeightResult = trie.height();
			newPrefixResult = trie.prefix(word);
			
			if (!trie.isEmpty()) {
				trie.print(word); // display a tree for visual comparison
			}

			assertTrue("1- Search", previewsSearchResult == true && newSearchResult == false);
			assertTrue("2- CountWords", newCountWordsResult == previewsCountWordsResult - 1);
			if (previewsListWordsResult != null && newListWordsResult != null) {
				assertTrue("3- ListWords", previewsListWordsResult.contains(word) && !newListWordsResult.contains(word));
			}
			assertTrue("4- CountNull", newCountNullResult == expectedCountNullResult);
			assertTrue("5- Height", newHeightResult == expectedHeightResult);
			assertTrue("6- Prefix", newPrefixResult == previewsPrefixResult - 1);
		} else {			
			assertTrue("Empty trie", trie.isEmpty());
		}		
	}
}
